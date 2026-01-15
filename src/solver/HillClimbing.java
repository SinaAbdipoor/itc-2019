package solver;

import java.util.Random;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

public class HillClimbing extends Algorithm{

    /**
     * Constructor for algorithms with time and evaluation limits.
     * Use -1 for unlimited time or evaluations.
     *
     * @param instance   Problem instance to solve.
     * @param maxSeconds Maximum allowed seconds for the algorithm (-1 for unlimited).
     * @param maxNfe     Maximum allowed function evaluations (-1 for unlimited).
     * @throws IllegalArgumentException if limits are invalid.
     */
    public HillClimbing(ProblemInstance instance, int maxSeconds, int maxNfe) throws IllegalArgumentException {
        super("Hill Climbing", instance, maxSeconds, maxNfe);
    }

    @Override
    Timetable initialize(final Random random) {
        return createRandomTimetable(random);
    }

    @Override
    Timetable step(final Random random) {
        Timetable candidate = this.getSolution();
        Class theClass = instance.classes()[random.nextInt(instance.classes().length)];
        Event event = candidate.getEvent(theClass);
        if (theClass.possibleRooms() == null || random.nextInt(2) == 1) {
			event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
		} else {
            if (event.getAvailableRooms() != null) {
				event.setRoomAssignment(event.getAvailableRooms()[random.nextInt(event.getAvailableRooms().length)]);
			} else {
				event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
			}
        }
        return candidate;
    }

    Timetable BigStep(final Random random) {
        Timetable candidate = this.getSolution();
        boolean first = true;
        while (first || random.nextInt(5) > 0) {
            first = false;
            Class theClass = instance.classes()[random.nextInt(instance.classes().length)];
            Event event = candidate.getEvent(theClass);
            if (theClass.possibleRooms() == null || random.nextInt(2) == 1) {
                event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
            } else {
                if (event.getAvailableRooms() != null) {
                    event.setRoomAssignment(event.getAvailableRooms()[random.nextInt(event.getAvailableRooms().length)]);
                } else {
                    event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
                }
            }
        }
        return candidate;
    }
}
