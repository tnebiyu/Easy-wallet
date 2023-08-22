package com.nebiyu.Kelal.controllers;

import com.nebiyu.Kelal.dto.SignUpDTO;
import com.nebiyu.Kelal.dto.UserDTO;
import com.nebiyu.Kelal.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {
    @Autowired
    private AuthService authService;
@PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
     UserDTO createdUser= authService.signUp(signUpDTO);
     if(createdUser == null){

         return  new ResponseEntity<>("User not created", HttpStatus.BAD_REQUEST);
     }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);


    }


}
