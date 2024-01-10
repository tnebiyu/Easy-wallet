package com.nebiyu.Kelal.dao;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequestWithPhone {
    private String senderPhone;
    private String receiverPhone;
    private BigDecimal amount;
}
