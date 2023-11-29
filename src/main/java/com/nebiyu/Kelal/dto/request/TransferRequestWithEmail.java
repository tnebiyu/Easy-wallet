package com.nebiyu.Kelal.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestWithEmail {

    private String senderEmail;
    private String receiverEmail;
    private BigDecimal amount;
}
