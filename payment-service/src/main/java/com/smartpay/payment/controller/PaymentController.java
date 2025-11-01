package com.smartpay.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartpay.payment.dto.PaymentRequest;
import com.smartpay.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody PaymentRequest request) {

        String txId = paymentService.processPayment(request);

        return ResponseEntity.ok().body(
                new Object() {
                    public String message = "payment_success";
                    public String transactionId = txId;
                }
        );
    }
}
