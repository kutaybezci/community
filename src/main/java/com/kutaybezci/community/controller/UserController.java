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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kutaybezci.community.types.fe.UserForm;

/**
 *
 * @author kutay.bezci
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/create")
    public String getCreate(Model model){
        model.addAttribute("userForm", new UserForm() );
        return "user";
    }
    @PostMapping("/create")
    public String postCreate(@ModelAttribute UserForm userForm){
        System.out.println(userForm.toString());
        return "user";
    }
}
