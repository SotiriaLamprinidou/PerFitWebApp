<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    // Fetch username from session and weight history JSON from request
    String username = (String) session.getAttribute("username");
    String weightHistoryJson = (String) request.getAttribute("weightHistoryJson");
    if (weightHistoryJson == null) {
        weightHistoryJson = "[]"; // Default empty array if null
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Progress Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Favicon and Stylesheets -->
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">
    
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <!-- Chart.js Library -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="logo">
            <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo" height="50">
        </div>
        <ul class="nav-links">
            <li><a href="./AthleteHome.jsp">Home</a></li>
            <li><a href="./AthleteTrainingPage.jsp">Training</a></li>
            <li><a href="AthleteProgressServlet">Progress</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
        </ul>
    </nav>

    <!-- Welcome Message -->
    <header class="page-header">
        <h1>Welcome back, <span class="highlight"><%= username %></span>!</h1>
        <p>Let's track your progress and stay strong 💪</p>
    </header>

    <!-- Progress Statistics + Logging Section -->
    <section class="progress-wrapper">

        <!-- Statistic Display Cards -->
        <div class="stat-box-container">

            <!-- Weekly Workout Count -->
            <div class="stat-box">
                <h3><i class="fas fa-dumbbell"></i> Workouts This Week</h3>
                <p><strong>${totalWorkouts}</strong> completed sessions</p>
            </div>

            <!-- Total Training Duration -->
            <div class="stat-box">
                <h3><i class="fas fa-clock"></i> Total Training Time</h3>
                <p><strong>${totalMinutes}</strong> minutes</p>
            </div>

            <!-- Weight Tracker with Chart -->
            <div class="stat-box tracker">
                <h3><i class="fas fa-weight"></i> Weight Tracker</h3>
                <p>
                    Current: <strong>${currentWeight} kg</strong><br>
                    Previous: <strong>${previousWeight} kg</strong>
                </p>
                <div class="tooltip">
                    <!-- Chart canvas for weight trend -->
                    <canvas id="weightChart" width="300" height="200"></canvas>
                </div>
            </div>
        </div>

        <!-- Form to Log New Workout/Weight -->
        <div class="log-box">
            <h2>Log New Progress</h2>
            <form action="AthleteProgressServlet" method="post">
                <div class="form-group">
                    <label for="duration">Duration (minutes)</label>
                    <input type="number" id="duration" name="duration" placeholder="e.g., 45" min="1" required>
                </div>
                <div class="form-group">
                    <label for="weight">Current Weight (kg)</label>
                    <input type="number" id="weight" name="weight" placeholder="e.g., 72" step="0.1" required>
                </div>
                <button type="submit">Save</button>
            </form>
        </div>

    </section>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Athletes. All rights reserved.</p>
    </footer>

    <!-- Script for Chart.js to render the weight graph -->
    <script>
        const weightHistory = <%= weightHistoryJson %>; // Data from server
        const ctx = document.getElementById('weightChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: weightHistory.map((_, i) => `Entry ${i + 1}`), // Label entries
                datasets: [{
                    label: 'Weight (kg)',
                    data: weightHistory,
                    borderColor: 'rgb(175, 105, 238)',
                    backgroundColor: 'rgba(175, 105, 238, 0.2)',
                    tension: 0.3 // Curve smoothness
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } },
                scales: {
                    x: { display: false }, // Hide x-axis
                    y: { beginAtZero: false }
                }
            }
        });
    </script>

</body>
</html>
