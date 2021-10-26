package com.xia.demo01;

import com.xia.demo01.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MyTest {
    public static void main(String[] args) throws Exception{
        Connection cnn = null;
        Statement  st  = null;
        ResultSet  rs  = null;

        try {
            cnn = JdbcUtils.getConnection();
            st = cnn.createStatement();
            String sql ="INSERT INTO `users`  (`id`,`name`,`password`,`birthday`)" + "VALUES ('6','honglong','jgz','2001-5-13')";
            int i = st.executeUpdate(sql);
            if(i > 0){
                System.out.println("nice!!!!!");
            }
            else{
                System.out.println("Fucking fucked");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(cnn,st,null);
        }
    }
}
