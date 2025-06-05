// NutritionistService.java (with comments)
package Services;

import Interfaces.INutritionistService;
import Models.NutritionistData;
import Utils.NutritionistApiClient;
import Utils.NutritionistApiClient.Recipe;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class NutritionistService implements INutritionistService {

    private final NutritionistApiClient spoonClient;

    // Constructor injecting the API client
    public NutritionistService(NutritionistApiClient spoonClient) {
        this.spoonClient = spoonClient;
    }

    // Extracts form parameters from the HTTP request and builds a NutritionistData object
    @Override
    public NutritionistData extractDataFromRequest(HttpServletRequest request) {
        return new NutritionistData(
            request.getParameter("name"),
            request.getParameter("gender"),
            Integer.parseInt(request.getParameter("age")),
            Integer.parseInt(request.getParameter("height")),
            Integer.parseInt(request.getParameter("weight")),
            request.getParameter("goal"),
            request.getParameter("diet"),
            request.getParameter("allergies"),
            request.getParameter("activity"),
            request.getParameter("mealFrequency"),
            request.getParameter("cuisine")
        );
    }

    // Generates a full-day meal plan based on user data
    @Override
    public String generateMealPlan(NutritionistData data) throws Exception {
        validate(data); // Basic input validation

        // Calculate Basal Metabolic Rate (BMR) using Mifflin-St Jeor formula
        boolean isMale = data.gender().equalsIgnoreCase("Male");
        double bmr = isMale
                ? 10 * data.weight() + 6.25 * data.height() - 5 * data.age() + 5
                : 10 * data.weight() + 6.25 * data.height() - 5 * data.age() - 161;

        // Apply activity multiplier to get TDEE (Total Daily Energy Expenditure)
        double tdee = bmr * getActivityMultiplier(data.activity());

        // Adjust calories based on goal (cut, maintain, bulk)
        double adjustedCalories = switch (data.goal()) {
            case "Lose Weight" -> tdee * 0.85;
            case "Gain Muscle" -> tdee * 1.15;
            default -> tdee;
        };

        // Get macro distribution and convert to grams
        MacronutrientProfile macros = getMacronutrientProfile(data.diet(), data.goal());
        double targetProtein = adjustedCalories * macros.proteinPct / 4;
        double targetCarbs = adjustedCalories * macros.carbsPct / 4;
        double targetFat = adjustedCalories * macros.fatPct / 9;

        // Parse number of meals from user preference
        int meals = getMealCount(data.mealFrequency());

        // Fetch recipes from Spoonacular based on constraints
        List<Recipe> allRecipes = spoonClient.searchRecipes(
            data.diet(), data.allergies(), data.cuisine(), 8
        );

        // Select best-fitting recipes for the day
        List<Recipe> selected = spoonClient.selectBestRecipes(
            allRecipes, meals, targetProtein, targetCarbs, targetFat
        );

        // Prepare the output
        double totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFats = 0;
        StringBuilder plan = new StringBuilder();
        plan.append("\uD83D\uDCCA Estimated daily calories: ").append((int) adjustedCalories).append(" kcal\n\n")
            .append("Macronutrient Targets:\n")
            .append("• Protein: ").append((int) targetProtein).append("g\n")
            .append("• Carbs: ").append((int) targetCarbs).append("g\n")
            .append("• Fats: ").append((int) targetFat).append("g\n\n")
            .append("\uD83C\uDF7D️ Meal Plan:\n\n");

        // Calculate per-meal macro targets
        double perMealProtein = targetProtein / meals;
        double perMealCarbs = targetCarbs / meals;
        double perMealFats = targetFat / meals;

        // Format each selected meal
        for (int i = 0; i < selected.size(); i++) {
            Recipe r = selected.get(i);
            double scale = r.scaleToMatch(perMealProtein, perMealCarbs, perMealFats);

            double p = r.protein() * scale;
            double c = r.carbs() * scale;
            double f = r.fat() * scale;
            double cal = r.calories() * scale;

            totalProtein += p;
            totalCarbs += c;
            totalFats += f;
            totalCalories += cal;

            plan.append(String.format(
                "Meal %d: %s (x%.1f serving)\n- Protein: %.1fg\n- Carbs: %.1fg\n- Fat: %.1fg\n- Calories: %.1f kcal\n\n",
                i + 1, r.title(), scale, p, c, f, cal
            ));
        }

        // Summary of totals
        plan.append("Total from selected meals:\n")
            .append(String.format("• Protein: %.1fg\n", totalProtein))
            .append(String.format("• Carbs: %.1fg\n", totalCarbs))
            .append(String.format("• Fats: %.1fg\n", totalFats))
            .append(String.format("• Calories: %.1f kcal\n", totalCalories))
            .append("\n\uD83D\uDCA7 Hydration Tip: Aim for ").append(data.weight() * 35).append(" ml water daily.");

        return plan.toString();
    }

    // Inner record for holding macro distribution
    private record MacronutrientProfile(double carbsPct, double proteinPct, double fatPct) {}

    // Simple validations to ensure data is in a reasonable range
    private void validate(NutritionistData data) {
        if (data.age() <= 0 || data.age() > 120)
            throw new IllegalArgumentException("Age must be between 1 and 120.");
        if (data.height() < 100 || data.height() > 250)
            throw new IllegalArgumentException("Height must be between 100 and 250 cm.");
        if (data.weight() < 30 || data.weight() > 250)
            throw new IllegalArgumentException("Weight must be between 30 and 250 kg.");
    }

    // Multiplier based on activity level selected
    private double getActivityMultiplier(String activity) {
        return switch (activity) {
            case "Sedentary (little to no exercise)" -> 1.2;
            case "Lightly active (1-3 days/week)" -> 1.375;
            case "Moderately active (3-5 days/week)" -> 1.55;
            case "Very active (6-7 days/week)" -> 1.725;
            default -> 1.2;
        };
    }

    // Parse meal frequency string into numeric value
    private int getMealCount(String frequency) {
        return switch (frequency) {
            case "2 meals/day", "Intermittent Fasting (16/8)" -> 2;
            case "3 meals/day" -> 3;
            case "5 small meals/day" -> 5;
            default -> 3;
        };
    }

    // Define macro ratio based on diet type and goal
    private MacronutrientProfile getMacronutrientProfile(String diet, String goal) {
        return switch (diet) {
            case "Keto" -> new MacronutrientProfile(0.05, 0.25, 0.70);
            case "Vegan" -> new MacronutrientProfile(0.50, 0.30, 0.20);
            default -> switch (goal) {
                case "Gain Muscle" -> new MacronutrientProfile(0.45, 0.30, 0.25);
                case "Lose Weight" -> new MacronutrientProfile(0.40, 0.30, 0.30);
                default -> new MacronutrientProfile(0.40, 0.30, 0.30);
            };
        };
    }
}