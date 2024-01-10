package com.nebiyu.Kelal.controllers;

import com.nebiyu.Kelal.dao.OtpRequest;
import com.nebiyu.Kelal.dao.OtpValidationRequest;
import com.nebiyu.Kelal.dto.Response;
import com.nebiyu.Kelal.dto.OtpResponse;
import com.nebiyu.Kelal.services.SmsService;
import com.nebiyu.Kelal.utils.otp.OtpData;
import com.nebiyu.Kelal.utils.otp.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class SmsController {
    @Autowired
    SmsService smsService;
    @Autowired
    OtpGenerator otpGenerator;

@PostMapping("/send_otp")
    public ResponseEntity<OtpResponse> sendOtp(@RequestBody OtpRequest request){
        OtpData otpData = OtpGenerator.generateOtp(request.getTo());
        OtpResponse response = smsService.sendOtpSms(request.getTo(), otpData.getOtp());

        if (response.isSuccess()){
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<Response> validateOtp(@RequestBody OtpValidationRequest validationRequest) {


        boolean isValid = otpGenerator.validateOtp(validationRequest.getPhone(), validationRequest.getOtp());

        if (isValid) {
            var verifyOtp = Response.VerifyOtp
                    .builder().isCorrect(true)
                    .otp(validationRequest.getOtp())
                    .phone(validationRequest.getPhone())
                    .build();
            var data = Response.Data
                    .builder().verifyOtp(verifyOtp).build();
            Response response = Response.builder()
                    .error(false)
                    .error_msg("")
                    .data(data).build();
            return ResponseEntity.ok().body(response);
        } else {
            Response response = Response.builder()
                    .error(true)
                    .error_msg("Invalid OTP or expired").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
