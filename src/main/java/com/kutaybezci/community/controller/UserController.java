/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kutaybezci.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import types.fe.UserForm;

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
