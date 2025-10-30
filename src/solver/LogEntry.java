package solver;


/**One log entry collected during an algorithm run. */
public record LogEntry(long fes, long time, long f) {
    /**
     * Construct a log entry.
     *
     * @param fes the number of consumed objective function evaluations
     * @param time the time when the log entry was collected
     * @param f the objective value
     * @throws IllegalArgumentException If the passed penalty is invalid.
     */
    public LogEntry {
        if (fes <= 0) {
			throw new IllegalArgumentException("Fes must be positive");
		}
        if (time < 0) {
			throw new IllegalArgumentException("cannot be < 0");
		}
        if (f < 0) {
			throw new IllegalArgumentException("f must be >= 0");
		}
    }
}