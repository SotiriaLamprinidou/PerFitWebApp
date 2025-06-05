<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Meta Configuration -->
    <meta charset="UTF-8">
    <title>AI Workout Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Custom Styles -->
    <link rel="stylesheet" href="./css/layout.css?=1.1">
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="logo">
            <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">
        </div>
        <ul class="nav-links">
            <li><a href="./AthleteHome.jsp" class="hover:text-teal-400">Home</a></li>
            <li><a href="./AthleteTrainingPage.jsp" class="hover:text-teal-400">Training</a></li>
            <li><a href="AthleteProgressServlet" class="hover:text-teal-400 font-bold">Progress</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet" class="hover:text-teal-400">Logout</a></li>
        </ul>
    </nav>

    <!-- Hero Section: Welcome message and value proposition -->
    <header>
        <h1>Welcome, Athlete</h1>
        <p>Receive fully personalized workouts tailored to your goals, crafted by expert guidance and your fitness profile.</p>
    </header>

    <!-- Feature Cards Section -->
    <section class="card-container">

        <!-- Card: AI-Personalized Workout Plans -->
        <div class="card">
            <h2 class="card-title"><i class="fas fa-robot"></i>Personalized Training Programs</h2>
            <p>
                Our advanced engine creates a plan tailored to your body, goals, and time. The more you train, the smarter it becomes.
            </p>
            <ul style="list-style: disc; padding-left: 20px; margin: 15px 0;">
                <li>Customized weekly schedules</li>
                <li>Built-in rest and recovery logic</li>
                <li>Auto-adapts to your progress</li>
            </ul>
            <a href="AthleteTrainingPage.jsp"
               style="display: inline-block; background-color: #AF69EE; color: #111; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none;">
               Generate My Plan
            </a>
        </div>

        <!-- Card: Progress Tracking -->
        <div class="card">
            <h2 class="card-title"><i class="fas fa-chart-line"></i>Track Your Progress</h2>
            <p>
                Monitor your fitness journey and track achievements.
            </p>
            <ul style="list-style: disc; padding-left: 20px; margin: 15px 0;">
                <li>Workouts completed this week</li>
                <li>Total training time</li>
                <li>Weight tracking and trends</li>
            </ul>
            <a href="./AthleteProgressPage.jsp"
               style="display: inline-block; background-color: #AF69EE; color: #111; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none;">
               Go to Progress Dashboard
            </a>
        </div>

    </section>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Athletes. All rights reserved.</p>
    </footer>

</body>
</html>
