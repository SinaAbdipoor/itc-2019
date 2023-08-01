package itc2019.dataset;

/**
 * This class represents a possible room assignment for a class as defined in the ITC 2019.
 */
record RoomAssignment(Room room, int penalty) {
    /**
     * Constructs a room assignment object.
     *
     * @param room    The possible room.
     * @param penalty The penalty associated with this assignment.
     * @throws IllegalArgumentException If the passed penalty is invalid.
     */
    RoomAssignment {
        if (penalty < 0) throw new IllegalArgumentException("Penalty cannot be negative!");
    }
}