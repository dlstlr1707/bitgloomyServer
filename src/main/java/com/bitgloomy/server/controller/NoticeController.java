package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Notice;
import com.bitgloomy.server.dto.RequestNoticeInfoDTO;
import com.bitgloomy.server.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NoticeController {
    NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/notice")
    public ResponseEntity<?> findNoticeInfo(@RequestBody RequestNoticeInfoDTO requestNoticeInfoDTO){
        System.out.println(requestNoticeInfoDTO.getType());
        int total = noticeService.countTotal(requestNoticeInfoDTO.getType());

        ArrayList<Notice> results = null;
        try {
            results = noticeService.findNotices(requestNoticeInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // total과 results를 Map에 담아서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("results", results);

        // total josn에 추가해서 반환 해줘야함
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
