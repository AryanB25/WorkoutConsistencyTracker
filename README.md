# WorkoutConsistencyTracker# Workout Consistency Tracker

## Overview

This project, the **Workout Consistency Tracker**, is a simple *Java*-based application designed to help users log their workouts, analyze past workouts, and identify training patterns over time, with a strong emphasis on tracking consistency. The application allows users to record their workout sessions, view their workout history, and gain valuable insights into various aspects of their training, including frequency, streaks, and overall consistency.

This project is intended for gym-goers and students who want a robust and structured way to monitor their progress toward their fitness goals while gaining key insights into their habits, accomplishments, and discipline. It promotes physical activity, self-accountability, and motivates users to stay consistent over time. This project strongly resonates with my values and beliefs and is particularly meaningful to someone like me. I firmly believe in the importance of staying consistent and showing up, not only to grow physically but also to pursue self-improvement and personal growth. As a result, creating this project allows me to combine my interests in computer science and physical activity while working with real-world data and emphasizing clean, well-structured software design.

## User Stories

- As a user, I want to be able to add multiple exercises to a workout session so that I can record a full workout.
- As a user, I want to be able to add multiple workout sessions to my workout log so that I can track my training history over time.
- As a user, I want to be able to view a list of all workout sessions in my workout log so that I can review my past training.
- As a user, I want to view summary statistics across all my workout sessions (total sets, total reps, total weight lifted, and longest/shortest workout) so that I can evaluate my performance.
- As a user, I want to have the option to save the entire state of my workout log, keeping intact the workout sessions, to the file so that I can resume my training data later.
- As a user, I want to have the option to load my workout log from file so that I can continue exactly where I left off. 

## Instructions for End User

- You can view the panel that displays the workout sessions that have already been added to the workout log by looking at the "Workout Sessions" panel in the middle of the window. Every session you have logged shows up there with its ID, date, number of exercises, and total volume.
- You can generate the first required action related to the user story "adding multiple exercises to a workout session" by clicking the "Add Session" button at the top of the window. It will ask you how many exercises you did, then walk you through entering the name, reps, sets, weight, and time for each one.
- You can generate the second required action related to the user story "adding multiple workout sessions to a workout log" by clicking the "View Session" button at the top of the window and typing in a Session ID. It will pop up a table showing every exercise from that session along with all the stats.
- You can locate my visual component by looking at the bar chart at the bottom of the window labelled "Training Volume per Session (lbs)". It shows your training volume for each workout session and the bars change colour depending on how hard you trained where green means high volume, orange is medium, and red is low. There is also a splash screen with a drawn dumbbell graphic that appears when you first open the app for aesthetics.
- You can save the state of my application by clicking the "Save" button at the top of the window, or by hitting "Yes" when the app asks you if you want to save when you close it.
- You can reload the state of my application by clicking the "Load" button at the top of the window, or by hitting "Yes" when the app asks you if you want to load your previous data when you first open it.

## Phase 4: Task 2

Wed Mar 25 21:00:09 PDT 2026
Exercise added to session 1: Leg Raises
Wed Mar 25 21:00:09 PDT 2026
Workout session added to log. Session ID: 1
Wed Mar 25 21:00:09 PDT 2026
Exercise added to session 2: Squats
Wed Mar 25 21:00:09 PDT 2026
Workout session added to log. Session ID: 2
Wed Mar 25 21:00:09 PDT 2026
Exercise added to session 3: Leg
Wed Mar 25 21:00:09 PDT 2026
Workout session added to log. Session ID: 3
Wed Mar 25 21:00:09 PDT 2026
Exercise added to session 5: Arya
Wed Mar 25 21:00:09 PDT 2026
Workout session added to log. Session ID: 5

## Phase 4: Task 3
One of the main areas I would refactor is how responsibilities are currently handled in the UI layer, especially in Main and WorkoutTrackerGUI. Right now, a lot of the program’s logic is controlled through large switch-case structures that handle different user actions. This makes the code harder to read and extend, since adding new features would require modifying these already complex sections. If I had more time, I would refactor this by introducing separate classes (such as command or handler classes) for each action. This would follow a more modular design where each class is responsible for a single behavior, making the overall system easier to maintain and scale.

I would also improve the structure of the model layer by separating logic and arithmetic-related operations into different classes. Currently, classes like WorkoutLog and WorkoutSession handle both storing data and performing calculations, which makes them more complex and tightly coupled. Moving calculation logic into dedicated helper or service classes, the model classes could focus purely on representing data. This separation of concerns would make the code easier to understand, test, and modify, while also making it simpler to extend functionality without overloading existing classes.