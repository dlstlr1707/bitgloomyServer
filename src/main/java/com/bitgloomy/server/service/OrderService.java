package com.bitgloomy.server.service;


import com.bitgloomy.server.domain.PaymentInfo;
import com.bitgloomy.server.dto.RequestValidateDTO;
import com.bitgloomy.server.dto.RequestPreparationDTO;
import com.bitgloomy.server.mybatis.OrderMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
public class OrderService {
    @Value("${Iamport.rest.api.key}")
    private String REST_API_KEY;

    @Value("${Iamport.rest.api.secret}")
    private String REST_API_SECRET;

    private IamportClient api;
    private OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    // @PostConstruct 어노테이션을 사용하여 객체 초기화
    @PostConstruct
    public void init() {
        // @Value가 주입된 후 IamportClient 객체 초기화
        this.api = new IamportClient(REST_API_KEY, REST_API_SECRET);
    }

    public String prepareValid(RequestPreparationDTO requestDTO)throws Exception {
        PrepareData prepareData = new PrepareData(requestDTO.getMerchantUid(), requestDTO.getTotalPrice());

        api.postPrepare(prepareData);

        orderMapper.savePayment(requestDTO); // 주문번호와 결제예정 금액 DB 저장
        return requestDTO.getMerchantUid();
    }
    public Payment validatePayment(RequestValidateDTO request) throws Exception {
        PaymentInfo findPaymentInfo = orderMapper.findPaymentById(request.getMerchantUid());
        if(findPaymentInfo == null){
            throw new Exception();
        }
        RequestPreparationDTO prePayment = new RequestPreparationDTO();
        prePayment.setMerchantUid(request.getMerchantUid());
        prePayment.setTotalPrice(findPaymentInfo.getPrice());

        BigDecimal preAmount = prePayment.getTotalPrice(); // DB에 저장된 결제요청 금액

        IamportResponse<Payment> iamportResponse = api.paymentByImpUid(request.getImpUid());
        BigDecimal paidAmount = iamportResponse.getResponse().getAmount(); // 사용자가 실제 결제한 금액

        if (!preAmount.equals(paidAmount)) {
            CancelData cancelData = cancelPayment(iamportResponse);
            api.cancelPaymentByImpUid(cancelData);
        }
        System.out.println("리턴전 까지 완료");
        return iamportResponse.getResponse();
    }
    public CancelData cancelPayment(IamportResponse<Payment> response) {
        return new CancelData(response.getResponse().getImpUid(), true);
    }

}
