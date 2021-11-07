package com.liang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("jb")
public class ControllerTest2 {
    @RequestMapping("t2")
    public String test(Model model){
        model.addAttribute("msg","jb");
        return "jgz";
    }
}