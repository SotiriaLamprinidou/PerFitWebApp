package Models;

// Holds input data for generating a nutritionist meal plan
public record NutritionistData(
	    String name,
	    String gender,
	    int age,
	    int height,
	    int weight,
	    String goal,
	    String diet,
	    String allergies,
	    String activity,
	    String mealFrequency,
	    String cuisine
){}
