package com.example.Izikwen.service;

import com.example.Izikwen.entity.Order;
import com.example.Izikwen.entity.Payment;
import com.example.Izikwen.entity.Transaction;
import com.example.Izikwen.enums.OrderStatus;
import com.example.Izikwen.enums.PaymentStatus;
import com.example.Izikwen.enums.TransactionStatus;
import com.example.Izikwen.repository.OrderRepository;
import com.example.Izikwen.repository.PaymentRepository;
import com.example.Izikwen.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            TransactionRepository transactionRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Payment confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            return payment; // already handled
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        payment.setProviderTxId("SIM-" + UUID.randomUUID());

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);

        // "Settle" transaction (simulate sending crypto)
        Transaction tx = transactionRepository.findAll().stream()
                .filter(t -> t.getOrder().getId().equals(order.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transaction not found for order"));

        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setTxHash("0xSIMULATED_" + UUID.randomUUID());

        order.setStatus(OrderStatus.COMPLETED);

        // Save
        orderRepository.save(order);
        transactionRepository.save(tx);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment failPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAYMENT_FAILED);

        orderRepository.save(order);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.REFUNDED);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.REFUNDED);

        orderRepository.save(order);
        return paymentRepository.save(payment);
    }
}
