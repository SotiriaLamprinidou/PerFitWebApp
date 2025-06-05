// SpoonacularApiClient.java (with comments)
package Utils;

import config.ApiConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NutritionistApiClient {

    // Inner class to represent recipe data with macros and calories
    public static class Recipe {
        private final String title;
        private final double protein;
        private final double carbs;
        private final double fat;
        private final double calories;

        public Recipe(String title, double protein, double carbs, double fat, double calories) {
            this.title = title;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.calories = calories;
        }

        public String title() { return title; }
        public double protein() { return protein; }
        public double carbs() { return carbs; }
        public double fat() { return fat; }
        public double calories() { return calories; }

        // Scales the recipe to match the macro targets using average of individual scaling factors
        public double scaleToMatch(double targetProtein, double targetCarbs, double targetFat) {
            double pScale = protein > 0 ? targetProtein / protein : 0;
            double cScale = carbs > 0 ? targetCarbs / carbs : 0;
            double fScale = fat > 0 ? targetFat / fat : 0;
            double scale = (pScale + cScale + fScale) / 3.0;
            return Math.max(0.25, Math.min(scale, 3.0)); // Clamp scale to avoid unrealistic portions
        }

        // Returns a score measuring deviation from macro targets after scaling
        public double deviationScore(double targetProtein, double targetCarbs, double targetFat) {
            double scale = scaleToMatch(targetProtein, targetCarbs, targetFat);
            double p = protein * scale;
            double c = carbs * scale;
            double f = fat * scale;
            return Math.abs(p - targetProtein) + Math.abs(c - targetCarbs) + Math.abs(f - targetFat);
        }
    }

    // Fetches recipes from Spoonacular API based on dietary restrictions and preferences
    public List<Recipe> searchRecipes(String diet, String intolerances, String cuisine, int number) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            String baseUrl = "https://api.spoonacular.com/recipes/complexSearch";
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("?apiKey=").append(ApiConfig.getSpoonacularApiKey());
            urlBuilder.append("&addRecipeNutrition=true");
            urlBuilder.append("&number=").append(number);

            if (!diet.equalsIgnoreCase("Standard"))
                urlBuilder.append("&diet=").append(URLEncoder.encode(diet, StandardCharsets.UTF_8));

            if (cuisine != null && !cuisine.isBlank() && !cuisine.equalsIgnoreCase("General"))
                urlBuilder.append("&cuisine=").append(URLEncoder.encode(cuisine, StandardCharsets.UTF_8));

            if (intolerances != null && !intolerances.isBlank())
                urlBuilder.append("&intolerances=").append(URLEncoder.encode(intolerances, StandardCharsets.UTF_8));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            // Read JSON response from API
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String response = reader.lines().reduce("", (acc, line) -> acc + line);
                JSONObject json = new JSONObject(response);
                JSONArray results = json.optJSONArray("results");

                if (results != null) {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        String title = obj.optString("title");
                        JSONArray nutritionArray = obj.optJSONObject("nutrition") != null
                            ? obj.optJSONObject("nutrition").optJSONArray("nutrients")
                            : null;

                        double protein = 0, carbs = 0, fat = 0, calories = 0;
                        if (nutritionArray != null) {
                            for (int j = 0; j < nutritionArray.length(); j++) {
                                JSONObject nutrient = nutritionArray.getJSONObject(j);
                                String name = nutrient.optString("name");
                                double amount = nutrient.optDouble("amount", 0);

                                switch (name) {
                                    case "Protein" -> protein = amount;
                                    case "Carbohydrates" -> carbs = amount;
                                    case "Fat" -> fat = amount;
                                    case "Calories" -> calories = amount;
                                }
                            }
                        }

                        // Only consider recipes with valid title and calorie data
                        if (!title.isBlank() && calories > 0)
                            recipes.add(new Recipe(title, protein, carbs, fat, calories));
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("\u26A0\uFE0F Spoonacular API error: " + e.getMessage());
        }
        return recipes;
    }

    // Chooses the best 'meals' number of recipes minimizing macro deviation
    public List<Recipe> selectBestRecipes(List<Recipe> candidates, int meals, double targetProtein, double targetCarbs, double targetFat) {
        return candidates.stream()
            .sorted(Comparator.comparingDouble(r -> r.deviationScore(targetProtein / meals, targetCarbs / meals, targetFat / meals)))
            .limit(meals)
            .toList();
    }
}
