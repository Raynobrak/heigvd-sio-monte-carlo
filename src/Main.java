import montecarlo.*;
import statistics.*;

import java.util.Random;

public class Main {

	private static void RunSimulation1() {
		Random rnd = new Random(0x134D6EE);

		final int K = 23;
		final int M = 2;
		final int Y = 365;
		Experiment birthdayExp = new SameBirthdayExperiment(K,M,Y);

		final double LEVEL = 0.95;
		final long INITIAL_NB_RUNS = (long)10e6;
		final long ADDITIONAL_NB_RUNS = (long)10e5;
		final int NB_HALFWIDTH_TESTS = 3;
		double maxHalfWidth = 10e-4;

		for(int i = 0; i < NB_HALFWIDTH_TESTS; ++i, maxHalfWidth /= 2) {
			var stat = new StatCollector();
			MonteCarloSimulation.simulateTillGivenCIHalfWidth(birthdayExp, LEVEL, maxHalfWidth, INITIAL_NB_RUNS, ADDITIONAL_NB_RUNS, rnd, stat);

			System.out.printf("****************************%n  Simulation 1 results %d/%d%n****************************%n", i+1, NB_HALFWIDTH_TESTS);
			System.out.printf("Birthday experiment parameters : K=%d, M=%d, Y=%d%n", K,M,Y);
			System.out.printf("Max half width 				  : %.5f%n", maxHalfWidth);
			System.out.printf("Number of experiment runs 	  : %d%n", stat.getNumberOfObs());
			System.out.printf("Estimated p23 value			  : %.5f%n", stat.getAverage());
			System.out.printf("Confidence Interval (95%%)	  : %.5f +/- %.5f%n", stat.getAverage(), stat.getConfidenceIntervalHalfWidth(LEVEL));
		}
	}

	private static void RunSimulation2() {
		var rnd = new Random(0x134D6EE);

		final int K = 23;
		final int M = 2;
		final int Y = 365;
		Experiment birthdayExperiment = new SameBirthdayExperiment(K,M,Y);

		final long NB_INTERVALS = 1000;
		final long NB_RUNS = (long)10e6;
		final double LEVEL = 0.95;
		final double EXPECTED_VALUE = 0.5072972343;

		var intervalsExperiment = new IntervalThresholdExperiment(birthdayExperiment, NB_RUNS, LEVEL, EXPECTED_VALUE);

		var stat = new StatCollector();
		MonteCarloSimulation.simulateNRuns(intervalsExperiment, NB_INTERVALS, rnd, stat);

		System.out.printf("*************************%n  Simulation 2 results %n*************************%n");
		System.out.printf("Birthday experiment parameters : K=%d, M=%d, Y=%d%n", K,M,Y);
		System.out.printf("Target p23 value               : %.10f%n", EXPECTED_VALUE);
		System.out.printf("Sample size per interval       : %d%n", NB_RUNS);
		System.out.printf("Number of intervals tested     : %d%n", NB_INTERVALS);
		System.out.printf("Confidence interval coverage   : %.5f +/- %.5f%n", stat.getAverage(), stat.getConfidenceIntervalHalfWidth(LEVEL));
	}

	private static void RunSimulation3() {
		Random rnd = new Random(0x134D6EE);
		final int M = 3;
		final int Y = 365;
		final int MIN_K = 80;
		final int MAX_K = 100;
		final long NB_RUNS = (long)10e6;
		final double MIN_AVG_THRESHOLD = 0.5;

		int K = MIN_K - 1;
		double average = 0;
		while(K <= MAX_K && average <= MIN_AVG_THRESHOLD) {
			Experiment exp = new SameBirthdayExperiment(++K,M,Y);
			var stat = new StatCollector();
			MonteCarloSimulation.simulateNRuns(exp, NB_RUNS, rnd, stat);
			average = stat.getAverage();

		}

		System.out.printf("*************************%n  Simulation 3 results %n*************************%n");
		System.out.printf("Min. avg. threshold     : %.5f%n", MIN_AVG_THRESHOLD);
		System.out.printf("Min. avg. value found   : %.5f%n", average);
		System.out.printf("Experiment parameters   : K=%d, M=%d, Y=%d%n", K,M,Y);

	}

	public static void main(String[] args) {
		RunSimulation1();
		RunSimulation2();
		RunSimulation3();
	}
}
