package itc2019.dataset;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents an event as defined in the ITC 2019. An event simply is a scheduled class that has been
 * assigned a time, a room, and a list of students. <strong>Note that at any given time, an event can be half or not
 * scheduled, and the time, room, and student parameters can be null. Also, if the class, as defined, does not require
 * a room, the assigned room of its respective event is null.</strong>
 */
class Event extends Class {

    private TimeAssignment assignedTime;
    private RoomAssignment assignedRoom;
    private final ArrayList<Student> students;

    /**
     * Constructs an unscheduled event object. To schedule this event, set the time, room (if required), and student
     * parameters using the set methods.
     *
     * @param id            The id of this class.
     * @param limit         The limit (capacity) of this class.
     * @param possibleTimes The possible time periods for this class.
     * @param possibleRooms The possible room assignments for this class. <strong>If this class does not require a room, pass null.</strong>
     * @param parent        The parent class of this class. <strong>If this class is not in a parent-child relationship, pass null.</strong>
     * @throws IllegalArgumentException If the passed id or limit parameters are invalid.
     */
    Event(int id, int limit, TimeAssignment[] possibleTimes, RoomAssignment[] possibleRooms, Class parent) throws IllegalArgumentException {
        super(id, limit, possibleTimes, possibleRooms, parent);
        students = new ArrayList<>();
    }

    /**
     * Gets the event time.
     *
     * @return The current time period assigned to this event. <strong>Returns null if the time of this event is not
     * currently scheduled.</strong>
     */
    TimeAssignment getAssignedTime() {
        return assignedTime;
    }

    /**
     * Assigns a time to this event.
     *
     * @param assignedTime The time period to schedule this event at.
     * @throws IllegalArgumentException If the passed time assignment is invalid. <strong>To reduce the running time
     *                                  from O(n), where n is the number of possible time assignments for this event's
     *                                  class, to O(1), comment if statement in the method. However, doing so will allow
     *                                  invalid (not included in the possible time assignments of this event's class)
     *                                  time assignments.</strong>
     */
    void setAssignedTime(TimeAssignment assignedTime) throws IllegalArgumentException {
        //TODO (OPTIMIZATION): For faster running time, comment the following if. However, doing so will allow invalid time assignments.
        if (!Arrays.asList(getPossibleTimes()).contains(assignedTime))
            throw new IllegalArgumentException("This time assignment is invalid as it is not in the list of possible time assignment of this class!");
        //COMMENT UNTIL HERE
        this.assignedTime = assignedTime;
    }

    /**
     * Gets the event room.
     *
     * @return The current room assigned to this event. <strong>Returns null if the room of this event is not currently
     * scheduled or if the class does not require a room.</strong>
     */
    RoomAssignment getAssignedRoom() {
        return assignedRoom;
    }

    /**
     * Assigns a room to this event.
     *
     * @param assignedRoom The room to schedule this event in.
     * @throws IllegalArgumentException If this event's class does not require a room or if the passed room assignment
     *                                  is invalid. <strong>To reduce the running time from O(n), where n is the number
     *                                  of possible room assignments for this event's class, to O(1), comment all the if
     *                                  statements in the method. However, doing so will allow invalid room assignments.
     *                                  </strong>
     */
    void setAssignedRoom(RoomAssignment assignedRoom) throws IllegalArgumentException {
        //TODO (OPTIMIZATION): For faster running time, comment the following if. However, doing so will allow invalid room assignments.
        if (getPossibleRooms() == null)
            throw new IllegalArgumentException("The class of this event does not require a room!");
        if (!Arrays.asList(getPossibleRooms()).contains(assignedRoom))
            throw new IllegalArgumentException("This room assignment is invalid as it is not in the list of possible room assignment of this class!");
        //COMMENT UNTIL HERE
        this.assignedRoom = assignedRoom;
    }

    @Override
    public String toString() {
        return "Event{" + "assignedTime=" + assignedTime + ", assignedRoom=" + assignedRoom + ", students=" + students + '}';
    }

    /**
     * <p>Adds the student to this event (enrolls the students in this event's class).</p>
     * <strong><p>IMPORTANT: This method does NOT check the following issues:</p>
     * <p>1) If this event's class is in the list of the demanded courses of the student;</p>
     * <p>2) If the student has been enrolled in the parent class of this event's class before being added here;</p>
     * <p>3) If the student has taken one class from each subpart of a single configuration.</p>
     * <p>Checking mechanisms of these issues should later be added here (if possible) or in other classes.</p></strong>
     *
     * @param student The student to add.
     * @throws IllegalArgumentException If this event's class has reached its max limit.
     */
    void addStudent(Student student) throws IllegalArgumentException {
        if (students.size() == getLimit()) throw new IllegalArgumentException("This event has reached max capacity!");
        //TODO: Add checking for the student needing this event's class (here or in another class).
        //TODO: Add checking for the student being enrolled in the parent class (here or in another class).
        //TODO: Add checking for the student taking one class from each subpart of a single configuration (here or in another class).
        students.add(student);
    }
}