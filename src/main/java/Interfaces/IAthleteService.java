package Interfaces;

import Models.AthleteData;
import javax.servlet.http.HttpServletRequest;

public interface IAthleteService {

    // Extracts athlete input data from the HTTP request
    AthleteData extractAthleteDataFromRequest(HttpServletRequest request);

    // Generates a training program and stores it in the database
    String generateAndStoreProgram(AthleteData user, String email) throws Exception;
}
