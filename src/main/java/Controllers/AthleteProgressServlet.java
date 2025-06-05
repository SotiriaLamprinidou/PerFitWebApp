package Controllers;

import Interfaces.IAthleteProgressService;
import Models.AthleteProgressRecord;
import Services.AthleteProgressService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// Map the servlet to the URL pattern /AthleteProgressServlet
@WebServlet("/AthleteProgressServlet")
public class AthleteProgressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for redirection and access control
    private static final String LOGIN_PAGE = "login.html";             // Redirect here if not authenticated
    private static final String JSP_PAGE = "AthleteProgressPage.jsp";  // JSP to render progress data
    private static final String ROLE = "Athlete";                      // Expected role for access

    // Inject the service that handles business logic
    private final IAthleteProgressService progressService = new AthleteProgressService();

    // Handles POST requests, typically for submitting new progress data
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure UTF-8 encoding for incoming/outgoing data
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve session and get authenticated user info
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Redirect to login page if session is invalid or role is not Athlete
        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Parse submitted duration and weight values from form
            int duration = Integer.parseInt(request.getParameter("duration"));
            double weight = Double.parseDouble(request.getParameter("weight"));

            // Log the user's progress through the service layer
            progressService.logProgress(userId, duration, weight);

        } catch (NumberFormatException e) {
            // Handle input errors gracefully and return a 400 Bad Request
            System.err.println("[SECURITY] Invalid number format: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number input.");
            return;

        } catch (Exception e) {
            // Catch-all for other errors and respond with a 500 Internal Server Error
            System.err.println("[SERVER ERROR] " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error.");
            return;
        }

        // Redirect back to the same servlet to refresh the displayed data
        response.sendRedirect("AthleteProgressServlet");
    }

    // Handles GET requests, usually for viewing progress data
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set UTF-8 encoding for consistency
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Validate the session and ensure the user has the right role
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Redirect unauthorized users to login page
        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Fetch the athlete's progress record from the service
            AthleteProgressRecord progress = progressService.fetchProgress(userId);

            // Populate request attributes with progress data for use in JSP
            request.setAttribute("totalWorkouts", progress.weekWorkouts());
            request.setAttribute("totalMinutes", progress.totalMinutes());
            request.setAttribute("currentWeight", progress.currentWeight());
            request.setAttribute("previousWeight", progress.previousWeight());
            request.setAttribute("weightHistoryJson", progress.weightHistoryJson());

        } catch (Exception e) {
            // If fetching data fails, log and show error in the UI
            System.err.println("[DB ERROR] " + e.getMessage());
            request.setAttribute("errorMessage", "Unable to load progress data.");
        }

        // Forward the request to the JSP page for rendering
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }
}
