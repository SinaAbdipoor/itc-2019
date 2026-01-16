package solver;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

import java.util.Random;

public class HillClimbingWithRS_3 extends LoggingAlgorithm {

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
	public HillClimbingWithRS_3(ProblemInstance instance, int maxSeconds, int maxNfe) throws IllegalArgumentException {
		super("BigJump Hill Climbing", instance, maxSeconds, maxNfe);
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

		Timetable best_x = null;
		long best_f = Long.MAX_VALUE;
		long luby_idx = 0;
		long stuck_degree = 0L;
		boolean lock = false;
		long lock_times = 0L;
		int num_classes = instance.classes().length;
		int factor = 50;
		long baseThreshold = (long) num_classes * factor;
		long benchmark = best_f;

		best_x = createRandomTimetable(random);
		best_f = this.evaluate(best_x);

		while (!terminationReached()) {
			if (!lock) {
				Timetable new_x = this.step(random, best_x);
				long new_f = this.evaluate(new_x);
				if (new_f <= best_f) {
					if (new_f < best_f)
						stuck_degree = 0L;
					else {
						stuck_degree++;
					}
					best_f = new_f;
					best_x = new_x;
				}
				if (stuck_degree >= baseThreshold * luby(luby_idx + 1))
					lock = true;
			}
			else {
				if (best_f < benchmark) {
					luby_idx = 0;
					benchmark = best_f;
				} else {
					luby_idx++;
				}
				// Reset & improve
				lock_times++;
				lock = false;
				stuck_degree = 0L;

				best_x = BigStep(random, best_x, luby(luby_idx + 1));
				best_f = this.evaluate(best_x);

				System.out.printf("[Restart #%d] Factor=%d, Base=%d, LubyNext=%d, LubyId=%d%n",
						lock_times, factor, baseThreshold, luby(luby_idx + 1), luby_idx);
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

	Timetable BigStep(final Random random, Timetable use_x, long currentLuby) {
		Timetable candidate = use_x.deepCopy(instance);
		boolean first = true;
		long strength = 5L + (long)(Math.log(2*currentLuby) / Math.log(2));

		while (first || random.nextInt((int)strength) > 0) {
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

	public static final void main(String[] args) {
		for (int lubytest = 1; lubytest <= 10; lubytest++) {
			System.out.println("" + lubytest + ": " + luby(lubytest));
		}
	}
}
