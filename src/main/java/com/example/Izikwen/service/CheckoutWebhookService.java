package com.example.Izikwen.service;

import com.example.Izikwen.entity.Order;
import com.example.Izikwen.repository.OrderRepository;
import com.example.Izikwen.enums.OrderStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service

public class CheckoutWebhookService {

    public CheckoutWebhookService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${checkout.webhook-secret}")
    private String webhookSecret;

    public void processWebhook(String payload, String signature) {

        // 1️⃣ Verify signature
        if (!isValidSignature(payload, signature)) {
            throw new RuntimeException("Invalid webhook signature");
        }

        try {
            JsonNode root = objectMapper.readTree(payload);

            String eventType = root.get("type").asText();
            JsonNode data = root.get("data");

            String reference = data.get("reference").asText();

            Order order = orderRepository
                    .findByReference(reference)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            //  Handle events
            switch (eventType) {

                case "payment_approved":
                    order.setStatus(OrderStatus.PAID);
                    break;

                case "payment_declined":
                case "payment_failed":
                    order.setStatus(OrderStatus.FAILED);
                    break;

                default:
                    return;
            }

            orderRepository.save(order);

        } catch (Exception e) {
            throw new RuntimeException("Webhook processing failed", e);
        }
    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            String computed =
                    Base64.getEncoder().encodeToString(hash);

            return computed.equals(signature);

        } catch (Exception e) {
            return false;
        }
    }
}