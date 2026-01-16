package solver;

import java.util.HashMap;
import java.util.Random;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

public class FRLS extends LoggingAlgorithm {

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
	public FRLS(final ProblemInstance instance, final int maxSeconds, final int maxNfe) throws IllegalArgumentException {
		super("FRLS", instance, maxSeconds, maxNfe);
	}

	private static final void __inc(final HashMap<Long, long[]> H, final Long v) {
		if (H.containsKey(v)) {
			++H.get(v)[0];
		} else {
			H.put(v, new long[] { 1L });
		}
	}

	private static final long __get(final HashMap<Long, long[]> H, final Long v) {
		return H.get(v)[0];
	}

	/** Executes the main optimization loop for this algorithm. */
	@Override
	public void run(final Random random) {
		final HashMap<Long, long[]> H = new HashMap<>();

		Timetable best_x = this.createRandomTimetable(random);
		Long best_f = Long.valueOf(this.evaluate(best_x));

		while (!this.terminationReached()) {
			final Timetable new_x = this.step(random, best_x);
			final Long new_f = Long.valueOf(this.evaluate(new_x));
			FRLS.__inc(H, new_f);
			FRLS.__inc(H, best_f);
			if (FRLS.__get(H, new_f) <= FRLS.__get(H, best_f)) {
				best_f = new_f;
				best_x = new_x;
			}
		}
	}

	Timetable step(final Random random, final Timetable use_x) {
		final Timetable candidate = use_x.deepCopy(this.instance);
		final Class theClass = this.instance.classes()[random.nextInt(this.instance.classes().length)];
		final Event event = candidate.getEvent(theClass);
		if (theClass.possibleRooms() == null || random.nextInt(2) == 1) {
			event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
		} else if (event.getAvailableRooms() != null) {
			event.setRoomAssignment(event.getAvailableRooms()[random.nextInt(event.getAvailableRooms().length)]);
		} else {
			event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
		}
		return candidate;
	}

	public static final void main(final String[] args) {
		final HashMap<Long, long[]> H = new HashMap<>();

		FRLS.__inc(H, Long.valueOf(1));
		System.out.println(FRLS.__get(H, Long.valueOf(1)));

		FRLS.__inc(H, Long.valueOf(1));
		FRLS.__inc(H, Long.valueOf(1));
		System.out.println(FRLS.__get(H, Long.valueOf(1)));

		FRLS.__inc(H, Long.valueOf(2));
		FRLS.__inc(H, Long.valueOf(2));
		System.out.println(FRLS.__get(H, Long.valueOf(2)));

		FRLS.__inc(H, Long.valueOf(1));
		FRLS.__inc(H, Long.valueOf(1));
		System.out.println(FRLS.__get(H, Long.valueOf(1)));
	}
}
