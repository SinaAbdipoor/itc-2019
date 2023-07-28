package itc2019.dataset;

import java.util.Arrays;

/**
 * <p>This class represents time as defined in the ITC 2019. Each problem has a certain number of weeks nrWeeks, a
 * number of days nrDays in each week, and a number of time slots per day slotsPerDay. These parameters, together with
 * the instance name, are defined in the root element of the XML file.</p>
 * <p>While the data format allows for variations, in all the competition instances one time slot takes 5 minutes, which
 * allows us to model travel times as well as classes that meet at irregular times. There are 288 time slots covering
 * the whole day, from midnight to midnight.</p>
 * <p>A class can meet several times a week during some or all weeks of the semester. All meetings of a class start at
 * the same time, run for the same number of slots, and are placed in the same room. Each time is specified by its
 * starting time slot start within a day and have a duration length given as a number of time slots. Days and weeks with
 * a meeting are specified using the days and weeks binary strings. For example, days 1010100 means that the class meets
 * three times a week (on Monday, Wednesday, and Friday) each week when it is meeting. Similarly, weeks 0101010101010
 * specifies that the class would only meet during even weeks of the semester (during the 2nd, 4th, . . . , and 12th
 * weeks of the semester).</p>
 */
class Time {
    private final boolean[] weeks, days;
    private final int start, length;

    /**
     * Constructs a Time object.
     *
     * @param weeks  The semester weeks when the class takes place.
     * @param days   The days of the week when the class takes place.
     * @param start  The starting time slot of the class (each time slot is 5 mins and there are 288 time slots in a day).
     * @param length The duration of the class in time slots (each time slot is 5 mins and there are 288 time slots in a day).
     * @throws IllegalArgumentException If the input days, start, or length parameters are invalid.
     */
    Time(boolean[] weeks, boolean[] days, int start, int length) throws IllegalArgumentException {
        this.weeks = weeks;
        if (days.length > 7)
            throw new IllegalArgumentException("There cannot be more than 7 days in a week! " + days.toString());
        this.days = days;
        if (start < 0 || length <= 0 || (start + length > 288))
            throw new IllegalArgumentException("This start (" + start + ") and/or length (" + length + ") " + "parameter(s) are illegal!");
        this.start = start;
        this.length = length;
    }

    /**
     * Gets the semester weeks of this Time.
     *
     * @return A boolean string indicating on which weeks of the semester this Time takes place.
     */
    boolean[] getWeeks() {
        return weeks;
    }

    /**
     * Gets the days of this Time.
     *
     * @return A boolean string indicating on which days of a week this Time takes place.
     */
    boolean[] getDays() {
        return days;
    }

    /**
     * Gets the start time.
     *
     * @return The starting time slot of this Time.
     */
    int getStart() {
        return start;
    }

    /**
     * Gets the time length.
     *
     * @return The length (in time slots) of this Time.
     */
    int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Time{" + "weeks=" + Arrays.toString(weeks) + ", days=" + Arrays.toString(days) + ", start=" + start + ", length=" + length + '}';
    }

    /**
     * Gets the end of this Time.
     *
     * @return The ending time slot of this Time.
     */
    int calcEnd() {
        return start + length;
    }
}