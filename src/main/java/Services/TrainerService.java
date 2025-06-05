package Services;

import Interfaces.ITrainerDAO;
import Interfaces.ITrainerService;
import Models.TrainerData;
import Utils.TrainerApiClient;

import java.util.List;
import java.util.Set;

public class TrainerService implements ITrainerService {
    private final ITrainerDAO trainerDAO;
    private static final Set<String> VALID_GOALS = Set.of("strength", "hypertrophy", "endurance", "rehabilitation");

    public TrainerService(ITrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    // Generates a workout plan based on the trainer input
    @Override
    public String generatePlan(TrainerData trainer) throws Exception {
        // Validate input
        if (trainer.getMuscleGroup() == null || trainer.getEquipment() == null || 
            !VALID_GOALS.contains(trainer.getGoal().toLowerCase())) {
            throw new IllegalArgumentException("Invalid input");
        }

        // Fetch exercise list from external API
        List<String> exercises = TrainerApiClient.fetchExercises(trainer);
        if (exercises.isEmpty()) return "No exercises found for the specified criteria.";

        // Build workout plan
        StringBuilder plan = new StringBuilder();
        plan.append("• Focus: ").append(trainer.getGoal()).append("\n");
        plan.append("• Muscle Group: ").append(trainer.getMuscleGroup()).append("\n");
        plan.append("• Equipment: ").append("no".equalsIgnoreCase(trainer.getEquipment()) ? "Bodyweight" : "Gym/Weights").append("\n\n");

        // Format exercise instructions based on goal
        for (String exercise : exercises) {
            switch (trainer.getGoal().toLowerCase()) {
                case "strength" -> plan.append("• ").append(exercise).append(" - 4 sets of 4-6 reps (heavy)\n");
                case "hypertrophy" -> plan.append("• ").append(exercise).append(" - 3 sets of 8-12 reps\n");
                case "endurance" -> plan.append("• ").append(exercise).append(" - 2 sets of 15-20 reps\n");
                case "rehabilitation" -> plan.append("• ").append(exercise).append(" - 2 sets of 10 reps (slow tempo)\n");
                default -> plan.append("• ").append(exercise).append(" - 3 sets of 10 reps\n");
            }
        }

        return plan.toString();
    }
}
