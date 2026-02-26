package com.example.Izikwen.controller.dto;

import com.example.Izikwen.entity.Payment;
import com.example.Izikwen.service.CheckoutPaymentService;
import com.example.Izikwen.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
     private final CheckoutPaymentService checkoutPaymentService;
    public PaymentController(PaymentService paymentService, CheckoutPaymentService checkoutPaymentService) {
        this.paymentService = paymentService;
        this.checkoutPaymentService = checkoutPaymentService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Map<String, Object> request) {

        Long amount = Long.valueOf(request.get("amount").toString());

        String response = checkoutPaymentService.createHostedPayment(amount).toString();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/confirm")
    public Payment confirm(@PathVariable Long paymentId) {
        return paymentService.confirmPayment(paymentId);
    }

    @PostMapping("/{paymentId}/fail")
    public Payment fail(@PathVariable Long paymentId) {
        return paymentService.failPayment(paymentId);
    }

    @PostMapping("/{paymentId}/refund")
    public Payment refund(@PathVariable Long paymentId) {
        return paymentService.refundPayment(paymentId);
    }
}