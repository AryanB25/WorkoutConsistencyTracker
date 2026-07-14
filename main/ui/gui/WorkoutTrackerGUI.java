package ui.gui;

import model.Event;
import model.EventLog;
import model.Exercise;
import model.WorkoutLog;
import model.WorkoutSession;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

// Represents the main window of the Workout Consistency Tracker GUI application.
// Handles all user interaction including adding sessions, viewing sessions,
// saving and loading the workout log.
// Structure based on the LabelChanger example provided in the Phase 3 specification.
// https://stackoverflow.com/questions/6578205/swing-jlabel-text-change-on-the-running-application
// WindowAdapter pattern based on AlarmSystem project.

@ExcludeFromJacocoGeneratedReport
public class WorkoutTrackerGUI extends JFrame implements ActionListener {

    private static final String location = "./data/workoutlog.json";
    private static final int windowWidth = 800;
    private static final int windowHeight = 600;
    private static final int formRows = 5;
    private static final int formCols = 2;
    private static final int splashTime = 2000;
    private static final int splashWidth = 400;
    private static final int splashHeight = 230;
    private static final int splashFontSize = 20;
    private static final Color splashBg = new Color(34, 49, 63);
    private static final Color splashFg = new Color(52, 152, 219);
    private static final int dumbbellY = 80;
    private static final int dumbbellLength = 45;
    private static final int dumbbellWeightWidth = 28;
    private static final int dumbbellWeightHeight = 50;
    private static final int dumbbellBarThickness = 8;
    private static final int titleY = 160;
    private static final int subY = 185;
    private static final int splashSubSize = 13;

    private WorkoutLog workoutLog;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private SessionPanel sessionPanel;
    private ChartPanel chartPanel;

