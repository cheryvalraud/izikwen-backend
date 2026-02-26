package com.example.Izikwen.entity;


import com.example.Izikwen.enums.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String reference;

    @ManyToOne
    private User user;

    @ManyToOne
    private Asset asset;

    private BigDecimal amountFiat;
    private BigDecimal amountCrypto;

    private String fiatCurrency;
    private String walletAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Asset getAsset() { return asset; }
    public BigDecimal getAmountFiat() { return amountFiat; }
    public BigDecimal getAmountCrypto() { return amountCrypto; }
    public String getFiatCurrency() { return fiatCurrency; }
    public String getWalletAddress() { return walletAddress; }
    public OrderStatus getStatus() { return status; }

    public void setUser(User user) { this.user = user; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public void setAmountFiat(BigDecimal amountFiat) { this.amountFiat = amountFiat; }
    public void setAmountCrypto(BigDecimal amountCrypto) { this.amountCrypto = amountCrypto; }
    public void setFiatCurrency(String fiatCurrency) { this.fiatCurrency = fiatCurrency; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}