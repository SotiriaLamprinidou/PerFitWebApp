package Utils;

import java.util.*;

public class SplitUtil {

    /**
     * Returns a weekly muscle group split based on the number of training days.
     *
     * @param days number of training days per week
     * @return map where the key is the training day and the value is an array of target muscle groups
     */
    public static Map<Integer, String[]> getSplit(int days) {
        Map<Integer, String[]> split = new HashMap<>();

        switch (days) {
            case 1 -> 
                // One-day plan: full-body workout
                split.put(1, new String[]{"Full Body"});

            case 2 -> {
                // Two-day plan: upper/lower split
                split.put(1, new String[]{"Upper Body"});
                split.put(2, new String[]{"Lower Body"});
            }

            case 3 -> {
                // Three-day plan: push/pull/legs-style
                split.put(1, new String[]{"Chest", "Triceps"});
                split.put(2, new String[]{"Back", "Biceps"});
                split.put(3, new String[]{"Legs", "Abs"});
            }

            case 4 -> {
                // Four-day plan: more isolation with emphasis on legs and abs
                split.put(1, new String[]{"Chest", "Triceps"});
                split.put(2, new String[]{"Legs", "Glutes"});
                split.put(3, new String[]{"Shoulders", "Biceps"});
                split.put(4, new String[]{"Abs", "Legs"});
            }

            case 5 -> {
                // Five-day plan: classic body part split
                split.put(1, new String[]{"Chest"});
                split.put(2, new String[]{"Back"});
                split.put(3, new String[]{"Legs"});
                split.put(4, new String[]{"Shoulders"});
                split.put(5, new String[]{"Arms", "Abs"});
            }

            case 6, 7 -> {
                // Six- or seven-day plan: advanced split with dedicated days and recovery
                split.put(1, new String[]{"Chest"});
                split.put(2, new String[]{"Back"});
                split.put(3, new String[]{"Legs"});
                split.put(4, new String[]{"Shoulders"});
                split.put(5, new String[]{"Arms"});
                split.put(6, new String[]{"Abs", "Mobility"});
                if (days == 7)
                    // 7th day is active recovery (e.g. light cardio or stretching)
                    split.put(7, new String[]{"Active Recovery"});
            }

            default -> 
                // Default fallback to full-body day
                split.put(1, new String[]{"Full Body"});
        }

        return split;
    }
}
