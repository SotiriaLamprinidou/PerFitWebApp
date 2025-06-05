package Interfaces;

import Models.OtpRecord;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface IOtpDAO {

    // Checks if an OTP record already exists for the given email
    boolean otpExists(String email) throws SQLException, ClassNotFoundException;

    // Inserts a new OTP record with its expiry and creation timestamps
    void insertOtp(String email, String otp, Timestamp expiry, Timestamp now) throws SQLException, ClassNotFoundException;

    // Updates an existing OTP record with a new code and timestamps
    void updateOtp(String email, String otp, Timestamp expiry, Timestamp now) throws SQLException, ClassNotFoundException;

    // Deletes the OTP record associated with the given email
    void deleteOtp(String email) throws SQLException, ClassNotFoundException;

    // Increments the attempt counter for the given email (duplicate of incrementAttempts)
    void incrementAttempt(String email) throws SQLException, ClassNotFoundException;

    // Retrieves the OTP record for the specified email
    OtpRecord getOtpRecord(String email) throws SQLException, ClassNotFoundException;

    // Increments the attempt count for the given email (functionally same as incrementAttempt)
    void incrementAttempts(String email) throws SQLException, ClassNotFoundException;
}
