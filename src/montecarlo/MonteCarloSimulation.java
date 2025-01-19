package montecarlo;

import statistics.StatCollector;

import java.util.Random;

/**
 * This class provides methods for simple Monte Carlo simulations.
 */
public class MonteCarloSimulation {
	/**
	 * Private constructor. Makes it impossible to instantiate.
	 */
	private MonteCarloSimulation() {
	}

	/**
	 * Simulates experiment exp n times, using rnd as a source of pseudo-random numbers and collect
	 * the results in stat.
	 *
	 * @param exp  experiment to be run each time
	 * @param n    number of runs to be performed
	 * @param rnd  random source to be used to simulate the experiment
	 * @param stat collector to be used to collect the results of each experiment
	 */
	public static void simulateNRuns(Experiment exp,
									 long n,
									 Random rnd,
									 StatCollector stat) {
		for (long run = 0; run < n; ++run) {
			stat.add(exp.execute(rnd));
		}
	}

	/**
	 * First simulates experiment exp initialNumberOfRuns times, then estimates the number of runs
	 * needed for a 95% confidence interval half width no more than maxHalfWidth. If final C.I. is
	 * too wide, simulates additionalNumberOfRuns before recalculating the C.I. and repeats the process
	 * as many times as needed.
	 * <p>
	 * Uses rnd as a source of pseudo-random numbers and collects the results in stat.
	 *
	 * @param exp                    experiment to be run each time
	 * @param level                  confidence level of the confidence interval
	 * @param maxHalfWidth           maximal half width of the confidence interval
	 * @param initialNumberOfRuns    initial number of runs to be performed
	 * @param additionalNumberOfRuns additional number of runs to be performed if C.I. is too wide
	 * @param rnd                    random source to be used to simulate the experiment
	 * @param stat                   collector to be used to collect the results of each experiment
	 */
	public static void simulateTillGivenCIHalfWidth(Experiment exp,
													double level,
													double maxHalfWidth,
													long initialNumberOfRuns,
													long additionalNumberOfRuns,
													Random rnd,
													StatCollector stat) {
		//Write your code here
		final double Z_VALUE = 1.96;
		
		//1) On commence par réaliser N init simulations de l’expérience (voir la méthode simulate-NRuns).
		simulateNRuns(exp, initialNumberOfRuns, rnd, stat);

		// 2) À partir des données récoltées on calcule une estimation du nombre N de réalisations
		// à générer aﬁn d’obtenir un intervalle de conﬁance dont la demi-largeur ne dépasse
		// pas Δ max (voir la page 48 du cours). Cette valeur de N est ensuite arrondie, vers le
		// haut, au plus proche multiple de N add .
		double halfWidth = stat.getConfidenceIntervalHalfWidth(level);
		long NEstimate = (long) Math.ceil(Math.pow(Z_VALUE, 2) * Math.pow(stat.getStandardDeviation(), 2) / Math.pow(halfWidth, 2));
		NEstimate = (NEstimate / additionalNumberOfRuns) * additionalNumberOfRuns;

		// 3) La simulation est poursuivie jusqu’à atteindre N réalisations de l’expérience.
		if(NEstimate > initialNumberOfRuns) {
			simulateNRuns(exp, NEstimate - initialNumberOfRuns, rnd, stat);
		}

		// 4) Si la demi-largeur de l’intervalle de conﬁance, calculée sur la base de ces N réalisations,
		// est inférieure ou égale à Δ max le processus s’arrête. Sinon N add simulations supplé-
		// mentaires sont eﬀectuées avant de recalculer un nouvel intervalle de conﬁance et de
		// retester la condition d’arrêt. Ce processus est répété jusqu’à ce que la condition d’arrêt
		// soit satisfaite.
		halfWidth = stat.getConfidenceIntervalHalfWidth(level);
		while (halfWidth > maxHalfWidth) {
			simulateNRuns(exp, additionalNumberOfRuns, rnd, stat);
			halfWidth = stat.getConfidenceIntervalHalfWidth(level);
		}
	}
}
