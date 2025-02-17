package com.bitgloomy.server.mybatis;

import com.bitgloomy.server.domain.DisplayOrder;
import com.bitgloomy.server.domain.Order;
import com.bitgloomy.server.domain.PaymentInfo;
import com.bitgloomy.server.dto.RequestPreparationDTO;
import com.bitgloomy.server.dto.RequestValidateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface OrderMapper {
    void savePayment(RequestPreparationDTO requestDTO);
    boolean saveOrder(Order order);
    PaymentInfo findPaymentById(String merchantUid);
    boolean modifyPayment(PaymentInfo paymentInfo);
    ArrayList<DisplayOrder> displayOrder(int uid);
}
