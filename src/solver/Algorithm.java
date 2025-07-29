package solver;

import dataset.ProblemInstance;

abstract class Algorithm {
    private final ProblemInstance instance;
    private final int maxSeconds;
    private final int secondsToFeasibility = -1;
    private final int nfeToFeasibility = -1;
    private int nfe = 0;
}