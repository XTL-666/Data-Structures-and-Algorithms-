package demo01.liang.dao;

import java.util.List;
import java.util.Map;

import demo01.liang.pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.Alias;


public interface UserMapper {
    List<User> getUserByRowBounds();
    List<User> getUserList();
    User getUsername(String name);
    int addUser(User user);
    int deleteUser(int id);
    int updateUser(User user);
    int addUser2(Map<String,Object> map);
    List<User> getUserLike(String value);
    List<User> getUserByLimit(Map<String,Integer> map);
    @Select("select * from user")
    List<User> getUsers();
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") int id);
    @Insert("insert into user(id,name,pwd) values(#{id},#{name},#{pwd})")
    int addUser2(User user);
    @Update("update user set name = #{name},pwd = #{pwd} where id = #{id}")
    int update2(User user);
    @Delete("delete from user where id = #{id}")
    int deleteUser2(@Param("id") int id);
}
