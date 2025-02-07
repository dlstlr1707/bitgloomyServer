package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    ArrayList<Notice> findNotices(String tableName,int limitNum,int offsetNum);
    // notice CUD
    void saveNotice(Notice notice);
    void modifyNotice(Notice notice);
    void deleteNotice(int uid);

    int countTotal(String tableName);
}
