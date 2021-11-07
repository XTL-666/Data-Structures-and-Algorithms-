package com.liang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class Encodingcontroller {

    @PostMapping("/e/t")
    public String test(String name, Model model){
        model.addAttribute("msg",name);
        return "test";
    }
}
