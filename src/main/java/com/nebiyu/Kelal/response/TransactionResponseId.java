package com.nebiyu.Kelal.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nebiyu.Kelal.model.User;
import jakarta.persistence.*;
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
public class TransactionResponseId {
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
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private Long senderId;

        @ManyToOne
        private Long receiverId;

        private BigDecimal amount;
        private Date timestamp;
    }
}


