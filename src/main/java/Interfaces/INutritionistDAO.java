package Interfaces;

import java.util.List;
import Models.NutritionistData;
import Models.NutritionistProgram;

public interface INutritionistDAO {

    // Saves a generated meal plan along with the user's nutritional input data
    void saveMealPlan(int userId, NutritionistData data, String mealPlan) throws Exception;

    // Retrieves all meal plans created by a specific user
    List<NutritionistProgram> getMealPlansByUser(int userId) throws Exception;

    // Updates the content of an existing meal plan if it belongs to the user
    boolean updateMealPlanContent(int userId, int planId, String newContent) throws Exception;
}
