package com.bitgloomy.server.dto;

public class RequestNoticeInfoDTO {
    private int page;
    private int displayPageAmount;
    private String type;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getDisplayPageAmount() {
        return displayPageAmount;
    }

    public void setDisplayPageAmount(int displayPageAmount) {
        this.displayPageAmount = displayPageAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
