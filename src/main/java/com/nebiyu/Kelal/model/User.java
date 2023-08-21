package com.nebiyu.Kelal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
