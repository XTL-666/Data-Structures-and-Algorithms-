package zhu.ben.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import zhu.ben.pojo.User;

@Repository
@Mapper
public interface UserMapper {

    public User queryUserByName(String name);
}
