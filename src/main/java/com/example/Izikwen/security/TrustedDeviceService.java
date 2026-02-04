package com.example.Izikwen.security;

import com.example.Izikwen.entity.TrustedDevice;
import com.example.Izikwen.repository.TrustedDeviceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TrustedDeviceService {

    private final TrustedDeviceRepository repo;

    @Value("${trusted.device.days:30}")
    private long trustedDays;

    public TrustedDeviceService(TrustedDeviceRepository repo) {
        this.repo = repo;
    }

    public boolean isTrusted(String email, String deviceId) {
        if (deviceId == null || deviceId.isBlank()) return false;

        return repo.findByUserEmailAndDeviceId(email, deviceId)
                .filter(td -> td.getExpiresAt().isAfter(Instant.now()))
                .map(td -> {
                    // update last used (optional, but nice)
                    td.setLastUsedAt(Instant.now());
                    repo.save(td);
                    return true;
                })
                .orElse(false);
    }

    // Backwards compatible
    public void register(String email, String deviceId) {
        register(email, deviceId, null, null);
    }

    // store metadata
    public void register(String email, String deviceId, String deviceName, String platform) {
        if (deviceId == null || deviceId.isBlank()) return;

        Instant expiresAt = Instant.now().plus(trustedDays, ChronoUnit.DAYS);

        TrustedDevice td = repo.findByUserEmailAndDeviceId(email, deviceId)
                .orElseGet(TrustedDevice::new);

        td.setUserEmail(email);
        td.setDeviceId(deviceId);
        td.setDeviceName(deviceName);
        td.setPlatform(platform);
        td.setLastUsedAt(Instant.now());
        if (td.getCreatedAt() == null) td.setCreatedAt(Instant.now());
        td.setExpiresAt(expiresAt);

        repo.save(td);
    }

    public List<TrustedDevice> list(String email) {
        return repo.findAllByUserEmailOrderByLastUsedAtDesc(email);
    }

    public void revoke(String email, String deviceId) {
        if (deviceId == null || deviceId.isBlank()) return;
        repo.deleteByUserEmailAndDeviceId(email, deviceId);
    }

    public void revokeAll(String email) {
        repo.deleteAllByUserEmail(email);
    }
}
