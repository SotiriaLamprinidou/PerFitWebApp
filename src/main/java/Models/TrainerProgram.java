package Models;

public class TrainerProgram {
    // Fields to store program details
    private int id;
    private String goal;
    private String muscleGroup;
    private boolean equipmentAvailable;
    private String content;
    private String createdAt;
    private boolean editMode;

    // Constructor to initialize all fields
    public TrainerProgram(int id, String goal, String muscleGroup, boolean equipmentAvailable, String content, String createdAt) {
        this.id = id;
        this.goal = goal;
        this.muscleGroup = muscleGroup;
        this.equipmentAvailable = equipmentAvailable;
        this.content = content;
        this.createdAt = createdAt;
        this.editMode = false;
    }

    // Getters
    public int getId() { return id; }
    public String getGoal() { return goal; }
    public String getMuscleGroup() { return muscleGroup; }
    public boolean isEquipmentAvailable() { return equipmentAvailable; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public boolean isEditMode() { return editMode; }

    // Setter
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
