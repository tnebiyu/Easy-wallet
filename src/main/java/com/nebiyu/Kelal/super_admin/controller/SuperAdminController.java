package com.nebiyu.Kelal.super_admin.controller;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.service.SuperAdminAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController{
    private final SuperAdminAuthenticationService superAdminService;

    @PostMapping("/register")
    public ResponseEntity<AuthorizationResponse> register(@RequestBody RegisterRequest request) {
        AuthorizationResponse response = superAdminService.registerSuperAdmin(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = superAdminService.authenticate(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }

            return ResponseEntity.ok().body(response);

    }
    @PostMapping("/createAdmin")
    public ResponseEntity<AuthenticationResponse> createAdmin(@RequestBody RegisterRequest registerRequest, AuthenticationRequest request,   @RequestHeader("Authorization") String jwtToken){

        AuthenticationResponse response = superAdminService.createAdminAccount( request,registerRequest, jwtToken);

        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }


}

