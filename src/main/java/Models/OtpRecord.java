package Models;

import java.sql.Timestamp;

// Represents an OTP entry stored for a user
public class OtpRecord {
    private final String otp;
    private final Timestamp expiry;
    private final int attemptCount;
    private final Timestamp firstAttempt;

    // Constructor to initialize OTP record fields
    public OtpRecord(String otp, Timestamp expiry, int attemptCount, Timestamp firstAttempt) {
        this.otp = otp;
        this.expiry = expiry;
        this.attemptCount = attemptCount;
        this.firstAttempt = firstAttempt;
    }

    // Get the OTP code
    public String getOtp() {
        return otp;
    }

    // Get the expiration timestamp
    public Timestamp getExpiry() {
        return expiry;
    }

    // Get number of verification attempts
    public int getAttemptCount() {
        return attemptCount;
    }

    // Get timestamp of the first attempt
    public Timestamp getFirstAttempt() {
        return firstAttempt;
    }
}
