package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.request.RegisterRequest;
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
    public ResponseEntity<ResponseEntity<AuthorizationResponse>> register(@RequestBody RegisterRequest request){
return ResponseEntity.ok(service.register(request));

    }
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request){
return ResponseEntity.ok(service.authenticate(request));
    }


}



