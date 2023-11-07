package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.request.AuthenticationRequest;
import com.nebiyu.Kelal.request.ChangePasswordRequest;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.request.RegisterRequest;
import com.nebiyu.Kelal.response.ChangePasswordResponse;
import com.nebiyu.Kelal.services.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${AUTH_CLASS_REQUEST_MAPPING}")
@RequiredArgsConstructor
public class AuthController{
    private final AuthenticationService service;

    @PostMapping("${REGISTER_API_CALL}")
    public ResponseEntity<AuthorizationResponse> register(@RequestBody RegisterRequest request) {
        AuthorizationResponse response = service.register(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("${LOGIN_API_CALL}")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
      AuthenticationResponse response = service.authenticate(request);
      if (response.isError()){
          return ResponseEntity.badRequest().body(response);
      }
      else{
          return ResponseEntity.ok().body(response);
      }
    }

    @PostMapping("/change_user_password")
    public ResponseEntity<ChangePasswordResponse> changeUserPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Auhorization") String jwtToken){
        ChangePasswordResponse response = service.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }



}



