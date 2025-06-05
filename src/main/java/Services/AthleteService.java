package Services;

import Interfaces.IAthleteService;
import Models.AthleteData;
import Interfaces.IAthleteDAO;
import DAO.AthleteDAO;
import Utils.AthleteApiClient;
import Utils.SplitUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class AthleteService implements IAthleteService {

    private final IAthleteDAO athleteDAO = new AthleteDAO();

    @Override
    public AthleteData extractAthleteDataFromRequest(HttpServletRequest request) {
        // Extracts form input and returns an AthleteData object
        return new AthleteData(
            request.getParameter("name"),
            Integer.parseInt(request.getParameter("age")),
            request.getParameter("gender"),
            Integer.parseInt(request.getParameter("height")),
            Integer.parseInt(request.getParameter("weight")),
            request.getParameter("fitnessGoals"),
            request.getParameter("experienceLevel"),
            request.getParameter("trainingMethod"),
            Integer.parseInt(request.getParameter("trainingDays")),
            request.getParameter("workoutLength"),
            request.getParameter("injuries"),
            request.getParameter("conditions"),
            request.getParameter("activities")
        );
    }

    @Override
    public String generateAndStoreProgram(AthleteData user, String email) throws Exception {
        // Generates training plan and saves it in DB
        String program = generateProgramString(user);
        athleteDAO.saveProgram(user, email, program);
        return program;
    }

    private String generateProgramString(AthleteData user) throws Exception {
        // Builds a personalized workout plan string
        StringBuilder plan = new StringBuilder();
        double bmi = user.weight() / Math.pow(user.height() / 100.0, 2);

        // Add user info
        plan.append("User: ").append(user.name()).append(", Age: ").append(user.age()).append(", Gender: ").append(user.gender()).append("\n");
        plan.append("Height: ").append(user.height()).append(" cm, Weight: ").append(user.weight()).append(" kg\n");
        plan.append("BMI: ").append(String.format("%.1f", bmi)).append("\n");
        plan.append("Goal: ").append(user.goal()).append("\nExperience: ").append(user.experience()).append("\n");
        plan.append("Method: ").append(user.method()).append(", Activity: ").append(user.activity()).append("\n");
        plan.append("Training Days: ").append(user.trainingDays()).append(", Duration: ").append(user.workoutLength()).append("\n");
        plan.append("Injury: ").append(user.injury()).append(", Condition: ").append(user.condition()).append("\n\n");

        // Warn if BMI is too high
        if (bmi >= 30) {
            plan.append("⚠️ Your BMI suggests a need for professional health guidance. Please consult a doctor or trainer.\n\n");
        }

        // Determine sets and reps
        int sets = switch (user.experience()) {
            case "Advanced Athlete" -> 5;
            case "Some Experience" -> 4;
            default -> 3;
        };

        int reps = switch (user.goal().toLowerCase()) {
            case "gain muscle mass" -> 10;
            case "lose weight", "achieve body toning and shaping" -> 12;
            case "increase strength" -> 6;
            default -> 10;
        };

        // Create a training day split and generate exercises
        Map<Integer, String[]> split = SplitUtil.getSplit(user.trainingDays());

        for (int day = 1; day <= user.trainingDays(); day++) {
            plan.append("🔥 Day ").append(day).append(" - ")
                .append(String.join(" & ", split.get(day))).append("\n");
            plan.append("Warm-up: light cardio + mobility (5–10 min)\n\n");

            for (String group : split.get(day)) {
                for (String part : group.split("&")) {
                    String trimmedPart = part.trim();
                    List<String> exercises = AthleteApiClient.fetchExercises(trimmedPart, user.method(), user.injury());
                    Collections.shuffle(exercises);

                    if (exercises.isEmpty()) {
                        plan.append("⚠️ No exercises found for ").append(trimmedPart).append("\n");
                        exercises = AthleteApiClient.fetchExercises(trimmedPart, null, user.injury());
                        if (!exercises.isEmpty()) {
                            plan.append("Found alternative exercises:\n");
                        }
                    }

                    for (int i = 0; i < Math.min(2, exercises.size()); i++) {
                        plan.append(" - ").append(exercises.get(i)).append(" ").append(sets).append(" sets x ").append(reps).append(" reps\n");
                    }
                }
            }

            plan.append("\nCooldown: static stretching\n\n");
        }

        return plan.toString();
    }
}
