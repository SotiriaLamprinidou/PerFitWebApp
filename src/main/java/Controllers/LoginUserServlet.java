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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

// Servlet mapped to handle login requests
@WebServlet("/LoginUserServlet")
public class LoginUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Dependency injection via interface implementations
    private final IUserService userService = new UserService();              // Handles user auth logic
    private final IOtpSenderService otpSender = new OtpSenderService();      // Handles sending OTPs
    private final IOtpDAO otpDAO = new OtpDAO();                             // Handles OTP persistence
    private final IOtpService otpService = new OtpService(otpDAO, otpSender);// Coordinates OTP logic

    // Simple input validation for username/password
    private boolean isValidInput(String input) {
        return input != null && input.matches("^[a-zA-Z0-9_@$!%*?&]{3,50}$");
    }

    // Handle POST requests for login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        authenticate(request, response);
    }

    // Allow GET requests to trigger the same logic (not ideal for login but may support OTP retry)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        authenticate(request, response);
    }

    // Core login and OTP dispatch logic
    private void authenticate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Set request and response encoding for internationalization support
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve username and password from form submission
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input format to prevent simple injection or malformed entries
        if (!isValidInput(username) || !isValidInput(password)) {
            alertBox(response, "Invalid username or password format.");
            return;
        }

        try {
            // Authenticate user using the service
            UserData user = userService.authenticateUser(username, password);

            if (user != null) {
                // Invalidate any existing session for security
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) oldSession.invalidate();

                // Start a new session and store necessary user info
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(600); // Session timeout in seconds (10 mins)

                // Store user info in session for future access control
                session.setAttribute("username", user.getUsername());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("role", user.getRole().name()); // Store role as String
                session.setAttribute("userId", user.getId());
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("otpSent", true);
                session.setAttribute("otpAttempts", 0);

                // Generate and store a new OTP
                String otp = otpService.generateOtp();
                otpService.storeOtp(user.getEmail(), otp);
                otpSender.sendOtpByEmail(user.getEmail(), otp); // Send the OTP via email

                // Redirect to OTP verification page
                response.sendRedirect("verifyOtp.jsp");

            } else {
                // Failed login attempt
                alertBox(response, "Incorrect username or password.");
            }

        } catch (Exception e) {
            // Catch-all exception handler
            e.printStackTrace();
            alertBox(response, "An unexpected error occurred. Please try again later.");
        }
    }

    // Utility method to safely redirect with a user-friendly error message
    private void alertBox(HttpServletResponse response, String message) throws IOException {
        // Strip any potentially unsafe characters from error messages
        String safeMsg = message.replaceAll("[^a-zA-Z0-9 ,.?!@#&()\\-]", "");
        response.sendRedirect("login.html?error=" + java.net.URLEncoder.encode(safeMsg, "UTF-8"));
    }
}
