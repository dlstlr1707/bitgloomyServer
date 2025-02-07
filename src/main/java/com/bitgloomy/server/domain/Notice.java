package com.bitgloomy.server.domain;

import java.time.LocalDateTime;

public class Notice {
    private int uid;
    private String title;
    private String content;
    private LocalDateTime writeDate;
    private String deleteyn;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(LocalDateTime writeDate) {
        this.writeDate = writeDate;
    }

    public String getDeleteyn() {
        return deleteyn;
    }

    public void setDeleteyn(String deleteyn) {
        this.deleteyn = deleteyn;
    }
}
