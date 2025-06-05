package Models;

// Represents input data needed to generate an athlete training program
public record AthleteData(
    String name,
    int age,
    String gender,
    int height,
    int weight,
    String goal,
    String experience,
    String method,
    int trainingDays,
    String workoutLength,
    String injury,
    String condition,
    String activity
) {}
