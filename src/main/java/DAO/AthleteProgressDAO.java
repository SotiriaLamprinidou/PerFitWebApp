package DAO;

import Interfaces.IAthleteProgressDAO;
import Models.AthleteProgressRecord;
import config.DBConfig;

import java.sql.*;
import org.json.JSONArray;

// DAO implementation for handling athlete training progress operations
public class AthleteProgressDAO implements IAthleteProgressDAO {

    /**
     * Checks whether a progress entry exists for the current week.
     * @param userId ID of the athlete
     * @return true if an entry exists within the last 7 days, otherwise false
     */
    @Override
    public boolean entryExistsForUserThisWeek(int userId) throws Exception {
        String sql = "SELECT 1 FROM athlete_progress WHERE user_id = ? AND created_at >= NOW() - INTERVAL 7 DAY";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // returns true if any row exists
        }
    }

    /**
     * Updates an existing weekly entry: increments workout count, training time, and appends weight.
     * @param userId ID of the athlete
     * @param duration Duration of workout in minutes
     * @param weight New weight to add to the tracking history
     */
    @Override
    public void updateProgress(int userId, int duration, double weight) throws Exception {
        String sql = """
            UPDATE athlete_progress
            SET week_workouts = week_workouts + 1,
                total_training_time_minutes = total_training_time_minutes + ?,
                weight_kg = JSON_ARRAY_APPEND(weight_kg, '$', ?)
            WHERE user_id = ? AND created_at >= NOW() - INTERVAL 7 DAY
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, duration);     // add to training time
            ps.setDouble(2, weight);    // append to weight_kg array
            ps.setInt(3, userId);       // filter by user and week
            ps.executeUpdate();
        }
    }

    /**
     * Inserts a new progress record for a user (used when none exists this week).
     * @param userId ID of the athlete
     * @param duration Training duration
     * @param weight Initial weight entry
     */
    @Override
    public void insertProgress(int userId, int duration, double weight) throws Exception {
        String sql = """
            INSERT INTO athlete_progress (user_id, week_workouts, total_training_time_minutes, weight_kg)
            VALUES (?, ?, ?, JSON_ARRAY(?))
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, 1);            // first workout of the week
            ps.setInt(3, duration);     // initial training time
            ps.setDouble(4, weight);    // start weight tracking with a single value
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves the most recent progress record for a user.
     * @param userId ID of the athlete
     * @return AthleteProgressRecord containing workout stats and weight history
     */
    @Override
    public AthleteProgressRecord getProgress(int userId) throws Exception {
        String sql = """
            SELECT week_workouts, total_training_time_minutes, weight_kg
            FROM athlete_progress
            WHERE user_id = ?
            ORDER BY created_at DESC
            LIMIT 1
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int workouts = rs.getInt("week_workouts");
                int minutes = rs.getInt("total_training_time_minutes");
                String weightsJson = rs.getString("weight_kg");

                double currentWeight = 0;
                double previousWeight = 0;

                // Parse weight history JSON array
                JSONArray jsonArray = new JSONArray(weightsJson);
                int len = jsonArray.length();

                if (len > 0) {
                    currentWeight = jsonArray.getDouble(len - 1);      // latest weight
                    if (len > 1) previousWeight = jsonArray.getDouble(len - 2); // one before latest
                }

                return new AthleteProgressRecord(workouts, minutes, currentWeight, previousWeight, weightsJson);
            } else {
                // No progress entry found, return default object
                return new AthleteProgressRecord(0, 0, 0, 0, "[]");
            }
        }
    }
}
