package DAO;

import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Interfaces.ITrainerDAO;
import Models.TrainerData;
import Models.TrainerProgram;

// DAO implementation for managing trainer-generated AI workout programs
public class TrainerDAO implements ITrainerDAO {

    /**
     * Persists a new AI-generated workout program for a trainer into the database.
     * @param userId  ID of the logged-in trainer
     * @param data    Trainer preferences and inputs (goal, muscle group, equipment)
     * @param program The generated program text to save
     */
    @Override
    public void saveTrainerProgram(int userId, TrainerData data, String program) throws Exception {
        try (Connection con = DBConfig.getConnection()) {
            String sql = """
                INSERT INTO trainer_ai_programs 
                (user_id, workout_goal, muscle_group, equipment_available, program, created_at)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, data.getGoal());
                ps.setString(3, data.getMuscleGroup());

                // Store 'true' if any equipment is specified (other than "no")
                ps.setBoolean(4, !"no".equalsIgnoreCase(data.getEquipment()));

                ps.setString(5, program); // the generated program text
                ps.executeUpdate();       // execute the insert
            }
        }
    }

    /**
     * Retrieves all workout programs created by a specific trainer.
     * @param userId ID of the trainer
     * @return List of TrainerProgram objects ordered by most recent first
     */
    @Override
    public List<TrainerProgram> getProgramsForUser(int userId) {
        List<TrainerProgram> programs = new ArrayList<>();

        String query = """
            SELECT id, workout_goal, muscle_group, equipment_available, program, created_at
            FROM trainer_ai_programs
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Build TrainerProgram object from DB row
                    programs.add(new TrainerProgram(
                        rs.getInt("id"),
                        rs.getString("workout_goal"),
                        rs.getString("muscle_group"),
                        rs.getBoolean("equipment_available"),
                        rs.getString("program"),
                        rs.getString("created_at")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Ideally replace with proper logging
        }

        return programs;
    }

    /**
     * Updates the content of a specific trainer program.
     * Only succeeds if the program belongs to the user (verified by userId).
     * @param userId ID of the trainer
     * @param programId ID of the program to update
     * @param newContent New content to store
     */
    @Override
    public void updateProgramContent(int userId, int programId, String newContent) {
        String sql = "UPDATE trainer_ai_programs SET program = ? WHERE id = ? AND user_id = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newContent);
            ps.setInt(2, programId);
            ps.setInt(3, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // Replace with logging in production
        }
    }
}
