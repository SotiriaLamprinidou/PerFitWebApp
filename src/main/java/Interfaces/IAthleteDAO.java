package Interfaces;

import Models.AthleteData;

/**
 * Interface for Data Access Object (DAO) that handles persistence
 * operations related to athlete-specific program data.
 */
public interface IAthleteDAO {
     // Persists a training program for a given athlete. 
    void saveProgram(AthleteData user, String email, String program) throws Exception;
}
