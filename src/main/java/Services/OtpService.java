package Services;

import DAO.OtpDAO;
import Interfaces.IOtpDAO;
import Interfaces.IOtpSenderService;
import Interfaces.IOtpService;
import Models.OtpRecord;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Pattern;

public class OtpService implements IOtpService {

    private final IOtpDAO otpDAO;
    private final IOtpSenderService otpSender;

    // Email format validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Limits and timing configs
    private static final int MAX_ATTEMPTS = 5;
    private static final int ATTEMPT_WINDOW_SECONDS = 600; // 10 minutes
    private static final int OTP_EXPIRY_SECONDS = 300; // 5 minutes

    public OtpService(IOtpDAO otpDAO, IOtpSenderService otpSender) {
        this.otpDAO = otpDAO;
        this.otpSender = otpSender;
    }

    @Override
    public String generateOtp() {
        // Generate 6-digit random OTP
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }

    @Override
    public void storeOtp(String email, String otp) throws SQLException, ClassNotFoundException {
        validateInputs(email, otp);
        Timestamp now = Timestamp.from(Instant.now());
        Timestamp expiry = Timestamp.from(now.toInstant().plusSeconds(OTP_EXPIRY_SECONDS));

        // Save or update OTP in DB
        if (otpDAO.otpExists(email)) {
            otpDAO.updateOtp(email, otp, expiry, now);
        } else {
            otpDAO.insertOtp(email, otp, expiry, now);
        }
    }

    @Override
    public boolean verifyOtp(String email, String enteredOtp) throws SQLException, ClassNotFoundException {
        validateInputs(email, enteredOtp);

        OtpRecord record = otpDAO.getOtpRecord(email);
        if (record == null) return false;

        // Check if too many attempts were made in a short time
        boolean tooManyAttempts = record.getAttemptCount() >= MAX_ATTEMPTS &&
                record.getFirstAttempt() != null &&
                record.getFirstAttempt().after(Timestamp.from(Instant.now().minusSeconds(ATTEMPT_WINDOW_SECONDS)));

        if (tooManyAttempts) return false;

        // Check if OTP matches and is still valid
        boolean isValid = record.getOtp().equals(enteredOtp) &&
                record.getExpiry().after(Timestamp.from(Instant.now()));

        if (isValid) {
            otpDAO.deleteOtp(email); // Clear OTP after successful use
            return true;
        } else {
            otpDAO.incrementAttempt(email); // Count failed attempt
            return false;
        }
    }

    @Override
    public void resendOtp(String email) throws SQLException, ClassNotFoundException {
        if (!isValidEmail(email)) throw new IllegalArgumentException("Invalid email");

        String otp = generateOtp();
        storeOtp(email, otp); // Store new OTP
        otpSender.sendOtpByEmail(email, otp); // Send via email
    }

    // Helper to validate email and OTP format
    private void validateInputs(String email, String otp) {
        if (!isValidEmail(email) || !isValidOtp(otp)) {
            throw new IllegalArgumentException("Invalid email or OTP format");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidOtp(String otp) {
        return otp != null && otp.matches("\\d{6}");
    }
}
