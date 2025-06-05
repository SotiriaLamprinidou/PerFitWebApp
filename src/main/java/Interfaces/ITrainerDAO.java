package Interfaces;

import java.util.List;
import Models.TrainerData;
import Models.TrainerProgram;

public interface ITrainerDAO {

    // Saves a newly generated trainer program for the specified user
    void saveTrainerProgram(int userId, TrainerData data, String program) throws Exception;

    // Retrieves all training programs associated with a specific user
    List<TrainerProgram> getProgramsForUser(int userId);

    // Updates the content of a specific training program for a user
    void updateProgramContent(int userId, int programId, String newContent);
}
