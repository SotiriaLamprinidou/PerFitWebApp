<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String username = (String) session.getAttribute("username");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Generated Programs</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/layout.css?v=1.1">
    <link rel="stylesheet" href="./css/athlete.css?v=1.1">
    <link rel="icon" type="image/x-icon" href="./css/PerFitLogoGear.ico">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <div class="logo">
        <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">
    </div>
    <ul class="nav-links">
        <li><a href="TrainerHome.jsp">Home</a></li>
        <li><a href="TrainerProgramGenerator.jsp">Program Generator</a></li>
        <li><a href="/PersonalFitnessGuide/TrainerProgramsServlet" class="active">My Programs</a></li>
        <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
    </ul>
</nav>

<!-- Page Header -->
<header class="page-header">
    <h1>Welcome back, <span class="highlight"><%= username %></span>!</h1>
    <p>View and manage your previously created plans.</p>
</header>

<!-- Main Section: Display Programs -->
<section class="progress-wrapper">
    <c:choose>
        <c:when test="${not empty programs}">
            <c:forEach var="program" items="${programs}">
                <div class="program-card">
                    <div class="program-header">
                        <h2>${program.muscleGroup} Plan - ${program.goal}</h2>
                    </div>

                    <div class="program-meta">
                        <span><strong>Equipment:</strong> ${program.equipmentAvailable ? 'Yes' : 'No'}</span>
                        <span><strong>Created:</strong> ${program.createdAt}</span>
                    </div>

                    <c:choose>
                        <c:when test="${program.editMode}">
                            <form method="post" action="TrainerProgramsServlet" class="edit-form">
                                <input type="hidden" name="id" value="${program.id}" />
                                <textarea name="content">${program.content}</textarea>
                                <div class="program-actions">
                                    <button type="submit">Save</button>
                                </div>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="program-content">
                                <pre>${program.content}</pre>
                            </div>
                            <div class="program-actions">
                                <form method="get" action="TrainerProgramsServlet">
                                    <input type="hidden" name="edit" value="${program.id}" />
                                    <button type="submit">Edit</button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <div class="program-card" style="text-align: center;">
                <p style="color: #aaa;">No saved programs found.</p>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<!-- Footer -->
<footer>
    <p>&copy; 2025 PerFit Trainers. All rights reserved.</p>
</footer>

</body>
</html>
