package Controllers;

import Interfaces.INutritionistDAO;


import Interfaces.INutritionistService;
import DAO.NutritionistDAO;
import Services.NutritionistService;
import Utils.NutritionistApiClient;
import Models.NutritionistData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// Servlet mapped to /NutritionistMealPlanServlet, used for generating meal plans
@WebServlet("/NutritionistMealPlanServlet")
public class NutritionistMealPlanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for page routing and role-based access control
    private static final String LOGIN_PAGE = "login.html";                      // Redirect here if not authenticated
    private static final String JSP_PAGE = "NutritionistMealPlanGenerator.jsp"; // View to display generated meal plan
    private static final String ROLE = "Nutritionist";                          // Required role to access this servlet

    // Dependencies: DAO and Service layer abstractions
    private final INutritionistDAO nutritionistDAO = new NutritionistDAO();
    NutritionistApiClient apiClient = new NutritionistApiClient();
    INutritionistService nutritionistService = new NutritionistService(apiClient);


    // Handle POST requests to generate and store a meal plan
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Set UTF-8 encoding for safe input/output handling
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Validate session and user role
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            // Redirect unauthorized users to login
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Extract nutritionist input data from the HTTP request
            NutritionistData data = nutritionistService.extractDataFromRequest(request);

            // Generate a personalized meal plan using API/service logic
            String mealPlan = nutritionistService.generateMealPlan(data);

            // Persist the generated plan for the current user
            nutritionistDAO.saveMealPlan(userId, data, mealPlan);

            // Attach the meal plan to the request scope for display in the JSP
            request.setAttribute("mealPlan", mealPlan);

            // Forward to the meal plan display page
            request.getRequestDispatcher(JSP_PAGE).forward(request, response);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<h2>⚠️ Invalid input:</h2><p>" + e.getMessage() + "</p>");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<h2>⚠️ System error:</h2><pre>" + e.getMessage() + "</pre>");
        }
      
    }
}
