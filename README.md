# 🏋️‍♀️ PerFit – Personal Fitness & Nutrition Guide

PerFit is a web-based fitness platform tailored to provide personalized workout and nutrition guidance for **Athletes**, **Trainers**, **Nutritionists**, and **Admins**. The application supports secure authentication, role-based access, and data-driven program generation through a clean and maintainable object-oriented design (OOP) using the MVC architecture.

---

## 📌 Features

- 🔐 Role-based access: Athlete, Trainer, Nutritionist, Admin
- 💡 Personalized workout generation based on user input
- 📈 Athlete progress tracking (workouts, weight, history)
- 📬 OTP system for secure email verification
- 📨 Contact form system with email notifications
- 🔗 Integration with third-party APIs for exercise suggestions
- 🧠 Uses Java records, enums, interfaces, and DAO pattern for maintainability

---

## 🛠️ Technology Stack

| Layer       | Tech                                 |
|-------------|--------------------------------------|
| Backend     | Java (Servlets, Jakarta Mail API)    |
| Frontend    | HTML5, CSS3, JavaScript, JSP         |
| Database    | MySQL                                |
| Server      | Apache Tomcat 8.5+                   |
| Email       | SMTP via Gmail                       |
| Build Tool  | Maven                                |

## 🧠 System Algorithms Overview

### 🏃 Athlete Algorithm
The athlete fills in their personal data (such as gender, age, height, weight, goals, experience, injuries, health conditions, training method, available days, and training duration). The system then:

- Calculates Body Mass Index (BMI) to assess health status.
- Selects the appropriate number of sets and repetitions based on the user's goal (e.g., strength, toning, weight loss).
- Generates a training plan using the function `SplitUtil.getSplit()`, which determines the distribution of muscle groups per day.
- Calls an external exercise database API (RapidAPI) to select safe exercises based on training method (bodyweight or weights), muscle group, and any existing injuries.
- Saves the training plan and displays it to the user.

### 🏋️ Coach Algorithm
The coach specifies certain criteria for the type of program they want to create (e.g., muscle group, goal, equipment). The system:

- Validates that the selected "goal" is valid (e.g., strength, hypertrophy).
- Filters exercises from the external API (RapidAPI) based on the desired equipment and training objective.
- Generates a suggested workout plan with appropriate sets and repetitions depending on the training type.
- Enables rapid creation of multiple programs based on different clients' needs.

### 🍎 Nutritionist Algorithm
The nutritionist provides specific criteria for the dietary plan they want to generate (e.g., gender, age, height, weight, goal, diet type, allergies). The system:

- Calculates the user's daily caloric needs based on gender, BMR, and activity level.
- Adjusts calories according to the user's goal (e.g., weight loss, muscle gain).
- Determines macronutrient targets (protein, carbs, fat) based on the user's goal and diet type (e.g., Keto, Vegan).
- Retrieves recipes via the Spiracular API that meet dietary requirements, cuisine preferences, and allergy constraints.
- Calculates proper portion sizes for each meal to meet nutrition goals.
- Selects the optimal combination of recipes for the day based on minimal deviation from target macronutrients.
- Generates a detailed meal plan with portions, calories, and macros for each meal.
- Supports rapid creation of personalized meal plans for multiple clients.

### 🔐 Admin Algorithm
The admin does not participate in program creation but has the following capabilities:

- User management  
- Adding new users  
- Deleting users


## How to Run
Open the project in Eclipse and run the main servlet or server file.


