<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AI Workout Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Link to custom layout CSS -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

    <!-- Navbar section: logo and navigation links -->
    <nav class="navbar">
        <!-- Admin Logo Image -->
        <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">

        <!-- Navigation links for the admin -->
        <ul class="nav-links">
            <li><a href="./AdminHome.jsp">Home</a></li>
            <li><a href="/PersonalFitnessGuide/AdminUserManagementServlet">Management</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
        </ul>
    </nav>

    <!-- Hero header section with welcome message -->
    <header>
        <h1>Welcome, Admin</h1>
        <p>Manage users, oversee platform activity, and ensure a personalized training experience for all athletes.</p>
    </header>

    <!-- Admin Capabilities Card Section -->
    <section class="card-container">
        <div class="card">
            <h2 class="card-title">
                <!-- Admin icon from Font Awesome -->
                <i class="fas fa-user-shield"></i> Admin Capabilities
            </h2>

            <!-- Brief description of admin privileges -->
            <p class="card-description">
                As an administrator, you have access to powerful tools to manage the platform effectively:
            </p>

            <!-- List of admin functions -->
            <ul class="card-list">
                <li>Add new users (e.g., athletes or other admins)</li>
                <li>Edit existing user information (roles, credentials, etc.)</li>
                <li>Delete users from the system</li>
            </ul>

            <!-- Vertical spacing -->
            <div style="height: 16px;"></div>

            <!-- Button linking to user management servlet -->
            <div class="card-button-container">
                <a href="/PersonalFitnessGuide/AdminUserManagementServlet" class="admin-card-button">
                    Manage Users
                </a>
            </div>
        </div>
    </section>

    <!-- Page footer -->
    <footer class="footer">
        <p>&copy; 2025 PerFit Admins. All rights reserved.</p>
    </footer>

</body>
</html>
