package itc2019.dataset;

import java.util.Arrays;

/**
 * <p>This class represents a timetable. A timetable is a group of events (scheduled classes) and represents a candidate
 * solution. It is the final output of a method and what we use to assess the quality of a solution. <strong> Please
 * note that at any given time a timetable can be half or not scheduled and some (or all) its events can have empty time
 * and room assignments.</strong></p>
 * <p>A timetable is in a 1 to 1 relationship with the classes defined in the ITC 2019. In other words, each timetable
 * includes n events, where n is the number of total classes in the problem instance. Furthermore, the first event
 * element in a timetable event[0] correlates with the class with id of 1. Similarly: event[1] -> class[id=2],
 * event[2] -> class[id=3], etc.</p>
 */
public class Timetable {
    private final Event[] events;

    /**
     * <p>Constructs an empty timetable including empty events for each of the classes defined in the dataset.</p>
     * <p><strong>To populate the timetable with events, use the setEvent method.</strong></p>
     *
     * @param classNo Total number of classes in the problem instance.
     */
    Timetable(int classNo) {
        events = new Event[classNo];
    }

    /**
     * Gets the event with the given class id.
     *
     * @param classId The class id of the event.
     * @return The event corresponding with the given class id.
     */
    Event getEvent(int classId) {
        return events[classId - 1];
    }

    /**
     * Populates the timetable with the given event.
     *
     * @param event The event to be added to the timetable.
     * @throws IllegalArgumentException If in the timetable there is already an event with the class id of the passed
     *                                  event. <strong>In each run, you only need to set all the events (based on all
     *                                  the classes in the problem instance) in the timetable ONCE. After they are all
     *                                  initialized, using this set method, you only need to schedule them, and you do
     *                                  not need to set them again.</strong>
     */
    void setEvent(Event event) throws IllegalArgumentException {
        if (events[event.getTheClass().id() - 1] != null)
            throw new IllegalArgumentException("An event with this class id is already added!");
        events[event.getTheClass().id() - 1] = event;
    }

    @Override
    public String toString() {
        return "Timetable{" + "events=" + Arrays.toString(events) + '}';
    }
}