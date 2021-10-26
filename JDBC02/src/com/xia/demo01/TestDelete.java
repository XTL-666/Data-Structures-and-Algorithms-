
package com.xia.demo01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import com.xia.demo01.utils.JdbcUtils;

public class TestDelete {

    public static void main(String[] args) throws Exception{

        Connection con=null;
        PreparedStatement pst=null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();
            String sql ="DELETE  FROM users WHERE id = ?";
            pst=con.prepareStatement(sql);
            pst.setInt(1, 4);
            int i = pst.executeUpdate();
            if(i==1)
                System.out.println("Nice!! Delete the id Successfully!!!!");
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