package com.nebiyu.Kelal.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nebiyu.Kelal.model.Role;
import com.nebiyu.Kelal.model.TransactionModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Response.Data data;
    private boolean error;
    private String error_msg;


    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class Data {
        private Response.UserData user_data;
        private Response.AdminResponse adminResponse;
        private Response.TopUpResponse topUpResponse;
        private Response.ResponseOtp responseOtp;
        private Response.VerifyOtp verifyOtp;
        private Response.AdminCreated adminCreated;
        private Response.ResetPassword resetPassword;
        private Response.AuthorizationResponse authorizationResponse;
        private Response.ChangePassword changePassword;
        private Response.RefreshToken refreshToken;
    }

    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {
        private String access_token;
        private String email;
        private String phoneNumber;
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
        private String newPassword;
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
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdminCreated {
        private boolean isAdminCreated;
        private String adminEmail;
        private String adminPassword;
        private String adminFirstName;
        private String adminLastName;
        private String createdBy;
        private Date timeStamp;

    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResetPassword {
        private boolean success;
        private String status;
        private String newPassword;


    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationResponse {
        private String email;
        private Long user_id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String password;
        private BigDecimal balance;


    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RefreshToken {
        private String token;
        private String refreshToken;



    }


}
