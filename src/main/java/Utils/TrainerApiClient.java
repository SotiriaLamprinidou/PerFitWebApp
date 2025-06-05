package Utils;

import Models.TrainerData;
import com.google.gson.*;
import config.ApiConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TrainerApiClient {

    /**
     * Fetches a list of exercises based on muscle group and equipment preference.
     * Uses the ExerciseDB API through a GET request with appropriate headers and filters.
     *
     * @param trainer TrainerData containing muscle group and equipment preferences
     * @return list of exercise names (max 5), filtered by equipment match
     * @throws IOException if an API call fails
     */
    public static List<String> fetchExercises(TrainerData trainer) throws IOException {
        List<String> results = new ArrayList<>();

        // Translate the muscle group to ExerciseDB target muscle term
        String target = mapMuscle(trainer.getMuscleGroup());
        if (target == null) return results;

        String apiUrl = ApiConfig.getExerciseDbBaseUrl() + "/exercises/target/" + target;
        HttpURLConnection conn = null;

        try {
            // Prepare and send HTTP GET request
            conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", ApiConfig.getRapidApiKey());
            conn.setRequestProperty("X-RapidAPI-Host", ApiConfig.getRapidApiHost());
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logErrorStream(conn);
                return results;
            }

            // Parse API JSON response
            String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            JsonArray array = JsonParser.parseString(response).getAsJsonArray();

            for (JsonElement el : array) {
                if (!el.isJsonObject()) continue;

                JsonObject obj = el.getAsJsonObject();

                // Extract exercise name and equipment
                String name = obj.has("name") && !obj.get("name").isJsonNull()
                        ? obj.get("name").getAsString()
                        : null;

                String equip = obj.has("equipment") && !obj.get("equipment").isJsonNull()
                        ? obj.get("equipment").getAsString().toLowerCase()
                        : "body weight";

                if (name == null) continue;

                // Match equipment preference: 'no' => bodyweight/none, else gym equipment
                boolean match = "no".equalsIgnoreCase(trainer.getEquipment())
                        ? (equip.contains("body weight") || equip.contains("none"))
                        : (!equip.contains("body weight") && !equip.contains("none"));

                if (match) results.add(name);
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout while connecting or reading from ExerciseDB API: " + e.getMessage());
        } catch (IOException | JsonParseException e) {
            System.err.println("Error during ExerciseDB API fetch: " + e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }

        // Limit to 5 exercises
        return results.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Logs the error response body if the API call fails.
     */
    private static void logErrorStream(HttpURLConnection conn) {
        try (InputStream errorStream = conn.getErrorStream()) {
            if (errorStream != null) {
                String error = new BufferedReader(new InputStreamReader(errorStream))
                        .lines().collect(Collectors.joining("\n"));
                System.err.println("ExerciseDB API error response: " + error);
            }
        } catch (IOException ex) {
            System.err.println("Failed to read error stream: " + ex.getMessage());
        }
    }

    /**
     * Maps user-friendly muscle group names to ExerciseDB-compatible targets.
     *
     * @param muscleGroup human-readable muscle group
     * @return target name recognized by ExerciseDB
     */
    private static String mapMuscle(String muscleGroup) {
        return Map.ofEntries(
            Map.entry("chest", "pectorals"),
            Map.entry("back", "lats"),
            Map.entry("upper back", "traps"),
            Map.entry("lower back", "spine"),
            Map.entry("shoulders", "delts"),
            Map.entry("biceps", "biceps"),
            Map.entry("triceps", "triceps"),
            Map.entry("abs", "abs"),
            Map.entry("core", "abs"),
            Map.entry("legs", "quads"),
            Map.entry("quads", "quads"),
            Map.entry("hamstrings", "hamstrings"),
            Map.entry("glutes", "glutes"),
            Map.entry("calves", "calves"),
            Map.entry("forearms", "forearms"),
            Map.entry("arms", "biceps")
        ).getOrDefault(muscleGroup.toLowerCase(), null);
    }
}
