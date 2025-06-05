package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import Models.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebFilter("/*")  // 🔒 This filter intercepts all incoming HTTP requests to the web app
public class RoleBasedAccessFilter implements Filter {

    // 🗺️ Map to store the route-to-allowed-roles relationship loaded from properties
    private final Map<String, List<String>> accessMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        // 🔃 Load role-based access control rules from a properties file at startup
        try (InputStream input = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("role-access.properties")) {

            if (input == null) {
                throw new RuntimeException("role-access.properties not found in resources.");
            }

            Properties props = new Properties();
            props.load(input);

            // 🧮 Populate the accessMap where each key is a route and value is a list of roles
            for (String path : props.stringPropertyNames()) {
                String[] roles = props.getProperty(path).split(",");
                accessMap.put(path.trim(), Arrays.asList(roles));  // no role normalization here
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading role-access.properties", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();  // 📍 The current request path
        HttpSession session = req.getSession(false);
        UserData user = (session != null) ? (UserData) session.getAttribute("user") : null;

        // 🎯 Check if this path has access restrictions
        List<String> allowedRoles = accessMap.get(path);

        if (allowedRoles != null) {
            // 🚫 User is either not logged in or lacks a required role
            if (user == null || !allowedRoles.contains(user.getRole().name().toLowerCase())) {
                res.sendRedirect("login.html");  // Redirect to login if unauthorized
                return;
            }
        }

        // ✅ User is authorized or path is public, continue request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Optional: clean up resources if needed (not used here)
    }
}
