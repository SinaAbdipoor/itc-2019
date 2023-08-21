package itc2019.dataset;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>This class represents an event. Event is simply defined as a scheduled class, i.e. a class that has been assigned
 * a time slot, a room (if required), and a list of students.</p>
 * <p><strong>Note that at any given moment, an event can be half or not scheduled and its time, room, and student
 * parameters can be null.</strong></p>
 */
class Event {
    private final Class theClass;
    private TimeAssignment time;
    private RoomAssignment room;
    private final ArrayList<Student> students;

    /**
     * Constructs an empty (not scheduled) event object for the given class.
     *
     * @param theClass The class to be scheduled.
     */
    Event(Class theClass) {
        this.theClass = theClass;
        students = new ArrayList<>();
    }

    /**
     * Gets the class of this event.
     *
     * @return This event's class to be scheduled.
     */
    Class getTheClass() {
        return theClass;
    }

    /**
     * Gets the current time assigned to this event.
     *
     * @return Time assignment of this event. Returns null if this event is not assigned a time.
     */
    TimeAssignment getTime() {
        return time;
    }

    /**
     * Sets the time of this event.
     *
     * @param time The time to be assigned to this event.
     * @throws IllegalArgumentException If the passed time is invalid for this event.
     *                                  <p>Running time = O(n), where n is the number of possible time assignments for
     *                                  this event's class. To reduce the running time to O(1), comment the first if in
     *                                  the method. However, doing so will allow invalid (not included in the possible
     *                                  time assignments of this event's class) time assignments.</p>
     */
    void setTime(TimeAssignment time) throws IllegalArgumentException {
        //TODO OPTIMIZATION: For faster running time, comment the following if. However, doing so will allow invalid (not included in the possible time assignments of this event's class) time assignments.
        if (!Arrays.asList(theClass.possibleTimes()).contains(time))
            throw new IllegalArgumentException("The passed time does not exist in the possible time assignments of this class!");
        // COMMENT UNTIL HERE!
        this.time = time;
    }

    /**
     * Gets the current room assigned to this event. <strong>Note that some classes (as defined) do not require a room.
     * </strong>
     *
     * @return Room assignment of this event. Returns null if this event is not assigned a room or does not require a
     * room.
     */
    RoomAssignment getRoom() {
        return room;
    }

    /**
     * Sets the room of this event.
     *
     * @param room The room to be assigned to this event.
     * @throws IllegalArgumentException If the passed room is invalid for this event or this event's class does not need
     *                                  a room.
     *                                  <p>Running time = O(n), where n is the number of possible room assignments for
     *                                  this event's class. To reduce the running time to O(1), comment the first if in
     *                                  the method. However, doing so will allow invalid (not included in the possible
     *                                  room assignments of this event's class) room assignments.</p>
     */
    void setRoom(RoomAssignment room) throws IllegalArgumentException {
        //TODO OPTIMIZATION: For faster running time, comment the following if. However, doing so will allow invalid (not included in the possible room assignments of this event's class) room assignments.
        if (!Arrays.asList(theClass.possibleRooms()).contains(room))
            throw new IllegalArgumentException("The passed room does not exist in the possible room assignments of this class!");
        // COMMENT UNTIL HERE!
        this.room = room;
    }

    ArrayList<Student> getStudents() {
        //TODO: Add students restrictions.
        return students;
    }

    @Override
    public String toString() {
        return "Event{" + "theClass=" + theClass + ", time=" + time + ", room=" + room + ", students=" + students + '}';
    }
}