    // MODIFIES: this
    // EFFECTS: constructs the main window, initializes all panels and components,
    // prompts the user to load previous data, then displays the window
    public WorkoutTrackerGUI() {
        super("Workout Consistency Tracker");
        workoutLog = new WorkoutLog();
        jsonReader = new JsonReader(location);
        jsonWriter = new JsonWriter(location);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sessionPanel = new SessionPanel();
        chartPanel = new ChartPanel(workoutLog);

        add(setupButtons(), BorderLayout.NORTH);
        add(sessionPanel, BorderLayout.CENTER);
        add(chartPanel, BorderLayout.SOUTH);

        // WindowAdapter pattern based on AlarmSystem project
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                promptSave();
                printLog();
                System.exit(0);
            }
        });

        promptLoad();
        setVisible(true);
    }

    // EFFECTS: displays a splash screen with a drawn dumbbell and coloured
    // background for splashTime milliseconds, then disposes it.
    // paintComponent pattern based on GamePanel from SpaceInvader.
    private static void showSplash() {
        JWindow splash = new JWindow();
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSplashBackground(g);
                drawDumbbell(g);
                drawSplashText(g);
            }
        };

        splash.add(panel);
        splash.setSize(splashWidth, splashHeight);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
        try {
            Thread.sleep(splashTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        splash.dispose();
    }

    // EFFECTS: creates and returns a panel containing the four action buttons;
    // each button is wired to this ActionListener.
    // Button wiring pattern based on LabelChanger example.
    private JPanel setupButtons() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addBtn = new JButton("Add Session");
        JButton viewBtn = new JButton("View Session");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        addBtn.setActionCommand("addSession");
        viewBtn.setActionCommand("viewSession");
        saveBtn.setActionCommand("save");
        loadBtn.setActionCommand("load");

        addBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(saveBtn);
        panel.add(loadBtn);
        return panel;
    }

    // MODIFIES: g
    // EFFECTS: fills the splash panel with the dark navy background colour
    private static void drawSplashBackground(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(splashBg);
        g.fillRect(0, 0, splashWidth, splashHeight);
        g.setColor(savedCol);
    }

    // MODIFIES: g
    // EFFECTS: draws a dumbbell shape onto g using two filled rectangles for the
    // weights and one filled rectangle for the bar
    private static void drawDumbbell(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(splashFg);
        int dumbellX = splashWidth / 2;
        int barTop = dumbbellY - dumbbellBarThickness / 2;
        g.fillRect(dumbellX - dumbbellLength, barTop, dumbbellLength * 2, dumbbellBarThickness);
        g.fillRect(dumbellX - dumbbellLength - dumbbellWeightWidth, dumbbellY - dumbbellWeightHeight / 2,
                dumbbellWeightWidth, dumbbellWeightHeight);
        g.fillRect(dumbellX + dumbbellLength, dumbbellY - dumbbellWeightHeight / 2, dumbbellWeightWidth,
                dumbbellWeightHeight);
        g.setColor(savedCol);
    }

    // MODIFIES: g
    // EFFECTS: draws the app title and subtitle text centred onto g
    private static void drawSplashText(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, splashFontSize));
        FontMetrics fm = g.getFontMetrics();

        String title = "Workout Consistency Tracker";
        g.drawString(title, (splashWidth - fm.stringWidth(title)) / 2, titleY);
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.ITALIC, splashSubSize));

        FontMetrics fmSub = g.getFontMetrics();
        g.drawString("Loading...", (splashWidth - fmSub.stringWidth("Loading...")) / 2, subY);
        g.setColor(savedCol);
    }

    // EFFECTS: dispatches button click events to the correct handler method
    // based on the action command of the button that was clicked.
    // Pattern based on LabelChanger example:
    // https://stackoverflow.com/questions/6578205/swing-jlabel-text-change-on-the-running-application
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addSession")) {
            addSession();
        } else if (e.getActionCommand().equals("viewSession")) {
            viewSession();
        } else if (e.getActionCommand().equals("save")) {
            save();
        } else if (e.getActionCommand().equals("load")) {
            load();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user for the number of exercises, builds a WorkoutSession,
    // adds it to the workout log, and refreshes the UI;
    // does nothing if the user cancels at any point
    private void addSession() {
        int num = getNumExercises();
        if (num <= 0) {
            return;
        }
        WorkoutSession workout = buildSession(num);
        if (workout == null) {
            return;
        }
        workoutLog.addWorkoutSession(workout);
        sessionPanel.refresh(workoutLog);
        chartPanel.refresh(workoutLog);
        JOptionPane.showMessageDialog(this, "Session added! ID: " + workout.getSessionId());
    }

    // EFFECTS: prompts user to enter the number of exercises and returns it;
    // returns 0 if the user cancels or enters an invalid number
    private int getNumExercises() {
        String numStr = JOptionPane.showInputDialog(this, "How many exercises?");
        if (numStr == null) {
            return 0;
        }
        try {
            int num = Integer.parseInt(numStr.trim());
            if (num <= 0) {
                throw new NumberFormatException();
            }
            return num;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a positive whole number.");
            return 0;
        }
    }

    // REQUIRES: num > 0
    // EFFECTS: collects num exercises from the user one at a time and returns
    // a new WorkoutSession containing them;
    // returns null if the user cancels any exercise input
    private WorkoutSession buildSession(int num) {
        WorkoutSession workout = new WorkoutSession(LocalDateTime.now());
        for (int i = 0; i < num; i++) {
            Exercise exercise = getExerciseInput(i + 1, num);
            if (exercise == null) {
                return null;
            }
            workout.addExercise(exercise);
        }
        return workout;
    }

    // REQUIRES: current >= 1, total >= current
    // EFFECTS: shows a form dialog for one exercise and returns the Exercise;
    // returns null if the user cancels
    private Exercise getExerciseInput(int current, int total) {
        JPanel form = new JPanel(new GridLayout(formRows, formCols));
        JTextField nameField = new JTextField();
        JTextField repsField = new JTextField();
        JTextField setsField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField timeField = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Reps:"));
        form.add(repsField);
        form.add(new JLabel("Sets:"));
        form.add(setsField);
        form.add(new JLabel("Weight (lbs):"));
        form.add(weightField);
        form.add(new JLabel("Time (mins):"));
        form.add(timeField);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Exercise " + current + " of " + total, JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        return parseExercise(nameField, repsField, setsField, weightField, timeField);
    }

    // EFFECTS: reads values from the five text fields and returns a new Exercise;
    // returns null and shows an error dialog if any numeric field is invalid
    private Exercise parseExercise(JTextField nameField, JTextField repsField,
            JTextField setsField, JTextField weightField, JTextField timeField) {
        try {
            String name = nameField.getText().trim();
            int reps = Integer.parseInt(repsField.getText().trim());
            int sets = Integer.parseInt(setsField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int time = Integer.parseInt(timeField.getText().trim());
            return new Exercise(name, reps, sets, weight, time);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers.");
            return null;
        }
    }

    // EFFECTS: prompts user for a session ID and shows a detail panel for that
    // session;
    // shows an error dialog if the input is not a valid integer,
    // or a warning dialog if the session ID is not found
    private void viewSession() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Session ID:");
        if (idStr == null) {
            return;
        }
        int sessionId;
        try {
            sessionId = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer.");
            return;
        }
        WorkoutSession session = workoutLog.getWorkoutSessionbyId(sessionId);
        if (session == null) {
            JOptionPane.showMessageDialog(this, "Session ID " + sessionId + " not found.");
            return;
        }
        JOptionPane.showMessageDialog(this, buildDetailPanel(session),
                "Session #" + sessionId, JOptionPane.INFORMATION_MESSAGE);
    }

    // REQUIRES: session != null
    // EFFECTS: builds and returns a panel containing a summary label and an
    // exercise table for the given WorkoutSession
    private JPanel buildDetailPanel(WorkoutSession session) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildSessionInfo(session), BorderLayout.NORTH);
        panel.add(new JScrollPane(buildExerciseTable(session)), BorderLayout.CENTER);
        return panel;
    }

    // REQUIRES: session != null
    // EFFECTS: builds and returns a JLabel showing the key stats for the session
    private JLabel buildSessionInfo(WorkoutSession session) {
        String text = "ID: " + session.getSessionId()
                + "  |  Date: " + session.getDateTime().toString()
                + "  |  Reps: " + session.getTotalReps()
                + "  |  Sets: " + session.getTotalSets()
                + "  |  Weight: " + session.getTotalWeight() + " lbs"
                + "  |  Duration: " + session.getTotalTime() + " mins";
        return new JLabel(text);
    }

    // REQUIRES: session != null
    // EFFECTS: builds and returns a JTable listing each exercise in the session.
    // Object[][] pattern based on SimpleTableDemo from Oracle Swing tutorial:
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
    private JTable buildExerciseTable(WorkoutSession session) {
        String[] cols = { "Exercise", "Reps", "Sets", "Weight", "Time", "Volume" };
        List<Exercise> exercises = session.getExercise();
        Object[][] data = new Object[exercises.size()][cols.length];
        for (int i = 0; i < exercises.size(); i++) {
            Exercise ex = exercises.get(i);
            data[i][0] = ex.getName();
            data[i][1] = ex.getReps();
            data[i][2] = ex.getSets();
            data[i][3] = ex.getWeight();
            data[i][4] = ex.getTime();
            data[i][5] = (int) ex.getVolumeTrained() + " lbs";
        }
        JTable table = new JTable(data, cols);
        table.setEnabled(false);
        return table;
    }

    // MODIFIES: this
    // EFFECTS: writes the current workout log to file at location
    // shows a success dialog on completion or an error dialog on failure
    private void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(workoutLog);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Saved successfully!");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Could not save to file.");
        }
    }

    // MODIFIES: this
    // EFFECTS: reads the workout log from file, syncs the session ID counter,
    // and refreshes both panels.
    // shows a success dialog on completion or an error dialog on failure
    private void load() {
        try {
            workoutLog = jsonReader.read();
            syncCounter();
            sessionPanel.refresh(workoutLog);
            chartPanel.refresh(workoutLog);
            JOptionPane.showMessageDialog(this, "Loaded successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not load from file.");
        }
    }

    // MODIFIES: this
    // EFFECTS: shows dialog asking the user to load previous data on
    // startup.
    // if YES, reads from file, syncs the session counter, and refreshes panels.
    // shows an error dialog if the file cannot be read
    private void promptLoad() {
        int choice = JOptionPane.showConfirmDialog(null,
                "Load previous workout data?", "Load", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                workoutLog = jsonReader.read();
                syncCounter();
                sessionPanel.refresh(workoutLog);
                chartPanel.refresh(workoutLog);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not load from file.");
            }
        }
    }

    // EFFECTS: shows a YES/NO dialog asking the user to save before exiting.
    // calls save() if the user selects YES
    private void promptSave() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Save before exiting?", "Save", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            save();
        }
    }

    // MODIFIES: WorkoutSession (variable counter)
    // EFFECTS: finds the highest session ID in the loaded workout log and advances
    // the WorkoutSession counter past it so new sessions do not reuse existing IDs
    private void syncCounter() {
        int maxId = 0;
        for (WorkoutSession s : workoutLog.getAllWorkoutSessionsStats()) {
            if (s.getSessionId() > maxId) {
                maxId = s.getSessionId();
            }
        }
        WorkoutSession.updateCounter(maxId);
    }

    // EFFECTS: prints all events logged since the application started to the
    // console
    private void printLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
    }

    // EFFECTS: displays the splash screen and then launches the GUI
    public static void main(String[] args) {
        showSplash();
        new WorkoutTrackerGUI();
    }
}