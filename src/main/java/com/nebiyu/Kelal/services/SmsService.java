package com.nebiyu.Kelal.services;
import com.nebiyu.Kelal.dto.request.SmsRequest;
import com.nebiyu.Kelal.dto.response.NotifyResponse;
import com.nebiyu.Kelal.dto.response.OtpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SmsService {
    @Value("${OTP_KMI_API}")
    private String apiUrl;

    @Value("${SMS_ACCESS_KEY}")
    private String accessKey;

    @Value("${SMS_SECRET_KEY}")
    private String secretKey;
    public OtpResponse sendOtpSms(String to, String otp) {

            String from = "Ariob";
            String message = otp + " is your OTP for Kelal and will be valid for" +
                    " 5 minutes. " +
                    "Don't share your OTP with anyone!";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            SmsRequest smsRequest = new SmsRequest(accessKey, secretKey, from, to, message);
            HttpEntity<SmsRequest> requestEntity = new HttpEntity<>(smsRequest, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OtpResponse> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, OtpResponse.class);

            return responseEntity.getBody();
    }
    public NotifyResponse sendNotify(String to, String message){
        String from = "Ariob";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SmsRequest smsRequest = new SmsRequest(accessKey, secretKey, from, to, message);
        HttpEntity<SmsRequest> requestEntity = new HttpEntity<>(smsRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NotifyResponse> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, NotifyResponse.class);

        return responseEntity.getBody();
    }

}
