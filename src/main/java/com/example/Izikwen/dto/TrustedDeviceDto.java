package com.example.Izikwen.dto;

import java.time.Instant;

public class TrustedDeviceDto {
    public String deviceId;
    public String deviceName;
    public String platform;
    public Instant createdAt;
    public Instant lastUsedAt;
    public Instant expiresAt;

    public TrustedDeviceDto(String deviceId, String deviceName, String platform,
                            Instant createdAt, Instant lastUsedAt, Instant expiresAt) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.platform = platform;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
        this.expiresAt = expiresAt;
    }
}
