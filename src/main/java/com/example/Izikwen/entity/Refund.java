package com.example.Izikwen.entity;

import com.example.Izikwen.enums.RefundStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Payment payment;

    private String providerRefundId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public Payment getPayment() { return payment; }
    public String getProviderRefundId() { return providerRefundId; }
    public BigDecimal getAmount() { return amount; }
    public RefundStatus getStatus() { return status; }

    public void setPayment(Payment payment) { this.payment = payment; }
    public void setProviderRefundId(String providerRefundId) { this.providerRefundId = providerRefundId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setStatus(RefundStatus status) { this.status = status; }
}