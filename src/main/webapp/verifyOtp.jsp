<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>OTP Verification</title>

    <!-- Styles and Favicon -->
    <link rel="stylesheet" href="./css/login.css?v=1.1">
    <link rel="icon" href="./css/PerFitLogoGear.ico" type="image/x-icon">
</head>
<body>

<!-- OTP Container -->
<div class="container">
    <h2>Enter OTP</h2>

    <!-- Error message display -->
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>

    <!-- Informational message display -->
    <c:if test="${not empty infoMessage}">
        <div class="info-message">${infoMessage}</div>
    </c:if>

    <!-- Display number of attempts left -->
    <c:if test="${not empty attemptsLeft}">
        <div class="error-message">
            <c:choose>
                <c:when test="${attemptsLeft == 0}">
                    You have used all 3 attempts.
                </c:when>
                <c:otherwise>
                    Attempts remaining: <strong>${attemptsLeft}</strong>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <!-- OTP Verification Form -->
    <form action="VerifyOtpServlet" method="POST">
        <label for="otp">OTP:</label>
        <!-- Input restricted to 6-digit numeric pattern -->
        <input type="text" id="otp" name="otp" pattern="\d{6}" title="OTP must be 6 digits">

        <!-- Button controls -->
        <div class="button-group">
            <!-- Resend button starts disabled until countdown finishes -->
            <button type="submit" name="action" value="resend" class="resend-btn" id="resend-btn" disabled>Resend OTP</button>
            <button type="submit" name="action" value="verify">Verify OTP</button>
        </div>

        <!-- Countdown message shown under buttons -->
        <div id="countdown-msg" style="margin-top: 10px; color: gray; font-size: 14px;"></div>
    </form>
</div>

<!-- Back to homepage -->
<a href="index.html" class="back-to-home" style='font-size:29px;'>&#129144;</a>

<!-- Countdown Script for enabling Resend OTP -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    const resendBtn = document.getElementById("resend-btn");
    const countdownMsg = document.getElementById("countdown-msg");
    const form = document.querySelector("form");

    let seconds = 60; // Countdown duration (in seconds)

    updateCountdownMsg(seconds); // Initialize message

    // Countdown logic
    const interval = setInterval(() => {
        seconds--;

        if (seconds > 0) {
            updateCountdownMsg(seconds);
        } else {
            clearInterval(interval);
            resendBtn.disabled = false;
            countdownMsg.textContent = "You can now resend OTP";
        }
    }, 1000);

    // Prevent early submission if resend is clicked before enabled
    resendBtn.addEventListener("click", (e) => {
        if (resendBtn.disabled) {
            e.preventDefault();
        }
    });

    // Updates countdown message each second
    function updateCountdownMsg(sec) {
        countdownMsg.textContent = "You can resend OTP in " + sec + " second" + (sec === 1 ? "" : "s");
    }
});
</script>

</body>
</html>
