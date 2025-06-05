package Controllers;

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

// Servlet to handle OTP verification and post-login redirection
@WebServlet("/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service layer dependencies
    private final IOtpService otpService = new OtpService(new OtpDAO(), new OtpSenderService());
    private final IUserService userService = new UserService();

    private static final int MAX_ATTEMPTS = 3; // Limit on OTP retries

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set UTF-8 encoding for safety
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve session
        HttpSession session = request.getSession(false);

        // No session? Redirect to login
        if (session == null) {
            System.out.println(">>> No session found. Redirecting to login.");
            response.sendRedirect("login.html");
            return;
        }

        // Get user email from session
        String email = (String) session.getAttribute("email");
        if (email == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Check if user is requesting a resend
        String action = request.getParameter("action");
        Integer attempts = (Integer) session.getAttribute("otpAttempts");
        if (attempts == null) attempts = 0;

        if ("resend".equals(action)) {
            try {
                // Resend OTP and reset attempt counter
                otpService.resendOtp(email);
                session.setAttribute("otpAttempts", 0);
                request.setAttribute("infoMessage", "A new OTP has been sent to your email.");
                request.setAttribute("attemptsLeft", MAX_ATTEMPTS);
                request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Failed to resend OTP. Please try again.");
                request.setAttribute("attemptsLeft", MAX_ATTEMPTS - attempts);
                request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
                return;
            }
        }

        // Validate OTP input format (must be 6 digits)
        String enteredOtp = request.getParameter("otp");
        if (enteredOtp == null || !enteredOtp.matches("\\d{6}")) {
            request.setAttribute("errorMessage", "Invalid OTP format.");
            request.setAttribute("attemptsLeft", MAX_ATTEMPTS - attempts);
            request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
            return;
        }

        // Check if max attempts exceeded
        if (attempts >= MAX_ATTEMPTS) {
            request.setAttribute("errorMessage", "You have used all 3 attempts.");
            request.setAttribute("attemptsLeft", 0);
            request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
            return;
        }

        try {
            // Attempt OTP verification
            boolean isValid = otpService.verifyOtp(email, enteredOtp);

            if (isValid) {
                // Reset attempts and mark session as verified
                session.removeAttribute("otpAttempts");
                session.setAttribute("otpVerified", true);

                // Load full user data from DB
                UserData user = userService.findUserByEmail(email);
                if (user == null) {
                    response.sendRedirect("login.html");
                    return;
                }

                // Store user in session for application-wide access
                session.setAttribute("user", user);

                // Role-based redirection to appropriate dashboard
                switch (user.getRole()) {
                    case ATHLETE -> response.sendRedirect("AthleteHome.jsp");
                    case TRAINER -> response.sendRedirect("TrainerHome.jsp");
                    case NUTRITIONIST -> response.sendRedirect("NutritionistHome.jsp");
                    case ADMIN -> response.sendRedirect("AdminHome.jsp");
                    default -> {
                        session.invalidate(); // Unknown role, force logout
                        response.sendRedirect("login.html");
                    }
                }

            } else {
                // Invalid OTP - increment attempts and show error
                attempts++;
                session.setAttribute("otpAttempts", attempts);
                request.setAttribute("errorMessage", "Invalid or expired OTP.");
                request.setAttribute("attemptsLeft", MAX_ATTEMPTS - attempts);
                request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // Handle unexpected server errors
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred.");
        }
    }
}
