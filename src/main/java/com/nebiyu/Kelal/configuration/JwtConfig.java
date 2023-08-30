package com.nebiyu.Kelal.configuration;

import com.nebiyu.Kelal.services.util.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

public class JwtConfig {
    private String jwtSecret;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey());
    }
}
