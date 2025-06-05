package Controllers;

import Interfaces.IUserService;

import Models.UserData;
import Services.UserService;
import enums.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

// This annotation maps the servlet to a specific URL pattern
@WebServlet("/AdminUserManagementServlet")
public class AdminUserManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Service layer abstraction for user-related operations
    private IUserService adminService;

    @Override
    public void init() {
        // Initialize the service when the servlet is created
        adminService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve current session (if exists) and get current user
            HttpSession session = request.getSession(false);
            UserData currentUser = (UserData) session.getAttribute("user");

            // Get the list of all users from the service
            List<UserData> users = adminService.listAllUsers();
            request.setAttribute("users", users);

            // Optionally provide current user ID to the JSP (e.g. for UI highlighting)
            if (currentUser != null) {
                request.setAttribute("currentUserId", currentUser.getId());
            }

            // Forward request to JSP page for rendering
            request.getRequestDispatcher("AdminUserManagement.jsp").forward(request, response);

        } catch (Exception e) {
            // Log and respond with a 500 error in case of any failure
            e.printStackTrace();
            response.sendError(500, "Error loading users: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Determine action type from request parameters
        String action = request.getParameter("action");

        try {
            // Handle the action (add, edit, delete) using helper methods
            switch (action) {
                case "add" -> handleAdd(request);
                case "edit" -> handleEdit(request);
                case "delete" -> handleDelete(request);
                default -> {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
                    return;
                }
            }

            // After performing action, reload users and forward to JSP
            HttpSession session = request.getSession(false);
            UserData currentUser = (UserData) session.getAttribute("user");
            List<UserData> users = adminService.listAllUsers();
            request.setAttribute("users", users);

            if (currentUser != null) {
                request.setAttribute("currentUserId", currentUser.getId());
            }

            request.getRequestDispatcher("AdminUserManagement.jsp").forward(request, response);

        } catch (Exception e) {
            // On error, set error message and forward to JSP
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("AdminUserManagement.jsp").forward(request, response);
        }
    }

    // Handles adding a new user to the system
    private void handleAdd(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String roleStr = request.getParameter("role");

        // Ensure email and username are unique
        if (adminService.findUserByEmail(email) != null) {
            throw new Exception("A user with this email already exists.");
        }
        if (adminService.findUserByUsername(username) != null) {
            throw new Exception("A user with this username already exists.");
        }

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Create and register the new user
        UserData user = new UserData(name, username, hashedPassword, email, roleStr);
        adminService.registerUser(user);
    }

    // Handles editing an existing user's details
    private void handleEdit(HttpServletRequest request) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        

        // Look up user by ID (not email!)
        UserData existingUser = adminService.findUserById(id);
        if (existingUser == null) {
            throw new Exception("User not found.");
        }

        // Only hash password if it's changed
        String hashedPassword = (password == null || password.isEmpty())
                ? existingUser.getPassword()
                : BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Convert role string to enum (optional: validate against known roles)
        UserRole role = UserRole.valueOf(existingUser.getRole().name());

        // Update user object
        UserData updatedUser = new UserData(id, name, username, hashedPassword, email, role.name());
        adminService.updateUser(updatedUser);
    }


    // Handles deleting a user by ID, prevents self-deletion
    private void handleDelete(HttpServletRequest request) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession(false);
        UserData currentUser = (UserData) session.getAttribute("user");

        // Prevent users from deleting their own accounts
        if (currentUser != null && currentUser.getId() == id) {
            throw new Exception("You cannot delete your own account.");
        }

        // Remove user and any related data
        adminService.removeUserAndDependencies(id);
    }
}
