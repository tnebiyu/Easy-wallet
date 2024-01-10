package com.nebiyu.Kelal.super_admin.controller;
import com.nebiyu.Kelal.dao.*;
import com.nebiyu.Kelal.dto.Response;
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
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {

        Response response = superAdminService.registerSuperAdmin(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request){

        Response response = superAdminService.authenticate(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }

            return ResponseEntity.ok().body(response);

    }
    @PostMapping("/refresh_token")
    public ResponseEntity<Response> refreshToken(@RequestBody RefreshTokenRequest request) {
        Response response = superAdminService.refreshToken(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }
    @PostMapping("/createAdmin")
        public ResponseEntity<Response> createAdmin(@RequestBody SadminCreateAdminRequest request, @RequestHeader("Authorization") String jwtToken){

        Response response = superAdminService.createAdminAccount(request,jwtToken);

        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/top_up_user")
 public ResponseEntity<Response> topUpUser(@RequestBody TopUpRequest request, @RequestHeader("Authorization") String jwtToken ){
        Response response = superAdminService.topUpUser(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/update_super_admin_password")
    public ResponseEntity<Response> updateSuperAdminPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String jwtToken){
        Response response = superAdminService.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup_by_phone_number")
    public ResponseEntity<Response> signupByPhoneNumber(@RequestBody RegisterWithPhoneRequest request){
        Response response = superAdminService.registerWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/signIn_by_phone_number")
    public ResponseEntity<Response> signInByPhoneNumber(@RequestBody PhoneAuthRequest request){
        Response response = superAdminService.signInWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

}

