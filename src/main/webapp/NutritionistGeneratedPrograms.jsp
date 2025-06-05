<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, Models.NutritionistProgram" %>
<%
    // Retrieve logged-in username from session
    String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Meal Plans</title>
    
    <!-- Styles and Icons -->
    <link rel="icon" href="./css/PerFitLogoGear.ico">
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

<!-- Navbar -->
<nav class="navbar">
    <div><img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo"></div>
    <ul class="nav-links">
        <!-- Navigation links for the nutritionist role -->
        <li><a href="NutritionistHome.jsp" class="hover:text-teal-400 font-bold">Home</a></li>
        <li><a href="NutritionistMealPlanGenerator.jsp" class="hover:text-teal-400">Meal Plan Generator</a></li>
        <li><a href="/PersonalFitnessGuide/NutritionistProgramsServlet" class="hover:text-teal-400">Saved Plans</a></li>
        <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
    </ul>
</nav>

<!-- Welcome Header -->
<header class="page-header">
    <h1>Welcome back, <span class="highlight"><%= username %></span>!</h1>
    <p>Manage your saved meal plans.</p>
</header>

<!-- Meal Plan Cards Section -->
<section class="progress-wrapper">
<%
    // Retrieve list of saved meal plans from the request scope
    List<NutritionistProgram> programs = (List<NutritionistProgram>) request.getAttribute("programs");

    if (programs != null && !programs.isEmpty()) {
        for (NutritionistProgram program : programs) {
            boolean isEdit = program.isEditMode(); // Check if current card is in edit mode
%>

    <!-- Meal Plan Card -->
    <div class="meal-card">

        <!-- Header -->
        <div class="program-header">
            <h2><%= program.getGoal() %> - <%= program.getDietType() %> Meal Plan</h2>
        </div>

        <% if (isEdit) { %>
        <!-- Edit Mode: Form to update the meal plan content -->
        <form method="post" action="NutritionistProgramsServlet" class="edit-form">
            <input type="hidden" name="id" value="<%= program.getId() %>">
            <textarea name="content"><%= program.getMealPlan() %></textarea>
            <div class="program-actions">
                <button type="submit">Save</button>
            </div>
        </form>
        <% } else { %>

        <!-- View Mode: Display meal plan details and content -->
        <div class="mealplan-display">

            <!-- Meal Plan Metadata: split in two columns -->
            <div class="mealplan-meta">
                <div class="meta-left">
                    <p><strong>Name:</strong> <%= program.getName() %></p>
                    <p><strong>Gender:</strong> <%= program.getGender() %></p>
                    <p><strong>Height:</strong> <%= program.getHeightCm() %> cm</p>
                    <p><strong>Weight:</strong> <%= program.getWeightKg() %> kg</p>
                    <p><strong>Goal:</strong> <%= program.getGoal() %></p>
                    <p><strong>Activity Level:</strong> <%= program.getActivityLevel() %></p>     
                </div>
                <div class="meta-right">
                    <p><strong>Cuisine:</strong> <%= program.getCuisinePreference() %></p>
                    <p><strong>Diet:</strong> <%= program.getDietType() %></p>
                    <p><strong>Allergies:</strong> <%= program.getAllergies() %></p>
                    <p><strong>Meal Frequency:</strong> <%= program.getMealFrequency() %></p>
                    <p><strong>Created:</strong> <%= program.getCreatedAt() %></p>
                </div>
            </div>

            <hr class="mealplan-divider">

            <!-- Meal Plan Content (formatted using <pre> for readability) -->
            <pre><%= program.getMealPlan() %></pre>
        </div>

        <!-- Edit Button -->
        <div class="program-actions">
            <form method="get" action="NutritionistProgramsServlet">
                <input type="hidden" name="edit" value="<%= program.getId() %>">
                <button type="submit">Edit</button>
            </form>
        </div>
        <% } %>
    </div>

<% 
        } // end for
    } else { 
%>
    <!-- No Meal Plans Found Message -->
    <div class="meal-card">
        <p style="color: #aaa;">No saved meal plans found.</p>
    </div>
<% } %>
</section>

<!-- Footer -->
<footer>
    <p>&copy; 2025 PerFit Nutritionists. All rights reserved.</p>
</footer>

</body>
</html>
