package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.User;
import com.bitgloomy.server.dto.RequestJoinDTO;
import com.bitgloomy.server.dto.RequestLoginDTO;
import com.bitgloomy.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
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
            User foundUser = userService.login(user);
            HttpSession session = request.getSession();
            session.setAttribute("userUid",foundUser.getUid());
            session.setAttribute("auth",foundUser.getAuth());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDTO requestLoginDTO, HttpServletRequest request){
        User user = new User();
        user.setId(requestLoginDTO.getId());
        user.setPassword(requestLoginDTO.getPassword());
        try {
            User foundUser = userService.login(user);
            HttpSession session = request.getSession();
            session.setAttribute("userUid",foundUser.getUid());
            session.setAttribute("auth",foundUser.getAuth());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/checkID/{id}")
    public ResponseEntity<?> checkUserId(@PathVariable(value = "id")String id){
        try {
            userService.checkUserId(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
