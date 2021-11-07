package demo.liang.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.liang.pojo.User;
import demo.liang.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.JarException;

@Controller
public class UserController {
    @RequestMapping(value="/j1",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String json1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("秦疆老师",3,"male");
        String str = mapper.writeValueAsString(user);
        return str;
    }

    @RequestMapping(value="/j2",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String json2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList  = new ArrayList<User>();
        User user1 = new User("秦",3124,"female");
        User user2 = new User("疆",335,"male");
        User user3 = new User("老师",2,"female");
        User user4 = new User("老",3231,"female");
        User user5 = new User("师",34123,"male");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        String str = mapper.writeValueAsString(userList);
        return str;
    }


//    @RequestMapping(value="/j3",produces = "application/json;charset=utf-8")
//    public String json3(){
//        Date date = new Date();
//        return JsonUtils.getJson(date,"yyyy-MM-dd HH:mm:ss");
//    }

    @RequestMapping(value="/j4",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String json4() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList  = new ArrayList<User>();
        User user1 = new User("秦",3124,"female");
        User user2 = new User("疆",335,"male");
        User user3 = new User("老师",2,"female");
        User user4 = new User("老",3231,"female");
        User user5 = new User("师",34123,"male");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        String res = JSON.toJSONString(userList);
        return res;
    }



}
