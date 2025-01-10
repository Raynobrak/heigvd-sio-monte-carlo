package montecarlo;

import java.util.Hashtable;
import java.util.Random;

public class SameBirthdayExperiment implements Experiment {
    private int K;
    private int M;
    private int Y;

    /**
     * @param K Nombre de personnes dans le groupe
     * @param M Nombre de dates possibles
     * @param Y Nombre d'anniversaires communs pour que l'expérience "réussisse"
     */
    public SameBirthdayExperiment(int K, int M, int Y) {
        this.K = K;
        this.M = M;
        this.Y = Y;
    }

    private int randomDay(Random rnd) {
        return rnd.nextInt(0, M);
    }

    @Override
    public double execute(Random rnd) {
        Hashtable<Integer, Integer> frequencies = new Hashtable<Integer, Integer>();

        for(int i = 0; i < K; ++i) {
            int day = randomDay(rnd);
            int count = frequencies.containsKey(day) ? frequencies.get(day) + 1 : 1;

            if(count >= Y)
                return 1.;

            frequencies.put(day, count);
        }

        return 0.;
    }
}
