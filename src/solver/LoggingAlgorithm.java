package solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;
import io.Decoder;

/**
 * Abstract base class for all optimization algorithms in the ITC 2019 project.
 * Subclasses must implement initialization and a single search step.
 */
public abstract class LoggingAlgorithm {
	// the offset of the DTF
	private static final long DF_OFFSET = Integer.MAX_VALUE + 1L;
	public final String name;
	protected final ProblemInstance instance;
	private long endTime;
	private long startTime;
	private long seed;
	private long totalTime;
	private final long maxNfe;
	private final long maxMilliSeconds;
	private Timetable solution;
	private long nfe = 0;
	private long best = Long.MAX_VALUE;
	private final ArrayList<LogEntry> log = new ArrayList<>();

	/**
	 * Constructor for algorithms with time and evaluation limits. Use -1 for
	 * unlimited time or evaluations.
	 *
	 * @param name       Name of the algorithm.
	 * @param instance   Problem instance to solve.
	 * @param maxSeconds Maximum allowed seconds for the algorithm (-1 for
	 *                   unlimited).
	 * @param maxNfe     Maximum allowed function evaluations (-1 for unlimited).
	 * @throws IllegalArgumentException if limits are invalid.
	 */
	public LoggingAlgorithm(String name, ProblemInstance instance, long maxSeconds, long maxNfe)
			throws IllegalArgumentException {
		this.name = name;
		this.instance = instance;
		if (maxSeconds < 1L && maxSeconds != -1L) {
			throw new IllegalArgumentException("maxSeconds must be positive or -1 for unlimited time.");
		}
		this.maxMilliSeconds = maxSeconds <= 0L ? Long.MAX_VALUE : Math.multiplyExact(maxSeconds, 1000L);
		if (maxNfe < 1 && maxNfe != -1) {
			throw new IllegalArgumentException("maxNfe must be positive or -1 for unlimited evaluations.");
		}
		this.maxNfe = (maxNfe <= 0L) ? Long.MAX_VALUE : maxNfe;
		if ((this.maxNfe >= 1_000_000_000_000L) && (this.maxMilliSeconds >= 1_000_000_000_000L)) {
			throw new IllegalArgumentException("run will take too long");
		}
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
	 * @param seed the random seed to use
	 * @return the best {@link Timetable} solution found during the search
	 */
	public Timetable run(long seed) {
		// set up the time limits
		this.startTime = System.currentTimeMillis();
		this.endTime = this.maxMilliSeconds + this.startTime;
		if ((this.endTime < this.maxMilliSeconds) || (this.endTime < this.startTime)) {
			this.endTime = Long.MAX_VALUE;
		}
		this.nfe = 0L;
		this.solution = null;
		this.seed = seed;
		this.log.clear();

		this.run(new Random(seed));
		this.totalTime = System.currentTimeMillis() - this.startTime;

		return solution;
	}

	protected void run(Random random) {
		// Here goes the actual algorithm, using "evaluate" and "terminationReached"
	}

	/**
	 * The entry point for the code. This invokes the run method with a predefined
	 * seed.
	 */
	public final Timetable run() {
		try {
			this.run(new Random().nextLong());
		} finally {
			this.writeLog(null);
		}
		return this.solution;
	}

	/**
	 * Evaluate a candidate Solution. This function returns an objective value based
	 * on DTF and cost, depending on the situation. It will update the internal
	 * counters and remember the new best solution, if need be.
	 *
	 * @param candidate the candidate solution.
	 * @return the objective value
	 */
	protected final long evaluate(Timetable candidate) {
		final int dtf = candidate.isFeasible(instance);
		final long f = (dtf <= 0) ? candidate.calcCost(this.instance) : dtf + DF_OFFSET;
		final long fes = this.nfe += 1L;
		if (f <= this.best) { // copy solution also if it is equally good!
			this.solution = candidate.deepCopy(this.instance);
			if (f < this.best) {
				this.best = f;
				this.log.add(new LogEntry(fes, System.currentTimeMillis(), f));
			}
		}
		return f;
	}

	/**
	 * Checks whether the termination condition for the algorithm has been reached.
	 *
	 * @return {@code true} if the termination condition is met; {@code false}
	 *         otherwise.
	 */
	protected final boolean terminationReached() {
		return ((nfe >= maxNfe) || (System.currentTimeMillis() >= this.endTime));
	}

	@Override
	public String toString() {
		return "Algorithm{" + "name='" + name + '\'' + ", instance=" + instance.instanceName() + ", best=" + best
				+ ", maxSeconds=" + maxMilliSeconds + /* ", solution=" + solution + */ ", nfe=" + nfe + " / " + maxNfe
				+ ", feasible=" + (this.best < DF_OFFSET) + '}';
	}

	/**
	 * Generates a random timetable for the current problem instance.
	 * <p>
	 * This basic generator assigns each event a random valid time slot and a random
	 * valid room (if needed) from the possible options for its class. It does not
	 * consider student assignments. This method is intended for generating initial
	 * solutions or for random sampling in optimization algorithms.
	 * </p>
	 *
	 * @return a {@link Timetable} with random time and room assignments for all
	 *         events
	 */
	protected Timetable createRandomTimetable(final Random random) {
		Timetable randomTimetable = new Timetable(instance.classes());
		for (Class theClass : instance.classes()) {
			Event event = randomTimetable.getEvent(theClass);
			event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
			if (theClass.possibleRooms() != null) {
				if (event.getAvailableRooms() != null) {
					event.setRoomAssignment(
							event.getAvailableRooms()[random.nextInt(event.getAvailableRooms().length)]);
				} else {
					event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
				}
			}
		}
		return randomTimetable;
	}

	protected Timetable getSolution() {
		return solution.deepCopy(this.instance);
	}

	/**
	 * Write the log to a destination file.
	 *
	 * @param dest the path
	 */
	public final void writeLog(final String dest) {
		final String useDest;

		final String clazzName = this.getClass().getSimpleName().toLowerCase();

		if (dest == null) {
			String useInst = this.instance.instanceName();
			int eqIdx = useInst.indexOf('=');
			if (eqIdx > 0) {
				int commaIdx = useInst.indexOf(',');
				if (commaIdx > eqIdx) {
					useInst = useInst.substring(eqIdx + 1, commaIdx);
				}
			}

			useDest = clazzName + "_" + useInst + "_" + Long.toHexString(this.seed) + ".txt";
		} else {
			useDest = dest;
		}

		try {
			final File destFile = new File(useDest).getCanonicalFile();
			System.out.println("Now logging results to file " + destFile);

			try (final FileWriter fw = new FileWriter(destFile)) {
				try (final BufferedWriter bw = new BufferedWriter(fw)) {
					if (this.log.size() > 0) {
						bw.write("fes;time;f");
						bw.newLine();
						for (final LogEntry line : this.log) {
							bw.write(String.valueOf(line.fes()) + ";" + String.valueOf(line.time() - this.startTime)
									+ ";" + String.valueOf(line.f()));
							bw.newLine();

						}
					} else {
						bw.write("EMPTY LOG!!!");
						bw.newLine();
					}
					bw.write("#");
					bw.newLine();
					bw.write("# name: " + this.name);
					bw.newLine();
					bw.write("# algorithm_class: " + this.getClass().getCanonicalName());
					bw.newLine();
					bw.write("# instance: " + this.instance.instanceName());
					bw.newLine();
					bw.write("# seed: " + this.seed);
					bw.newLine();

					bw.write("# bestF: " + this.best);
					bw.newLine();
					bw.write("# DTF: " + ((this.best >= DF_OFFSET) ? (this.best - DF_OFFSET) : 0));
					bw.newLine();
					bw.write("# cost: " + ((this.best < DF_OFFSET) ? this.best
							: ((this.solution != null) ? this.solution.calcCost(this.instance) : Long.MAX_VALUE)));
					bw.newLine();

					bw.write("# startTime: " + this.startTime);
					bw.newLine();
					bw.write("# totalTime: " + this.totalTime);
					bw.newLine();
					bw.write("# totalFEs: " + this.nfe);
					bw.newLine();
					bw.write("# maxFEs: " + this.maxNfe);
					bw.newLine();
					bw.write("# maxMilliSeconds: " + this.maxMilliSeconds);
					bw.newLine();
					bw.write("# maxSeconds: " + (this.maxMilliSeconds / 1000L));
					bw.newLine();
					if (this.solution != null) {
						bw.write("#");
						bw.newLine();
						new Decoder(new Solution(this.instance.instanceName(), (int) (this.maxMilliSeconds / 1000L), 1,
								clazzName, "ScholORs", "Bejing/Hefei/Malaysia", "China and Malaysia",
								this.solution), bw);
					}
				}
			}

			System.out.println("Done logging results to file " + destFile);
		} catch (Exception ie) {
			ie.printStackTrace();
		}
	}

}