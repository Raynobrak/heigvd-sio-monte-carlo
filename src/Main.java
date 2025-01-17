import montecarlo.*;
import statistics.*;

import java.util.Random;

// Juste pour l'exemple
class FairCoinTossExperiment implements Experiment {
	public double execute(Random rnd) {
		return rnd.nextDouble() < 0.5 ? 1.0 : 0.0;
	}
}

public class Main {

	private static void RunSimulation1() {
		Random rnd = new Random();
		rnd.setSeed(0x134D6EE);

		int K = 23;
		int M = 2;
		int Y = 365;
		Experiment exp = new SameBirthdayExperiment(K,M,Y);

		double level = 0.95;
		double maxHalfWidth = 10e-4;
		int initialNumberOfRuns = (int)10e6;
		int additionalNumberOfRuns = (int)10e5;

		// Point 1)

		for(int i = 0; i < 3; ++i) {
			var stat = new StatCollector();
			MonteCarloSimulation.simulateTillGivenCIHalfWidth(exp, level, maxHalfWidth, initialNumberOfRuns, additionalNumberOfRuns, rnd, stat);

			System.out.printf("**********************%n  Simulation results%n**********************%n");
			System.out.printf("Max half width: %.5f%n", maxHalfWidth);
			System.out.printf("Number of experiment runs: %d%n", stat.getNumberOfObs());
			System.out.printf("K=%d, M=%d, Y=%d%n", K,M,Y);
			System.out.printf("Estimated p23 :    %.5f%n", stat.getAverage());
			System.out.printf("Confidence interval (95%%):  %.5f +/- %.5f%n", stat.getAverage(), stat.getConfidenceIntervalHalfWidth(0.95));

			maxHalfWidth /= 2;
		}
	}

	private static void RunSimulation2() {
		int K = 23;
		int M = 2;
		int Y = 365;
		Experiment birthdayExperiment = new SameBirthdayExperiment(K,M,Y);

		var rnd = new Random(0x134D6EE);

		int numberOfInterval = 100;
		long N = (long)10e4; // todo : remettre la bonne valeur après
		long numberOfIntervalThatFitIn = 0;
		double threshold = 0.95;
		final double expectedValue = 0.5072972343;

		var experiment = new IntervalThresholdExperiment(birthdayExperiment, N, threshold, expectedValue);
		var stat = new StatCollector(); // todo : on s'est arrêté ici
		MonteCarloSimulation.simulateNRuns(experiment, numberOfInterval, rnd, stat);
		System.out.println(stat.getAverage());
		/*for(int i = 0; i < numberOfInterval; ++i) {
			int success = (int)experiment.execute(rnd);
			numberOfIntervalThatFitIn += success;

			if(i % 10 == 0)
				System.out.printf("%d/%d\n", i, numberOfInterval);
		}*/
		//System.out.printf("Empiric cover threshold : %.3f", numberOfIntervalThatFitIn / (double)numberOfInterval);
	}

	private static void RunSimulation3() {

	}

	public static void main(String[] args) {
		RunSimulation2();
	}
}
