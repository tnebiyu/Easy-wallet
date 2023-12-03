package com.nebiyu.Kelal.dto.request;

import lombok.Data;

@Data
public class OtpValidationRequest {
    private String phone;
    private String otp;
}
