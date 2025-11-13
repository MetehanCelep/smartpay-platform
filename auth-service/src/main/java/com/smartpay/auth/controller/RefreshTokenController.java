package com.smartpay.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartpay.auth.dto.AuthResponse;
import com.smartpay.auth.entity.Merchant;
import com.smartpay.auth.entity.RefreshToken;
import com.smartpay.auth.service.MerchantService;
import com.smartpay.auth.service.RefreshTokenService;
import com.smartpay.common.dto.BaseResponse;
import com.smartpay.common.jwt.JwtProvider;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final MerchantService merchantService;

    public RefreshTokenController(RefreshTokenService refreshTokenService,
            JwtProvider jwtProvider,
            MerchantService merchantService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
        this.merchantService = merchantService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<AuthResponse>> refresh(@RequestParam("refreshToken") String refreshToken) {

        RefreshToken rt = refreshTokenService.verifyRefreshToken(refreshToken);

        Merchant merchant = merchantService.findByEmail(rt.getEmail());

        String newAccessToken = jwtProvider.generateToken(rt.getEmail());

        AuthResponse resp = new AuthResponse(
            newAccessToken, 
            refreshToken, 
            "Bearer", 
            merchant.getEmail(),
            merchant.getId().toString(),
            merchant.getMerchantName()
        );

        return ResponseEntity.ok(new BaseResponse<>("success", resp));
    }

}