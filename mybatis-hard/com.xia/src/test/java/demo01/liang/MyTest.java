package demo01.liang;

import demo01.liang.dao.StudentMapper;
import demo01.liang.dao.TeacherMapper;
import demo01.liang.pojo.Student;
import demo01.liang.pojo.Teacher;
import demo01.liang.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyTest {
//    public static void main(String[] args) {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
//        Teacher teacher = mapper.getTeacher(1);
//        System.out.println(teacher);
//        sqlSession.close();
//    }
//    @Test
//    public void test1(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
//        List<Student> studentList = mapper.getStudent2();
//        for (Student student : studentList) {
//            System.out.println(student);
//        }
//        sqlSession.close();
//    }
    @Test
    public void test02(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher2(1);
        System.out.println(teacher);
        sqlSession.close();
    }
//    @Test
//    public void test03(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        TeacherMapper mapper = getClass(TeacherMapper.class);
//        Teacher teacher = mapper.getTeacher(1);
//        System.out.println(teacher);
//        sqlSession.close();
//    }
}
