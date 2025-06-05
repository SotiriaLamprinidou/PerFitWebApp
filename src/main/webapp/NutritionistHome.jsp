<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Nutritionist Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Link to shared layout styles -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

    <!-- Top Navigation Bar -->
    <nav class="navbar">
        <div class="logo">
            <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">
        </div>
        <ul class="nav-links">
            <!-- Navigation items for the nutritionist role -->
            <li><a href="NutritionistHome.jsp" class="hover:text-teal-400 font-bold">Home</a></li>
            <li><a href="NutritionistMealPlanGenerator.jsp" class="hover:text-teal-400">Meal Plan Generator</a></li>
            <li><a href="/PersonalFitnessGuide/NutritionistProgramsServlet" class="hover:text-teal-400">My Programs</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet" class="hover:text-teal-400">Logout</a></li>
        </ul>
    </nav>

    <!-- Page Header -->
    <header>
        <h1>Welcome, Nutritionist</h1>
        <p>Plan smarter diets with Perfit and manual customization.</p>
    </header>

    <!-- Feature Cards Section -->
    <section class="card-container">

        <!-- Card: Meal Plan Generator Tool -->
        <div class="card">
            <h2 class="card-title"><i class="fas fa-seedling"></i>Meal Plan Generator</h2>
            <p>Create a full day’s meal plan based on dietary goals, restrictions, and preferences.</p>
            <a href="NutritionistMealPlanGenerator.jsp">Start Generating</a>
        </div>

        <!-- Card: View/Edit Existing Meal Plans -->
        <div class="card">
            <h2 class="card-title"><i class="fas fa-edit"></i>Alter My Programs</h2>
            <p>View and edit previously generated meal plans to better fit your clients' evolving needs.</p>
            <a href="/PersonalFitnessGuide/NutritionistProgramsServlet">Edit Programs</a>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Nutritionists. All rights reserved.</p>
    </footer>

</body>
</html>
