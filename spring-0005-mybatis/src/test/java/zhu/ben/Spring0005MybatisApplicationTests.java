package zhu.ben;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class Spring0005MybatisApplicationTests {


    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
        try {
            System.out.println(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
