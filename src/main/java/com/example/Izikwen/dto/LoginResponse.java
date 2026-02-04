package com.example.Izikwen.dto;

public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private boolean requires2FA;
    private String twoFaToken;

    public LoginResponse(String accessToken, String refreshToken, boolean requires2FA, String twoFaToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.requires2FA = requires2FA;
        this.twoFaToken = twoFaToken;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public boolean isRequires2FA() { return requires2FA; }
    public String getTwoFaToken() { return twoFaToken; }
}
