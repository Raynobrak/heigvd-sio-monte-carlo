package montecarlo;

import statistics.StatCollector;

import java.util.Random;

public class IntervalThresholdExperiment implements Experiment {
    Experiment exp;
    long N;
    double threshold;
    double expectedValue;

    public IntervalThresholdExperiment(Experiment exp, long N, double threshold, double expectedValue) {
        this.exp = exp;
        this.N = N;
        this.threshold = threshold;
        this.expectedValue = expectedValue;
    }

    @Override
    public double execute(Random rnd) {
        var stat = new StatCollector();
        MonteCarloSimulation.simulateNRuns(exp, N, rnd, stat);

        double value = stat.getAverage();
        double interval = stat.getConfidenceIntervalHalfWidth(threshold);
        if(expectedValue > value - interval && expectedValue < value + interval)
            return 1.;
        return 0.;
    }
}
