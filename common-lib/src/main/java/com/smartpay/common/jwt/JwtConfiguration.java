package com.smartpay.common.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

    @Bean
    public JwtProvider jwtProvider(JwtProperties properties) {
        return new JwtProvider(properties.getSecret(), properties.getExpiration());
    }
}
