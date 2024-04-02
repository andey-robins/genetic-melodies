package genetics.crossover;

import genetics.Individual;
import genetics.interfaces.ICrossoverMechanism;
import midi.Note;

import java.util.ArrayList;
import java.util.Random;

public class UniformCrossover implements ICrossoverMechanism {

    private static final Random RNG = new Random();

    public UniformCrossover() {}

    @Override
    public Individual[] crossover(Individual p1, Individual p2) {

        ArrayList<Note> m1 = new ArrayList<>();
        ArrayList<Note> m2 = new ArrayList<>();

        // determine bounds since there is no guarantee about melody lengths
        boolean p1Longer = true;
        int shortestIndividual = p1.size();
        if (p2.size() < shortestIndividual) {
            shortestIndividual = p2.size();
            p1Longer = false;
        }

        // crossover the overlapping parts of the two melodies
        for (int i = 0; i < shortestIndividual; i++) {
            Note parentOneNote = p1.getMelody().get(i);
            Note parentTwoNote = p2.getMelody().get(i);

            if (RNG.nextBoolean()) {
                m1.add(parentOneNote);
                m2.add(parentTwoNote);
            } else {
                m2.add(parentOneNote);
                m1.add(parentTwoNote);
            }
        }

        // get the tail that isn't part of the overlapping crossover
        ArrayList<Note> overflow = new ArrayList<>();
        if (p1.size() != p2.size() && p1Longer) {
            for (int i = shortestIndividual; i < p1.size(); i++) {
                overflow.add(p1.getMelody().get(i));
            }
        } else if (p1.size() != p2.size()) {
            for (int i = shortestIndividual; i < p2.size(); i++) {
                overflow.add(p2.getMelody().get(i));
            }
        }

        // add the tail to a random melody
        if (RNG.nextBoolean()) {
            m1.addAll(overflow);
        } else {
            m2.addAll(overflow);
        }

        return new Individual[]{new Individual(m1), new Individual(m2)};
    }
}
