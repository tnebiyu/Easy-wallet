package com.nebiyu.Kelal.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.TransactionModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private AuthenticationResponse.Data data;
    private boolean error;
    private String error_msg;


    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class Data {
        private AuthenticationResponse.UserData user_data;
    }

    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {
        private String access_token;
        private String email;
        private Long user_id;
        private String password;
        private BigDecimal balance;
        private List<TransactionModel> sentTransaction;
        private List<TransactionModel> receivedTransaction;
    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdminResponse {
        private String status;
        private String email;
        private Long user_id;
        private BigDecimal balance;
        private Role role;
    }

}
