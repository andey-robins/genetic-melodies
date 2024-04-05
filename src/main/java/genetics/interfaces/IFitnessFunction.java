package genetics.interfaces;

import genetics.Individual;

public interface IFitnessFunction {

    // TODO: Determine intelligent methods of fitness calculation. Some directions to explore:
    // Consonance to Dissonance Ratio
    // Contour Analysis (Compare to predefined 'good' contours?)
    // Variety (Penalizing monotony)
    // Resolution (If the melody returns to the root note)
    // Tonal Coherence (If all the notes reside in the same key. Might be too restrictive...)
    // Motiffs & Callbacks (If working with longer melodies)

    double score(Individual i);
}
