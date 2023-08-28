package itc2019.dataset;

import itc2019.dataset.constraints.HardConstraint;
import itc2019.dataset.constraints.SoftConstraint;

/**
 * This class represents a problem instance of the International Timetabling Competition (ITC) 2019 dataset.
 */
public record ProblemInstance(String instanceName, int nrDays, int nrWeeks, int slotsPerDay, int timePenaltyWeight,
                              int roomPenaltyWeight, int distributionPenaltyWeight, int studentPenaltyWeight,
                              Room[] rooms, Course[] courses, HardConstraint[] hardConstraints,
                              SoftConstraint[] softConstraints, Student[] students, TravelTime travelTimes) {
    /**
     * Constructs a new problem instance object with the provided parameters and performs validation checks.
     *
     * @param instanceName              The name of the problem instance.
     * @param nrDays                    The number of days in a week.
     * @param nrWeeks                   The number of weeks in a semester.
     * @param slotsPerDay               The number of time slots per day.
     * @param timePenaltyWeight         The weight for time-related penalties.
     * @param roomPenaltyWeight         The weight for room-related penalties.
     * @param distributionPenaltyWeight The weight for distribution-related penalties.
     * @param studentPenaltyWeight      The weight for student-related penalties.
     * @param rooms                     An array of rooms available for scheduling.
     * @param courses                   An array of courses to be scheduled.
     * @param hardConstraints           An array of hard constraints to be satisfied.
     * @param softConstraints           An array of soft constraints to be optimized.
     * @param students                  An array of students and their preferences.
     * @param travelTimes               The travel time matrix between rooms.
     * @throws IllegalArgumentException If the provided parameters violate the specified constraints.
     **/
    public ProblemInstance {
        // TODO: Do I need to add a check for ids being sequential and unique?
        if (nrDays < 0 || nrDays > 7)
            throw new IllegalArgumentException("The number of days in a week should be between 0 and 7!");
        if (nrWeeks < 0) throw new IllegalArgumentException("The number of weeks in a semester cannot be negative!");
        if (slotsPerDay < 0 || slotsPerDay > 288)
            throw new IllegalArgumentException("The number of slots (each slot is 5 mins) per day should be between 0 and 288!");
        if (timePenaltyWeight < 1) throw new IllegalArgumentException("The time penalty weight should be above 0!");
        if (roomPenaltyWeight < 1) throw new IllegalArgumentException("The room penalty weight should be above 0!");
        if (distributionPenaltyWeight < 1)
            throw new IllegalArgumentException("The distribution penalty weight should be above 0!");
        if (studentPenaltyWeight < 1)
            throw new IllegalArgumentException("The student penalty weight should be above 0!");
        if (travelTimes.getRowCount() != rooms.length)
            throw new IllegalArgumentException("The passed travel times is not of the size of total number of rooms in the problem instance!");
    }
}