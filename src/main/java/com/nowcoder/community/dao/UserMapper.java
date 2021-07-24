package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author wjh
 * @create 2021 - 07 - 23 16:41
 */
@Mapper //MyBatis提供的注解 标识bean 替代@Repository
public interface UserMapper {

    //接口不需要实现类，需要配置文件，写出每个方法对应的sql语句

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status); //修改用户状态

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
