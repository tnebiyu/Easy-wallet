package com.nebiyu.Kelal.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String email;
    private String password;
    private String newPassword;

}
