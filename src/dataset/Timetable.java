package dataset;

import dataset.constraints.HardConstraint;
import dataset.constraints.SoftConstraint;

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
     * Constructs an empty timetable (a group of unscheduled events) based on the passed classes. <strong>Make sure the
     * passed classes include ALL the classes defined in the problem statement as this timetable is constructed and
     * initialized with events (based on passed classes) ONLY ONCE.</strong>
     *
     * @param classes The list of ALL classes defined in the problem instance.
     * @throws IllegalArgumentException  If there are multiple classes in the list of passed classes with the same id.
     * @throws IndexOutOfBoundsException When the ids of the passed classes are not sequential.
     */
    public Timetable(Class[] classes) throws IllegalArgumentException, IndexOutOfBoundsException {
        events = new Event[classes.length];
        for (Class aClass : classes) {
            if (events[aClass.id() - 1] != null)
                throw new IllegalArgumentException("Multiple classes with the same id have been found!");
            events[aClass.id() - 1] = new Event(aClass);
        }
    }

    /**
     * Gets the events of this timetable.
     *
     * @return The list of this timetable's events.
     */
    public Event[] getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "Timetable{" + "events=" + Arrays.toString(events) + '}';
    }

    /**
     * Gets the corresponding event of the passed class.
     *
     * @param aClass The class to find the corresponding event of.
     * @return The event of the passed class.
     */
    public Event getEvent(Class aClass) {
        return events[aClass.id() - 1];
    }

    /**
     * Checks if this timetable is feasible by verifying that all hard constraints
     * in the given problem instance are satisfied.
     *
     * @param instance The problem instance containing the hard constraints.
     * @return true if all hard constraints are satisfied; false otherwise.
     */
    public boolean isFeasible(ProblemInstance instance) {
        for (HardConstraint constraint : instance.hardConstraints())
            if (!constraint.constraint().isSatisfied(this)) return false;
        return true;
    }

    /**
     * Calculates the number of student conflicts in this timetable.
     *
     * @return The number of student conflicts.
     */
    public int calcStudentConflicts() {
        // TODO: Implement the logic to calculate student conflicts based on the events in this timetable.
        return 0;
    }

    /**
     * Calculates the total time penalty for this timetable.
     * <p>
     * Sums the penalty values from all time assignments of the events in this timetable.
     * </p>
     *
     * @return The total time penalty.
     */
    public int calcTimePenalty() {
        int timePenalty = 0;
        for (Event event : events) {
            TimeAssignment timeAssignment = event.getTimeAssignment();
            if (timeAssignment != null) timePenalty += timeAssignment.penalty();
        }
        return timePenalty;
    }

    /**
     * Calculates the total room penalty for this timetable.
     * <p>
     * Sums the penalty values from all room assignments of the events in this timetable.
     * </p>
     *
     * @return The total room penalty.
     */
    public int calcRoomPenalty() {
        int roomPenalty = 0;
        for (Event event : events) {
            RoomAssignment roomAssignment = event.getRoomAssignment();
            if (roomAssignment != null) roomPenalty += roomAssignment.penalty();
        }
        return roomPenalty;
    }

    /**
     * Calculates the total distribution penalty for this timetable.
     * <p>
     * Sums the penalties from all soft constraints in the given problem instance,
     * as evaluated on this timetable.
     * </p>
     *
     * @param instance The problem instance containing the soft constraints.
     * @return The total distribution penalty.
     */
    public int calcDistributionPenalty(ProblemInstance instance) {
        int distributionPenalty = 0;
        for (SoftConstraint constraint : instance.softConstraints()) {
            distributionPenalty += constraint.calcPenalty(this);
        }
        return distributionPenalty;
    }

    /**
     * Calculates the total cost of this timetable based on the weighted sum of
     * student conflicts, time penalties, room penalties, and distribution penalties.
     *
     * @param instance The problem instance providing penalty weights.
     * @return The total cost of the timetable.
     */
    public int calcCost(ProblemInstance instance) {
        return ((instance.studentPenaltyWeight() * calcStudentConflicts()) + (instance.timePenaltyWeight() * calcTimePenalty()) + (instance.roomPenaltyWeight() * calcRoomPenalty()) + (instance.distributionPenaltyWeight() * calcDistributionPenalty(instance)));
    }
}