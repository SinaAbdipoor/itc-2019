package itc2019.dataset;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>This class represents classes as defined in the ITC 2019. Each student must attend one class from each subpart of
 * a single configuration. All students in the course configuration must be sectioned into classes of each subpart such
 * that their limit is not exceeded (one student attending configuration Lec-Rec must take one class from each of its
 * subparts 1 Lecture and 2 Recitation, e.g., Lec1 and Rec3). Each class has a unique id and belongs to one subpart
 * (classes Rec5, Rec6, Rec7, and Rec8 belong to subpart 4 Recitation).</p>
 * <p>A class may have a parent class defined which means that a student who attends the class must also attend its
 * parent class. For example, Lab3 has the parent Rec7 which has the parent Lec4. This means that a student attending
 * Lab3 must also attend Rec7 and Lec4 and no other combination including Lab3 is allowed. On the other hand, there is
 * no parent-child relationship between classes of subparts 1 Lecture and 2 Recitation, so a student may take any
 * lecture Lec1 or Lec2 and any recitation Rec1, Rec2, Rec3 or Rec4.</p>
 * <p>Each class has a defined set of possible times when it can meet. Each eligible time has a specified penalty which
 * must be included in the overall time penalization when the time is selected for the class. Valid time specifications
 * were described in Section Times. Each class also has a defined a set of possible rooms where it can meet (if a room
 * is needed). Each eligible room has a specified penalty to be included in the overall time penalization when selected.
 * Valid room specifications were given in Section Rooms.</p>
 * <p>An event simply is a scheduled class that has been assigned a time, a room, and a list of students.
 * <strong>Note that at any given time, a class can be half or not scheduled, and the time, room, and student parameters
 * can be null. Also, if the class, as defined, does not require a room, the assigned room of its respective
 * event is null.</strong></p>
 */
class Class {

    private final int id;
    private final int limit;
    private final TimeAssignment[] possibleTimes;
    private final RoomAssignment[] possibleRooms;
    private final Class parent;
    private TimeAssignment assignedTime;
    private RoomAssignment assignedRoom;
    private final ArrayList<Student> students;

    /**
     * Constructs an unscheduled Class object. To schedule this class, set the time, room (if required), and student
     * parameters using the set methods.
     *
     * @param id            The id of this class.
     * @param limit         The limit (capacity) of this class.
     * @param possibleTimes The possible time periods for this class.
     * @param possibleRooms The possible room assignments for this class. <strong>If this class does not require a room, pass null.</strong>
     * @param parent        The parent class of this class. <strong>If this class is not in a parent-child relationship, pass null.</strong>
     * @throws IllegalArgumentException If the passed id or limit parameters are invalid.
     */
    Class(int id, int limit, TimeAssignment[] possibleTimes, RoomAssignment[] possibleRooms, Class parent) throws IllegalArgumentException {
        if (id < 1) throw new IllegalArgumentException("Class id cannot be less than 1!");
        this.id = id;
        if (limit < 0) throw new IllegalArgumentException("Class limit cannot be negative!");
        this.limit = limit;
        this.possibleTimes = possibleTimes;
        this.possibleRooms = possibleRooms;
        this.parent = parent;
        students = new ArrayList<>();
    }

    /**
     * Gets the class id.
     *
     * @return The id of this class.
     */
    int getId() {
        return id;
    }

    /**
     * Gets the class limit.
     *
     * @return The limit (capacity) of this class.
     */
    int getLimit() {
        return limit;
    }

    /**
     * Gets all the possible time assignments of this class.
     *
     * @return A list containing all the possible time periods when this class can be held.
     */
    TimeAssignment[] getPossibleTimes() {
        return possibleTimes;
    }

    /**
     * Gets all the possible room assignments of this class.
     *
     * @return A list containing all the possible rooms where this class can be held. <strong>Returns null If this class, by definition, does not require a physical room.</strong>
     */
    RoomAssignment[] getPossibleRooms() {
        return possibleRooms;
    }

    /**
     * Gets the parent class of this class.
     *
     * @return The class that should be taken with this class. <strong>Returns null if this class, by definition, does not have a parent class.</strong>
     */
    Class getParent() {
        return parent;
    }

