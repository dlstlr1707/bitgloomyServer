package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Address;
import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void saveUser(User user);
    void saveAddress(Address address);
    User findUserById(String id);
    String findID(@Param("name")String name,@Param("email") String email);
    void modifyPW(String password,int uid);
    UserProfile findUserProfile(int uid);
    void deleteUser(int uid);
}
