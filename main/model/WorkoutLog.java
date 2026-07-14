package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents a log of workout sessions and provides average statistics across all sessions
public class WorkoutLog implements Writable {

    private List<WorkoutSession> sessions;

    // EFFECTS: initializes this workout log with an empty list of workout sessions
    public WorkoutLog() {
        sessions = new ArrayList<>();
    }

    // REQUIRED: session is not null
    // MODIFIES: this
    // EFFECTS: adds workout session to this workout log
    public void addWorkoutSession(WorkoutSession session) {
        sessions.add(session);
        EventLog.getInstance()
                .logEvent(new Event("Workout session added to log. Session ID: " + session.getSessionId()));
    }

    // EFFECTS: returns the number of workout sessions in this log
    public int numWorkoutSessions() {
        return sessions.size();
    }

    // EFFECTS: returns the total number of sets across all sessions in this log
    public int getSetsAcrossAllSessions() {
        int totalSets = 0;

        for (WorkoutSession ws : sessions) {
            totalSets += ws.getTotalSets();
        }

        return totalSets;
    }

    // EFFECTS: returns the total number of reps across all sessions in this log
    public int getRepsAcrossAllSessions() {
        int totalReps = 0;

        for (WorkoutSession ws : sessions) {
            totalReps += ws.getTotalReps();
        }

        return totalReps;
    }

    // EFFECTS: returns the total weight across all sessions in this log
    public double getTotalWeightAcrossAllSessions() {
        double totalWeight = 0.0;

        for (WorkoutSession ws : sessions) {
            totalWeight += ws.getTotalWeight();
        }

        return totalWeight;
    }

    // EFFECTS: returns the total volume trained across all sessions in this log
    public double getVolumeTrainedAcrossAllSessions() {
        double volumeTrained = 0;

        for (WorkoutSession ws : sessions) {
            volumeTrained += ws.getVolumeTrained();
        }

        return volumeTrained;
    }

    // REQUIRES: there should be atleast one session in the log
    // EFFECTS: returns the workout session with the greatest total time.
    // If multiple sessions tie, returns the first longest session found
    public WorkoutSession longestWorkoutSession() {

        if (sessions.size() == 1) {
            return sessions.get(0);
        }

        WorkoutSession longestWorkout = sessions.get(0);
        int longestWorkoutTime = longestWorkout.getTotalTime();

        for (int i = 1; i < sessions.size(); i++) {
            if (sessions.get(i).getTotalTime() > longestWorkoutTime) {
                longestWorkout = sessions.get(i);
            }
        }

        return longestWorkout;
    }

    // REQUIRES: there should be atleast one session in the log
    // EFFECTS: returns the workout session with the shortest total time.
    // If multiple sessions tie, returns the first shortest session found
    public WorkoutSession shortestWorkoutSession() {

        if (sessions.size() == 1) {
            return sessions.get(0);
        }

        WorkoutSession shortestWorkout = sessions.get(0);
        int shortestWorkoutTime = shortestWorkout.getTotalTime();

        for (int i = 1; i < sessions.size(); i++) {
            if (sessions.get(i).getTotalTime() < shortestWorkoutTime) {
                shortestWorkout = sessions.get(i);
            }
        }

        return shortestWorkout;
    }

    // EFFECTS: returns the workout session with the given id if it exists,
    // returns null otherwise.
    public WorkoutSession getWorkoutSessionbyId(int id) {

        for (WorkoutSession w : sessions) {
            if (w.getSessionId() == id) {
                return w;
            }
        }

        return null;
    }

    // EFFECTS: returns all the workout sessions in the workout log.
    public List<WorkoutSession> getAllWorkoutSessionsStats() {
        return sessions;
    }

    @Override
    // EFFECTS: Returns the JSON Object of the Workout Log
    public JSONObject convertToJson() {
        JSONObject json = new JSONObject();
        JSONArray sessionsJson = new JSONArray();

        for (WorkoutSession s : sessions) {
            sessionsJson.put(s.convertToJson());
        }

        json.put("sessions", sessionsJson);
        return json;
    }

}