package com.example.Izikwen.controller.dto;

import com.example.Izikwen.dto.TrustedDeviceDto;
import com.example.Izikwen.entity.TrustedDevice;
import com.example.Izikwen.entity.User;
import com.example.Izikwen.security.TrustedDeviceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/trusted-devices")
public class TrustedDevicesController {

    private final TrustedDeviceService trustedDeviceService;

    public TrustedDevicesController(TrustedDeviceService trustedDeviceService) {
        this.trustedDeviceService = trustedDeviceService;
    }

    @GetMapping
    public List<TrustedDeviceDto> list(@AuthenticationPrincipal User user) {
        return trustedDeviceService.list(user.getEmail())
                .stream()
                .map(td -> new TrustedDeviceDto(
                        td.getDeviceId(),
                        td.getDeviceName(),
                        td.getPlatform(),
                        td.getCreatedAt(),
                        td.getLastUsedAt(),
                        td.getExpiresAt()
                ))
                .toList();
    }

    @DeleteMapping("/{deviceId}")
    public Map<String, String> revoke(@AuthenticationPrincipal User user, @PathVariable String deviceId) {
        trustedDeviceService.revoke(user.getEmail(), deviceId);
        return Map.of("message", "Device revoked");
    }

    @DeleteMapping
    public Map<String, String> revokeAll(@AuthenticationPrincipal User user) {
        trustedDeviceService.revokeAll(user.getEmail());
        return Map.of("message", "All trusted devices revoked");
    }
}
