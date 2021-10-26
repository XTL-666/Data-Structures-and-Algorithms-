package demo01.liang.dao;

import demo01.liang.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User queryUserById(@Param("id")int id);
}
