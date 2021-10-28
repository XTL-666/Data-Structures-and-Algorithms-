package demo01.liang.mapper;

import demo01.liang.pojo.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl extends SqlSessionDaoSupport implements UserMapper {

    @Override
    public List<User> selectUser() {
        User user = new User(11,"zhubi","156645156");
        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        mapper.addUser(user);
//        mapper.deleteUser(user);
        return mapper.selectUser();
    }

    @Override
    public int addUser(User user) {
        return getSqlSession().getMapper(UserMapper.class).addUser(user);
    }

    @Override
    public int deleteUser(User user) {
        return getSqlSession().getMapper(UserMapper.class).deleteUser(user);
    }

}
