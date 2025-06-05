package Interfaces;

import Models.NutritionistData;
import javax.servlet.http.HttpServletRequest;

public interface INutritionistService {

    // Extracts nutrition-related input data from the HTTP request
    NutritionistData extractDataFromRequest(HttpServletRequest request);

    // Generates a personalized meal plan based on the provided data
    String generateMealPlan(NutritionistData data) throws Exception;
}
