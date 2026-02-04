package com.example.Izikwen.entity;

import com.example.Izikwen.enums.TransactionStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private String txHash;
    private String network;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public String getTxHash() { return txHash; }
    public String getNetwork() { return network; }
    public TransactionStatus getStatus() { return status; }

    public void setOrder(Order order) { this.order = order; }
    public void setTxHash(String txHash) { this.txHash = txHash; }
    public void setNetwork(String network) { this.network = network; }
    public void setStatus(TransactionStatus status) { this.status = status; }
}
