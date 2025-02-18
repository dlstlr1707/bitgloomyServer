package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    ArrayList<Notice> findNotices(@Param("tableName") String tableName, @Param("limitNum") int limitNum, @Param("offsetNum") int offsetNum);
    // notice CUD
    void saveNotice(@Param("tableName") String tableName,@Param("notice")Notice notice);
    void modifyNotice(@Param("tableName") String tableName,@Param("notice")Notice notice);
    void deleteNotice(@Param("tableName") String tableName,@Param("uid")int uid);
    Notice findNotice(@Param("tableName") String tableName,@Param("uid")int uid);
    int countTotal(@Param("tableName") String tableName);
}
