package com.xia.demo01;

import com.xia.demo01.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestTransaction {
    public static void main(String[] args) {
        Connection cnn = null;
        PreparedStatement st = null;
        ResultSet rt = null;
        try {
            cnn = JdbcUtils.getConnection();
            cnn.setAutoCommit(false);
            String sql1 = "update users set `name` = 'zzs' where `birthday` = '2021-10-21'";
            st = cnn.prepareStatement(sql1);
            st.executeUpdate();
            String sql2 = "update users set `name` = 'zzs' where `birthday` = '2021-10-21'";
            st = cnn.prepareStatement(sql2);
            st.executeUpdate();
            cnn.commit();
            System.out.println("Success!!");
        } catch (SQLException e) {
            try {
                cnn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.release(cnn, st, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
