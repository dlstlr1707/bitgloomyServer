package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.Order;
import com.bitgloomy.server.domain.PaymentInfo;
import com.bitgloomy.server.dto.RequestPreparationDTO;
import com.bitgloomy.server.dto.RequestValidateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void savePayment(RequestPreparationDTO requestDTO);
    boolean saveOrder(Order order);
    PaymentInfo findPaymentById(String merchantUid);
    boolean modifyPayment(PaymentInfo paymentInfo);
}
