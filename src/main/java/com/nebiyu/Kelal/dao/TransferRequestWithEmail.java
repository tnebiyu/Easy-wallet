package com.nebiyu.Kelal.dao;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestWithEmail {

    private String senderEmail;
    private String receiverEmail;
    private BigDecimal amount;
}
