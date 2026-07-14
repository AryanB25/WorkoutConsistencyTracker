package persistence;

import model.Exercise;
import model.WorkoutSession;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class JsonTest {

    protected void checkExercise(String name, int reps, int sets, double weight, int time, Exercise e) {
        assertEquals(name, e.getName());
        assertEquals(reps, e.getReps());
        assertEquals(sets, e.getSets());
        assertEquals(weight, e.getWeight());
        assertEquals(time, e.getTime());
    }

    protected void checkSession(int sessionId, String dateTime, int numExercises, WorkoutSession s) {
        assertEquals(sessionId, s.getSessionId());
        assertEquals(LocalDateTime.parse(dateTime), s.getDateTime());
        assertEquals(numExercises, s.getExercise().size());
    }
}