package genetics.interfaces;

import genetics.Individual;
import genetics.Population;
import org.javatuples.Pair;

public interface ISelectionMechanism {

    /**
     * selectOnce will draw a pair of individuals from the population
     * @param p - the pop to draw from
     * @return a pair of individuals which can be combined
     */
    Pair<Individual, Individual> selectOnce(Population p);

    /**
     * selectTop will return the pairs of individuals to undergo c
     * @param p - the population to select from
     * @param n - the number of pairs to select from the population
     * @return an array of pairs for combination
     */
    Pair<Individual, Individual>[] selectTop(Population p, int n);
}
