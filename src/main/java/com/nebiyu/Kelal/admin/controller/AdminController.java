package com.nebiyu.Kelal.admin.controller;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.dao.*;
import com.nebiyu.Kelal.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController{
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        Response response = adminService.registerAdmin(request);


        if (response.isError()) {
            System.out.println("admin error occured");

            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request){
        Response response = adminService.authenticateAdmin(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        else{
            return ResponseEntity.ok().body(response);
        }
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<Response> refreshToken(@RequestBody RefreshTokenRequest request) {
        Response response = adminService.refreshToken(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }
    @PostMapping("/signup_by_phone_number")
    public ResponseEntity<Response> signupByPhoneNumber(@RequestBody RegisterWithPhoneRequest request){
        Response response = adminService.registerWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/signIn_by_phone_number")
    public ResponseEntity<Response> signInByPhoneNumber(@RequestBody PhoneAuthRequest request){
        Response response = adminService.signInWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/top_up_user")
    public ResponseEntity<Response> topUpUser(@RequestBody TopUpRequest request, @RequestHeader("Authorization") String jwtToken){
        Response response = adminService.topUpUser(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);

        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/update_admin_password")
    public ResponseEntity<Response> updateSuperAdminPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String jwtToken){
        Response response = adminService.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


}
