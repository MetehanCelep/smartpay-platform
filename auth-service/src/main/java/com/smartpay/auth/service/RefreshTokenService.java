package com.smartpay.auth.service;

import com.smartpay.auth.entity.Merchant;
import com.smartpay.auth.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Merchant merchant);

    RefreshToken verifyRefreshToken(String token);
}
