package com.forloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String root(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
