package com.nebiyu.Kelal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String accessKey;
    private String secretKey;
    private String from;
    private String to;
    private String message;
//    private String callbackUrl;
}
