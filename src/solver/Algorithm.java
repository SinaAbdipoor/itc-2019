package solver;

import java.util.Random;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

/**
 * Abstract base class for all optimization algorithms in the ITC 2019 project.
 * Subclasses must implement initialization and a single search step.
 */
public abstract class Algorithm extends LoggingAlgorithm {
    /**
     * Constructor for algorithms with time and evaluation limits.
     * Use -1 for unlimited time or evaluations.
     *
     * @param name       Name of the algorithm.
     * @param instance   Problem instance to solve.
     * @param maxSeconds Maximum allowed seconds for the algorithm (-1 for unlimited).
     * @param maxNfe     Maximum allowed function evaluations (-1 for unlimited).
     * @throws IllegalArgumentException if limits are invalid.
     */
    public Algorithm(String name, ProblemInstance instance, long maxSeconds, long maxNfe) throws IllegalArgumentException {
    	super(name, instance, maxSeconds, maxNfe);
    }

    /**
     * Initializes the algorithm and generates the initial solution.
     * <p>
     * Subclasses must implement this method to create a complete and valid {@link Timetable}
     * for the given {@link ProblemInstance}. The returned timetable should assign every class
     * to a room and time slot, ensuring that the initial solution is fully specified. If the initial solution should be
     * random, use the {@code createRandomTimetable()} method. There is no need to check feasibility or cost
     * in this method, as the algorithm will handle that during the search process.
     * </p>
     *
     * @return a complete initial {@link Timetable} solution for the problem instance
     */
    abstract Timetable initialize(Random random);

    /**
     * Performs a single search step and returns a new {@link Timetable} candidate solution.
     * <p>
     * Subclasses must implement this method to generate and return a new timetable based on the current best solution.
     * The timetable created in this method must be independent and must not modify or interfere with the current best
     * timetable ({@code solution}). Implementations should use a deep copy of the current solution before making
     * changes. There is no need to check feasibility or cost in this method, as the algorithm will handle that
     * during the search process. However, if there are cost evaluation checks involved while generating the candidate
     * solution, NFE should be incremented accordingly.
     * </p>
     *
     * @return a new {@link Timetable} candidate solution for evaluation.
     */
    abstract Timetable step(Random random);

    /**
     * Executes the main optimization loop for this algorithm.
     * <p>
     * This method initializes the search, then repeatedly performs search steps until the termination
     * condition is met (such as time or evaluation limits). At each iteration, it prints search
     * progress, updates feasibility and cost information, and replaces the current best solution
     * if a better candidate is found. The method returns the best {@link Timetable} found.
     * <br>
     * <b>Note:</b> Subclasses must ensure that the {@code initialize()} and {@code step()} methods
     * are implemented correctly, and that the candidate solutions do not interfere with the current
     * best solution.
     * </p>
     *
     * @return the best {@link Timetable} solution found during the search
     */
    @Override
	public void run(final Random random) {
        replaceSolution(initialize(random));
        while (!terminationReached()) {
            replaceSolution(step(random));
        }
    }

    /**
     * Replaces the current best solution with the given timetable if it is better.
     * The replacement occurs if the new timetable is feasible and the current is not,
     * or if the new timetable has a lower cost.
     *
     * @param candidate the new {@link Timetable} candidate solution
     */
    final void replaceSolution(Timetable candidate) {
    	this.evaluate(candidate);
    }

    /**
     * Generates a random timetable for the current problem instance.
     * <p>
     * This basic generator assigns each event a random valid time slot and a random valid room (if needed) from the
     * possible options for its class. It does not consider student assignments. This method is intended for generating
     * initial solutions or for random sampling in optimization algorithms.
     * </p>
     *
     * @return a {@link Timetable} with random time and room assignments for all events
     */
    @Override
	protected Timetable createRandomTimetable(Random random) {
        Timetable randomTimetable = new Timetable(instance.classes());
        for (Class theClass : instance.classes()) {
            Event event = randomTimetable.getEvent(theClass);
            event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
            if (theClass.possibleRooms() != null) {
                if (event.getAvailableRooms() != null) {
					event.setRoomAssignment(event.getAvailableRooms()[random.nextInt(event.getAvailableRooms().length)]);
				} else {
					event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
				}
            }
        }
        return randomTimetable;
    }
}