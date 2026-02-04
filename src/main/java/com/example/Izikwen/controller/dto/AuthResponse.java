package com.example.Izikwen.controller.dto;

public class AuthResponse {

    private String token;
    private Long userId;
    private String email;
    private String firstName;

    public AuthResponse(String token, Long userId, String email, String firstName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
}