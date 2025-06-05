package Models;

// Represents input data from a trainer for generating workout plans
public class TrainerData {
    private String muscleGroup;
    private String equipment;
    private String goal;

    // Constructor to initialize all fields
    public TrainerData(String muscleGroup, String equipment, String goal) {
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
        this.goal = goal;
    }

    // Getters and setters
    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
}
