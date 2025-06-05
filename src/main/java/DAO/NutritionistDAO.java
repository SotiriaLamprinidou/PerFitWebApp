package DAO;

import Interfaces.INutritionistDAO;
import Models.NutritionistData;
import Models.NutritionistProgram;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// DAO implementation responsible for CRUD operations on nutritionist meal plans
public class NutritionistDAO implements INutritionistDAO {

    /**
     * Saves a newly generated meal plan to the database.
     * @param userId   ID of the nutritionist user
     * @param data     User input data used to generate the meal plan
     * @param mealPlan The generated meal plan content
     */
    @Override
    public void saveMealPlan(int userId, NutritionistData data, String mealPlan) throws Exception {
    	String sql = """
    		    INSERT INTO nutritionist_meal_plans 
    		    (user_id, goal, diet_type, allergies, activity_level, name, gender, age, height_cm, weight_kg, 
    		     meal_frequency, cuisine_preference, meal_plan, created_at)
    		    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    		""";


        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Bind user-submitted and generated data to the query
        	ps.setInt(1, userId);
        	ps.setString(2, data.goal());
        	ps.setString(3, data.diet());
        	ps.setString(4, data.allergies());
        	ps.setString(5, data.activity());
        	ps.setString(6, data.name());
        	ps.setString(7, data.gender());
        	ps.setInt(8, data.age());
        	ps.setInt(9, data.height());
        	ps.setInt(10, data.weight());
        	ps.setString(11, data.mealFrequency());
        	ps.setString(12, data.cuisine());
        	ps.setString(13, mealPlan);

            // Execute insert query
            ps.executeUpdate();
        }
     }

    /**
     * Retrieves all meal plans created by a specific nutritionist.
     * @param userId ID of the nutritionist user
     * @return List of NutritionistProgram objects
     */
    @Override
    public List<NutritionistProgram> getMealPlansByUser(int userId) throws Exception {
        List<NutritionistProgram> plans = new ArrayList<>();

        String sql = """
            SELECT id, user_id, goal, diet_type, allergies, activity_level, name, gender, age, height_cm, weight_kg, 
                   meal_frequency, cuisine_preference, meal_plan, created_at 
            FROM nutritionist_meal_plans 
            WHERE user_id = ? 
            ORDER BY created_at DESC
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                // Populate a list of program objects from result set
                while (rs.next()) {
                    NutritionistProgram program = new NutritionistProgram(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("goal"),
                        rs.getString("diet_type"),
                        rs.getString("allergies"),
                        rs.getString("activity_level"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getInt("height_cm"),
                        rs.getInt("weight_kg"),
                        rs.getString("meal_frequency"),
                        rs.getString("cuisine_preference"),
                        rs.getString("meal_plan"),
                        rs.getTimestamp("created_at")
                    );
                    plans.add(program);
                }
            }
        }

        return plans;
    }

    /**
     * Updates the content of a meal plan, only if it belongs to the authenticated user.
     * @param userId     ID of the nutritionist user
     * @param planId     ID of the specific plan to update
     * @param newContent New meal plan content
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateMealPlanContent(int userId, int planId, String newContent) throws Exception {
        String sql = "UPDATE nutritionist_meal_plans SET meal_plan = ? WHERE id = ? AND user_id = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Bind updated content and ensure plan belongs to user
            ps.setString(1, newContent.trim());
            ps.setInt(2, planId);
            ps.setInt(3, userId);

            // Return true if at least one row was affected
            return ps.executeUpdate() > 0;
        }
    }
}
