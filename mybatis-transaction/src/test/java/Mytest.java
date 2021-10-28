import demo01.liang.mapper.UserMapper;
import demo01.liang.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Mytest {
    @Test
    public void test() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = context.getBean("userMapper",UserMapper.class);
        for(User user : userMapper.selectUser()){
            System.out.println(user);
        }
    }
}
