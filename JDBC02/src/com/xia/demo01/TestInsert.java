package com.xia.demo01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import com.xia.demo01.utils.JdbcUtils;

public class TestInsert {

    public static void main(String[] args) throws Exception{

        Connection con=null;
        PreparedStatement pst=null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();
            String sql ="insert into users(id,name,password,birthday) values(?,?,?,?)";
            pst=con.prepareStatement(sql);
            pst.setInt(1, 12);
            pst.setString(2, "diao si xtl");
            pst.setString(3, "5201314888");
            pst.setDate(4, new java.sql.Date(new Date().getTime()) );
            int i=pst.executeUpdate();
            String sql2 = "SELECT * FROM users";
            st = con.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next())
            {
                System.out.print("id="+rs.getInt("id")+" ");
                System.out.print("name="+rs.getString("name")+" ");
                System.out.print("password="+rs.getString("password")+" ");
                System.out.println("birthday="+rs.getString("birthday"));
            }
            if(i==1)
                System.out.println("suck my dick!!!!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(pst!=null)
                try {
                    pst.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if(con!=null)
                try {
                    con.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

}