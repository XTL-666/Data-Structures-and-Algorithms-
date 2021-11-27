package zhu.ben.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zhu.ben.service.AsyncService;

@RestController
public class AsyncController {
    @Autowired
    AsyncService asyncService;


    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
        asyncService.Hello();
        return "OK";
    }
}
