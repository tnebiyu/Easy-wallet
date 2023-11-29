package com.nebiyu.Kelal.dto.response;

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
        private AuthenticationResponse.AdminResponse adminResponse;
        private AuthenticationResponse.TopUpResponse topUpResponse;
        private AuthenticationResponse.ResponseOtp responseOtp;
        private AuthenticationResponse.VerifyOtp verifyOtp;
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
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TopUpResponse {
        private String status;
        private String superAdminEmail;
        private String userEmail;
        private BigDecimal newBalance;
    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangePassword {
        private String status;
        private String email;
        private String password;
        private BigDecimal newPassword;
    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseOtp {
        private String status;
        private String otp;


    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VerifyOtp {
        private boolean isCorrect;
        private String otp;
        private String phone;

    }


}