    /**
     * Gets the class time.
     *
     * @return The current time period assigned to this class. <strong>Returns null if the time of this class is not
     * currently scheduled.</strong>
     */
    TimeAssignment getAssignedTime() {
        return assignedTime;
    }

    /**
     * Assigns a time to this class.
     *
     * @param assignedTime The time period to schedule this class at.
     * @throws IllegalArgumentException If the passed time assignment is invalid. <strong>To reduce the running time
     *                                  from O(n), where n is the number of possible time assignments for this class,
     *                                  to O(1), comment if statement in this method. However, doing so will allow
     *                                  invalid (not included in the possible time assignments of this class) time
     *                                  assignments.</strong>
     */
    void setAssignedTime(TimeAssignment assignedTime) throws IllegalArgumentException {
        //TODO (OPTIMIZATION): For faster running time, comment the following if. However, doing so will allow invalid time assignments.
        if (!Arrays.asList(possibleTimes).contains(assignedTime))
            throw new IllegalArgumentException("This time assignment is invalid as it is not in the list of possible time assignment of this class!");
        //COMMENT UNTIL HERE
        this.assignedTime = assignedTime;
    }

    /**
     * Gets the class room.
     *
     * @return The current room assigned to this class. <strong>Returns null if the room of this class is not currently
     * scheduled or if the class does not require a room.</strong>
     */
    RoomAssignment getAssignedRoom() {
        return assignedRoom;
    }

    /**
     * Assigns a room to this class.
     *
     * @param assignedRoom The room to schedule this class in.
     * @throws IllegalArgumentException If this class does not require a room or if the passed room assignment is
     *                                  invalid. <strong>To reduce the running time from O(n), where n is the number
     *                                  of possible room assignments for this class, to O(1), comment the second if
     *                                  statement in this method. However, doing so will allow invalid room assignments.
     *                                  </strong>
     */
    void setAssignedRoom(RoomAssignment assignedRoom) throws IllegalArgumentException {
        //TODO (OPTIMIZATION): For faster running time, comment the following if. However, doing so will allow invalid room assignments.
        if (possibleRooms == null) throw new IllegalArgumentException("This class does not require a room!");
        if (!Arrays.asList(possibleRooms).contains(assignedRoom))
            throw new IllegalArgumentException("This room assignment is invalid as it is not in the list of possible room assignment of this class!");
        //COMMENT UNTIL HERE
        this.assignedRoom = assignedRoom;
    }

    @Override
    public String toString() {
        return "Class{" + "id=" + id + ", limit=" + limit + ", possibleTimes=" + Arrays.toString(possibleTimes) + ", possibleRooms=" + Arrays.toString(possibleRooms) + ", parent=" + parent + ", assignedTime=" + assignedTime + ", assignedRoom=" + assignedRoom + ", students=" + students + '}';
    }

    /**
     * Checks if this class contains the given student.
     *
     * @param student The student to search for.
     * @return True if the passed student is enrolled in this class; False otherwise.
     */
    boolean containsStudent(Student student) {
        return students.contains(student);
    }

    /**
     * <p>Adds the student to this class (enrolls the students in this class).</p>
     * <strong><p>IMPORTANT: This method does NOT check if the student needs this class or if the student has taken one
     * class from each subpart of a single configuration.</p></strong>
     *
     * @param student The student to add.
     * @throws IllegalArgumentException If this class has reached its max limit, or if the student has not taken the
     *                                  parent class first. <strong>To significantly reduce the running time, comment
     *                                  the if statements in this method. However, doing so will allow invalid student
     *                                  enrollments. </strong>
     */
    void addStudent(Student student) throws IllegalArgumentException {
        //TODO (OPTIMIZATION): For faster running time, comment the following if. However, doing so will allow invalid student enrollments.
        if (students.size() == limit) throw new IllegalArgumentException("This class has reached max capacity!");
        if (parent != null && !parent.containsStudent(student))
            throw new IllegalArgumentException("The student has not taken the parent class!");
//        if (!student.needsClass(this))
//            throw new IllegalArgumentException("The student does not need to take this class");
        //COMMENT UNTIL HERE
        //TODO: Add checking for the student taking one class from each subpart of a single configuration (here or in another class).
        students.add(student);
    }
}