package com.smartpay.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartpay.auth.dto.AuthResponse;
import com.smartpay.auth.entity.RefreshToken;
import com.smartpay.auth.security.JwtProvider;
import com.smartpay.auth.service.RefreshTokenService;
import com.smartpay.common.dto.BaseResponse;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public RefreshTokenController(RefreshTokenService refreshTokenService,
            JwtProvider jwtProvider) {
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<AuthResponse>> refresh(@RequestParam("refreshToken") String refreshToken) {

        RefreshToken rt = refreshTokenService.verifyRefreshToken(refreshToken);

        String newAccessToken = jwtProvider.generateToken(rt.getEmail());

        AuthResponse resp = new AuthResponse(newAccessToken, refreshToken, "Bearer", rt.getEmail());

        return ResponseEntity.ok(new BaseResponse<>("success", resp));
    }

}
