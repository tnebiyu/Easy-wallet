package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.request.*;
import com.nebiyu.Kelal.dao.response.Response;
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
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        Response response = service.register(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }


    @PostMapping("${LOGIN_API_CALL}")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request){
      Response response = service.authenticate(request);
      if (response.isError()){
          return ResponseEntity.badRequest().body(response);
      }
      else{
          return ResponseEntity.ok().body(response);
      }
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<Response> refreshToken(@RequestBody RefreshTokenRequest request) {
        Response response = service.refreshToken(request);

        if (response.isError()) {
            return ResponseEntity.badRequest()
                    .body(response);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }

    @PostMapping("/change_user_password")
    public ResponseEntity<Response> changeUserPassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Auhorization") String jwtToken){
        Response response = service.changePassword(request, jwtToken);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup_by_phone_number")
    public ResponseEntity<Response> signupByPhoneNumber(@RequestBody RegisterWithPhoneRequest request){
        Response response = service.registerWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/signIn_by_phone_number")
    public ResponseEntity<Response> signInByPhoneNumber(@RequestBody PhoneAuthRequest request){
        Response response = service.signInWithPhoneNumber(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/reset_password_via_otp")
    public ResponseEntity<Response> resetPasswordViaOtp(@RequestBody ResetPasswordRequest request){
        Response response = service.resetPassword(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

}



