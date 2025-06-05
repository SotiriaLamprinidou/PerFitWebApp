package enums;

/**
 * Enum representing user roles within the system.
 * Provides both a formal enum name and a display label.
 */
public enum UserRole {
    // Define roles with human-readable labels
    ADMIN("Admin"),
    ATHLETE("Athlete"),
    TRAINER("Trainer"),
    NUTRITIONIST("Nutritionist");

    // Display label (e.g., for UI or readable strings)
    private final String label;

    // Constructor to associate each enum with a label
    UserRole(String label) {
        this.label = label;
    }

    /**
     * Retrieves the display label for the role.
     * @return A human-readable string version of the role
     */
    public String getLabel() {
        return label;
    }

    /**
     * Parses a string to match a corresponding UserRole.
     * Matches against both the enum name and the label, case-insensitively.
     * 
     * @param role String value to match
     * @return Matching UserRole
     * @throws IllegalArgumentException if the role does not match any known values
     */
    public static UserRole fromString(String role) {
        for (UserRole r : UserRole.values()) {
            // Match against either the enum name or the label, case-insensitive
            if (r.name().equalsIgnoreCase(role) || r.getLabel().equalsIgnoreCase(role)) {
                return r;
            }
        }
        // Throw exception if no match found
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
