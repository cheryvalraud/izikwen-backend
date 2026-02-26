package com.example.Izikwen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service

public class CheckoutPaymentService {

    private final WebClient webClient;

    public CheckoutPaymentService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${checkout.secret-key}")
    private String secretKey;

    @Value("${checkout.processing-channel-id}")
    private String processingChannelId;

    public Map<String, Object> createHostedPayment(Long amount) {

        String url = "https://api.sandbox.checkout.com/payment-sessions";

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("currency", "USD");
        body.put("reference", "IZIKWEN-" + System.currentTimeMillis());

        Map<String, Object> response = webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + secretKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response;
    }
}