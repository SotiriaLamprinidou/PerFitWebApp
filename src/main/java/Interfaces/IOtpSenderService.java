package Interfaces;

public interface IOtpSenderService {

    // Sends the OTP code to the specified email address
    void sendOtpByEmail(String email, String otp);
}
