package ui.gui;

import model.WorkoutLog;
import model.WorkoutSession;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.util.List;

// Represents the panel that draws a bar chart of training volume per workout session.
// Structure based on GamePanel from SpaceInvaders:
// paintComponent delegates to one top-level draw method which calls smaller helpers,
// and each helper saves and restores the Graphics colour before and after drawing.

@ExcludeFromJacocoGeneratedReport
public class ChartPanel extends JPanel {

    private static final int panelWidth = 800;
    private static final int panelHeight = 180;
    private static final int leftPadding = 65;
    private static final int topPadding = 25;
    private static final int bottomPadding = 35;
    private static final int gridLines = 4;
    private static final int maxBarWidth = 60;
    private static final int minBarWidth = 10;
    private static final int rightPadding = 10;
    private static final int sessionLabelOffset = 15;
    private static final int gridLabelOffset = 4;
    private static final int yAxisLabelGap = 5;
    private static final Color barColorHigh = new Color(39, 174, 96);
    private static final Color barColorMid = new Color(255, 140, 0);
    private static final Color barColorLow = new Color(192, 57, 43);
    private static final int lowVolumeMax = 500;
    private static final int highVolumeMin = 10;
    private static final int minBarHeight = 4;

    private WorkoutLog workoutLog;

    // REQUIRES: workoutLog != null
    // MODIFIES: this
    // EFFECTS: constructs a chart panel with a titled border, backed by workoutLog
    public ChartPanel(WorkoutLog workoutLog) {
        this.workoutLog = workoutLog;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBorder(BorderFactory.createTitledBorder("Training Volume per Session (lbs)"));
    }

    // REQUIRES: workoutLog != null
    // MODIFIES: this
    // EFFECTS: updates the workout log reference and repaints the chart
    public void refresh(WorkoutLog workoutLog) {
        this.workoutLog = workoutLog;
        repaint();
    }

