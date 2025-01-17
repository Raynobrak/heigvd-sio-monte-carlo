package montecarlo;

import java.util.Hashtable;
import java.util.Random;

public class SameBirthdayExperiment implements Experiment {
    private int K;
    private int M;
    private int Y;
    private int[] frequencies;

    /**
     * @param K Nombre de personnes dans le groupe
     * @param M Nombre d'anniversaires communs pour que l'expérience "réussisse"
     * @param Y Nombre de dates possibles
     */
    public SameBirthdayExperiment(int K, int M, int Y) {
        this.K = K;
        this.M = M;
        this.Y = Y;

        // Le tableau des fréquences est un attribut de la classe (pour éviter d'avoir à réserver la mémoire à chaque exécution de l'expérience)
        this.frequencies = new int[Y];
    }

    private int randomDay(Random rnd) {
        return rnd.nextInt(0, Y);
    }

    @Override
    public double execute(Random rnd) {
        // On remet à zéro le tableau des fréquences
        for(int i = 0; i < Y; ++i)
            frequencies[i] = 0;

        for(int i = 0; i < K; ++i) {
            if(++frequencies[randomDay(rnd)] >= M)
                return 1.;
        }

        return 0.;
    }
}
