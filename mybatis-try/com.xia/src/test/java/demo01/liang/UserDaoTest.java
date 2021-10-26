package demo01.liang;

import demo01.liang.dao.UserMapper;
import demo01.liang.pojo.User;
import demo01.liang.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDaoTest {
    static Logger logger = Logger.getLogger(UserDaoTest.class);
//    @Test
//    public void testLog4j(){
//        logger.info("info:tLog4j");
//        logger.debug("debug:testLog4j");
//        logger.error("error:testLog4j");
//    }
//    @Test
//    public void Test(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        int i = mapper.deleteUser2(5);
//        if(i > 0) System.out.println("delete success!!!");
//        sqlSession.close();
//    }
//    @Test
//    public void getUserByRowBoouds() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        RowBounds rowRounds = new RowBounds(1, 2);
//        List<User> userList = sqlSession.selectList("demo01.liang.dao.UserMapper.getUserByRowBounds", null, rowRounds);
//        for (User user : userList) {
//            System.out.println(user);
//        }
//        sqlSession.close();
//    }
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        Map<String,Integer> map = new HashMap<>();
//        map.put("startIndex",0);
//        map.put("pageSize",2);
//        List<User> userList = mapper.getUserByLimit(map);
//        for (User user : userList) {
//            System.out.println(user);
//        }
//    }
//    @Test
//    public void test() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        Map<String,Integer> map = new HashMap<>();
//        map.put("startIndex",0);
//        map.put("pageSize",2);
//        List<User> userList = mapper.getUserByLimit(map);
//        for (User user : userList) {
//            System.out.println(user);
//        }
//        sqlSession.close();
//    }
    @Test
    public void test() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
        logger.info("successfully into log4j");
        List<User> userList = userDao.getUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }
//    @Test
//    public void getUserById(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//
//        UserDao userDao = sqlSession.getMapper(UserDao.class);
//        userDao.updateUser(new User(4,"lajihjhj","nongye"));
//        sqlSession.commit();
//        System.out.println("success commit!!");
//        sqlSession.close();
//    }
//    @Test
//    public void addUser2(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("userId",9);
//        map.put("userName","shenyu");
//        int i = userDao.addUser2(map);
//        sqlSession.commit();
//        if(i > 0) System.out.println("Map put successfully commit!!");
//        sqlSession.close();
//    }
//    @Test
//    public void searchLike(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        UserDao userDao = sqlSession.getMapper(UserDao.class);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("userId",8);
//        map.put("userName","shenhua");
//        int i = userDao.addUser2(map);
//        sqlSession.commit();
//        if(i > 0) System.out.println("Map put successfully commit!!");
//        sqlSession.close();
//    }
//    @Test
//    public void searchLike(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
//        List<User> userList= userDao.getUserLike("%shen%");
//        for(User user : userList){
//            System.out.println(user);
//        }
//        System.out.println("success!!");
//        sqlSession.close();
//    }

}
