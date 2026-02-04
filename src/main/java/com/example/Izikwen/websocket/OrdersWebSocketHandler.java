package com.example.Izikwen.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrdersWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // email -> sessions
    private final ConcurrentHashMap<String, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String email = (String) session.getAttributes().get("email");
        if (email == null) {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No email"));
            } catch (Exception ignored) {}
            return;
        }

        userSessions.computeIfAbsent(email, k -> ConcurrentHashMap.newKeySet()).add(session);

        // optional: send a hello packet so frontend knows it’s connected
        sendToSession(session, new WsMessage("CONNECTED", null));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String email = (String) session.getAttributes().get("email");
        if (email == null) return;

        Set<WebSocketSession> set = userSessions.get(email);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) userSessions.remove(email);
        }
    }

    public void sendToUser(String email, Object payload) {
        Set<WebSocketSession> sessions = userSessions.get(email);
        if (sessions == null) return;

        String json;
        try {
            json = objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            return;
        }

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(json));
                } catch (Exception ignored) {}
            }
        }
    }

    private void sendToSession(WebSocketSession session, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(json));
        } catch (Exception ignored) {}
    }

    // helper DTO
    public record WsMessage(String type, Object data) {}
}
