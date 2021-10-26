package com.xia.demo01;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcFirstDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");//固定
        //2.连接用户信息和url
        String url="jdbc:mysql://localhost:3306/jdbcstudy?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username ="root";
        String password="666";
        //3.连接成功，数据库对象 Connection
        Connection connection=DriverManager.getConnection(url,username,password);
        //4.执行sql对象
        Statement statement  =connection.createStatement();
        //5.执行sql的对象去执行sql
        String sql = null;
        sql="select * from users";
        ResultSet rs=statement.executeQuery(sql);
        while (rs.next())
        {
            System.out.print("id="+rs.getObject("id")+" ");
            System.out.print("name="+rs.getObject("name")+" ");
            System.out.print("password="+rs.getObject("password")+" ");
            System.out.println("birthday="+rs.getObject("birthday"));
        }
        //6.释放连接
        rs.close();
        statement.close();
        connection.close();
    }
}

