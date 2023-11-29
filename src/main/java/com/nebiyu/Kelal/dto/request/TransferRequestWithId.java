package com.nebiyu.Kelal.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestWithId {
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;

}
