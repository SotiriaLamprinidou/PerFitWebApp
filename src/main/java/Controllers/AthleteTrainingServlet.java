package Controllers;

import Interfaces.IAthleteService;
import Models.AthleteData;
import Services.AthleteService;
import config.DBConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// Servlet mapped to handle requests at /AthleteTrainingServlet
@WebServlet("/AthleteTrainingServlet")
public class AthleteTrainingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for page redirection and role validation
    private static final String LOGIN_PAGE = "login.html";               // Redirect if unauthenticated
    private static final String JSP_PAGE = "AthleteTrainingPage.jsp";    // JSP to display training program
    private static final String ROLE = "Athlete";                        // Expected user role

    // Service used for athlete-specific operations
    private final IAthleteService athleteService = new AthleteService();

    // Handle POST requests, typically to generate a training program
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set UTF-8 character encoding for proper handling of text data
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve session and extract email and role
        HttpSession session = request.getSession(false);
        String email = (session != null) ? (String) session.getAttribute("email") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Redirect to login page if session is invalid or role doesn't match
        if (email == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Extract athlete data (e.g. preferences, metrics) from the request
            AthleteData user = athleteService.extractAthleteDataFromRequest(request);

            // Generate a tailored training program and store it for the user
            String program = athleteService.generateAndStoreProgram(user, email);

            // Make the program available as a request attribute for the JSP
            request.setAttribute("program", program);

            // Forward request and data to the JSP page for rendering
            request.getRequestDispatcher(JSP_PAGE).forward(request, response);

        } catch (Exception e) {
            // Log the error and return a 500 Internal Server Error
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage());
        }
    }
}
