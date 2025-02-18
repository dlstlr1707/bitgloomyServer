package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Address;
import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.domain.UserProfile;
import com.bitgloomy.server.dto.RequestFindIdDTO;
import com.bitgloomy.server.dto.RequestJoinDTO;
import com.bitgloomy.server.dto.RequestLoginDTO;
import com.bitgloomy.server.dto.RequestModifyPWDTO;
import com.bitgloomy.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/users")
    public ResponseEntity<?> signup(@RequestBody RequestJoinDTO requestJoinDTO,HttpServletRequest request){
        User user = new User();
        user.setId(requestJoinDTO.getId());
        user.setPassword(requestJoinDTO.getPassword());
        user.setName(requestJoinDTO.getName());
        user.setPhoneNum(requestJoinDTO.getPhoneNum());
        user.setSmsReception(requestJoinDTO.getSmsReception());
        user.setEmail(requestJoinDTO.getEmail());
        user.setEmailReception(requestJoinDTO.getEmailReception());
        try {
            userService.signup(user);
            userService.saveAddress(requestJoinDTO);
            UserProfile foundUser = userService.login(user);
            HttpSession session = request.getSession();
            session.setAttribute("userUid",foundUser.getUid());
            session.setAttribute("auth",foundUser.getAuth());
            String jsonResponse = String.format("{\"userUid\": \"%s\", \"auth\": \"%s\",\"name\": \"%s\",\"email\": \"%s\",\"phoneNum\": \"%s\",\"address1\": \"%s\",\"postcode1\": \"%s\"}", foundUser.getUid(), foundUser.getAuth(),foundUser.getName(),foundUser.getEmail(),foundUser.getPhoneNum(),foundUser.getAddress().getAddress1(),foundUser.getAddress().getPostcode1());
            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDTO requestLoginDTO, HttpServletRequest request){
        User user = new User();
        System.out.println(requestLoginDTO.getId());
        System.out.println(requestLoginDTO.getPassword());
        user.setId(requestLoginDTO.getId());
        user.setPassword(requestLoginDTO.getPassword());
        try {
            UserProfile foundUser = userService.login(user);
            HttpSession session = request.getSession();
            session.setAttribute("userUid",foundUser.getUid());
            session.setAttribute("auth",foundUser.getAuth());

            System.out.println("auth is : "+session.getAttribute("auth"));

            String jsonResponse = String.format("{\"userUid\": \"%s\", \"auth\": \"%s\",\"name\": \"%s\",\"email\": \"%s\",\"phoneNum\": \"%s\",\"address1\": \"%s\",\"postcode1\": \"%s\"}", foundUser.getUid(), foundUser.getAuth(),foundUser.getName(),foundUser.getEmail(),foundUser.getPhoneNum(),foundUser.getAddress().getAddress1(),foundUser.getAddress().getPostcode1());
            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/checkID/{id}")
    public ResponseEntity<?> checkUserId(@PathVariable(value = "id")String id){
        String result = userService.checkUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/users/{uid}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "uid")int uid,HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.deleteUser(uid);
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/profile/{uid}")
    public ResponseEntity<?> findUserProfile(@PathVariable(value = "uid")String uid,HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            UserProfile userProfile = userService.findUserProfile(Integer.parseInt(uid));
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/findID")
    public ResponseEntity<?> findID(@RequestBody RequestFindIdDTO requestFindIdDTO){
        try {
            String findID = userService.findID(requestFindIdDTO);
            return ResponseEntity.status(HttpStatus.OK).body(findID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PatchMapping("/modifyPW")
    public ResponseEntity<?> modifyID(@RequestBody RequestModifyPWDTO requestModifyPWDTODTO){
        try {
            userService.modifyPW(requestModifyPWDTODTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/address/{uid}")
    public ResponseEntity<?> findAllAddress(@PathVariable(value = "uid")String uid){
        try {
            Address results = userService.findAllAddress(Integer.parseInt(uid));
            return ResponseEntity.status(HttpStatus.OK).body(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PatchMapping("/address")
    public ResponseEntity<?> modifyAddress (@RequestBody Address address){
        userService.modifyAddress(address);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
