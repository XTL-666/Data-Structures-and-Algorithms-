package com.xia.demo01;
import com.xia.demo01.utils.JdbcUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

public class sql {
    public static void main(String[] args)throws Throwable{
        sql.login("'or' 1=1 "," 'or' 1 = 1");
    }
    public static void login(String username,String password){
        Connection cnn = null;
        Statement sta = null;
        ResultSet re = null;
        try {
            cnn = JdbcUtils.getConnection();
            sta = cnn.createStatement();
            String sql = "SELECT * FROM `users`  WHERE `name`='" + username + "' AND `password`='" + password + "'";
            re = sta.executeQuery(sql);
            while (re.next()) {
                System.out.println(re.getString("name") + "   ");
                System.out.println(re.getString("password"));
            }
        }catch(SQLException e){
                e.printStackTrace();
            }
    }
}
