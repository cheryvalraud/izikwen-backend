package com.example.Izikwen.controller.dto;

import com.example.Izikwen.dto.ChangePasswordRequest;
import com.example.Izikwen.entity.User;
import com.example.Izikwen.repository.UserRepository;
import com.example.Izikwen.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityController(PasswordEncoder encoder, UserRepository userRepository, JwtService jwtService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Real change password:
     * - verify current password
     * - set new password
     * - increment tokenVersion (kills old tokens on all devices)
     * - return fresh tokens so user remains logged in
     */
    @PostMapping("/change-password")
    public Map<String, String> changePassword(
            @RequestBody ChangePasswordRequest req,
            @AuthenticationPrincipal User user
    ) {
        if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank()
                || req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing password fields");
        }

        if (!encoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        String newPass = req.getNewPassword();

        if (newPass.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
        }

        // Save new password
        user.setPassword(encoder.encode(newPass));

        //Invalidate old tokens everywhere
        user.incrementTokenVersion();

        userRepository.save(user);

        // Issue fresh tokens (same session continues, old tokens die)
        String access = jwtService.generateAccessToken(user.getEmail(), user.getTokenVersion());
        String refresh = jwtService.generateRefreshToken(user.getEmail(), user.getTokenVersion());

        return Map.of(
                "message", "Password updated",
                "accessToken", access,
                "refreshToken", refresh
        );
    }
}
