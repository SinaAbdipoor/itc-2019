package solver;

import dataset.Timetable;

public record Solution(String name, int runTime, int cores, String technique, String author, String institution, String country, Timetable timetable) {
    /**
 * Represents a solution for a scheduling problem.
 * This record holds information about the solution's characteristics and performance.
 *
 * @param name        The name of the instance.
 * @param runTime     The time taken to run the algorithm, in seconds.
 * @param cores       The number of CPU cores used for the algorithm.
 * @param technique   The technique used to generate the solution.
 * @param author      The author of the solution.
 * @param institution The institution associated with the author.
 * @param country     The country of the institution.
 * @param timetable   The timetable associated with this solution.
 * @throws IllegalArgumentException if the runTime is less than 1 second, the cores is less than 1, or the timetable is null.
 */
    public Solution{
        if (runTime < 1)
            throw new IllegalArgumentException("runTime must be at least 1 second.");
        if (cores < 1)
            throw new IllegalArgumentException("cores must be at least 1.");
        if (timetable == null)
            throw new IllegalArgumentException("timetable must not be null.");
    }
}
