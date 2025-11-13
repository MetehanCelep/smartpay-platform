package com.smartpay.auth.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartpay.auth.dto.AuthResponse;
import com.smartpay.auth.dto.LoginRequest;
import com.smartpay.auth.dto.RegisterRequest;
import com.smartpay.auth.entity.Merchant;
import com.smartpay.auth.entity.RefreshToken;
import com.smartpay.auth.repository.MerchantRepository;
import com.smartpay.auth.repository.RefreshTokenRepository;
import com.smartpay.auth.service.AuthService;
import com.smartpay.common.enums.Status;
import com.smartpay.common.jwt.JwtProvider;

@Service
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(
            MerchantRepository merchantRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.merchantRepository = merchantRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        merchantRepository.findByEmail(request.getEmail()).ifPresent(m -> {
            throw new RuntimeException("Email already exists");
        });

        Merchant merchant = new Merchant();
        merchant.setEmail(request.getEmail());
        merchant.setMerchantName(request.getMerchantName());
        merchant.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        merchant.setPhone(request.getPhone());
        merchant.setStatus(Status.ACTIVE);
        merchant.setCreatedAt(LocalDateTime.now());

        merchantRepository.save(merchant);

        String accessToken = jwtProvider.generateToken(merchant.getEmail());

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setEmail(merchant.getEmail());
        refresh.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refresh);

        return new AuthResponse(
            accessToken, 
            refresh.getToken(), 
            "Bearer", 
            merchant.getEmail(),
            merchant.getId().toString(),
            merchant.getMerchantName() 
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Merchant merchant = merchantRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), merchant.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateToken(merchant.getEmail());

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setEmail(merchant.getEmail());
        refresh.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refresh);

        return new AuthResponse(
            accessToken, 
            refresh.getToken(), 
            "Bearer", 
            merchant.getEmail(),
            merchant.getId().toString(),
            merchant.getMerchantName() 
        );
    }
}
