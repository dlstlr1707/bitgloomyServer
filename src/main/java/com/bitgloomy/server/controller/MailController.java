package com.bitgloomy.server.controller;

import com.bitgloomy.server.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
public class MailController {
    private MailService mailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // 인증 이메일 전송
    @GetMapping("/mailSend/{email}")
    public HashMap<String, Object> mailSend(@PathVariable String email) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(email);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck/{code}")
    public ResponseEntity<?> mailCheck(@PathVariable String code) {

        boolean isMatch = code.equals(String.valueOf(number));
        System.out.println(number);
        System.out.println(code);
        if(isMatch){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
