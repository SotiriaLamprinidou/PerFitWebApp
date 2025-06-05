package Interfaces;

import Models.AthleteProgressRecord;

/**
 * Interface for accessing and managing athlete training progress data in the database.
 * This is typically used to track weekly workout stats and weight changes.
 */
public interface IAthleteProgressDAO {

     // Checks if the athlete has already logged a progress entry in the current week.     
    boolean entryExistsForUserThisWeek(int userId) throws Exception;

     // Updates the existing weekly progress record with new training duration and weight entry.
    void updateProgress(int userId, int duration, double weight) throws Exception;

    // Inserts a new progress record for the user, assuming no existing entry for the week.
    
    void insertProgress(int userId, int duration, double weight) throws Exception;

    // Retrieves the most recent progress record for the given athlete.
     
    AthleteProgressRecord getProgress(int userId) throws Exception;
}
