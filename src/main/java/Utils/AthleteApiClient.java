package Utils;

import com.google.gson.*;
import config.ApiConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.stream.Collectors;

public class AthleteApiClient {

    // Fetches a list of exercises based on muscle group, training method, and injury limitations.
    // Makes a call to the ExerciseDB API and filters results accordingly.
    public static List<String> fetchExercises(String muscleGroup, String method, String injury) throws IOException {
        List<String> exercises = new ArrayList<>();
        String target = mapMuscleGroup(muscleGroup); // Translate friendly group name to API term

        if (target == null) return exercises; // Exit early if mapping failed

        String apiUrl = ApiConfig.getExerciseDbBaseUrl() + "/exercises/target/" + target;
        HttpURLConnection conn = null;

        try {
            // Configure HTTP GET request with headers
            conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", ApiConfig.getRapidApiKey());
            conn.setRequestProperty("X-RapidAPI-Host", ApiConfig.getRapidApiHost());
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            // If not OK, return empty list
            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) return exercises;

            // Parse JSON response into an array
            String json = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    .lines().collect(Collectors.joining());
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            // Process each exercise entry
            for (JsonElement element : array) {
                if (!element.isJsonObject()) continue;
                JsonObject ex = element.getAsJsonObject();

                String name = ex.get("name").getAsString();

                // Skip exercise if it's risky for user's injury
                if (isInjuryRisk(name, injury)) continue;

                // Determine if exercise fits the training method
                String equipment = ex.has("equipment") ? ex.get("equipment").getAsString().toLowerCase() : "body weight";
                boolean valid = method == null ||
                        ("weighted".equalsIgnoreCase(method) && !equipment.contains("body weight")) ||
                        ("bodyweight".equalsIgnoreCase(method) && equipment.contains("body weight"));

                if (valid) exercises.add(name);
            }

        } catch (SocketTimeoutException e) {
            System.err.println("ExerciseDB timeout: " + e.getMessage());
        } catch (IOException | JsonParseException e) {
            System.err.println("ExerciseDB error: " + e.getMessage());
        } finally {
            if (conn != null) conn.disconnect(); // Always clean up connection
        }

        return exercises;
    }

    // Maps user-friendly muscle group names to ExerciseDB's expected targets
    private static String mapMuscleGroup(String muscle) {
        return switch (muscle.toLowerCase()) {
            case "chest" -> "pectorals";
            case "back" -> "lats";
            case "upper back" -> "traps";
            case "lower back" -> "spine";
            case "shoulders" -> "delts";
            case "biceps" -> "biceps";
            case "triceps" -> "triceps";
            case "abs", "core" -> "abs";
            case "legs", "quads" -> "quads";
            case "hamstrings" -> "hamstrings";
            case "glutes" -> "glutes";
            case "calves" -> "calves";
            case "forearms" -> "forearms";
            default -> null;
        };
    }

    // Determines whether a given exercise should be excluded due to injury concerns.
    // Basic keyword-matching logic to avoid aggravating injuries.
    private static boolean isInjuryRisk(String exercise, String injury) {
        if (injury == null || injury.isEmpty()) return false;
        String e = exercise.toLowerCase();
        String i = injury.toLowerCase();

        return (i.contains("shoulder") && (e.contains("press") || e.contains("raise") || e.contains("fly"))) ||
               (i.contains("knee") && (e.contains("squat") || e.contains("lunge") || e.contains("jump"))) ||
               (i.contains("back") && (e.contains("deadlift") || e.contains("bend") || e.contains("row"))) ||
               (i.contains("neck") && (e.contains("shrug") || e.contains("extension"))) ||
               (i.contains("wrist") && (e.contains("pushup") || e.contains("press"))) ||
               (i.contains("hip") && (e.contains("thrust") || e.contains("bridge")));
    }
}
