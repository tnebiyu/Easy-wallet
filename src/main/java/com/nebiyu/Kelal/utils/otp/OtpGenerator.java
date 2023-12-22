package com.nebiyu.Kelal.utils.otp;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Component
public class OtpGenerator {
    private static final int OTP_LENGTH = 6;
    private static final int EXPIRATION_TIME_SECONDS = 300;

    private static final Map<String, OtpData> otpDataMap = new HashMap<>();

    public static OtpData generateOtp(String phoneNumber) {

        String otp = generateRandomOtp();
        System.out.println("otp is : " + otp);

        long expirationTime = System.currentTimeMillis() + (EXPIRATION_TIME_SECONDS * 1000);
        OtpData otpData = new OtpData(otp, expirationTime);
        otpDataMap.put(phoneNumber, otpData);

        return otpData;
    }

    private static String generateRandomOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OtpGenerator.OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
    public boolean validateOtp(String phoneNumber, String enteredOtp) {
        OtpData otpData = otpDataMap.get(phoneNumber);

        return otpData != null &&
                otpData.getOtp().equals(enteredOtp) &&
                System.currentTimeMillis() <= otpData.getExpirationTime();
    }

}
