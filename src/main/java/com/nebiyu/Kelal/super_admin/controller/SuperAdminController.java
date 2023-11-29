package com.nebiyu.Kelal.super_admin.controller;

import com.nebiyu.Kelal.dto.request.*;
import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.AuthorizationResponse;
import com.nebiyu.Kelal.dto.response.ChangePasswordResponse;
import com.nebiyu.Kelal.super_admin.service.SuperAdminAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        public ResponseEntity<AuthenticationResponse> createAdmin(@RequestBody SadminCreateAdminRequest request, @RequestHeader("Authorization") String jwtToken){

        AuthenticationResponse response = superAdminService.createAdminAccount(request,jwtToken);

        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/top_up_user")
 public ResponseEntity<AuthenticationResponse> topUpUser(@RequestBody TopUpRequest request, @RequestHeader("Authorization") String jwtToken ){
        AuthenticationResponse response = superAdminService.topUpUser(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/update_super_admin_password")
    public ResponseEntity<ChangePasswordResponse> updateSuperAdminPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String jwtToken){
        ChangePasswordResponse response = superAdminService.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}

