package Interfaces;

import Models.AthleteProgressRecord;

/**
 * Service interface for managing business logic related to an athlete's workout progress.
 * This abstracts the persistence layer (DAO) and focuses on logic like updating or retrieving stats.
 */
public interface IAthleteProgressService {

    /**
     * Logs a workout session for the given athlete. This includes:
     * - Updating or creating a weekly progress record
     * - Adding training duration and weight to the existing progress data
     */
    void logProgress(int userId, int duration, double weight) throws Exception;

    // Retrieves the most recent weekly progress stats for a specific athlete.
    AthleteProgressRecord fetchProgress(int userId) throws Exception;
}
