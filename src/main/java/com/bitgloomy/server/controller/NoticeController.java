package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Notice;
import com.bitgloomy.server.dto.RequestModifyNoticeDTO;
import com.bitgloomy.server.dto.RequestNoticeInfoDTO;
import com.bitgloomy.server.dto.RequestUploadNoticeDTO;
import com.bitgloomy.server.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/notices")
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
    @PostMapping("/notice")
    public ResponseEntity<?> uploadNotice(@RequestBody RequestUploadNoticeDTO requestUploadNoticeDTO){
        noticeService.uploadNotice(requestUploadNoticeDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PatchMapping("/notice")
    public ResponseEntity<?> modifyNotice(@RequestBody RequestModifyNoticeDTO requestModifyNoticeDTO){
        noticeService.modifyNotice(requestModifyNoticeDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/notice/{tableName}/{uid}")
    public ResponseEntity<?> deleteNotice(@PathVariable(value = "tableName")String tableName,@PathVariable(value = "uid")int uid){
        noticeService.deleteNotice(tableName,uid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/notice/{tableName}/{uid}")
    public ResponseEntity<?> findNotice(@PathVariable(value = "tableName")String tableName,@PathVariable(value = "uid")int uid){
        try {
            Notice foundNotice = noticeService.findNotice(tableName,uid);
            return ResponseEntity.status(HttpStatus.OK).body(foundNotice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
