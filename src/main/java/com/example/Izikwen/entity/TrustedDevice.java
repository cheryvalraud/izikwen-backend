package com.example.Izikwen.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "trusted_devices",
        indexes = {
                @Index(name = "idx_td_user_email", columnList = "userEmail"),
                @Index(name = "idx_td_user_email_device_id", columnList = "userEmail,deviceId")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_td_user_email_device_id", columnNames = {"userEmail", "deviceId"})
        }
)
public class TrustedDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false, length = 128)
    private String deviceId;

    @Column(length = 120)
    private String deviceName; // e.g. "Valraud’s iPhone"

    @Column(length = 50)
    private String platform;   // "ios" | "android" | "web"

    private Instant createdAt = Instant.now();

    private Instant lastUsedAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt;

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getDeviceId() { return deviceId; }
    public String getDeviceName() { return deviceName; }
    public String getPlatform() { return platform; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastUsedAt() { return lastUsedAt; }
    public Instant getExpiresAt() { return expiresAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
