package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import model.Customer;
import model.Exercise;
import model.WorkoutSession;
import model.WorkoutLog;
import persistence.JsonReader;
import persistence.JsonWriter;

// Runs the console-based workout tracker application and handles user interaction

@ExcludeFromJacocoGeneratedReport
public class Main {

    private static String firstName;
    private static String lastName;
    private static int age;
    private static Customer customer;
    private static int option;
    private static Exercise exercise;
    private static WorkoutSession workout;
    private static WorkoutLog workoutLog = new WorkoutLog();
    private static Scanner input = new Scanner(System.in);
    private static String name;
    private static int reps;
    private static int sets;
    private static double weight;
    private static int time;
    private static final String location = "./data/workoutlog.json";
    private static JsonReader jsonReader = new JsonReader(location);
    private static JsonWriter jsonWriter = new JsonWriter(location);

    // Runs the workout tracker UI loop, repeatedly showing the menu and executing
    // user-selected actions until quit
    public static void main(String[] args) throws Exception {

        customerWelcomeandDetails();
        Thread.sleep(1500);

        do {
            menu();
            option = input.nextInt();
            System.out.println();
            try {
                multipleOptionCases(option);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            } catch (NullPointerException e) {
                System.out.println("Data not found. Please check your input.");
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again later.");
            }
        } while (option != 8);
        input.close();
    }

    // Displays the main menu options to the console and prompts the user to select
    // an option
    public static void menu() {
        System.out.println(
                "\t\t\t\tSelect one of the options below to continue: ");
        System.out.println();

        System.out.println("Option 1: Proceed to enter stats for ONE of my workouts");
        System.out.println("Option 2: Proceed to get stats about ONE of my workouts");
        System.out.println("Option 3: Proceed to get stats about ALL my workouts");
        System.out.println("Option 4: View Past Workout Sessions");
        System.out.println("Option 5: Save");
        System.out.println("Option 6: Load previous");
        System.out.println("Option 7: Quit");

        System.out.println();
        System.out.print("Option: ");
    }

    // Prompts the user for personal details, constructs a Customer, and prints a
    // welcome message
    public static void customerWelcomeandDetails() {
        System.out.println("\t\t\t\t\tWelcome to the WORKOUT CONSISTENCY TRACKER!");
        System.out.println();

        System.out.print("Please enter your first name: ");
        firstName = input.next();
        System.out.println();

        System.out.print("Please enter your last name: ");
        lastName = input.next();
        System.out.println();

        System.out.print("Enter your age: ");
        age = input.nextInt();

        customer = new Customer(firstName + " " + lastName, age);

        System.out.println();
        System.out.println("Welcome " + firstName + "!");
    }

    // Prompts the user for a workout session’s exercises, builds a WorkoutSession,
    // and adds it to the workout log
    public static void buildWorkoutSession() {
        workout = new WorkoutSession(LocalDateTime.now());

        System.out.print("Enter the number of exercises in this workout: ");
        int numExercises = input.nextInt();
        System.out.println();

        input.nextLine();

        for (int i = 0; i < numExercises; i++) {
            System.out.println("Exercise " + (i + 1));
            requiredFirstCaseInput();

            input.nextLine();

            exercise = new Exercise(name, reps, sets, weight, time);
            workout.addExercise(exercise);
            System.out.println("Session ID: " + workout.getSessionId());
            System.out.println();
        }

        System.out.println();
        System.out.println("\t\t\t\t\tTask accomplished successfully!");
        System.out.println();

        workoutLog.addWorkoutSession(workout);
    }

    // Prompts the user for exercise details and stores them in the corresponding
    // fields for later Exercise construction
    public static void requiredFirstCaseInput() {
        System.out.print("Enter the name of the exercise: ");
        name = input.nextLine();
        System.out.println();

        System.out.print("Enter the reps of the exercise: ");
        reps = input.nextInt();
        System.out.println();

        System.out.print("Enter the sets of the exercise: ");
        sets = input.nextInt();
        System.out.println();

        System.out.print("Enter the time taken to complete the exercise (to the nearest minute): ");
        time = input.nextInt();
        System.out.println();

        System.out.print("Enter the weight of the exercise (in lbs): ");
        weight = input.nextDouble();
        System.out.println();
    }

