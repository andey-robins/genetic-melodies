package genetics.interfaces;

import genetics.Individual;

public interface ICrossoverMechanism {

    // Some Thoughts: Our standard three crossover operators will be more than enough here, but
    // if we would like to go above and beyond, we could explore music-specific crossovers such as:
    // Motiff Crossover: If we are able to accurately discern if an individual contains a motiff, swapping
    // two motiffs between melodies could be interesting
    // Measure Crossover: Assuming equal BPMs and time signatures, we could crossover at the boundaries of measures
    // Key Crossover: If two parents have different keys (defined by their tonal center), we could crossover
    // only the notes that would fit that other parents key, getting us closer to harmonic coherence

    // Note: This *must not* be an in-place combination
    Individual[] crossover(Individual p1, Individual p2);
}
