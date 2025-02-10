package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.mybatis.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserMapper userMapper;
    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public void signup(User user) throws Exception {
        User foundUser = userMapper.findUserById(user.getId());
        if(foundUser != null){
            throw new Exception();
        }
        userMapper.saveUser(user);
    }
    public User login(User user) throws Exception {
        User foundUser = userMapper.findUserById(user.getId());
        if((foundUser != null)&&(user.getPassword().equals(foundUser.getPassword()))){
            return foundUser;
        }else{
            throw new Exception();
        }
    }
    public void checkUserId(String id) throws Exception{
        User foundUser = userMapper.findUserById(id);
        if(foundUser != null){
            throw new Exception();
        }
    }
    public void deleteUser(int uid) {
        userMapper.deleteUser(uid);
    }
}
