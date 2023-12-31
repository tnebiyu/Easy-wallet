package com.nebiyu.Kelal.admin.controller;

import com.nebiyu.Kelal.admin.admin_service.AdminService;
import com.nebiyu.Kelal.dto.request.AuthenticationRequest;
import com.nebiyu.Kelal.dto.request.ChangePasswordRequest;
import com.nebiyu.Kelal.dto.request.RegisterRequest;
import com.nebiyu.Kelal.dto.request.TopUpRequest;
import com.nebiyu.Kelal.dao.response.Response;
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
