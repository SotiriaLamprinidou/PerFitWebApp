package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final String PROPERTIES_FILE = "config.properties";
    private static Properties properties;

    // Load properties on class initialization
    static {
        properties = new Properties();
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    // RapidAPI config
    public static String getRapidApiKey() {
        return properties.getProperty("rapidapi.key");
    }

    public static String getRapidApiHost() {
        return properties.getProperty("rapidapi.host");
    }

    public static String getExerciseDbBaseUrl() {
        return properties.getProperty("exercisedb.baseurl");
    }

    // USDA API config
    public static String getUsdaApiKey() {
        return properties.getProperty("usda.api.key");
    }

    public static String getUsdaApiBaseUrl() {
        return properties.getProperty("usda.api.baseurl");
    }
    
    public static String getSpoonacularApiKey() {
        return properties.getProperty("spoonacular.api.key");
    }

    // Cache duration in minutes (default: 1440 mins / 24 hours)
    public static int getUsdaCacheExpiryMinutes() {
        try {
            return Integer.parseInt(properties.getProperty("usda.cache.expiry.minutes", "1440"));
        } catch (NumberFormatException e) {
            return 1440;
        }
    }
}
