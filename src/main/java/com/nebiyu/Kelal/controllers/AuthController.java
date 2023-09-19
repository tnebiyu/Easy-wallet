package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.configuration.JwtConfig;
import com.nebiyu.Kelal.dto.AuthenticationResponse;
import com.nebiyu.Kelal.services.auth.AuthService;
import com.nebiyu.Kelal.services.auth.AuthenticationRequest;
import com.nebiyu.Kelal.services.auth.AuthenticationService;
import com.nebiyu.Kelal.services.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController{
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
return ResponseEntity.ok(service.register(request));

    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
return ResponseEntity.ok(service.authenticate(request));
    }


}



