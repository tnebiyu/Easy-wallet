package com.nebiyu.Kelal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationDTO {

    private String email;
    private String password;
}
