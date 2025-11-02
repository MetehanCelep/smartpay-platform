package com.smartpay.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class GatewayController {

    @GetMapping("/health")
    public Mono<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "api-gateway");
        return Mono.just(response);
    }

    @GetMapping("/")
    public Mono<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "SmartPay API Gateway");
        response.put("version", "1.0.0");
        return Mono.just(response);
    }
}
