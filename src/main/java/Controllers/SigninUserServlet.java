package Controllers;

import Interfaces.IOtpDAO;
import Interfaces.IOtpSenderService;
import Interfaces.IOtpService;
import Interfaces.IUserService;
import Models.UserData;
import DAO.OtpDAO;
import Services.OtpSenderService;
import Services.OtpService;
import Services.UserService;
import enums.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import org.mindrot.jbcrypt.BCrypt;

// Servlet responsible for handling new user signups
@WebServlet("/SigninUserServlet")
public class SigninUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Dependency injection via interface-based services
    private final IUserService userService = new UserService();
    private final IOtpSenderService otpSender = new OtpSenderService();
    private final IOtpDAO otpDAO = new OtpDAO();
    private final IOtpService otpService = new OtpService(otpDAO, otpSender);

    // Handle POST request for user registration (sign-up)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure UTF-8 encoding to support international input
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve and sanitize user-submitted form fields
        String name = sanitize(request.getParameter("name"));
        String username = sanitize(request.getParameter("username"));
        String password = request.getParameter("password"); // Password not sanitized to preserve special chars
        String email = sanitize(request.getParameter("email"));
        String roleString = sanitize(request.getParameter("role"));

        // Input validation: check for missing/invalid values
        if (isInvalidInput(name, username, password, email, roleString) || !isValidEmail(email)) {
            redirectWithMessage(response, "signup.jsp", "Invalid or missing input!");
            return;
        }

        try {
            // Check if a user with the same username or email already exists
            if (userService.userExists(username, email)) {
                redirectWithMessage(response, "signup.jsp", "User already exists!");
                return;
            }

            // Securely hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // Parse role string into enum type
            UserRole roleEnum = UserRole.fromString(roleString);

            // Create a new UserData object and register the user
            UserData newUser = new UserData(name, username, hashedPassword, email, roleEnum.name());
            int userId = userService.registerAndReturnId(newUser);

            // Generate, store, and send a verification OTP
            String otp = otpService.generateOtp();
            otpService.storeOtp(email, otp);
            otpSender.sendOtpByEmail(email, otp);

            // Initialize a new session with user information
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(600); // 10 minutes session timeout
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("role", roleEnum.name());
            session.setAttribute("userId", userId);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("otpSent", true);
            session.setAttribute("otpAttempts", 0);

            // Redirect user to OTP verification page
            response.sendRedirect("verifyOtp.jsp");

        } catch (Exception e) {
            // Log and redirect to signup page with error message
            e.printStackTrace();
            redirectWithMessage(response, "signup.jsp", "Server error occurred!");
        }
    }

    // Utility method to validate basic email format
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Utility method to check for any null or empty input fields
    private boolean isInvalidInput(String... values) {
        for (String val : values) {
            if (val == null || val.trim().isEmpty()) return true;
        }
        return false;
    }

    // Simple sanitization to strip potential XSS characters from input
    private String sanitize(String input) {
        return (input == null) ? null : input.trim().replaceAll("[<>\"']", "");
    }

    // Utility to redirect to a page with a message (e.g., error or status)
    private void redirectWithMessage(HttpServletResponse response, String page, String message) throws IOException {
        String encoded = URLEncoder.encode(message, "UTF-8");
        response.sendRedirect(page + "?message=" + encoded);
    }
}
