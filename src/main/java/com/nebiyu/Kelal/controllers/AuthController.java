package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.model.OtpVerification;
import com.nebiyu.Kelal.repositories.OtpVerificationRepository;
import com.nebiyu.Kelal.request.*;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.AuthorizationResponse;
import com.nebiyu.Kelal.response.ChangePasswordResponse;
import com.nebiyu.Kelal.response.OtpResponse;
import com.nebiyu.Kelal.services.AuthenticationService;

import com.nebiyu.Kelal.services.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("${AUTH_CLASS_REQUEST_MAPPING}")
@RequiredArgsConstructor
public class AuthController{
    private final AuthenticationService service;

    private final TwilioService twilioService;

    private final OtpVerificationRepository otpVerificationRepository;

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
    @PostMapping("/request-otp")
    public ResponseEntity<OtpResponse> requestOTP(@RequestParam RequestOtp requestOtp) {

        String otp = twilioService.generateOTP(6);
        OtpResponse response = twilioService.sendSms(requestOtp, "Your OTP:" + otp);
        if (!response.isError()){
            var otpVerification = OtpVerification.builder().phoneNumber(
                    requestOtp.getPhoneNumber()).otp(otp).build();
            otpVerificationRepository.save(otpVerification);
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);




    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTP request) {

        Optional<OtpVerification> storedOtp = otpVerificationRepository.findByPhoneNumber(request.getPhoneNumber());

        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
            otpVerificationRepository.delete(storedOtp.get());
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("Invalid otp");
        }
    }





}



