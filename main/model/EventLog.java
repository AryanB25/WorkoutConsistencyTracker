package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Represents a log of events that have occurred in the application.
public class EventLog implements Iterable<Event> {

    private static EventLog theLog;
    private Collection<Event> events;

    // EFFECTS: constructs an empty event log (private to enforce Singleton)
    private EventLog() {
        events = new ArrayList<Event>();
    }

    // EFFECTS: returns the sole instance of EventLog, creating it if necessary
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    // MODIFIES: this
    // EFFECTS: adds the given event to the event log
    public void logEvent(Event e) {
        events.add(e);
    }

    // MODIFIES: this
    // EFFECTS: clears all events from the event log and logs that the log was
    // cleared
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: returns an iterator over the events in this event log
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}