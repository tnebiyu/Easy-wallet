package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.request.*;
import com.nebiyu.Kelal.model.OtpVerification;
import com.nebiyu.Kelal.repositories.OtpVerificationRepository;
import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.AuthorizationResponse;
import com.nebiyu.Kelal.dto.response.ChangePasswordResponse;
import com.nebiyu.Kelal.dto.response.OtpResponse;
import com.nebiyu.Kelal.services.AuthenticationService;

//import com.nebiyu.Kelal.services.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService service;

//    private final TwilioService twilioService;

    private final OtpVerificationRepository otpVerificationRepository;

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
//    @PostMapping("/request_otp")
//    public ResponseEntity<OtpResponse> requestOTP(@RequestBody RequestOtp requestOtp) {
//
//        String otp = twilioService.generateOTP(6);
//        System.out.println("this is the otp " + otp);
//        OtpResponse response = twilioService.sendOtpSms(requestOtp, "Your OTP:" + otp);
//        System.out.println("this is the response " + response);
//        if (!response.isError()){
//            System.out.println("error not occurred");
//            var otpVerification = OtpVerification.builder().phoneNumber(
//                    requestOtp.getPhoneNumber()).otp(otp).build();
//            otpVerificationRepository.save(otpVerification);
//            return ResponseEntity.ok().body(response);
//        }
//
//        return ResponseEntity.badRequest().body(response);
//
//
//
//
//    }
    @PostMapping("/verify_otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTP request) {

        Optional<OtpVerification> storedOtp = otpVerificationRepository.findByPhoneNumber(request.getPhoneNumber());
        System.out.println("this is the stored otp " + storedOtp);


        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
            otpVerificationRepository.delete(storedOtp.get());
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("Invalid otp");
        }
    }
//    @PostMapping("/reset_password")
//    public ResponseEntity<AuthenticationResponse> resetPassword(@RequestBody ResetPasswordRequest request){
//        AuthenticationResponse response = twilioService.resetPasswordRequest(request);
//        if (response.isError()){
//            return ResponseEntity.badRequest().body(response);
//        }
//        return ResponseEntity.ok().body(response);
//    }





}



