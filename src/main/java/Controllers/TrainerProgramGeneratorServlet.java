package Controllers;

import Interfaces.ITrainerDAO;
import DAO.TrainerDAO;
import Models.TrainerData;
import Interfaces.ITrainerService;
import Services.TrainerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// Servlet mapped to handle program generation requests from trainers
@WebServlet("/TrainerProgramGeneratorServlet")
public class TrainerProgramGeneratorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for view routing and role checking
    private static final String LOGIN_PAGE = "login.html";
    private static final String JSP_PAGE = "TrainerProgramGenerator.jsp";
    private static final String ROLE = "Trainer";

    // DAO and service initialization using interfaces for abstraction
    private final ITrainerDAO trainerDAO = new TrainerDAO();
    private final ITrainerService trainerService = new TrainerService(trainerDAO);

    // Utility method to clean user input (basic XSS protection)
    private String sanitize(String input) {
        return input == null ? null : input.trim().replaceAll("[<>\"']", "");
    }

    // Handle POST requests for generating and saving personalized workout plans
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Set UTF-8 encoding to ensure proper handling of form data
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve session and validate role
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null || !ROLE.equalsIgnoreCase(role)) {
            // Redirect to login if user is not authenticated or not a trainer
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        // Extract and sanitize trainer input from form fields
        TrainerData trainer = new TrainerData(
            sanitize(request.getParameter("muscleGroup")),
            sanitize(request.getParameter("equipment")),
            sanitize(request.getParameter("goal"))
        );

        try {
            // Generate a workout plan using the service layer
            String program = trainerService.generatePlan(trainer);

            // Persist the generated plan associated with the trainer's userId
            trainerDAO.saveTrainerProgram(userId, trainer, program);

            // Add the program to the request scope for rendering in the JSP
            request.setAttribute("program", program);

        } catch (Exception e) {
            // Log the error and show a user-friendly message
            e.printStackTrace();
            request.setAttribute("program", "Error generating plan. Try again.");
        }

        // Forward request and data to JSP page for displaying the result
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }
}
