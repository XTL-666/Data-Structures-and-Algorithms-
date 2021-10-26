package demo01.liang;


import demo01.liang.dao.UserMapper;
import demo01.liang.pojo.User;
import demo01.liang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {

    @Test
    public void test(){
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        User user1 = mapper1.queryUserById(1);
        System.out.println(user1);
        sqlSession1.close();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user2 = mapper2.queryUserById(1);
        System.out.println(user2);

        System.out.println("=================================");;

        sqlSession2.close();
    }
}
