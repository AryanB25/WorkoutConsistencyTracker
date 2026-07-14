package persistence;

import model.Exercise;
import model.WorkoutLog;
import model.WorkoutSession;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonWriterTest extends JsonTest {

    // Invalid path
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/thisFolderDoesNotExist/out.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Empty write
    @Test
    void testWriterEmptyWorkoutLog() {
        try {
            WorkoutLog log = new WorkoutLog();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkoutLog.json");
            writer.open();
            writer.write(log);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkoutLog.json");
            log = reader.read();
            assertEquals(0, log.numWorkoutSessions());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    // Full write (Round-trip)
    @Test
    void testWriterGeneralWorkoutLog() {
        try {
            WorkoutLog log = new WorkoutLog();

            WorkoutSession s1 = new WorkoutSession(LocalDateTime.parse("2026-02-26T20:00:00"), 10);
            s1.addExercise(new Exercise("Bench Press", 8, 3, 135.0, 20));
            s1.addExercise(new Exercise("Row", 10, 3, 95.0, 15));

            WorkoutSession s2 = new WorkoutSession(LocalDateTime.parse("2026-02-27T18:30:00"), 11);
            s2.addExercise(new Exercise("Squat", 5, 5, 185.0, 25));

            log.addWorkoutSession(s1);
            log.addWorkoutSession(s2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkoutLog.json");
            writer.open();
            writer.write(log);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkoutLog.json");
            WorkoutLog loaded = reader.read();

            assertEquals(2, loaded.numWorkoutSessions());

            WorkoutSession s10 = loaded.getWorkoutSessionbyId(10);
            assertTrue(s10 != null);
            checkSession(10, "2026-02-26T20:00:00", 2, s10);
            checkExercise("Bench Press", 8, 3, 135.0, 20, s10.getExercise().get(0));
            checkExercise("Row", 10, 3, 95.0, 15, s10.getExercise().get(1));

            WorkoutSession s11 = loaded.getWorkoutSessionbyId(11);
            assertTrue(s11 != null);
            checkSession(11, "2026-02-27T18:30:00", 1, s11);
            checkExercise("Squat", 5, 5, 185.0, 25, s11.getExercise().get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
