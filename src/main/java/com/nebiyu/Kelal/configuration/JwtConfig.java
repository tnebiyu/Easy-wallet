package com.nebiyu.Kelal.configuration;

import com.nebiyu.Kelal.services.util.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
@Configuration
public class JwtConfig {
    private String jwtSecret = "addkBBCDKe12312388u452345asdkfpwqpohadejBCidKlmppwernkeJJSc991l"; // Set your secret key here

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey());
    }
}
