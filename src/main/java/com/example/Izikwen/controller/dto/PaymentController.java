package com.example.Izikwen.controller.dto;

import com.example.Izikwen.entity.Payment;
import com.example.Izikwen.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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