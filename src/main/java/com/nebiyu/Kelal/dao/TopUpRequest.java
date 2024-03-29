package com.nebiyu.Kelal.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUpRequest {
    private String adminEmail;
    private BigDecimal amount;
    private String userEmail;
}
