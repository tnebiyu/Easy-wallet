package com.nebiyu.Kelal.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RefreshTokenRequest {
    private String token;
}
