package com.smartpay.auth.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.smartpay.auth.entity.RefreshToken;
import com.smartpay.auth.repository.RefreshTokenRepository;
import com.smartpay.auth.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(com.smartpay.auth.entity.Merchant merchant) {
        RefreshToken token = new RefreshToken();
        token.setToken(java.util.UUID.randomUUID().toString());
        token.setEmail(merchant.getEmail());
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("refresh token not found"));

        if (rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("refresh token expired");
        }

        return rt;
    }
}
