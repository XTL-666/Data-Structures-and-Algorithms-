package zhu.ben.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import zhu.ben.mapper.UserMapper;
import zhu.ben.pojo.User;

import java.util.List;

@RestController
public class Usercontroller {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/queryUserList")
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        return userList;
    }

    @GetMapping("/queryUserById/{id}")
    public User queryUserById(@PathVariable("id") int id){
        User user = userMapper.queryUserById(id);
        System.out.println(user);
        return user;
    }

    @GetMapping("/addUser")
    public String addUser(){
        userMapper.addUser(new User(13,"阿毛","15561"));
        return "add success!!";
    }

    @GetMapping("/updateUser")
    public String updateUser(){
        userMapper.updateUser(new User(13,"阿毛","4884"));
        return "update success!!";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(){
        userMapper.deleteUser(5);
        return "delete success!";
    }


}
