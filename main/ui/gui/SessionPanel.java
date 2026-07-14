package ui.gui;

import model.WorkoutLog;
import model.WorkoutSession;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;

// Represents the panel that displays all workout sessions in the workout log
// as a scrollable list.
// JScrollPane and DefaultListModel structure based on ListDemo from the Oracle
// Java Swing tutorial components examples, as referenced in Phase 3:
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html

@ExcludeFromJacocoGeneratedReport
public class SessionPanel extends JPanel {

    private static final int listFont = 12;

    private DefaultListModel<String> listModel;

    // MODIFIES: this
    // EFFECTS: constructs an empty session list panel with a titled border
    // and a scrollable list backed by listModel
    public SessionPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Workout Sessions"));
        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);

        list.setFont(new Font("Arial", Font.PLAIN, listFont));
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    // REQUIRES: workoutLog != null
    // MODIFIES: this
    // EFFECTS: clears the session list and repopulates it with one entry per
    // session in workoutLog, showing the session ID, date, number of
    // exercises, and total volume
    public void refresh(WorkoutLog workoutLog) {
        listModel.clear();

        for (WorkoutSession s : workoutLog.getAllWorkoutSessionsStats()) {
            String enter = "[ID: " + s.getSessionId() + "]  "
                    + s.getDateTime().toString()
                    + "  |  " + s.getNumExercisesInTheSession() + " exercise(s)"
                    + "  |  " + (int) s.getVolumeTrained() + " lbs vol";
            listModel.addElement(enter);
        }
    }
}