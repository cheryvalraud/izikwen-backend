package com.example.Izikwen.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OrdersWebSocketHandler ordersWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(OrdersWebSocketHandler ordersWebSocketHandler,
                           JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.ordersWebSocketHandler = ordersWebSocketHandler;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ordersWebSocketHandler, "/ws/orders")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*"); // dev only. lock down later.
    }
}
