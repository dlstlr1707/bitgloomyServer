package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.Address;
import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.domain.UserProfile;
import com.bitgloomy.server.dto.RequestFindIdDTO;
import com.bitgloomy.server.dto.RequestJoinDTO;
import com.bitgloomy.server.dto.RequestModifyPWDTO;
import com.bitgloomy.server.mybatis.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    public String checkUserId(String id){
        User foundUser = userMapper.findUserById(id);
        String result;
        if(foundUser != null){
            result = "해당 ID가 이미 존재합니다.";
        }else{
            result = "해당 ID는 사용 가능합니다.";
        }
        return result;
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
        System.out.println("주소저장 실행됨");
        User foundUser = userMapper.findUserById(requestJoinDTO.getId());
        Address address = new Address();
        address.setUserUid(foundUser.getUid());
        address.setAddress1(requestJoinDTO.getAddress());
        address.setAddress2("");
        address.setAddress3("");
        address.setAddress4("");
        address.setAddress5("");
        address.setPostcode1(requestJoinDTO.getPostcode());
        address.setPostcode2("");
        address.setPostcode3("");
        address.setPostcode4("");
        address.setPostcode5("");
        userMapper.saveAddress(address);
    }
    public Address findAllAddress(int uid) throws Exception {
        Address results = userMapper.findAllAddress(uid);
        if(results == null){
            throw new Exception();
        }
        return results;
    }
    public void modifyAddress(Address address){
        userMapper.modifyAddress(address);
    }
    public String findID(RequestFindIdDTO requestFindIdDTO) throws Exception {
        String findID = userMapper.findID(requestFindIdDTO.getName(), requestFindIdDTO.getEmail());
        if(findID == null || findID.equals("")){
            throw new Exception();
        }
        return findID;
    }
    public void modifyPW(RequestModifyPWDTO requestModifyPWDTO) throws Exception {
        User foundUser = userMapper.findUserById(requestModifyPWDTO.getId());
        if(foundUser == null){
            throw new Exception();
        }
        userMapper.modifyPW(requestModifyPWDTO.getChangePW(),foundUser.getUid());
    }
}
