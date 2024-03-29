package com.nebiyu.Kelal.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SadminCreateAdminRequest {
    String superAdminEmail;
    String adminFirstName;
    String adminLastName;
    String adminEmail;
    String adminPassword;
}
