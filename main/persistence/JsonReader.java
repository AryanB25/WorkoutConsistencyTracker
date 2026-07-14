package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Exercise;
import model.WorkoutLog;
import model.WorkoutSession;

// Represents a reader that reads workout log from JSON data stored in file
public class JsonReader {

    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workout log from file and returns it;
    // throws IOException if an error occurs reading data from file
    public WorkoutLog read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkoutLog(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workout log from JSON object and returns it
    private WorkoutLog parseWorkoutLog(JSONObject jsonObject) {
        WorkoutLog log = new WorkoutLog();
        addSessions(log, jsonObject);
        return log;
    }

    // MODIFIES: log
    // EFFECTS: parses workout sessions from JSON object and adds them to workout
    // log
    private void addSessions(WorkoutLog log, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("sessions");
        for (Object json : jsonArray) {
            JSONObject nextSession = (JSONObject) json;
            addSession(log, nextSession);
        }
    }

    // MODIFIES: log
    // EFFECTS: parses workout session from JSON object and adds it to workout log
    private void addSession(WorkoutLog log, JSONObject jsonObject) {
        int sessionId = jsonObject.getInt("sessionId");
        LocalDateTime dateTime = LocalDateTime.parse(jsonObject.getString("dateTime"));
        WorkoutSession session = new WorkoutSession(dateTime, sessionId);

        addExercises(session, jsonObject);
        log.addWorkoutSession(session);
    }

    // MODIFIES: session
    // EFFECTS: parses exercises from JSON object and adds them to workout session
    private void addExercises(WorkoutSession session, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("exercises");
        for (Object json : jsonArray) {
            JSONObject nextExercise = (JSONObject) json;
            addExercise(session, nextExercise);
        }
    }

    // MODIFIES: session
    // EFFECTS: parses exercise from JSON object and adds it to workout session
    private void addExercise(WorkoutSession session, JSONObject jsonObject) {
        Exercise exercise = Exercise.convertFromJson(jsonObject);
        session.addExercise(exercise);
    }

}
