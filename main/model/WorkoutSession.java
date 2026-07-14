package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;

// Represents one workout session at a specific date/time containing a list of exercises performed
public class WorkoutSession {

    private LocalDateTime dateTime;
    private List<Exercise> exercises;
    private static int counter = 1;
    private int sessionId;

    // MODIFIES: this
    // EFFECTS: initializes the various fields of the class
    public WorkoutSession(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        exercises = new ArrayList<>();
        sessionId = counter++;
    }

    // MODIFIES: this
    // EFFECTS: initializes the date and time, and session ID of the class
    public WorkoutSession(LocalDateTime dateTime, int sessionId) {
        this.dateTime = dateTime;
        this.sessionId = sessionId;
        this.exercises = new ArrayList<>();
    }

    // REQUIRES: e != null
    // MODIFIES: this
    // EFFECTS: Adds the exercise to the exercises done in the workout session
    public void addExercise(Exercise e) {
        exercises.add(e);
        EventLog.getInstance().logEvent(new Event("Exercise added to session " + sessionId + ": " + e.getName()));
    }

    // EFFECTS: returns the local date and time
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // EFFECTS: Returns the total time taken to complete the exercises in the
    // workout session
    public int getTotalTime() {
        int time = 0;

        for (Exercise e : exercises) {
            time += e.getTime();
        }

        return time;
    }

    // EFFECTS: returns the number of exercises in this session
    public int getNumExercisesInTheSession() {
        return exercises.size();
    }

    // EFFECTS: returns the exercises in this session
    public List<Exercise> getExercise() {
        return exercises;
    }

    // EFFECTS: returns the total Volume trained in this session
    public double getVolumeTrained() {
        double totalVolumeTrained = 0.0;

        for (Exercise e : exercises) {
            totalVolumeTrained += e.getVolumeTrained();
        }

        return totalVolumeTrained;
    }

    // EFFECTS: returns the exercises performed in this session
    public String getExercisesString() {
        String exceString = "";
        int count = 0;

        for (Exercise e : exercises) {
            ++count;

            if (count == exercises.size()) {
                exceString += e.getName();
            } else {
                exceString += e.getName() + ", ";
            }
        }

        return exceString;
    }

    // EFFECTS: returns the total sets in this session
    public int getTotalSets() {
        int totalSets = 0;

        for (Exercise e : exercises) {
            totalSets += e.getSets();
        }

        return totalSets;
    }

    // EFFECTS: returns the total reps in this session
    public int getTotalReps() {
        int totalReps = 0;

        for (Exercise e : exercises) {
            totalReps += e.getReps();
        }

        return totalReps;
    }

    // EFFECTS: returns the total weight used in this session
    public double getTotalWeight() {
        double totalWeight = 0;

        for (Exercise e : exercises) {
            totalWeight += e.getWeight();
        }

        return totalWeight;
    }

    // EFFECTS: Returns the session ID of the workout session
    public int getSessionId() {
        return sessionId;
    }

    // MODIFIES: counter
    // EFFECTS: sets counter to maxId + 1 if maxId >= counter,
    // preventing new sessions from reusing IDs after loading from file
    public static void updateCounter(int maxId) {
        if (maxId >= counter) {
            counter = maxId + 1;
        }
    }

    // EFFECTS: Returns the JSON Object of the Workout Session
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("sessionId", sessionId);
        jsonObject.put("dateTime", dateTime.toString());
        jsonObject.put("exercises", convertExercisestoJson());

        return jsonObject;
    }

    // EFFECTS: Converts the exercises in a Workout Session into a JSON array and
    // returns it
    public JSONArray convertExercisestoJson() {

        JSONArray array = new JSONArray();

        for (Exercise e : exercises) {
            array.put(e.convertToJson());
        }

        return array;
    }
}
