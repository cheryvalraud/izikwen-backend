package com.example.Izikwen.controller.dto;

import com.example.Izikwen.controller.dto.BuyRequest;
import com.example.Izikwen.controller.dto.BuyResponse;
import com.example.Izikwen.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/buy")
    public BuyResponse buy(
            @RequestBody BuyRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        return orderService.buy(userId, request);
    }

}