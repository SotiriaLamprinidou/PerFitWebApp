// Controllers/NutritionistProgramsServlet.java
package Controllers;

import Interfaces.INutritionistDAO;
import DAO.NutritionistDAO;
import Models.NutritionistProgram;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Servlet that manages viewing and editing of nutritionist-generated meal plans
@WebServlet("/NutritionistProgramsServlet")
public class NutritionistProgramsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for redirection and access control
    private static final String LOGIN_PAGE = "login.html";
    private static final String JSP_PAGE = "NutritionistGeneratedPrograms.jsp";
    private static final String ROLE = "Nutritionist";

    // Dependency: DAO for accessing and updating meal plans
    private final INutritionistDAO nutritionistDAO = new NutritionistDAO();

    // Handles POST requests for updating an existing meal plan's content
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set character encoding to handle special characters (e.g., accents, symbols)
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Session validation and role-based access check
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session !=null) ? (String) session.getAttribute("role") : null;

        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            // If user is not authenticated or not a nutritionist, redirect to login
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Extract plan ID and updated content from the request
            int planId = Integer.parseInt(request.getParameter("id"));
            String updatedPlan = request.getParameter("content");

            // Validate that content is not null or empty
            if (updatedPlan == null || updatedPlan.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Plan content is empty.");
                return;
            }

            // Attempt to update the plan; fails silently if not allowed
            boolean success = nutritionistDAO.updateMealPlanContent(userId, planId, updatedPlan.trim());
            if (!success) {
                System.err.println("Update failed or unauthorized for plan ID: " + planId);
            }

        } catch (NumberFormatException e) {
            // Handle invalid plan ID
            System.err.println("Invalid plan ID: " + request.getParameter("id"));
        } catch (Exception e) {
            // Log any unexpected errors
            e.printStackTrace();
        }

        // Redirect back to the GET handler to refresh the view
        response.sendRedirect("NutritionistProgramsServlet");
    }

    // Handles GET requests to load and display existing meal plans
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Session and role validation
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Fetch all meal plans created by the currently logged-in nutritionist
            List<NutritionistProgram> mealPlans = nutritionistDAO.getMealPlansByUser(userId);

            // Optional: Check if one specific plan is being edited (UI hint)
            String editId = request.getParameter("edit");
            if (editId != null) {
                try {
                    int editIdInt = Integer.parseInt(editId);
                    for (NutritionistProgram program : mealPlans) {
                        if (program.getId() == editIdInt) {
                            program.setEditMode(true);
                            break;
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
            request.setAttribute("programs", mealPlans);


        } catch (Exception e) {
            // Handle and report data loading errors
            e.printStackTrace();
            request.setAttribute("errorMessage", "Could not load meal plans.");
        }

        // Forward the request to the JSP page for rendering the list of meal plans
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }
}
