<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Basic page setup -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Athletes Training</title>

    <!-- Stylesheets and favicon -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

    <!-- Top navigation bar -->
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

    <!-- Main workout program form -->
    <section id="programs" class="programs">
        <header>
            <h2>Training program Generator</h2>
            <p>Fill in the information below to get a smart workout plan.</p>
        </header>

        <!-- Form for user to submit personal and fitness data -->
        <form action="AthleteTrainingServlet" method="post" class="program-form">
            <!-- Input: Name -->
            <div class="form-group">
                <label for="firstName">First Name:</label>
                <input type="text" id="firstName" name="name" placeholder="Your name" required>
            </div>

            <!-- Input: Age -->
            <div class="form-group">
                <label for="age">Age:</label>
                <input type="number" id="age" name="age" placeholder="e.g., 30" min="1" max="120" required>
            </div>

            <!-- Input: Gender -->
            <div class="form-group">
                <label for="gender">Gender:</label>
                <select id="gender" name="gender" required>
                    <option disabled selected value="">-- Select Gender --</option>
                    <option>Female</option>
                    <option>Male</option>
                    <option>Prefer not to answer</option>
                </select>
            </div>

            <!-- Input: Height and Weight -->
            <div class="form-group">
                <label for="height">Height (in cm):</label>
                <input type="number" id="height" name="height" placeholder="e.g., 167" min="50" max="300" required>
            </div>
            <div class="form-group">
                <label for="weight">Weight (in kg):</label>
                <input type="number" id="weight" name="weight" placeholder="e.g., 72" min="20" max="300" required>
            </div>

            <!-- Input: Fitness goals -->
            <div class="form-group">
                <label for="fitnessGoals">Fitness Goals:</label>
                <select id="fitnessGoals" name="fitnessGoals" required>
                    <option disabled selected value="">-- Select Goal --</option>
                    <option>Lose Weight</option>
                    <option>Increase Strength</option>
                    <option>Improve Cardio Health</option>
                    <option>Increase Flexibility and Mobility</option>
                    <option>Improve General Health</option>
                    <option>Gain Muscle Mass</option>
                    <option>Enhance Athletic Performance</option>
                    <option>Increase Energy Levels</option>
                    <option>Achieve Body Toning and Shaping</option>
                    <option>Improve Sleep Quality</option>
                    <option>Reduce Stress and Anxiety</option>
                </select>
            </div>

            <!-- Input: Experience level -->
            <div class="form-group">
                <label for="strengthLevel">Experience Level:</label>
                <select id="strengthLevel" name="experienceLevel" required>
                    <option disabled selected value="">-- Select Experience Level --</option>
                    <option>New to Exercise</option>
                    <option>Some Experience</option>
                    <option>Advanced Athlete</option>
                </select>
            </div>

            <!-- Input: Preferred training method -->
            <div class="form-group">
                <label for="trainingMethod">Training Method:</label>
                <select id="trainingMethod" name="trainingMethod" required>
                    <option disabled selected value="">-- Select Method --</option>
                    <option value="bodyweight">Bodyweight Training</option>
                    <option value="weighted">Weighted Training</option>
                    <option value="cardio">Cardio</option>
                </select>
            </div>

            <!-- Input: Preferred activity -->
            <div class="form-group">
                <label for="activities">Preferred Activities:</label>
                <select id="activities" name="activities" required>
                    <option disabled selected value="">-- Select Activity --</option>
                    <option>Running/Jogging</option>
                    <option>Swimming</option>
                    <option>Cycling/Mountain Biking</option>
                    <option>Yoga</option>
                    <option>Pilates</option>
                    <option>Hiking</option>
                    <option>Dancing (e.g., Zumba, Hip-Hop)</option>
                    <option>Team Sports</option>
                    <option>Golf</option>
                    <option>Martial Arts</option>
                </select>
            </div>

            <!-- Input: Available training days -->
            <div class="form-group">
                <label for="trainingDays">Days Per Week You Can Train:</label>
                <select id="trainingDays" name="trainingDays" required>
                    <option disabled selected value="">-- Select Days --</option>
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
                    <option>6</option>
                    <option>7</option>
                </select>
            </div>

            <!-- Input: Preferred session length -->
            <div class="form-group">
                <label for="workoutLength">Length of Each Workout:</label>
                <select id="workoutLength" name="workoutLength" required>
                    <option disabled selected value="">-- Select Duration --</option>
                    <option>30-45 minutes</option>
                    <option>45-60 minutes</option>
                    <option>60-75 minutes</option>
                    <option>75-90 minutes</option>
                </select>
            </div>

            <!-- Input: Conditions and injuries -->
            <div class="form-group">
                <label for="conditions">Cardiovascular / Metabolic Conditions:</label>
                <select id="conditions" name="conditions" required>
                    <option disabled selected value="">-- Select Option --</option>
                    <option>No</option>
                    <option>Yes</option>
                    <option>Not Sure</option>
                </select>
            </div>

            <div class="form-group">
                <label for="injuries">Injuries that affect your workouts:</label>
                <select id="injuries" name="injuries" required>
                    <option disabled selected value="">-- Select Injury --</option>
                    <option>Lower Back Pain/Injury</option>
                    <option>Knee Pain/Injury</option>
                    <option>Shoulder Pain/Injury</option>
                    <option>Neck Pain/Injury</option>
                    <option>Ankle Sprain/Injury</option>
                    <option>Wrist or Elbow Pain/Injury</option>
                    <option>Hip Pain/Injury</option>
                    <option>No injuries</option>
                    <option>Other</option>
                </select>
            </div>

            <!-- Submit button -->
            <div class="form-group" style="flex: 1 1 100%;">
                <button type="submit">Create Program</button>
            </div>
        </form>

        <!-- Display generated workout program -->
        <% if (request.getAttribute("program") != null) { %>
            <div class="program-result">
                <h3>Your Generated Program:</h3>
                <pre><%= request.getAttribute("program") %></pre>
            </div>
        <% } %>
    </section>

    <!-- Show loading spinner after form submission -->
    <script>
        document.querySelector('form').addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Generating Program...';
        });
    </script>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Athletes. All rights reserved.</p>
    </footer>

</body>
</html>
