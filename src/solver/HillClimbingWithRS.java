package solver;

import java.util.Random;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

public class HillClimbingWithRS extends LoggingAlgorithm {

	/**
	 * Constructor for algorithms with time and evaluation limits. Use -1 for
	 * unlimited time or evaluations.
	 *
	 * @param instance   Problem instance to solve.
	 * @param maxSeconds Maximum allowed seconds for the algorithm (-1 for
	 *                   unlimited).
	 * @param maxNfe     Maximum allowed function evaluations (-1 for unlimited).
	 * @throws IllegalArgumentException if limits are invalid.
	 */
	public HillClimbingWithRS(ProblemInstance instance, int maxSeconds, int maxNfe) throws IllegalArgumentException {
		super("Hill Climbing RS", instance, maxSeconds, maxNfe);
	}

	/**
	 * Executes the main optimization loop for this algorithm.
	 * <p>
	 * This method initializes the search, then repeatedly performs search steps
	 * until the termination condition is met (such as time or evaluation limits).
	 * At each iteration, it prints search progress, updates feasibility and cost
	 * information, and replaces the current best solution if a better candidate is
	 * found. The method returns the best {@link Timetable} found. <br>
	 * <b>Note:</b> Subclasses must ensure that the {@code initialize()} and
	 * {@code step()} methods are implemented correctly, and that the candidate
	 * solutions do not interfere with the current best solution.
	 * </p>
	 *
	 * @return the best {@link Timetable} solution found during the search
	 */
	@Override
	public void run(final Random random) {
		long fes_until_reset = 0L;
		Timetable best_x = null;
		long best_f = Long.MAX_VALUE;
		long luby_idx = 0;

		while (!terminationReached()) {
			if (--fes_until_reset <= 0L) {
				best_x = createRandomTimetable(random);
				best_f = this.evaluate(best_x);
				fes_until_reset = 100000L * luby(++luby_idx);
			} else {
				Timetable new_x = this.step(random, best_x);
				long new_f = this.evaluate(best_x);
				if (new_f <= best_f) {
					best_f = new_f;
					best_x = new_x;
				}
			}
		}
	}

	/**
	 * compute the luby restart length for the run at the given index
	 *
	 * @param index the index
	 * @return the next multiplier for the number of FEs
	 */
	static final long luby(final long index) {
		long twoByK, twoByKMinusOne;

		for (twoByK = 1;;) {
			twoByKMinusOne = twoByK;
			twoByK <<= 1L;
			if (twoByK < twoByKMinusOne) {
				return twoByKMinusOne;
			}
			if (index == (twoByK - 1L)) {
				return twoByKMinusOne;
			}
			if (index >= twoByK) {
				continue;
			}
			return luby((index - twoByKMinusOne) + 1);
		}
	}

	Timetable step(final Random random, Timetable use_x) {
		Timetable candidate = use_x.deepCopy(instance);
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

	public static final void main(String[] args) {
		for (int lubytest = 1; lubytest <= 10; lubytest++) {
			System.out.println("" + lubytest + ": " + luby(lubytest));
		}
	}
}
