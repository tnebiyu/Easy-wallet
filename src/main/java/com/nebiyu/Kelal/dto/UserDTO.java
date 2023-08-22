package com.nebiyu.Kelal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
