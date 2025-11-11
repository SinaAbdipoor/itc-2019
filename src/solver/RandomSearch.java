package solver;

import java.util.Random;

import dataset.ProblemInstance;
import dataset.Timetable;

/**
 * Implements the Random Search algorithm for solving the timetabling problem.
 * This class extends the abstract Algorithm class and generates random
 * candidate solutions.
 */
public class RandomSearch extends Algorithm {
	/**
	 * Constructs a RandomSearch algorithm instance.
	 *
	 * @param instance   The problem instance to solve.
	 * @param maxSeconds The maximum allowed runtime in seconds.
	 * @param maxNfe     The maximum number of function evaluations.
	 * @throws IllegalArgumentException if any parameter is invalid.
	 */
	public RandomSearch(ProblemInstance instance, int maxSeconds, int maxNfe) throws IllegalArgumentException {
		super("Random Search", instance, maxSeconds, maxNfe);
	}

	@Override
	Timetable initialize(final Random random) {
		return createRandomTimetable(random);
	}

	@Override
	Timetable step(final Random random) {
		return createRandomTimetable(random);
	}
}