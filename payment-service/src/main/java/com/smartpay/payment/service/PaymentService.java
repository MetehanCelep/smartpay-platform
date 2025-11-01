package com.smartpay.payment.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smartpay.payment.dto.PaymentRequest;

@Service
public class PaymentService {

    public String processPayment(PaymentRequest request) {

        String txId = UUID.randomUUID().toString();
        System.out.println("PAYMENT SUCCESS => " + txId);

        return txId;
    }
}
