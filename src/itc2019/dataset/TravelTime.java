package itc2019.dataset;

import java.util.Arrays;

/**
 * This class represents the travel time between the rooms of the ITC 2019 dataset, which expresses the number of
 * timeslots needed to get from one room to other rooms.
 */
class TravelTime {
    private final int[][] travelTimes;

    /**
     * Constructs a travel time object and initializes the travel times for rooms with 0.
     *
     * @param roomCount The number of all rooms in the problem instance.
     * @throws NegativeArraySizeException If the provided room count is negative.
     */
    TravelTime(int roomCount) throws NegativeArraySizeException {
        travelTimes = new int[roomCount][roomCount];
    }

    @Override
    public String toString() {
        return "TravelTime{" + "travelTimes=" + Arrays.toString(travelTimes) + '}';
    }

    /**
     * Gets the travel time between two rooms.
     *
     * @param room1 The first room.
     * @param room2 The second room.
     * @return The number of timeslots needed to go from room 1 to room 2.
     * @throws IndexOutOfBoundsException If room indices are out of bounds.
     */
    int getTravelTime(Room room1, Room room2) throws IndexOutOfBoundsException {
        return travelTimes[room1.id() - 1][room2.id() - 1];
    }

    /**
     * Sets the travel time between two rooms.
     *
     * @param room1     The first room.
     * @param room2     The second room.
     * @param timeslots The number of timeslots needed to travel between the rooms.
     * @throws IllegalArgumentException  If the number of timeslots is negative.
     * @throws IndexOutOfBoundsException If room indices are out of bounds.
     */
    void setTravelTime(Room room1, Room room2, int timeslots) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (timeslots < 0)
            throw new IllegalArgumentException("The number of timeslots needed to travel between two rooms cannot be negative!");
        travelTimes[room1.id() - 1][room2.id() - 1] = timeslots;
        travelTimes[room2.id() - 1][room1.id() - 1] = timeslots;
    }
}