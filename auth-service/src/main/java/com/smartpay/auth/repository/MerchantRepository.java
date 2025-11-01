package com.smartpay.auth.repository;

import com.smartpay.auth.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    Optional<Merchant> findByEmail(String email);
    boolean existsByEmail(String email);
}
