package demo01.liang;

import demo01.liang.dao.BlogMapper;
import demo01.liang.pojo.Blog;
import demo01.liang.utils.MybatisUtils;
import demo01.liang.utils.IdUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyTest {
    @Test
    public void queryBlogForEach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap<String,Object> map = new HashMap<String,Object>();
        ArrayList<Integer> ids = new ArrayList<Integer>(2);
        ids.add(1);
        ids.add(2);
        map.put("ids",ids);
        List<Blog> blogs = mapper.queryBlogForeach(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
//    public void queryBlogIF(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
//        HashMap map = new HashMap();
//        ArrayList<Integer> ids = new ArrayList<>();
//        ids.add(1);
//        ids.add(2);
//        ids.add(3);
//        map.put("id",ids);
//        List<Blog> blogs = mapper.queryBlogForeach(map);
//        for (Blog blog : blogs) {
//            System.out.println(blog);
//        }
//        sqlSession.close();
//    }
//    @Test
//    public void addInitBlog(){
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
//        Blog blog = new Blog();
//        blog.setId(IdUtils.getId());
//        blog.setTitle("MyBatis如此简单");
//        blog.setAuthor("狂神说");
//        blog.setCreateTime(new Date());
//        blog.setViews(9999);
//        mapper.addBlog(blog);
//        blog.setId(IdUtils.getId());
//        blog.setTitle("Java如此简单");
//        mapper.addBlog(blog);
//        blog.setId(IdUtils.getId());
//        blog.setTitle("Spring如此简单");
//        mapper.addBlog(blog);
//        blog.setId(IdUtils.getId());
//        blog.setTitle("微服务如此简单");
//        mapper.addBlog(blog);
//        sqlSession.close();
//    }
}
