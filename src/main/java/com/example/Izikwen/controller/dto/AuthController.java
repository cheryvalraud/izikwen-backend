package com.example.Izikwen.controller.dto;

import com.example.Izikwen.dto.*;
import com.example.Izikwen.entity.User;
import com.example.Izikwen.repository.UserRepository;
import com.example.Izikwen.security.JwtService;
import com.example.Izikwen.security.TrustedDeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final TrustedDeviceService trustedDeviceService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder encoder,
            JwtService jwtService,
            TrustedDeviceService trustedDeviceService
    ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.trustedDeviceService = trustedDeviceService;
    }

    // ---------------- REGISTER ----------------

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest req) {

        if (req.getEmail() == null || req.getEmail().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email or password");
        }

        String email = req.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(req.getPassword()));
        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);


        userRepository.save(user);
    }

    // ---------------- LOGIN ----------------

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {

        String email = req.getEmail() == null ? null : req.getEmail().trim().toLowerCase();
        String password = req.getPassword();
        String deviceId = req.getDeviceId();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing credentials");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        //  2FA enabled → trusted device bypass
        if (user.isTwoFactorEnabled()) {

            if (trustedDeviceService.isTrusted(user.getEmail(), deviceId)) {

                String access = jwtService.generateAccessToken(
                        user.getEmail(),
                        user.getTokenVersion()
                );

                String refresh = jwtService.generateRefreshToken(
                        user.getEmail(),
                        user.getTokenVersion()
                );

                return new LoginResponse(access, refresh, false, null);
            }

            // Require OTP
            String twoFaToken = jwtService.generateTwoFaToken(user.getEmail());
            return new LoginResponse(null, null, true, twoFaToken);
        }

        // Normal login
        String access = jwtService.generateAccessToken(
                user.getEmail(),
                user.getTokenVersion()
        );

        String refresh = jwtService.generateRefreshToken(
                user.getEmail(),
                user.getTokenVersion()
        );

        return new LoginResponse(access, refresh, false, null);
    }

    // ---------------- REFRESH TOKEN ----------------

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestHeader("Authorization") String header) {

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing refresh token");
        }

        String token = header.substring(7);

        if (!jwtService.isRefreshToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        long tokenVersion = jwtService.extractTokenVersion(token);

        if (tokenVersion != user.getTokenVersion()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token revoked");
        }

        String newAccess = jwtService.generateAccessToken(
                email,
                user.getTokenVersion()
        );

        return Map.of("accessToken", newAccess);
    }

    // ---------------- LOGOUT ----------------

    @PostMapping("/logout")
    public Map<String, String> logout() {
        return Map.of("message", "Logged out");
    }

    // ---------------- LOGOUT ALL DEVICES ----------------

    @PostMapping("/logout-all")
    public void logoutAll(@AuthenticationPrincipal User user) {
        user.incrementTokenVersion();
        userRepository.save(user);
    }
}
