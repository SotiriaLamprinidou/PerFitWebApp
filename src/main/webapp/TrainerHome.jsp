<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trainer Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Main layout styling -->
    <link rel="stylesheet" href="./css/layout.css?v=1.1">

    <!-- Reused styling from athlete.css for consistency -->
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="logo">
            <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">
        </div>
        <ul class="nav-links">
            <!-- Trainer navigation links -->
            <li><a href="TrainerHome.jsp">Home</a></li>
            <li><a href="TrainerProgramGenerator.jsp">Program Generator</a></li>
            <li><a href="/PersonalFitnessGuide/TrainerProgramsServlet" class="active">My Programs</a></li>
            <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
        </ul>
    </nav>

    <!-- Welcome Header -->
    <header>
        <h1>Welcome, Trainer</h1>
        <p>Design effective training programs with smart tools built just for you.</p>
    </header>

    <!-- Feature Cards Section -->
    <section class="card-container">
        <!-- Card: Program Generator -->
        <div class="card">
            <div>
                <h2 class="card-title"><i class="fas fa-microchip"></i>Program Generator</h2>
                <p>
                    Generate workouts based on muscle group and equipment availability.
                    Quickly produce sessions for strength, hypertrophy, or endurance.
                </p>
            </div>
            <!-- Button link to generator page -->
            <a href="TrainerProgramGenerator.jsp">Start Generating</a>
        </div>

        <!-- Card: Edit Existing Programs -->
        <div class="card">
            <div>
                <h2 class="card-title"><i class="fas fa-edit"></i>Alter My Programs</h2>
                <p>
                    Review and modify your previously generated training plans to match client progress and goals.
                </p>
            </div>
            <!-- Button link to view/edit saved programs -->
            <a href="/PersonalFitnessGuide/TrainerProgramsServlet">Edit Programs</a>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 PerFit Trainers. All rights reserved.</p>
    </footer>

</body>
</html>
