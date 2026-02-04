package com.example.Izikwen.websocket;

public class OrderWsEvents {

    public record OrderUpdated(Object order) {}

    public static OrdersWebSocketHandler.WsMessage orderUpdated(Object orderDto) {
        return new OrdersWebSocketHandler.WsMessage("ORDER_UPDATED", new OrderUpdated(orderDto));
    }
}
