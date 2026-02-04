package com.example.Izikwen.service;

import com.example.Izikwen.controller.dto.BuyRequest;
import com.example.Izikwen.controller.dto.BuyResponse;
import com.example.Izikwen.entity.*;
import com.example.Izikwen.enums.OrderStatus;
import com.example.Izikwen.enums.TransactionStatus;
import com.example.Izikwen.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderService {

    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public OrderService(
            AssetRepository assetRepository,
            OrderRepository orderRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository
    ) {
        this.assetRepository = assetRepository;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public BuyResponse buy(Long userId, BuyRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset asset = assetRepository.findBySymbol(req.assetSymbol)
                .orElseThrow(() -> new RuntimeException("Unknown asset"));

        if (!asset.isEnabled()) {
            throw new RuntimeException("Asset disabled");
        }

        // Pricing logic (TEMP)
        // 1 USDT = 1 USD
        BigDecimal cryptoAmount = req.amountFiat
                .setScale(8, RoundingMode.DOWN);

        Order order = new Order();
        order.setUser(user);
        order.setAsset(asset);
        order.setAmountFiat(req.amountFiat);
        order.setAmountCrypto(cryptoAmount);
        order.setFiatCurrency(req.fiatCurrency);
        order.setWalletAddress(req.walletAddress);
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        order = orderRepository.save(order);

        Transaction tx = new Transaction();
        tx.setOrder(order);
        tx.setNetwork(String.valueOf(req.network));
        tx.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(tx);

        return new BuyResponse(
                order.getId(),
                order.getStatus(),
                cryptoAmount,
                "Order created. Waiting for payment confirmation."
        );
    }
}
