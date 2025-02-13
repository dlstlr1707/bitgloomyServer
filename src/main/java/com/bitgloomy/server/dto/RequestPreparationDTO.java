package com.bitgloomy.server.dto;

import java.math.BigDecimal;

public class RequestPreparationDTO {
    private String merchantUid;
    private BigDecimal totalPrice;

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
