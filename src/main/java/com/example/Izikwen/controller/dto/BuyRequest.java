package com.example.Izikwen.controller.dto;

import com.example.Izikwen.enums.Network;

import java.math.BigDecimal;

public class BuyRequest {
    public String assetSymbol;      // "USDT"
    public Network network;         // TRC20
    public String walletAddress;    // user wallet
    public String fiatCurrency;     // "USD"
    public BigDecimal amountFiat;   // 50.00
    public String provider;         // "MONCASH"
}