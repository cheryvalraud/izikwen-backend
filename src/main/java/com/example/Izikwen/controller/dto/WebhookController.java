package com.example.Izikwen.controller.dto;

import com.example.Izikwen.service.CheckoutWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")

public class WebhookController {

    public WebhookController(CheckoutWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    private final CheckoutWebhookService webhookService;

    @PostMapping("/checkout")
    public ResponseEntity<?> handleCheckoutWebhook(
            @RequestBody(required = false) String payload,
            @RequestHeader(value = "Cko-Signature", required = false) String signature
    ) {

        System.out.println("Webhook received");
        System.out.println("Payload: " + payload);
        System.out.println("Signature: " + signature);

        return ResponseEntity.ok().build();
    }
}