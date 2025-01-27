package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void saveUser(User user);
    User findUserById(String id);
}
