package DAO;

import Interfaces.IAthleteDAO;
import Models.AthleteData;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

// DAO implementation for storing athlete training programs
public class AthleteDAO implements IAthleteDAO {

    /**
     * Saves a generated training program for a given athlete into the database.
     *
     * @param user    Athlete data containing personal and fitness details
     * @param email   Email of the user (used to resolve user_id)
     * @param program The generated training program content (text)
     * @throws Exception if any database operation fails
     */
    @Override
    public void saveProgram(AthleteData user, String email, String program) throws Exception {
        // Establish a database connection using the configured datasource
        try (Connection con = DBConfig.getConnection()) {

            // SQL INSERT statement with a subquery to get user_id from email
            String sql = """
                INSERT INTO athlete_programs 
                (user_id, name, age, gender, height_cm, weight_kg, fitness_goal, experience_level, training_method, 
                 preferred_activities, training_days, workout_length, health_condition, injury, program, created_at)
                VALUES (
                    (SELECT id FROM users WHERE email = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP
                )
            """;

            // Use PreparedStatement to prevent SQL injection and safely bind data
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);                        // used in subquery to resolve user_id
                ps.setString(2, user.name());
                ps.setInt(3, user.age());
                ps.setString(4, user.gender());
                ps.setInt(5, user.height());
                ps.setInt(6, user.weight());
                ps.setString(7, user.goal());
                ps.setString(8, user.experience());
                ps.setString(9, user.method());
                ps.setString(10, user.activity());
                ps.setInt(11, user.trainingDays());
                ps.setString(12, user.workoutLength());
                ps.setString(13, user.condition());
                ps.setString(14, user.injury());
                ps.setString(15, program);                     // the actual training plan content

                // Execute the insert
                ps.executeUpdate();
            }
        }
    }
}
