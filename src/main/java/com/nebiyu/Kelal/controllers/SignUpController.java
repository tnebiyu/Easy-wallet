package com.nebiyu.Kelal.controllers;

import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import com.nebiyu.Kelal.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {
    @Autowired
    private AuthService authService;

    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
     UserDTO createdUser= authService.createUser(signUpDTO);


    }


}
