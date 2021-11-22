package zhu.ben.spring004data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class Spring004DataApplicationTests {

    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() {
        System.out.println("This is dataSource:");
        System.out.print(dataSource.getClass());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("This is connection:");
        System.out.print(connection);
//        class com.zaxxer.hikari.HikariDataSource
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
