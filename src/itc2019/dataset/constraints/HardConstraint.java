package itc2019.dataset.constraints;

/**
 * <p>This class represents a distribution constraint that is hard (required="true"). A candidate solution (timetable)
 * which does not satisfy ALL hard constraints is deemed infeasible and worthless in the ITC 2019 dataset.</p>
 * <p>This class is basically the same as the {@link DistributionConstraint}. It only acts as a wrapper to create
 * separation between hard and soft constraints when instantiating in the main class {@link ProblemInstance} for further
 * simplicity.</p>
 *
 * @param constraint A hard distribution constraint
 */
record HardConstraint(DistributionConstraint constraint) {
}