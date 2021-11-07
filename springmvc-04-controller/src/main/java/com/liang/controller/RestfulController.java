package com.liang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RestfulController {
    @RequestMapping (value ="/add/{a}/{b}", method = RequestMethod.GET)
    public String test1(@PathVariable int a,@PathVariable String b, Model model){
        String res = a + b;
        model.addAttribute("msg","success ! result is " + res);
        return "test";
    }
}
