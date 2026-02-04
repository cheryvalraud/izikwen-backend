package com.example.Izikwen.controller.dto;

import com.example.Izikwen.enums.OrderStatus;

import java.math.BigDecimal;

public class BuyResponse {
    public Long orderId;
    public OrderStatus status;
    public BigDecimal amountAsset;
    public String message;

    public BuyResponse(Long orderId, OrderStatus status, BigDecimal amountAsset, String message) {
        this.orderId = orderId;
        this.status = status;
        this.amountAsset = amountAsset;
        this.message = message;
    }
}