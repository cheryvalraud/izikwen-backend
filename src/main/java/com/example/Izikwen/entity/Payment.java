package com.example.Izikwen.entity;

import com.example.Izikwen.enums.PaymentProvider;
import com.example.Izikwen.enums.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One order can have one payment in this MVP
    @OneToOne(optional = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amountFiat;

    // provider reference id (MonCash/NatCash/Card gateway id)
    private String providerTxId;

    private Instant createdAt = Instant.now();

    // getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public PaymentProvider getProvider() { return provider; }
    public PaymentStatus getStatus() { return status; }
    public BigDecimal getAmountFiat() { return amountFiat; }
    public String getProviderTxId() { return providerTxId; }
    public Instant getCreatedAt() { return createdAt; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setProvider(PaymentProvider provider) { this.provider = provider; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setAmountFiat(BigDecimal amountFiat) { this.amountFiat = amountFiat; }
    public void setProviderTxId(String providerTxId) { this.providerTxId = providerTxId; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
