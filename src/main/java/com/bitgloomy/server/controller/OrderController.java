package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.Order;
import com.bitgloomy.server.domain.PaymentInfo;
import com.bitgloomy.server.dto.RequestValidateDTO;
import com.bitgloomy.server.dto.RequestPreparationDTO;
import com.bitgloomy.server.service.OrderService;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/prepare")
    public ResponseEntity<?> preparePayment(@RequestBody RequestPreparationDTO requestPreparationDTO) {
        try {
            String resultMerchantUid = orderService.prepareValid(requestPreparationDTO);
            return ResponseEntity.status(HttpStatus.OK).body(resultMerchantUid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/order/validate")
    public ResponseEntity<?> validatePayment(@RequestBody RequestValidateDTO request){
        try {
            Payment payment = orderService.validatePayment(request);
            return ResponseEntity.status(HttpStatus.OK).body(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/save/payment")
    public ResponseEntity<?> savePayment(@RequestBody PaymentInfo payment){
        try {

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/save/order")
    public ResponseEntity<?> saveOrder(@RequestBody Order order){
        try {

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
