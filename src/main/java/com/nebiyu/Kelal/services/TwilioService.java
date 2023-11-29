package com.nebiyu.Kelal.services;
import com.nebiyu.Kelal.dto.response.TransactionResponseId;
import com.nebiyu.Kelal.model.OtpVerification;
import com.nebiyu.Kelal.model.PhoneUserModel;
import com.nebiyu.Kelal.repositories.OtpVerificationRepository;
import com.nebiyu.Kelal.repositories.UserRepoPhoneNumber;
import com.nebiyu.Kelal.dto.request.RequestOtp;
import com.nebiyu.Kelal.dto.request.ResetPasswordRequest;
import com.nebiyu.Kelal.dto.response.AuthenticationResponse;
import com.nebiyu.Kelal.dto.response.OtpResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class TwilioService {
    @Value("${twilio.account_sid}")
    private String accountSid;
    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepoPhoneNumber userRepoPhoneNumber;

    @Value("${twilio.auth_token}")
    private String authToken;

    public OtpResponse sendOtpSms(RequestOtp request, String body) {
        try{
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(request.getPhoneNumber()),
                    new com.twilio.type.PhoneNumber("+251932549271"),
                    body
            ).create();
            System.out.println("message created " + message);
            if (message.getSid() ==null){
                return OtpResponse.builder().error(true)
                        .error_msg("message is not delivered").build();
            }

            var message_status = OtpResponse.Message.builder()
                    .message(message).build();
            return OtpResponse.builder().error(false)
                    .message(message_status).build();
        }
        catch (Exception e) {
            return OtpResponse.builder().error(true)
                    .error_msg("error occured " + e.toString() ).build();

        }


    }
    public OtpResponse sendSms(TransactionResponseId.UserData request, String data){
        try{
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(request.getPhoneNumber()),
                    new com.twilio.type.PhoneNumber("+251932549271"),
                    data
            ).create();
            if (message.getSid() ==null){
                return OtpResponse.builder().error(true)
                        .error_msg("message is not delivered").build();
            }

            var message_status = OtpResponse.Message.builder()
                    .message(message).build();
            return OtpResponse.builder().error(false)
                    .message(message_status).build();
        }
        catch (Exception e) {
            return OtpResponse.builder().error(true)
                    .error_msg("error occured " + e.toString() ).build();

        }
    }
    public AuthenticationResponse resetPasswordRequest(ResetPasswordRequest request){
        try{
            Optional<PhoneUserModel> userExist = userRepoPhoneNumber.findByPhoneNumber(request.getPhoneNumber());
            Optional<OtpVerification> userOptional = otpVerificationRepository.findByPhoneNumber(request.getPhoneNumber());
            if (userExist.isEmpty()){
                return AuthenticationResponse.builder().error(true)
                        .error_msg("user with this phone number is not exist").build();
            }

            if(userOptional.isEmpty()){
                return AuthenticationResponse.builder().error(true).error_msg("phone number is not found").build();
            }
            OtpVerification otp = userOptional.get();





           if (!otp.getOtp().equals(request.getOtp()) ){

               return AuthenticationResponse.builder().error(true).error_msg("otp is incorrect").build();
           }
            PhoneUserModel user = userExist.get();

            user.setPassword(request.getNew_login_password());
            userRepoPhoneNumber.save(user);
            otpVerificationRepository.delete(otp);


            return AuthenticationResponse.builder().error(false).error_msg("").build();
        }
        catch (Exception e){
            return AuthenticationResponse.builder().error(true).error_msg("error occurred " + e.getMessage() ).build();

        }
    }



        public  String generateOTP(int length) {
            Random random = new Random();
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < length; i++) {
                otp.append(random.nextInt(10));
            }
            return otp.toString();
        }



}
