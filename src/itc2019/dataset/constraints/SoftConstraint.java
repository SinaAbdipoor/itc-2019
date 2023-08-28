package itc2019.dataset.constraints;

/**
 * <p>This class represents a distribution constraint that is hard (required="true"). Soft constraints may not be
 * satisfied and there is a penalty for each violation. When any of the constraints that can be validated on pairs of
 * classes is soft, the provided penalty is incurred for every pair of classes of the constraint that are in a
 * violation. In other words, if M pairs of classes do not satisfy the distribution constraint, the total penalty for
 * violation of this constraint is M × penalty.</p>
 * <p>This class is basically the same as the {@link DistributionConstraint}. It only acts as a wrapper to create
 * separation between hard and soft constraints when instantiating in the {@link itc2019.dataset.ProblemInstance} for
 * further simplicity.</p>
 */
public record SoftConstraint(DistributionConstraint constraint, int penalty) {
    /**
     * Constructs a soft constraint object.
     *
     * @param constraint A distribution constraint.
     * @param penalty    The penalty for each violation of this soft constraint.
     * @throws IllegalArgumentException If the passed penalty is negative.
     */
    public SoftConstraint {
        if (penalty < 0) throw new IllegalArgumentException("Constraint penalty cannot be negative!");
    }

    /**
     * Calculates the penalty based on the given violation count.
     *
     * @param violationCount The number of constraint violations.
     * @return The calculated penalty, which is the product of penalty and violation count.
     * @throws IllegalArgumentException If the violationCount is negative.
     */
    int calcPenalty(int violationCount) throws IllegalArgumentException {
        //TODO OPTIMIZATION: For faster running time, comment the following if statements. However, doing so will allow for negative penalty. Only do so when you are sure the passed violationCount is not negative.
//        if (violationCount < 0)
//            throw new IllegalArgumentException("The number of constraint violations cannot be negative!");
        // COMMENT UNTIL HERE!
        return penalty * violationCount;
    }
}