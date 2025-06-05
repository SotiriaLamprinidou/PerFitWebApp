package DAO;

import Models.UserData;
import config.DBConfig;
import enums.UserRole;
import Interfaces.IUserDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO implementation for managing user data (CRUD + dependency cleanup)
public class UserDAO implements IUserDAO {

    /**
     * Retrieves all users in the system.
     * @return List of UserData objects
     */
    @Override
    public List<UserData> listAllUsers() throws SQLException, ClassNotFoundException {
        List<UserData> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                userList.add(mapUser(rs)); // map each row to a UserData object
            }
        }

        return userList;
    }

    /**
     * Finds a user by email.
     */
    @Override
    public UserData findByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        }
        return null;
    }

    /**
     * Finds a user by username.
     */
    @Override
    public UserData findByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        }
        return null;
    }

    @Override
    public UserData findById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        }
        return null;
    }
    /**
     * Registers a new user.
     */
    @Override
    public void register(UserData userData) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (name, username, password, email, role, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userData.getName());
            stmt.setString(2, userData.getUsername());
            stmt.setString(3, userData.getPassword());
            stmt.setString(4, userData.getEmail());
            stmt.setString(5, userData.getRole().name());
            stmt.executeUpdate();
        }
    }


    /**
     * Registers a new user and returns the generated user ID.
     */
    @Override
    public int registerAndReturnId(UserData userData) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (name, username, password, email, role, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, userData.getName());
            stmt.setString(2, userData.getUsername());
            stmt.setString(3, userData.getPassword());
            stmt.setString(4, userData.getEmail());
            stmt.setString(5, userData.getRole().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                else throw new SQLException("Failed to retrieve user ID.");
            }
        }
    }

    /**
     * Updates existing user data.
     */
    @Override
    public void update(UserData userData) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET name=?, username=?, password=?, email=?, role=? WHERE id=?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userData.getName());
            stmt.setString(2, userData.getUsername());
            stmt.setString(3, userData.getPassword());
            stmt.setString(4, userData.getEmail());
            stmt.setString(5, userData.getRole().name());
            stmt.setInt(6, userData.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a user and all dependent records across related tables.
     * Uses a transaction to ensure all-or-nothing execution.
     */
    @Override
    public void deleteUserAndDependencies(int userId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false); // begin transaction

            String email = null;

            // Step 1: Retrieve email for use in OTP deletion
            try (PreparedStatement getEmailStmt = conn.prepareStatement("SELECT email FROM users WHERE id = ?")) {
                getEmailStmt.setInt(1, userId);
                try (ResultSet rs = getEmailStmt.executeQuery()) {
                    if (rs.next()) {
                        email = rs.getString("email");
                    } else {
                        throw new SQLException("User not found with id: " + userId);
                    }
                }
            }

            // Step 2: Delete dependent records in other domain-specific tables
            String[] tables = {
                "athlete_progress",
                "athlete_programs",
                "nutritionist_meal_plans",
                "trainer_ai_programs"
            };

            for (String table : tables) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + table + " WHERE user_id = ?")) {
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                }
            }

            // Step 3: Delete OTP record (using email instead of userId)
            try (PreparedStatement otpStmt = conn.prepareStatement("DELETE FROM user_otp WHERE email = ?")) {
                otpStmt.setString(1, email);
                otpStmt.executeUpdate();
            }

            // Step 4: Finally, delete the user record
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit(); // all deletions succeeded

        } catch (SQLException e) {
            // Rollback logic can be added here if desired
            throw new SQLException("Failed to delete user and related data", e);
        }
    }

    /**
     * Helper method to convert a ResultSet row into a UserData object.
     */
    private UserData mapUser(ResultSet rs) throws SQLException {
        return new UserData(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("role"),
            rs.getString("created_at")
        );
    }
}
