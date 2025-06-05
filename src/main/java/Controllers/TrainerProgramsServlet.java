package Controllers;

import Interfaces.ITrainerDAO;
import DAO.TrainerDAO;
import Models.TrainerProgram;
import Services.TrainerProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Servlet responsible for displaying and updating trainer-generated programs
@WebServlet("/TrainerProgramsServlet")
public class TrainerProgramsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Constants for access control and view routing
    private static final String LOGIN_PAGE = "login.html";
    private static final String JSP_PAGE = "TrainerPrograms.jsp";
    private static final String ROLE = "Trainer";

    // Dependency initialization
    private final ITrainerDAO dao = new TrainerDAO();
    private final TrainerProgramService service = new TrainerProgramService(dao);

    // Handle GET requests to display trainer programs
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set encoding to properly handle international characters
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Validate session and trainer role
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE); // Redirect unauthenticated or unauthorized users
            return;
        }

        // Optional parameter for edit mode (used by JSP to show an editable program)
        String editId = request.getParameter("edit");

        // Fetch programs using the service layer
        List<TrainerProgram> programs = service.getTrainerPrograms(userId, editId);

        // Attach data to the request scope and forward to the JSP
        request.setAttribute("programs", programs);
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }

    // Handle POST requests to update a program's content
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Validate user session and role
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (userId == null || role == null || !ROLE.equalsIgnoreCase(role)) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        try {
            // Extract and parse program ID and new content
            int programId = Integer.parseInt(request.getParameter("id"));
            String updatedContent = request.getParameter("content");

            // Delegate update logic to the service
            service.updateProgram(userId, programId, updatedContent);

        } catch (NumberFormatException e) {
            // Log any parsing errors for debugging
            System.err.println("Invalid ID: " + request.getParameter("id"));
        }

        // Redirect to refresh the view
        response.sendRedirect("TrainerProgramsServlet");
    }
}
