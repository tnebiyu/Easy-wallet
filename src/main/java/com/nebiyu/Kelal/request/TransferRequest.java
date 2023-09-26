package com.nebiyu.Kelal.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private Long senderId;
    private String email;
    private Long receiverId;
    private BigDecimal amount;
}
