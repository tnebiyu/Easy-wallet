package com.nebiyu.Kelal.services;
import com.nebiyu.Kelal.model.OtpVerification;
import com.nebiyu.Kelal.request.RequestOtp;
import com.nebiyu.Kelal.response.AuthenticationResponse;
import com.nebiyu.Kelal.response.OtpResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import java.util.Random;
public class TwilioService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    public OtpResponse sendSms(RequestOtp request, String body) {
        try{
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(request.getPhoneNumber()),
                    new com.twilio.type.PhoneNumber("+251932549271"),
                    body
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



        public  String generateOTP(int length) {
            Random random = new Random();
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < length; i++) {
                otp.append(random.nextInt(10));
            }
            return otp.toString();
        }


}
