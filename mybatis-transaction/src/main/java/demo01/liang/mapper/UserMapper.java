package demo01.liang.mapper;

import demo01.liang.pojo.User;

import java.util.List;

public interface UserMapper {
    public List<User> selectUser();
    public int addUser(User user);
    public int deleteUser(User user);
}
