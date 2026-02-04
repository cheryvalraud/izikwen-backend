package com.example.Izikwen.security;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TwoFAService {

    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final ZxingPngQrGenerator qrGenerator = new ZxingPngQrGenerator();

    private final CodeVerifier verifier =
            new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public String generateQrCode(String email, String secret) throws QrGenerationException {

        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("Izikwen")
                .build();

        byte[] image = qrGenerator.generate(data);
        return Base64.getEncoder().encodeToString(image);
    }

    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }
}
