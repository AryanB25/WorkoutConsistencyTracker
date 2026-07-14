package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkoutTests {

    private Exercise e;
    private Exercise e2;
    private Exercise e3;
    private Exercise e4;
    private Customer customer;
    private WorkoutLog workoutLog;
    private WorkoutSession workoutSession;
    private WorkoutSession workoutSession2;

    @BeforeEach
    void runBefore() {
        e = new Exercise("Shoulder Press", 2, 3, 30, 45);
        e2 = new Exercise("Dumbell Raises", 10, 3, 60, 14);
        e3 = new Exercise("Leg Raises", 10, 3, 60, 59);
        e4 = new Exercise("Glutes", 11, 3, 60, 70);
        workoutLog = new WorkoutLog();
        workoutSession = new WorkoutSession(LocalDateTime.now());
        workoutSession.addExercise(e);
        workoutSession.addExercise(e2);
        workoutLog.addWorkoutSession(workoutSession);
    }

    // Exercise constructor test
    @Test
    void exerciseConstructorTest() {

        assertTrue(e.getName().equalsIgnoreCase("Shoulder Press"));
        assertEquals(e.getReps(), 2);
        assertEquals(e.getSets(), 3);
        assertEquals(e.getWeight(), 30);
        assertEquals(e.getTime(), 45);

    }

    // Volume trained test
    @Test
    void getVolumeTrainedTest() {

        assertTrue(e.getName().equalsIgnoreCase("Shoulder Press"));
        assertEquals(e.getReps(), 2);
        assertEquals(e.getSets(), 3);
        assertEquals(e.getWeight(), 30);
        assertEquals(e.getTime(), 45);

        assertEquals(e.getVolumeTrained(), 180);

    }

    // Customer fields test
    @Test
    void customerTest() {

        customer = new Customer("Aryan", 18);
        assertEquals(customer.getName(), "Aryan");
        assertEquals(customer.getUserAge(), 18);
    }

    // Empty log test
    @Test
    void workoutLogConstructorTest() {
        WorkoutLog workoutLog2 = new WorkoutLog();
        assertEquals(0, workoutLog2.numWorkoutSessions());
    }

    // Add one session
    @Test
    void workoutLogAddSessionTestOne() {
        WorkoutLog workoutLog2 = new WorkoutLog();
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e);
        workoutLog2.addWorkoutSession(workoutSession2);

        assertEquals(1, workoutLog2.numWorkoutSessions());
        assertEquals(2, workoutLog2.getRepsAcrossAllSessions());
        assertEquals(3, workoutLog2.getSetsAcrossAllSessions());
        assertEquals(30, workoutLog2.getTotalWeightAcrossAllSessions());
    }

    // Add multiple sessions
    @Test
    void workoutLogAddSessionTestMultiple() {
        assertEquals(1, workoutLog.numWorkoutSessions());
        assertEquals(12, workoutLog.getRepsAcrossAllSessions());
        assertEquals(6, workoutLog.getSetsAcrossAllSessions());
        assertEquals(90, workoutLog.getTotalWeightAcrossAllSessions());
    }

    // Log total volume
    @Test
    void workoutLogGetVolumeTrainedTest() {
        assertEquals(1980, workoutLog.getVolumeTrainedAcrossAllSessions());
    }

    // Longest session test
    @Test
    void getLongestWorkoutSessionTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e2);
        workoutLog.addWorkoutSession(workoutSession2);

        assertEquals(workoutSession, workoutLog.longestWorkoutSession());
    }

    // Shortest session test
    @Test
    void getShortestWorkoutSessionTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e2);
        workoutLog.addWorkoutSession(workoutSession2);

        assertEquals(workoutSession2, workoutLog.shortestWorkoutSession());
    }

    // Longest single test
    @Test
    void getLongestWorkoutSessionOneTest() {
        assertEquals(workoutSession, workoutLog.longestWorkoutSession());
    }

    // Shortest single test
    @Test
    void getShortestWorkoutSessionOneTest() {
        assertEquals(workoutSession, workoutLog.shortestWorkoutSession());
    }

    // Longest tie test
    @Test
    void getLongestWorkoutSessionSameTimeTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e3);
        workoutLog.addWorkoutSession(workoutSession2);

        assertEquals(workoutSession, workoutLog.longestWorkoutSession());
    }

    // Longest new max
    @Test
    void getLongestWorkoutSessionNoneTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e4);
        workoutLog.addWorkoutSession(workoutSession2);

        assertEquals(workoutSession2, workoutLog.longestWorkoutSession());
    }

    // Shortest tie test
    @Test
    void getShortestWorkoutSessionSameTimeTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e3);
        workoutLog.addWorkoutSession(workoutSession2);

        assertEquals(workoutSession, workoutLog.shortestWorkoutSession());
    }

    // Session by ID test
    @Test
    void getWorkoutSessionbyIdTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e3);
        workoutLog.addWorkoutSession(workoutSession2);

        int id = workoutSession.getSessionId();

        assertEquals(workoutSession, workoutLog.getWorkoutSessionbyId(id));
    }

    // Session by ID test (none found)
    @Test
    void getWorkoutSessionbyIdNoneTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e3);

        int id = workoutSession2.getSessionId();

        assertEquals(null, workoutLog.getWorkoutSessionbyId(id));
    }

    // Session date time test
    @Test
    void workoutSessionGetDateTimeTest() {
        LocalDateTime fixedDate = LocalDateTime.of(2026, 2, 12, 22, 15);
        WorkoutSession session = new WorkoutSession(fixedDate);

        assertEquals(fixedDate, session.getDateTime());
    }

    // Exercise count test
    @Test
    void getNumExercisesInTheSessionTest() {
        assertEquals(2, workoutSession.getNumExercisesInTheSession());
    }

    // Exercise list test
    @Test
    void getExerciseTest() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(e);
        exercises.add(e2);

        assertEquals(exercises, workoutSession.getExercise());
    }

    // Exercise string test
    @Test
    void getExercisesStringTest() {
        assertEquals("Shoulder Press, Dumbell Raises", workoutSession.getExercisesString());
    }

    // Sessions stats list test
    @Test
    void getAllWorkoutSessionsStatsTest() {
        workoutSession2 = new WorkoutSession(LocalDateTime.now());
        workoutSession2.addExercise(e4);
        workoutLog.addWorkoutSession(workoutSession2);

        List<WorkoutSession> sessions = new ArrayList<>();
        sessions.add(workoutSession);
        sessions.add(workoutSession2);

        assertEquals(sessions, workoutLog.getAllWorkoutSessionsStats());
    }

    // Update counter test
    @Test
    void updateCounterAboveTest() {
        WorkoutSession session = new WorkoutSession(LocalDateTime.now());
        int highId = session.getSessionId() + 50;
        WorkoutSession.updateCounter(highId);

        WorkoutSession session2 = new WorkoutSession(LocalDateTime.now());
        assertEquals(highId + 1, session2.getSessionId());
    }

    // Update counter test
    @Test
    void updateCounterBelowTest() {
        WorkoutSession session1 = new WorkoutSession(LocalDateTime.now());
        int currentId = session1.getSessionId();
        WorkoutSession.updateCounter(currentId - 2);
        WorkoutSession session2 = new WorkoutSession(LocalDateTime.now());
        assertEquals(currentId + 1, session2.getSessionId());
    }

}
