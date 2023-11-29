package com.nebiyu.Kelal.admin.controller;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.dto.request.AuthenticationRequest;
import com.nebiyu.Kelal.dto.request.ChangePasswordRequest;
import com.nebiyu.Kelal.dto.request.RegisterRequest;
import com.nebiyu.Kelal.dto.request.TopUpRequest;
import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.AuthorizationResponse;
import com.nebiyu.Kelal.dto.response.ChangePasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController{
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<AuthorizationResponse> register(@RequestBody RegisterRequest request) {
        AuthorizationResponse response = adminService.registerAdmin(request);
        System.out.println("this is the user request " + request);
        System.out.println("this is the response  " + response);
        System.out.println("admin is going to register");

        if (response.isError()) {
            System.out.println("admin error occured");

            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            System.out.println("admin error is not occurred");
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = adminService.authenticateAdmin(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        else{
            return ResponseEntity.ok().body(response);
        }
    }
    @PostMapping("/top_up_user")
    public ResponseEntity<AuthenticationResponse> topUpUser(@RequestBody TopUpRequest request, @RequestHeader("Authorization") String jwtToken){
        AuthenticationResponse response = adminService.topUpUser(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);

        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/update_admin_password")
    public ResponseEntity<ChangePasswordResponse> updateSuperAdminPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String jwtToken){
        ChangePasswordResponse response = adminService.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


}
