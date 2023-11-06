package com.nebiyu.Kelal.super_admin.controller;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.request.SadminCreateAdminRequest;
import com.nebiyu.Kelal.request.TopUpRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.super_admin.model.SuperAdminModel;
import com.nebiyu.Kelal.super_admin.service.SuperAdminAuthenticationService;
import com.nebiyu.Kelal.super_admin.super_admin_repo.SuperAdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController{
    private final SuperAdminAuthenticationService superAdminService;
    private final SuperAdminRepo superAdminRepo;

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

}

