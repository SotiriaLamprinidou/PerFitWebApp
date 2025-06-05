package Services;

import Interfaces.ITrainerDAO;
import Models.TrainerProgram;

import java.util.List;

public class TrainerProgramService {

    private final ITrainerDAO dao;

    public TrainerProgramService(ITrainerDAO dao) {
        this.dao = dao;
    }

    // Retrieves trainer programs for a user, and marks one as editable if editId is provided
    public List<TrainerProgram> getTrainerPrograms(int userId, String editIdParam) {
        List<TrainerProgram> programs = dao.getProgramsForUser(userId);

        if (editIdParam != null) {
            try {
                int editId = Integer.parseInt(editIdParam);
                for (TrainerProgram p : programs) {
                    if (p.getId() == editId) {
                        p.setEditMode(true); // enable edit mode for matching program
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid edit ID: " + editIdParam);
            }
        }

        return programs;
    }

    // Updates the content of a specific training program for a user
    public void updateProgram(int userId, int programId, String content) {
        dao.updateProgramContent(userId, programId, content);
    }
}
