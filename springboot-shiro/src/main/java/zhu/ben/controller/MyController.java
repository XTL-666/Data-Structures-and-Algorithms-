package zhu.ben.controller;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello,shiro!!!");
        return "index";
    }

    @RequestMapping("user/add")
    public String add(){
        return "user/add";
    }

    @RequestMapping("user/update")
    public String update(){
        return "user/update";
    }


    @RequestMapping("/login")
    public String login(String username,String password,Model model){
        //gain the current users
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //excute login function
        try {
            subject.login(token);
            return "index";
        }catch(UnknownAccountException ice){
            model.addAttribute("msg","wrong username");
            System.out.println("wrong username");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return "login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","wrong password");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("wrong password");
            return "login";
        }
    }

}
