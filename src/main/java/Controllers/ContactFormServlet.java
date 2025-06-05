package Controllers;

import Interfaces.IContactFormService;
import Services.ContactFormService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/ContactFormServlet")
public class ContactFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IContactFormService contactFormService;

    @Override
    public void init() throws ServletException {
        contactFormService = new ContactFormService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        boolean success;
        String resultMessage;

        try {
            success = contactFormService.sendContactMessage(name, email, message);
            resultMessage = success ? "Message sent successfully!" : "Message failed to send.";
        } catch (Exception e) {
            resultMessage = "Error: " + e.getMessage();
        }

        // Redirect to #contact and include message
        response.sendRedirect("index.html?msg=" + java.net.URLEncoder.encode(resultMessage, "UTF-8") + "#contact");
    }
}
