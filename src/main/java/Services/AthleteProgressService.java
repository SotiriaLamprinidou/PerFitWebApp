package Services;

import Interfaces.IAthleteProgressDAO;
import Interfaces.IAthleteProgressService;
import Models.AthleteProgressRecord;
import DAO.AthleteProgressDAO;

public class AthleteProgressService implements IAthleteProgressService {

    // Handles database operations for athlete progress
    private final IAthleteProgressDAO dao = new AthleteProgressDAO(); // or inject

    @Override
    public void logProgress(int userId, int duration, double weight) throws Exception {
        // Validate input ranges
        if (duration <= 0 || duration > 1000 || weight <= 0 || weight > 500) {
            throw new IllegalArgumentException("Invalid input values.");
        }

        // Check if a record exists for this user this week
        boolean exists = dao.entryExistsForUserThisWeek(userId);

        // Update if exists, otherwise insert new progress
        if (exists) {
            dao.updateProgress(userId, duration, weight);
        } else {
            dao.insertProgress(userId, duration, weight);
        }
    }

    @Override
    public AthleteProgressRecord fetchProgress(int userId) throws Exception {
        // Get the latest progress record for the user
        return dao.getProgress(userId);
    }
}
