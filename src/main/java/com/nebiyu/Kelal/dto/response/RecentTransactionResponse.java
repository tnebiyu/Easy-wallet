package com.nebiyu.Kelal.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Builder
@Data

@AllArgsConstructor
public class RecentTransactionResponse {
    private Data data;



    private boolean error;
    private String error_msg;

    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        private UserData userData;


    }


    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserData {

        private List<SentTransaction> sent;

        private List<ReceivedTransaction> received;
    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SentTransaction {
        private String receiver;
        private BigDecimal balance;
        private Date timestamp;

    }
    @lombok.Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReceivedTransaction {
        private String sender;
        private BigDecimal balance;
        private Date timestamp;

    }
}