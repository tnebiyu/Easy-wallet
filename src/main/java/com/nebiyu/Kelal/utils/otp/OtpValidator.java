package com.nebiyu.Kelal.utils.otp;

public class OtpValidator {
    public static boolean validateOtp(String enteredOtp, OtpData otpData) {
        return otpData != null &&
                otpData.getOtp().equals(enteredOtp) &&
                System.currentTimeMillis() <= otpData.getExpirationTime();
    }
}
