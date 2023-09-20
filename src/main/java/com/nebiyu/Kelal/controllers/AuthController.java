package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.AuthenticationResponse;
import com.nebiyu.Kelal.services.auth.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController{
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<ResponseEntity<AuthenticationResponse>> register(@RequestBody RegisterRequest request){
return ResponseEntity.ok(service.register(request));

    }
//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//return ResponseEntity.ok(service.au);
//    }


}



