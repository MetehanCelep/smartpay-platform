package com.smartpay.auth.service;

import com.smartpay.auth.entity.Merchant;
import com.smartpay.auth.repository.MerchantRepository;
import com.smartpay.common.enums.Status;
import com.smartpay.common.exception.GlobalErrorCode;
import com.smartpay.common.exception.SmartPayException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant register(String email, String merchantName, String passwordHash) {

        if (merchantRepository.existsByEmail(email)) {
            throw new SmartPayException(GlobalErrorCode.MERCHANT_ALREADY_EXISTS, "Merchant already exists");
        }

        Merchant merchant = new Merchant();
        merchant.setEmail(email);
        merchant.setMerchantName(merchantName);
        merchant.setPasswordHash(passwordHash);
        merchant.setStatus(Status.ACTIVE);
        merchant.setCreatedAt(LocalDateTime.now());

        return merchantRepository.save(merchant);
    }

    public Merchant findByEmail(String email) {
        return merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new SmartPayException(GlobalErrorCode.USER_NOT_FOUND, "User not found"));
    }
}
