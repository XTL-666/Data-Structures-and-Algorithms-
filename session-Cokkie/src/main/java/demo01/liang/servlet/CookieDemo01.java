package demo01.liang.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class CookieDemo01 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            out.write("last into this page: ");
            for (int i = 0; i < cookies.length; i++) {
               Cookie cookie = cookies[i];
               if(cookie.getName().equals("lastLoginTime")){
                   long lastLoginTime = Long.parseLong(cookie.getValue());
                   Date date = new Date(lastLoginTime);
                   out.write(date.toLocaleString());
               }
            }
        }else{
            out.write("first into this html");
        }
        Cookie cookie = new Cookie("lastLoginTime",System.currentTimeMillis() + "");
        resp.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

}
