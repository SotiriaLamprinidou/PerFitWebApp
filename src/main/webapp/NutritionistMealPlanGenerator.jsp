<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Nutritionist Meal Plan Generator</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Stylesheets -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">
    <link rel="icon" href="./css/PerFitLogoGear.ico">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <div><img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo"></div>
    <ul class="nav-links">
        <!-- Navigation links for nutritionist -->
        <li><a href="NutritionistHome.jsp" class="hover:text-teal-400">Home</a></li>
        <li><a href="NutritionistMealPlanGenerator.jsp" class="hover:text-teal-400 font-bold">Meal Plan Generator</a></li>
        <li><a href="/PersonalFitnessGuide/NutritionistProgramsServlet" class="hover:text-teal-400">My Programs</a></li>
        <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
    </ul>
</nav>

<!-- Header Section -->
<section class="programs">
    <header>
        <h2>Meal Plan Generator</h2>
        <p>Generate daily meal plans using smart nutrition logic.</p>
    </header>

    <!-- Meal Plan Generation Form -->
    <form action="NutritionistMealPlanServlet" method="post" class="program-form">
    
         <!-- Allergies Input -->
        <div class="form-group">
            <label>Name:</label>
            <input type="text" name="name" required placeholder="e.g., niki">
        </div>
        
        <!-- Gender Dropdown -->
        <div class="form-group">
            <label>Gender:</label>
            <select name="gender" required>
                <option disabled selected value="">-- Select Gender --</option>
                <option>Male</option>
                <option>Female</option>
            </select>
        </div>
        
        <!-- User Input: Age -->
        <div class="form-group">
            <label>Age:</label>
            <input type="number" name="age" min="12" max="100" required placeholder="e.g., 30">
        </div>

        <!-- User Input: Height -->
        <div class="form-group">
            <label>Height (cm):</label>
            <input type="number" name="height" min="100" max="250" required placeholder="e.g., 175">
        </div>

        <!-- User Input: Weight -->
        <div class="form-group">
            <label>Weight (kg):</label>
            <input type="number" name="weight" min="30" max="200" required placeholder="e.g., 70">
        </div>

        <!-- Activity Level Dropdown -->
        <div class="form-group">
            <label>Activity Level:</label>
            <select name="activity" required>
                <option disabled selected value="">-- Select Activity Level --</option>
                <option>Sedentary (little to no exercise)</option>
                <option>Lightly active (1-3 days/week)</option>
                <option>Moderately active (3-5 days/week)</option>
                <option>Very active (6-7 days/week)</option>
            </select>
        </div>

        <!-- Meal Frequency Dropdown -->
        <div class="form-group">
            <label>Meal Frequency Preference:</label>
            <select name="mealFrequency" required>
                <option disabled selected value="">-- Select Frequency --</option>
                <option>3 meals/day</option>
                <option>5 small meals/day</option>
                <option>Intermittent Fasting (16/8)</option>
            </select>
        </div>

        <!-- Optional Cuisine Input -->
        <div class="form-group">
            <label>Preferred Cuisine (optional):</label>
            <input type="text" name="cuisine" placeholder="e.g., Mediterranean, Asian">
        </div>

        <!-- Goal Dropdown -->
        <div class="form-group">
            <label>Goal:</label>
            <select name="goal" required>
                <option disabled selected value="">-- Select Goal --</option>
                <option>Lose Weight</option>
                <option>Gain Muscle</option>
                <option>Maintain Weight</option>
            </select>
        </div>

        <!-- Diet Type Dropdown -->
        <div class="form-group">
            <label>Diet Type:</label>
            <select name="diet" required>
                <option disabled selected value="">-- Select Diet --</option>
                <option>Standard</option>
                <option>Vegan</option>
                <option>Keto</option>
                <option>Low Carb</option>
            </select>
        </div>

        <!-- Allergies Input -->
        <div class="form-group">
            <label>Allergies/Restrictions (optional):</label>
            <input type="text" name="allergies" placeholder="e.g., nuts, dairy">
        </div>

        <!-- Submit Button -->
        <div class="form-group" style="flex: 1 1 100%;">
            <button type="submit">Generate Plan</button>
        </div>
    </form>

    <!-- Display Generated Meal Plan -->
    <% if (request.getAttribute("mealPlan") != null) { %>
        <div class="program-result">
            <h3>Suggested Meal Plan:</h3>
            <pre><%= request.getAttribute("mealPlan") %></pre>
        </div>
    <% } %>
</section>

<!-- Disable submit button and show spinner while generating -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Generating Program...';
    });
</script>

<!-- Footer -->
<footer>
    <p>&copy; 2025 PerFit Nutritionists. All rights reserved.</p>
</footer>

</body>
</html>
