package DAO;

import Interfaces.IOtpDAO;
import Models.OtpRecord;
import config.DBConfig;

import java.sql.*;

// DAO responsible for managing OTP-related data in the database
public class OtpDAO implements IOtpDAO {

    /**
     * Checks if an OTP record already exists for the given email.
     * @param email user's email
     * @return true if an OTP exists, false otherwise
     */
    @Override
    public boolean otpExists(String email) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM user_otp WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Inserts a new OTP entry into the database.
     * @param email user's email
     * @param otp generated OTP code
     * @param expiry expiration timestamp
     * @param now timestamp when the OTP was generated
     */
    @Override
    public void insertOtp(String email, String otp, Timestamp expiry, Timestamp now) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO user_otp (email, otp, timestamp, attempt_count, first_attempt_time) VALUES (?, ?, ?, 0, ?)")) {
            ps.setString(1, email);
            ps.setString(2, otp);
            ps.setTimestamp(3, expiry);
            ps.setTimestamp(4, now);
            ps.executeUpdate();
        }
    }

    /**
     * Updates the existing OTP record with a new OTP, resets attempts.
     * @param email user's email
     * @param otp new OTP
     * @param expiry new expiration time
     * @param now current time as new first_attempt_time
     */
    @Override
    public void updateOtp(String email, String otp, Timestamp expiry, Timestamp now) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE user_otp SET otp = ?, timestamp = ?, attempt_count = 0, first_attempt_time = ? WHERE email = ?")) {
            ps.setString(1, otp);
            ps.setTimestamp(2, expiry);
            ps.setTimestamp(3, now);
            ps.setString(4, email);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes the OTP entry for a given email.
     */
    @Override
    public void deleteOtp(String email) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM user_otp WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    /**
     * Increments the attempt counter for a user's OTP entry.
     */
    @Override
    public void incrementAttempts(String email) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE user_otp SET attempt_count = attempt_count + 1 WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    // ❗ This method is a duplicate of incrementAttempts — consider removing to reduce confusion.
    @Override
    public void incrementAttempt(String email) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE user_otp SET attempt_count = attempt_count + 1 WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    /**
     * Fetches the OTP record for a given email.
     * @return OtpRecord if exists, otherwise null
     */
    @Override
    public OtpRecord getOtpRecord(String email) throws SQLException, ClassNotFoundException {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT otp, timestamp, attempt_count, first_attempt_time FROM user_otp WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OtpRecord(
                            rs.getString("otp"),
                            rs.getTimestamp("timestamp"),
                            rs.getInt("attempt_count"),
                            rs.getTimestamp("first_attempt_time")
                    );
                }
            }
        }
        return null;
    }
}
