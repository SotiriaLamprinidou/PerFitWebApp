<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AI Program Generator</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Core Stylesheets -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">         <!-- Main layout styles -->
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">        <!-- Reused styling for forms/cards -->
    <link rel="icon" href="./css/PerFitLogoGear.ico">             <!-- Favicon -->

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
            <!-- Trainer-specific navigation -->
            <li><a href="TrainerHome.jsp">Home</a></li>
            <li><a href="TrainerProgramGenerator.jsp">Program Generator</a></li>
            <li><a href="/PersonalFitnessGuide/TrainerProgramsServlet" class="active">My Programs</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
        </ul>
    </nav>

    <!-- Page Header -->
    <header class="page-header">
        <h1>Program Generator</h1>
        <p>Select muscle group and equipment to generate a program.</p>
    </header>

    <!-- Form Section: Input preferences for program generation -->
    <section class="program-form">
        <form action="TrainerProgramGeneratorServlet" method="post" style="width: 100%;">
            
            <!-- Workout Goal Selection -->
            <div class="form-group">
                <label for="goal">Workout Goal</label>
                <select id="goal" name="goal" required>
                    <option value="">-- Select --</option>
                    <option>Strength</option>
                    <option>Hypertrophy</option>
                    <option>Endurance</option>
                    <option>Rehabilitation</option>
                </select>
            </div>

            <!-- Muscle Group Selection -->
            <div class="form-group">
                <label for="muscleGroup">Muscle Group</label>
                <select id="muscleGroup" name="muscleGroup" required>
                    <option value="">-- Select --</option>
                    <option>Chest</option>
                    <option>Back</option>
                    <option>Legs</option>
                    <option>Shoulders</option>
                    <option>Arms</option>
                    <option>Core</option>
                    <option>Full Body</option>
                </select>
            </div>

            <!-- Equipment Availability -->
            <div class="form-group">
                <label for="equipment">Equipment Available?</label>
                <select id="equipment" name="equipment" required>
                    <option value="">-- Select --</option>
                    <option value="yes">Yes</option>
                    <option value="no">No</option>
                </select>
            </div>

            <!-- Submit Button -->
            <button type="submit">Generate</button>
        </form>
    </section>

    <!-- Display Section: Render the generated program if available -->
    <% if (request.getAttribute("program") != null) { %>
    <section class="program-result">
        <h3>Suggested Exercises:</h3>
        <pre><%= request.getAttribute("program") %></pre>
    </section>
    <% } %>

    <!-- Loading Indicator Script -->
    <script>
        document.querySelector('form').addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Generating Program...';
        });
    </script>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Trainers. All rights reserved.</p>
    </footer>

</body>
</html>
