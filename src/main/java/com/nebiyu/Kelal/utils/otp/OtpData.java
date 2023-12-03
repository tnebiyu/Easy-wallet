package com.nebiyu.Kelal.utils.otp;

public class OtpData {
    private String otp;
    private long expirationTime;

    public OtpData(String otp, long expirationTime) {
        this.otp = otp;
        this.expirationTime = expirationTime;
    }

    public String getOtp() {
        return otp;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
