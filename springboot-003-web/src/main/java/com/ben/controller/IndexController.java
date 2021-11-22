package com.ben.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
public class IndexController {
    @RequestMapping("/test")
    public String index(Model model) {
        model.addAttribute("msg","<h1>hello,springboot</h1>");
        model.addAttribute("users", Arrays.asList("qinjiang","kuangshen"));
        return "test";
    }
}
