package Interfaces;

import Models.TrainerData;

public interface ITrainerService {

    // Generates a workout plan based on the provided trainer data
    String generatePlan(TrainerData data) throws Exception;
}
