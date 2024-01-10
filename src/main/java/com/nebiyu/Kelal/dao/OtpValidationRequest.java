package com.nebiyu.Kelal.dao;

import lombok.Data;

@Data
public class OtpValidationRequest {
    private String phone;
    private String otp;
}
