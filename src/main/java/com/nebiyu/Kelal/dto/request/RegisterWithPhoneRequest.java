package com.nebiyu.Kelal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterWithPhoneRequest {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
}
