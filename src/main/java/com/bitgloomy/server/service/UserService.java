package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.Address;
import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.domain.UserProfile;
import com.bitgloomy.server.dto.RequestJoinDTO;
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
    public UserProfile login(User user) throws Exception {
        User foundUser = userMapper.findUserById(user.getId());
        UserProfile foundUserProfile = userMapper.findUserProfile(foundUser.getUid());

        if((foundUserProfile != null)&&(user.getPassword().equals(foundUser.getPassword()))){
            return foundUserProfile;
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
    public UserProfile findUserProfile (int uid) throws Exception {
        System.out.println(uid);
        UserProfile findUserProfile = userMapper.findUserProfile(uid);

        if(findUserProfile == null){
            throw new Exception();
        }

        return findUserProfile;
    }
    public void saveAddress(RequestJoinDTO requestJoinDTO){
        User foundUser = userMapper.findUserById(requestJoinDTO.getId());
        Address address = new Address();
        address.setUserUid(foundUser.getUid());
        address.setAddress1(requestJoinDTO.getAddress());
        address.setPostcode1(requestJoinDTO.getPostcode());
        userMapper.saveAddress(address);
    }
}
