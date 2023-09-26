package com.nebiyu.Kelal.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponse {
    private TransferResponse.Data data;
    private boolean error;
    private String error_msg;


    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class Data {
        private TransferResponse.UserData user_data;
    }

    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {
        private String access_token;
        private String senderEmail;
        private String receiverEmail;
        private Long user_id;
        private String password;
        private BigDecimal balance;
    }

}
