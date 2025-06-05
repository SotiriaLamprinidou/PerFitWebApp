<%@ page import="java.util.List" %>
<%@ page import="Models.UserData" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Meta and Head Config -->
    <meta charset="UTF-8">
    <title>Admin User Management</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Favicon and CSS -->
    <link rel="icon" href="./css/PerFitLogoGear.ico">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="./css/admin.css?v=1.1">
    <link rel="stylesheet" href="./css/layout.css?v=1.1">

    <!-- Dynamic class coloring for validation feedback -->
    <style>
        .valid { color: #22c55e; }
        .invalid { color: #f87171; }
    </style>
</head>
<body>

<%
    // Fetch users and current admin ID from request scope
    List<Models.UserData> users = (List<Models.UserData>) request.getAttribute("users");
    Integer currentUserId = (Integer) request.getAttribute("currentUserId");
%>

<!-- Navigation Bar -->
<nav class="navbar">
    <img src="./css/PerFitLogo.png" alt="Logo" class="admin-logo">
    <ul class="nav-links">
        <li><a href="./AdminHome.jsp">Home</a></li>
        <li><a href="/PersonalFitnessGuide/AdminUserManagementServlet">Management</a></li>
        <li><a href="/PersonalFitnessGuide/LogoutServlet">Logout</a></li>
    </ul>
</nav>

<!-- Main Admin Panel Section -->
<section class="admin-card-container">
    <div class="admin-card">
        <h2 class="admin-card-title"><i class="fas fa-users"></i> Existing Users</h2>
        
        <!-- Users Table -->
        <table class="admin-table">
            <thead>
                <tr class="admin-table-header">
                    <th>Name</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <% if (users != null && !users.isEmpty()) {
                for (Models.UserData u : users) {
                    String prefix = "edit-" + u.getId() + "-"; // used to uniquely identify DOM elements per user
            %>
                <!-- Normal view row -->
                <tr class="admin-table-row" id="row-<%= u.getId() %>">
                    <td><%= u.getName() %></td>
                    <td><%= u.getUsername() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getRole() %></td>
                    <td>
                        <button onclick="editUser(<%= u.getId() %>)" class="admin-btn edit">Edit</button>
                        <% if (currentUserId == null || u.getId() != currentUserId){ %>
                            <button onclick="confirmDelete(<%= u.getId() %>)" class="admin-btn delete">Delete</button>
                        <% } else { %>
                            <span class="admin-you">You</span> <!-- Prevent self-deletion -->
                        <% } %>
                    </td>
                </tr>

                <!-- Editable row form -->
                <tr id="edit-row-<%= u.getId() %>" class="admin-edit-row hidden">
                    <form action="AdminUserManagementServlet" method="post">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="id" value="<%= u.getId() %>">
                        <td><input type="text" name="name" value="<%= u.getName() %>" class="admin-input"></td>
                        <td><input type="text" name="username" value="<%= u.getUsername() %>" class="admin-input"></td>
                        <td><input type="email" name="email" value="<%= u.getEmail() %>" class="admin-input"></td>
                        <td><%= u.getRole() %></td>
                        <td>
                            <!-- Password update field with validation and visibility toggle -->
                            <div class="admin-password-container input-with-icon">
                                <input type="password" name="password" id="<%= prefix %>password" placeholder="New Password"
                                    class="admin-password-input"
                                    onfocus="toggleRules('<%= prefix %>password-rules', true)"
                                    onblur="handleBlur('<%= prefix %>password', '<%= prefix %>password-rules')"
                                    oninput="validatePassword('<%= prefix %>password', '<%= prefix %>save', '<%= prefix %>')">
                                <button type="button" onclick="togglePassword('<%= prefix %>password', this)" class="admin-btn eye">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>

                            <!-- Password rules (shown on focus) -->
                            <div id="<%= prefix %>password-rules" class="admin-password-rules hidden">
                                <div id="<%= prefix %>length" class="invalid"> At least 8 characters</div>
                                <div id="<%= prefix %>uppercase" class="invalid"> At least 1 uppercase letter</div>
                                <div id="<%= prefix %>lowercase" class="invalid"> At least 1 lowercase letter</div>
                                <div id="<%= prefix %>number" class="invalid"> At least 1 number</div>
                                <div id="<%= prefix %>special" class="invalid"> At least 1 special character (@$!%*?&)</div>
                                <div id="<%= prefix %>spaces" class="invalid"> No spaces</div>
                            </div>

                            <!-- Save and cancel buttons -->
                            <button id="<%= prefix %>save" type="submit" class="admin-btn save" >Save</button>
                            <button type="button" onclick="cancelEdit(<%= u.getId() %>)" class="admin-btn cancel">Cancel</button>
                        </td>
                    </form>
                </tr>
            <% }} else { %>
                <!-- No users fallback -->
                <tr><td colspan="5" class="admin-no-users">No users found.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Add New User Section -->
    <div class="admin-card">
        <h2 class="admin-card-title"><i class="fas fa-user-plus"></i> Add New User</h2>
        <form action="AdminUserManagementServlet" method="post" class="admin-form-grid">
            <input type="hidden" name="action" value="add">
            <input type="text" name="name" placeholder="Full Name" required class="admin-input">
            <input type="text" name="username" placeholder="Username" required class="admin-input">

            <!-- New user password field with validation rules -->
            <div class="admin-password-container input-with-icon">
                <input type="password" name="password" id="add-password" placeholder="Password" required class="admin-password-input"
                    onfocus="toggleRules('add-password-rules', true)"
                    onblur="handleBlur('add-password', 'add-password-rules')"
                    oninput="validatePassword('add-password', 'add-submit', 'add-')">
                <button type="button" onclick="togglePassword('add-password', this)" class="admin-btn eye">
                    <i class="fas fa-eye"></i>
                </button>
            </div>
            <div id="add-password-rules" class="admin-password-rules hidden">
                <div id="add-length" class="invalid"> At least 8 characters</div>
                <div id="add-uppercase" class="invalid"> At least 1 uppercase letter</div>
                <div id="add-lowercase" class="invalid"> At least 1 lowercase letter</div>
                <div id="add-number" class="invalid"> At least 1 number</div>
                <div id="add-special" class="invalid"> At least 1 special character (@$!%*?&)</div>
                <div id="add-spaces" class="invalid"> No spaces</div>
            </div>

            <input type="email" name="email" placeholder="Email" required class="admin-input">
            <select name="role" required class="admin-input">
                <option value="">Select Role</option>
                <option value="Athlete">Athlete</option>
                <option value="Trainer">Trainer</option>
                <option value="Nutritionist">Nutritionist</option>
                <option value="Admin">Admin</option>
            </select>
            <div class="admin-submit-container">
                <button id="add-submit" type="submit" disabled class="admin-btn submit">Add User</button>
            </div>
        </form>
    </div>
</section>

<!-- Modal for delete confirmation -->
<div id="deleteModal" class="admin-modal hidden">
    <div class="admin-modal-box">
        <h2 class="admin-modal-title">Confirm Deletion</h2>
        <p class="admin-modal-text">Are you sure you want to delete this user?</p>
        <form id="deleteForm" method="post" action="AdminUserManagementServlet" class="admin-modal-actions">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" id="deleteUserId">
            <button type="button" onclick="closeModal()" class="admin-btn cancel">Cancel</button>
            <button type="submit" class="admin-btn delete">Delete</button>
        </form>
    </div>
</div>

<!-- JavaScript logic for UI behavior and validation -->
<script>
    // Toggle password visibility
    function togglePassword(inputId, btn) {
        const input = document.getElementById(inputId);
        const icon = btn.querySelector("i");
        input.type = input.type === "password" ? "text" : "password";
        icon.classList.toggle("fa-eye");
        icon.classList.toggle("fa-eye-slash");
    }

    // Show editable row
    function editUser(id) {
        document.getElementById("row-" + id).classList.add("hidden");
        document.getElementById("edit-row-" + id).classList.remove("hidden");
    }

    // Cancel edit
    function cancelEdit(id) {
        document.getElementById("edit-row-" + id).classList.add("hidden");
        document.getElementById("row-" + id).classList.remove("hidden");
    }

    // Open delete modal
    function confirmDelete(userId) {
        document.getElementById("deleteUserId").value = userId;
        document.getElementById("deleteModal").classList.remove("hidden");
    }

    function closeModal() {
        document.getElementById("deleteModal").classList.add("hidden");
    }

    // Validate password fields with criteria
    function validatePassword(inputId, buttonId, prefix = "") {
        const password = document.getElementById(inputId).value;
        const submitBtn = document.getElementById(buttonId);
        const checks = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            number: /\d/.test(password),
            special: /[@$!%*?&]/.test(password),
            spaces: !/\s/.test(password)
        };
        Object.entries(checks).forEach(([key, passed]) => {
            const el = document.getElementById(prefix + key);
            if (el) {
                el.className = passed ? "valid" : "invalid";
                el.textContent = (passed ? '✔' : '❌') + " " + el.textContent.replace(/✔ |❌ /, '');
            }
        });
        submitBtn.disabled = !Object.values(checks).every(Boolean);
    }

    // Show/hide rules on focus/blur
    function toggleRules(id, show) {
        document.getElementById(id).classList.toggle("hidden", !show);
    }

    function handleBlur(inputId, rulesId) {
        const input = document.getElementById(inputId);
        if (!input.value.trim()) {
            document.getElementById(rulesId).classList.add("hidden");
        }
    }
</script>

<!-- Footer -->
<footer>
    <p>&copy; 2025 PerFit Admin Panel. All rights reserved.</p>
</footer>

</body>
</html>
