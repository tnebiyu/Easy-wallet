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
public class NotifyResponse {
    private boolean success;
    private String message;
    private int code;
    private OtpResponse.Result result;
    private long timestamp;

    @Data
    public static class Result {
        private String to;
        private String smsId;
        private String sendResult;
    }
}
