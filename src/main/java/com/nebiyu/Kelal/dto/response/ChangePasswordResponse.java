package com.nebiyu.Kelal.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordResponse {
    private boolean error;
    private String error_msg;
    private ChangePasswordResponse.Data data;




    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data{
        ChangePasswordResponse.ChangePassword changePassword;
    }



    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangePassword{
        private String email;
        private String password;
        private String newPassword;
    }
}
