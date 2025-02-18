package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.Notice;
import com.bitgloomy.server.dto.RequestModifyNoticeDTO;
import com.bitgloomy.server.dto.RequestNoticeInfoDTO;
import com.bitgloomy.server.dto.RequestUploadNoticeDTO;
import com.bitgloomy.server.mybatis.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoticeService {
    NoticeMapper noticeMapper;

    @Autowired
    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public ArrayList<Notice> findNotices(RequestNoticeInfoDTO requestNoticeInfoDTO) throws Exception {
        String tableName = requestNoticeInfoDTO.getType();
        int limitNum = requestNoticeInfoDTO.getDisplayPageAmount();
        int offsetNum = (requestNoticeInfoDTO.getPage()-1)*limitNum;
        System.out.println(tableName);
        ArrayList<Notice> result = noticeMapper.findNotices(tableName,limitNum,offsetNum);
        if(result ==null){
            throw new Exception();
        }
        return result;
    }
    public int countTotal(String tableName){
        int total = noticeMapper.countTotal(tableName);
        return total;
    }
    public Notice findNotice(String tableName,int uid) throws Exception {
        Notice findNotice = noticeMapper.findNotice(tableName,uid);
        if(findNotice == null){
            throw new Exception();
        }
        return findNotice;
    }
    public void uploadNotice(RequestUploadNoticeDTO requestUploadNoticeDTO){
        Notice notice = new Notice();
        notice.setTitle(requestUploadNoticeDTO.getTitle());
        notice.setContent(requestUploadNoticeDTO.getContent());
        String tableName = requestUploadNoticeDTO.getType();
        System.out.println(requestUploadNoticeDTO.getContent());
        noticeMapper.saveNotice(tableName,notice);
    }
    public void modifyNotice(RequestModifyNoticeDTO requestModifyNoticeDTO){
        Notice notice = new Notice();
        notice.setTitle(requestModifyNoticeDTO.getTitle());
        notice.setContent(requestModifyNoticeDTO.getContent());
        notice.setUid(requestModifyNoticeDTO.getUid());
        String tableName = requestModifyNoticeDTO.getType();
        noticeMapper.modifyNotice(tableName,notice);
    }
    public void deleteNotice(String tableName,int uid){
        noticeMapper.deleteNotice(tableName,uid);
    }
}
