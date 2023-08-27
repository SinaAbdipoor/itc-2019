package itc2019.dataset.constraints;

import itc2019.dataset.Class;
import itc2019.dataset.Event;
import utils.LogicalOperators;

/**
 * <p>Any two classes in this constraint must have some overlap in time.</p>
 * <p>Given classes overlap in time. Two classes overlap in time when they overlap in time of day, days of the week, as
 * well as weeks. This means that
 * (Cj.start < Ci.end) ∧ (Ci.start < Cj.end) ∧ ((Ci.days and Cj.days) ≠ 0) ∧ ((Ci.weeks and Cj.weeks) ≠ 0) for any two
 * classes Ci and Cj from the constraint, doing binary "and" between days and weeks of Ci and Cj.</p>
 */
class Overlap extends PairDistributionConstraint {
    /**
     * Constructs an overlap distribution constraint object with the given classes.
     *
     * @param classes Classes that this distribution constraint applies to.
     */
    Overlap(Class[] classes) {
        super(classes);
    }

    @Override
    boolean check(Event e1, Event e2) throws NullPointerException {
        // (Cj.start < Ci.end) ∧ (Ci.start < Cj.end) ∧ ((Ci.days and Cj.days) ≠ 0) ∧ ((Ci.weeks and Cj.weeks) ≠ 0)
        return (e2.getTimeAssignment().time().start() < e1.getTimeAssignment().time().getEnd())
                && (e1.getTimeAssignment().time().start() < e2.getTimeAssignment().time().getEnd())
                && (!LogicalOperators.areExclusive(e1.getTimeAssignment().time().days(), e2.getTimeAssignment().time().days()))
                && (!LogicalOperators.areExclusive(e1.getTimeAssignment().time().weeks(), e2.getTimeAssignment().time().weeks()));
    }
}