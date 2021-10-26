package demo01.liang.dao;

import demo01.liang.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
//    int addBlog(Blog blog);
    List<Blog> queryBlogIF(Map map);
    List<Blog> queryBlogChoose(Map map);
    int updateBlog(Map map);
    List<Blog> queryBlogForeach(Map map);
}