    // Prompts for a session ID and prints statistics for that session if it exists;
    // otherwise prints an error message
    public static void statsForOneSession() {
        System.out.print("Enter the Session ID: ");
        int sessionId = input.nextInt();
        System.out.println();

        WorkoutSession session = workoutLog.getWorkoutSessionbyId(sessionId);

        if (session != null) {
            System.out.println("Duration: " + session.getTotalTime() + " mins");
            System.out.println("Total Repititons: " + session.getTotalReps());
            System.out.println("Total Sets: " + session.getTotalSets());
            System.out.println("Total Weight: " + session.getTotalWeight() + " lbs");
            System.out.println("Total Volume Trained: " + session.getVolumeTrained() + " lbs");
            System.out.println();
        } else {
            System.out.println("Session ID not found. Please try again.");
            System.out.println();
        }
    }

    // Prints aggregate statistics across all workout sessions, including longest
    // and shortest session summaries
    public static void allTimeStats() {

        System.out.print("\t\t\t\t\t\tAll-Time Workout Stats");
        System.out.println();

        System.out.println("Number of Workout Sessions: " + workoutLog.numWorkoutSessions());
        System.out.println("Total Repitions: " + workoutLog.getRepsAcrossAllSessions());
        System.out.println("Total Sets: " + workoutLog.getSetsAcrossAllSessions());
        System.out.println("Total Weight: " + workoutLog.getTotalWeightAcrossAllSessions() + " lbs");
        System.out.println(
                "Total Volume Trained: " + workoutLog.getVolumeTrainedAcrossAllSessions() + " lbs");

        WorkoutSession longestSession = workoutLog.longestWorkoutSession();
        WorkoutSession shortestSession = workoutLog.shortestWorkoutSession();

        System.out.println("Shortest Workout Session on " + shortestSession.getDateTime()
                + " (Session ID: " + shortestSession.getSessionId() + "): "
                + shortestSession.getTotalTime() + " mins");
        System.out.println("Longest Workout Session on " + longestSession.getDateTime()
                + " (Session ID: " + longestSession.getSessionId() + "): "
                + longestSession.getTotalTime() + " mins");

        System.out.println();

    }

    // Prints a summary of each workout session currently stored in the workout log
    public static void currentWorkout() {
        for (WorkoutSession w : workoutLog.getAllWorkoutSessionsStats()) {
            System.out.println("Session ID: " + w.getSessionId());
            System.out.println("Date and Time: " + w.getDateTime());
            System.out.println("Exercises: " + w.getExercisesString());
            System.out.println("Total reps: " + w.getTotalReps());
            System.out.println("Total sets: " + w.getTotalSets());
            System.out.println("Total weight: " + w.getTotalWeight());
            System.out.println();
        }
    }

    // Saves the current workout log to the JSON file at the configured location and
    // prints success/failure feedback
    public static void savedToFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(workoutLog);
            System.out.println("Successfully saved workout log!");
            System.out.println();
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to file!");
            System.out.println();
        }
    }

    // Loads workout log data from the JSON file at the configured location and
    // replaces the current workout log
    public static void loadsData() {
        try {
            workoutLog = jsonReader.read();
            System.out.println("Successfully restored workout logs!");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Unable to restore from file!");
            System.out.println();
        }
    }

    // Prints a goodbye message and terminates the program
    public static void exitMessage() {
        System.out.print("Save before quitting? (y/n): ");
        String answer = input.next();

        if (answer.equalsIgnoreCase("y")) {
            savedToFile();
        }

        System.out.println();
        System.out.println("\t\t\t\t\tThank you! Have an amazing day!");
        System.out.println();
        System.exit(0);
    }

    // Dispatches the user’s chosen menu option to the corresponding handler method,
    // or prints an error for invalid options
    public static void multipleOptionCases(int option) {
        switch (option) {
            case 1:
                buildWorkoutSession();
                break;
            case 2:
                statsForOneSession();
                break;
            case 3:
                if (workoutLog.numWorkoutSessions() == 0) {
                    System.out.println("No workout sessions recorded yet. Start with Option 1.");
                    break;
                }
                allTimeStats();
                break;
            case 4:
                currentWorkout();
                break;
            case 5:
                savedToFile();
                break;
            case 6:
                loadsData();
                break;
            case 7:
                exitMessage();
            default:
                System.out.println("Invalid option. Enter a valid option between 1 and 7.");
                break;
        }
    }
}
