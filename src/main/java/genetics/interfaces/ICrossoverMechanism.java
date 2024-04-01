package genetics.interfaces;

import genetics.Individual;

public interface ICrossoverMechanism {

    // Note: This *must not* be an in-place combination
    Individual[] crossover(Individual p1, Individual p2);
}
