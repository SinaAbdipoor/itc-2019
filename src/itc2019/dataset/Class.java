package itc2019.dataset;

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
 */
class Class {

    private final int id;
    private final int limit;
    private final TimeAssignment[] possibleTimes;
    private final RoomAssignment[] possibleRooms;
    private final Class parent;

    /**
     * Constructs a Class object.
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

    @Override
    public String toString() {
        return "Class{" + "id=" + id + ", limit=" + limit + ", possibleTimes=" + Arrays.toString(possibleTimes) + ", possibleRooms=" + Arrays.toString(possibleRooms) + ", parent=" + parent + '}';
    }
}