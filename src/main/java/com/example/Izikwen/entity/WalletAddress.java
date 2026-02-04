package com.example.Izikwen.entity;

import com.example.Izikwen.enums.Network;
import jakarta.persistence.*;

@Entity
@Table(name = "wallet_addresses")
public class WalletAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Network network;

    @Column(nullable=false)
    private String address;

    @ManyToOne(optional=false)
    private User user;

    public Long getId() { return id; }
    public Network getNetwork() { return network; }
    public String getAddress() { return address; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setNetwork(Network network) { this.network = network; }
    public void setAddress(String address) { this.address = address; }
    public void setUser(User user) { this.user = user; }
}