package com.xia.demo01.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtils {

    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;
    static{
        try{
            InputStream in=JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties=new Properties();
            properties.load(in);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            Class.forName(driver);
            //驱动只加载一次
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //获取连接
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url,username,password);
    }

    //释放连接资源

    public static void release(Connection conn,Statement st, ResultSet rs) throws SQLException
    {
        if(rs!=null)
        {
            rs.close();
        }
        if(st!=null)
        {
            st.close();
        }
        if(conn!=null)
        {
            conn.close();
        }

    }

}

