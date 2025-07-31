package solver;

import dataset.Class;
import dataset.Event;
import dataset.ProblemInstance;
import dataset.Timetable;

import java.util.Random;

/**
 * Abstract base class for all optimization algorithms in the ITC 2019 project.
 * Subclasses must implement initialization and a single search step.
 */
public abstract class Algorithm {
    private final String name;
    private final ProblemInstance instance;
    private final int maxSeconds;
    private final int maxNfe;
    private Timetable solution;
    private int nfe = 0;
    private boolean feasible = false;
    private int secondsToFeasibility = -1;
    private int nfeToFeasibility = -1;
    private int cost = -1;

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
    public Algorithm(String name, ProblemInstance instance, int maxSeconds, int maxNfe) throws IllegalArgumentException {
        this.name = name;
        this.instance = instance;
        if (maxSeconds < 1 && maxSeconds != -1)
            throw new IllegalArgumentException("maxSeconds must be positive or -1 for unlimited time.");
        this.maxSeconds = maxSeconds;
        if (maxNfe < 1 && maxNfe != -1)
            throw new IllegalArgumentException("maxNfe must be positive or -1 for unlimited evaluations.");
        this.maxNfe = maxNfe;
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
    abstract Timetable initialize();

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
    abstract Timetable step();

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
    public Timetable run() {
        long startTime = System.currentTimeMillis();
        long printOverhead = 0L;

        replaceSolution(initialize());

        if (feasible) {
            secondsToFeasibility = 0;
            nfeToFeasibility = nfe;
        }

        while (!terminationReached(startTime, printOverhead)) {
            long beforePrint = System.currentTimeMillis();
            System.out.println(this);
            printOverhead += System.currentTimeMillis() - beforePrint;

            if (feasible && secondsToFeasibility < 0) {
                secondsToFeasibility = elapsedSeconds(startTime, printOverhead);
                nfeToFeasibility = nfe;
            }
            replaceSolution(step());
        }
        return solution;
    }

    /**
     * Replaces the current best solution with the given timetable if it is better.
     * The replacement occurs if the new timetable is feasible and the current is not,
     * or if the new timetable has a lower cost.
     *
     * @param candidate the new {@link Timetable} candidate solution
     */
    private void replaceSolution(Timetable candidate) {
        boolean candidateFeasible = candidate.isFeasible(instance);
        int candidateCost = candidate.calcCost(instance);
        nfe++;
        if ((cost == -1) || (candidateFeasible && !feasible) || (candidateFeasible == feasible && candidateCost < cost)) {
            solution = candidate;
            feasible = candidateFeasible;
            cost = candidateCost;
        }
    }

    /**
     * Checks whether the termination condition for the algorithm has been reached.
     * <p>
     * The search terminates if either the maximum number of function evaluations ({@code maxNfe})
     * or the maximum allowed time in seconds ({@code maxSeconds}) has been reached.
     * The elapsed time excludes time spent on printing/reporting.
     * </p>
     *
     * @param startTime     The timestamp (in milliseconds) when the search started.
     * @param printOverhead The total time (in milliseconds) spent on printing/reporting so far.
     * @return {@code true} if the termination condition is met; {@code false} otherwise.
     */
    private boolean terminationReached(long startTime, long printOverhead) {
        if (maxNfe > 0 && nfe >= maxNfe) return true;
        if (maxSeconds > 0 && elapsedSeconds(startTime, printOverhead) >= maxSeconds) return true;
        return false;
    }

    /**
     * Calculates the elapsed time in seconds since the start of the algorithm,
     * excluding the total time spent on printing or reporting.
     *
     * @param startTime     The timestamp (in milliseconds) when the search started.
     * @param printOverhead The total time (in milliseconds) spent on printing/reporting so far.
     * @return The elapsed time in seconds, excluding print/report overhead.
     */
    private int elapsedSeconds(long startTime, long printOverhead) {
        return (int) ((System.currentTimeMillis() - startTime - printOverhead) / 1000L);
    }

    @Override
    public String toString() {
        return "Algorithm{" + "name='" + name + '\'' + ", instance=" + instance.instanceName() + ", maxSeconds=" + maxSeconds + ", solution=" + /*solution +*/ ", nfe=" + nfe + " / " + maxNfe + ", feasible=" + feasible + ", secondsToFeasibility=" + secondsToFeasibility + ", nfeToFeasibility=" + nfeToFeasibility + ", cost=" + cost + '}';
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
    protected Timetable createRandomTimetable() {
        Random random = new Random();
        Class theClass;
        Timetable randomTimetable = new Timetable(instance.classes());
        for (Event event : randomTimetable.getEvents()) {
            theClass = event.getTheClass();
            event.setTimeAssignment(theClass.possibleTimes()[random.nextInt(theClass.possibleTimes().length)]);
            if (theClass.possibleRooms() != null)
                event.setRoomAssignment(theClass.possibleRooms()[random.nextInt(theClass.possibleRooms().length)]);
        }
        return randomTimetable;
    }
}