package com.xia.demo02;
import java.io.IOException;
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
    static {
        try{
          InputStream in=JdbcUtils.class.getClassLoader().getResourceAsStream("JDBC01/db.properties");
          Properties properties = new Properties();
          properties.load(in);
          driver = properties.getProperty("driver");
          url = properties.getProperty("url");
          username = properties.getProperty("username");
          password = properties.getProperty("password");
          class.forName(driver);
        }ca
    }
}
