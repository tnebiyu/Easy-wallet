package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.request.*;
import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.AuthorizationResponse;
import com.nebiyu.Kelal.dto.response.ChangePasswordResponse;
import com.nebiyu.Kelal.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService service;

    @PostMapping("/register")
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

    @PostMapping("/signup_by_phone_number")
    public ResponseEntity<AuthorizationResponse> signupByPhoneNumber(@RequestBody RegisterWithPhoneRequest request){
        AuthorizationResponse response = service.registerWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/signIn_by_phone_number")
    public ResponseEntity<AuthenticationResponse> signInByPhoneNumber(@RequestBody PhoneAuthRequest request){
        AuthenticationResponse response = service.signInWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

}



