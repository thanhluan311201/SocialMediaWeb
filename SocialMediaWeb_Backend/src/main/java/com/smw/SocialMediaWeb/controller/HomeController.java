package com.smw.SocialMediaWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String homePage(){
        return "index";
    }
    @RequestMapping("login")
    public String loginPage(){
        return "login";
    }
    @RequestMapping("register")
    public String registerPage(){
        return "register";
    }
    @RequestMapping("logout")
    public String logoutPage(){
        return "index";
    }
}
