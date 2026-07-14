package persistence;

import model.WorkoutLog;
import model.WorkoutSession;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonReaderTest extends JsonTest {

    // Missing file
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            WorkoutLog log = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Empty log
    @Test
    void testReaderEmptyWorkoutLog() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkoutLog.json");
        try {
            WorkoutLog log = reader.read();
            assertEquals(0, log.numWorkoutSessions());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    // Normal Log
    @Test
    void testReaderGeneralWorkoutLog() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkoutLog.json");
        try {
            WorkoutLog log = reader.read();
            assertEquals(2, log.numWorkoutSessions());

            WorkoutSession s10 = log.getWorkoutSessionbyId(10);
            for (WorkoutSession s : log.getAllWorkoutSessionsStats()) {
                System.out.println("Loaded session id: " + s.getSessionId()
                        + " dateTime: " + s.getDateTime()
                        + " exercises: " + s.getExercise().size());
            }

            assertTrue(s10 != null);
            checkSession(10, "2026-02-26T20:00:00", 2, s10);
            checkExercise("Bench Press", 8, 3, 135.0, 20, s10.getExercise().get(0));
            checkExercise("Row", 10, 3, 95.0, 15, s10.getExercise().get(1));

            WorkoutSession s11 = log.getWorkoutSessionbyId(11);
            assertTrue(s11 != null);
            checkSession(11, "2026-02-27T18:30:00", 1, s11);
            checkExercise("Squat", 5, 5, 185.0, 25, s11.getExercise().get(0));

        } catch (IOException e) {
            fail("Couldn't read from file!");
        }
    }

}
