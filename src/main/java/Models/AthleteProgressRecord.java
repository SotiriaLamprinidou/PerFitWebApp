package Models;

// Represents an athlete's weekly progress data and weight history
public record AthleteProgressRecord(
    int weekWorkouts,
    int totalMinutes,
    double currentWeight,
    double previousWeight,
    String weightHistoryJson
) {}
