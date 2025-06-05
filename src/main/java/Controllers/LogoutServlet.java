package Controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// Servlet mapped to handle logout functionality via /LogoutServlet
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Default constructor (not strictly necessary but provided explicitly)
    public LogoutServlet() {
        super();
    }

    // Handle GET requests (commonly used for logout actions)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set character encoding to support international characters
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Attempt to retrieve an existing session without creating a new one
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destroy session and all stored attributes
        }

        // Prevent cached content from being accessible after logout via the browser back button
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // Redirect the user to the homepage or login page after logout
        response.sendRedirect("index.html");
    }

    // Also handle POST requests by delegating to doGet (in case logout form uses POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
