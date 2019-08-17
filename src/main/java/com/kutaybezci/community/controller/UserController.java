/*
 * Copyright (C) 2019 kutay.bezci
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kutaybezci.community.controller;

import com.kutaybezci.community.bl.MemberService;
import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberResponse;
import com.kutaybezci.community.types.fe.InfoForm;
import com.kutaybezci.community.types.fe.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author kutay.bezci
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "user";
    }

    @GetMapping("/list")
    public String list(Model model) {
        ListMemberResponse response = memberService.listMember(new ListMemberRequest());
        model.addAttribute("memberList", response.getMemberList());
        return "userlist";
    }

    @PostMapping("/create")
    public ModelAndView postCreate(@ModelAttribute UserForm userForm) {
        if (!StringUtils.equals(userForm.getPassword(), userForm.getPassword2())) {
            throw new RuntimeException("Password does not match");
        }
        CreateMemberRequest request = new CreateMemberRequest();
        request.setEmail(userForm.getEmail());
        request.setFullname(userForm.getFullname());
        request.setPassword(userForm.getPassword());
        request.setPhone(userForm.getPhone());
        request.setUsername(userForm.getUsername());
        memberService.createMember(request);
        InfoForm infoForm = new InfoForm();
        infoForm.setCode("OK");
        infoForm.setMessage("user.create.ok");
        infoForm.setError(false);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("infoForm", infoForm);
        modelAndView.setViewName("info");
        return modelAndView;
    }

}