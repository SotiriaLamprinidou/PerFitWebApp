package Interfaces;

import java.sql.SQLException;

public interface IOtpService {

    // Generates a 6-digit OTP code
    String generateOtp();

    // Stores a new OTP for the given email address
    void storeOtp(String email, String otp) throws SQLException, ClassNotFoundException;

    // Verifies the entered OTP against the stored one for the email
    boolean verifyOtp(String email, String enteredOtp) throws SQLException, ClassNotFoundException;

    // Generates and sends a new OTP to the specified email
    void resendOtp(String email) throws SQLException, ClassNotFoundException;
}