    // MODIFIES: g
    // EFFECTS: paints the chart onto this panel by calling drawChart.
    // Structure based on GamePanel.paintComponent from SpaceInvaders in CPSC 210.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChart(g);
    }

    // MODIFIES: g
    // EFFECTS: draws the full chart onto g, including the background, grid lines,
    // axes, and bars; draws an empty message if there are no sessions
    private void drawChart(Graphics g) {
        List<WorkoutSession> sessions = workoutLog.getAllWorkoutSessionsStats();
        int chartWidth = getWidth() - leftPadding - rightPadding;
        int chartHeight = getHeight() - bottomPadding - topPadding;

        drawBackground(g, chartWidth, chartHeight);

        if (sessions.isEmpty()) {
            drawEmptyMessage(g, chartWidth, chartHeight);
            drawAxes(g, chartWidth, chartHeight);
            return;
        }

        double maxVolume = getMaxVolume(sessions);
        drawGridLines(g, chartWidth, chartHeight, maxVolume);
        drawAxes(g, chartWidth, chartHeight);
        drawBars(g, sessions, chartWidth, chartHeight, maxVolume);
    }

    // MODIFIES: g
    // EFFECTS: fills the chart area with a light grey background rectangle
    private void drawBackground(Graphics g, int chartWidth, int chartHeight) {
        Color savedCol = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(leftPadding, topPadding, chartWidth, chartHeight);
        g.setColor(savedCol);
    }

    // MODIFIES: g
    // EFFECTS: draws a horizontally centred message onto g when no sessions exist
    private void drawEmptyMessage(Graphics g, int chartWidth, int chartHeight) {
        Color savedCol = g.getColor();
        g.setColor(Color.GRAY);
        FontMetrics fm = g.getFontMetrics();
        String message = "Add a session to see your chart!";
        int messageX = leftPadding + (chartWidth - fm.stringWidth(message)) / 2;
        g.drawString(message, messageX, topPadding + chartHeight / 2);
        g.setColor(savedCol);
    }

    // REQUIRES: sessions is not empty
    // EFFECTS: returns the largest training volume across all sessions in the list;
    // returns 1.0 as a minimum so the chart always has a valid scale
    private double getMaxVolume(List<WorkoutSession> sessions) {
        double maxVolume = 1.0;

        for (WorkoutSession s : sessions) {
            if (s.getVolumeTrained() > maxVolume) {
                maxVolume = s.getVolumeTrained();
            }
        }
        return maxVolume;
    }

    // MODIFIES: g
    // EFFECTS: draws gridLines horizontal grid lines across the chart area onto g,
    // with a y-axis label showing the volume value at each line
    private void drawGridLines(Graphics g, int chartWidth, int chartHeight, double maxVolume) {
        Color savedCol = g.getColor();
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i <= gridLines; i++) {
            int y = topPadding + chartHeight - (int) (chartHeight * i / (double) gridLines);
            g.setColor(Color.GRAY);
            g.drawLine(leftPadding, y, leftPadding + chartWidth, y);
            g.setColor(Color.BLACK);
            String label = (int) (maxVolume * i / gridLines) + "";
            int labelX = leftPadding - fm.stringWidth(label) - yAxisLabelGap;
            g.drawString(label, labelX, y + gridLabelOffset);
        }
        g.setColor(savedCol);
    }

    // MODIFIES: g
    // EFFECTS: draws the x and y axes of the chart onto g
    private void drawAxes(Graphics g, int chartWidth, int chartHeight) {
        Color savedCol = g.getColor();
        g.setColor(Color.BLACK);
        g.drawLine(leftPadding, topPadding, leftPadding, topPadding + chartHeight);
        g.drawLine(leftPadding, topPadding + chartHeight,
                leftPadding + chartWidth, topPadding + chartHeight);
        g.setColor(savedCol);
    }

    // REQUIRES: sessions is not empty, maxVolume > 0
    // MODIFIES: g
    // EFFECTS: draws one filled blue bar per session onto g, with the session ID
    // label drawn below the x-axis
    private void drawBars(Graphics g, List<WorkoutSession> sessions,
            int chartWidth, int chartHeight, double maxVolume) {
        int numSessions = sessions.size();
        int barWidth = Math.max(minBarWidth, Math.min(maxBarWidth,
                chartWidth / (numSessions * 2 + 1)));
        int spacing = (chartWidth - numSessions * barWidth) / (numSessions + 1);

        for (int i = 0; i < numSessions; i++) {
            drawBar(g, sessions.get(i), i, barWidth, spacing, chartHeight, maxVolume);
        }
    }

    // REQUIRES: maxVolume > 0
    // MODIFIES: g
    // EFFECTS: draws one filled blue bar and its session ID label onto g
    // at position i among the bars
    private void drawBar(Graphics g, WorkoutSession session, int i,
            int barWidth, int spacing, int chartHeight, double maxVolume) {

        Color savedColor = g.getColor();
        int barHeight = (int) (chartHeight * session.getVolumeTrained() / maxVolume);
        barHeight = Math.max(barHeight, minBarHeight);
        int x = leftPadding + spacing + i * (barWidth + spacing);
        int y = topPadding + chartHeight - barHeight;

        g.setColor(getBarColor(session.getVolumeTrained()));
        g.fillRect(x, y, barWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawString("S" + session.getSessionId(), x, topPadding + chartHeight + sessionLabelOffset);
        g.setColor(savedColor);
    }

    // REQUIRES: volume >= 0
    // EFFECTS: returns green if volume is above lowVolumeMax,
    // red if volume is below highVolumeMin,
    // orange otherwise
    private Color getBarColor(double volume) {
        if (volume > lowVolumeMax) {
            return barColorHigh;
        } else if (volume < highVolumeMin) {
            return barColorLow;
        } else {
            return barColorMid;
        }
    }
}