package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.DisplayOrder;
import com.bitgloomy.server.domain.Order;
import com.bitgloomy.server.domain.PaymentInfo;
import com.bitgloomy.server.dto.RequestSaveOrderDTO;
import com.bitgloomy.server.dto.RequestSavePaymentDTO;
import com.bitgloomy.server.dto.RequestValidateDTO;
import com.bitgloomy.server.dto.RequestPreparationDTO;
import com.bitgloomy.server.service.OrderService;
import com.siot.IamportRestClient.response.Payment;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


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
    @PatchMapping("/payment")
    public ResponseEntity<?> modifyPayment(@RequestBody RequestSavePaymentDTO requestSavePaymentDTO){
        try {
            orderService.modifyPayment(requestSavePaymentDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/save/order")
    public ResponseEntity<?> saveOrder(@RequestBody ArrayList<RequestSaveOrderDTO> requestSaveOrderDTO){
        try {
            orderService.saveOrderData(requestSaveOrderDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/orders/{uid}")
    public ResponseEntity<?> displayOrder(@PathVariable(value = "uid")String uid){
        try {
            ArrayList<DisplayOrder> results = orderService.displayOrders(Integer.parseInt(uid));
            return ResponseEntity.status(HttpStatus.OK).body(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
