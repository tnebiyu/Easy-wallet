package com.nebiyu.Kelal.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponse {
    private boolean error;
    private String error_msg;
    private OtpResponse.Message message;

    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class Message {
       private com.twilio.rest.api.v2010.account.Message message;
    }
}
