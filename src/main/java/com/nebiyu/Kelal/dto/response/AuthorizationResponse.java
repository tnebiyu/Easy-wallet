package com.nebiyu.Kelal.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AuthorizationResponse {
    private Data data;
    private boolean error;
    private String error_msg;


@lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class Data {
        private UserData user_data;
    }

@lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {
        private String email;
        private Long user_id;
        private String firstName;
        private String lastName;
        private String password;
        private BigDecimal balance;

    }
}

