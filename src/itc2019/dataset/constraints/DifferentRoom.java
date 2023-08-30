package itc2019.dataset.constraints;

import itc2019.dataset.Class;
import itc2019.dataset.Event;

/**
 * <p>All classes in this constraint must be assigned to different rooms.</p>
 * <p>Given classes should be placed in different rooms. This means that (Ci.room ≠ Cj.room) for any two classes Ci and
 * Cj from the constraint.</p>
 */
class DifferentRoom extends PairDistributionConstraint {
    /**
     * Constructs a paired different room distribution constraint object with the given classes.
     *
     * @param classes Classes that this distribution constraint applies to.
     */
    DifferentRoom(Class[] classes) {
        super(classes);
    }

    @Override
    boolean check(Event e1, Event e2) throws NullPointerException {
        // (Ci.room ≠ Cj.room)
        return e1.getRoomAssignment().room() != e2.getRoomAssignment().room();
    }
}