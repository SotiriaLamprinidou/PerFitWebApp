<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Signin Form</title>

    <!-- Main CSS for styling the form -->
    <link rel="stylesheet" href="./css/login.css?v=1.2">

    <!-- Font Awesome icons for UI -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <!-- Favicon -->
    <link rel="icon" href="./css/PerFitLogoGear.ico" type="image/x-icon">
</head>
<body>

<!-- Signin Form Container -->
<div class="login-form">
    <!-- Form for account registration -->
    <form action="SigninUserServlet" method="post">
        <h2>Sign in</h2>
        
        <!-- Alert box for error or success messages -->
        <div id="alertBox" class="alert-message" style="display:none;"></div>
     
        <!-- Name input -->
        <div class="form-group">
            <input type="text" class="form-control" placeholder="Name" name="name" required>
        </div>

        <!-- Username input -->
        <div class="form-group">
            <input type="text" class="form-control" placeholder="Username" name="username" required>
        </div>

        <!-- Password input with visibility toggle and validation rules -->
        <div class="form-group password-field-group">
            <div class="input-with-icon">
                <input
                    type="password"
                    id="password"
                    name="password"
                    class="form-control"
                    placeholder="Password"
                    onfocus="showPasswordRules('')"
                    onblur="scheduleHidePasswordRules('')"
                    oninput="validatePassword('password', 'submit-btn', '')"
                />
                <button type="button" class="password-toggle" onclick="togglePassword('password', this)">
                    <i class="fas fa-eye"></i>
                </button>
            </div>

            <!-- Password requirement list -->
            <ul class="password-rules" id="password-rules" style="display: none;">
                <li id="length" class="invalid">At least 8 characters</li>
                <li id="uppercase" class="invalid">At least 1 uppercase letter</li>
                <li id="lowercase" class="invalid">At least 1 lowercase letter</li>
                <li id="number" class="invalid">At least 1 number</li>
                <li id="special" class="invalid">At least 1 special character (@$!%*?&)</li>
                <li id="spaces" class="invalid">No spaces</li>
            </ul>
        </div>

        <!-- Email input -->
        <div class="form-group">
            <input type="email" class="form-control" placeholder="Email" name="email" required>
        </div>

        <!-- Role selection -->
        <div class="form-group">
            <select class="form-control" name="role" required>
                <option value="" disabled selected hidden>Role</option>
                <option value="Athlete">Athlete</option>
                <option value="Trainer">Trainer</option>
                <option value="Nutritionist">Nutritionist</option>
            </select>
        </div>

        <!-- Submit button (disabled by default until valid password) -->
        <div class="form-group">
            <button type="submit" class="btn btn-primary" id="submit-btn">Sign in</button>
        </div>

        <!-- Navigation link for existing users -->
        <p class="text-center"><a href="./login.html">Already have an account</a></p>
    </form>
</div>

<!-- Back to homepage arrow -->
<a href="index.html" class="back-to-home" title="Back to home">&#129144;</a>

<!-- Password handling & validation script -->
<script>
    let passwordRulesVisible = false;
    let hideTimeout;

    // Toggle password visibility (eye icon)
    function togglePassword(inputId, toggleElement) {
        var input = document.getElementById(inputId);
        var icon = toggleElement.querySelector("i");
        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            input.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    }

    // Show password rules on input focus
    function showPasswordRules(prefix) {
        clearTimeout(hideTimeout);
        var rules = document.getElementById(prefix + "password-rules");
        if (rules) {
            rules.style.display = "block";
            passwordRulesVisible = true;
        }
    }

    // Hide rules if no input is entered after blur
    function scheduleHidePasswordRules(prefix) {
        hideTimeout = setTimeout(function () {
            var input = document.getElementById("password");
            var rules = document.getElementById(prefix + "password-rules");
            if (input && rules && input.value.trim() === "") {
                rules.style.display = "none";
                passwordRulesVisible = false;
            }
        }, 200);
    }

    // Real-time password validation logic
    function validatePassword(inputId, submitBtnId, prefix) {
        var passwordInput = document.getElementById(inputId);
        var submitBtn = document.getElementById(submitBtnId);
        if (!passwordInput) return;

        var password = passwordInput.value;
        var allValid = true;

        // Rules to check
        var rules = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            number: /[0-9]/.test(password),
            special: /[@$!%*?&]/.test(password),
            spaces: !/\s/.test(password)
        };

        // Update rule list display
        for (var key in rules) {
            if (rules.hasOwnProperty(key)) {
                var isValid = rules[key];
                var el = document.getElementById(prefix + key);
                if (el) {
                    el.classList.remove("valid", "invalid");
                    el.classList.add(isValid ? "valid" : "invalid");
                }
                if (!isValid) allValid = false;
            }
        }

        // Enable submit if all valid
        if (submitBtn) {
            submitBtn.disabled = !allValid;
        }
    }

    // Display message from server (e.g., username exists)
    window.addEventListener("DOMContentLoaded", () => {
        const params = new URLSearchParams(window.location.search);
        const message = params.get("message"); // Message could be error or success
        if (message) {
            const box = document.getElementById("alertBox");
            box.textContent = decodeURIComponent(message);
            box.style.display = "block";
        }
    });
</script>

</body>
</html>
