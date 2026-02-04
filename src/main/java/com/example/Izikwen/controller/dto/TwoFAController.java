package com.example.Izikwen.controller.dto;

import com.example.Izikwen.entity.User;
import com.example.Izikwen.repository.UserRepository;
import com.example.Izikwen.security.JwtService;
import com.example.Izikwen.security.TrustedDeviceService;
import com.example.Izikwen.security.TwoFAService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth/2fa")
public class TwoFAController {

    private final TwoFAService twoFAService;
    private final JwtService jwtService;
    private final TrustedDeviceService trustedDeviceService;
    private final UserRepository userRepository;

    public TwoFAController(
            TwoFAService twoFAService,
            JwtService jwtService,
            TrustedDeviceService trustedDeviceService,
            UserRepository userRepository
    ) {
        this.twoFAService = twoFAService;
        this.jwtService = jwtService;
        this.trustedDeviceService = trustedDeviceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/verify")
    public Map<String, String> verify(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User user
    ) {

        String code = body.get("code");
        String deviceId = body.get("deviceId");
        String deviceName = body.get("deviceName");
        String platform = body.get("platform");

        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing OTP code");
        }

        if (deviceId == null || deviceId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing device id");
        }

        if (!twoFAService.verifyCode(user.getTwoFactorSecret(), code)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP");
        }

        //  Register trusted device with metadata
        trustedDeviceService.register(
                user.getEmail(),
                deviceId,
                deviceName,
                platform
        );

        String access = jwtService.generateAccessToken(
                user.getEmail(),
                user.getTokenVersion()
        );

        String refresh = jwtService.generateRefreshToken(
                user.getEmail(),
                user.getTokenVersion()
        );

        return Map.of(
                "accessToken", access,
                "refreshToken", refresh
        );
    }
}
