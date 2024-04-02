package genetics.crossover;

import genetics.Individual;
import genetics.interfaces.ICrossoverMechanism;
import midi.Note;

import java.util.ArrayList;
import java.util.Random;

public class OnePointCrossover implements ICrossoverMechanism {

    private static final Random RNG = new Random();

    public OnePointCrossover() {}

    @Override
    public Individual[] crossover(Individual p1, Individual p2) {

        // determine bounds since there is no guarantee about melody lengths
        int shortestIndividual = p1.size();
        if (p2.size() < shortestIndividual) {
            shortestIndividual = p2.size();
        }

        int crossoverPoint = RNG.nextInt(shortestIndividual);
        ArrayList<Note> m1 = new ArrayList<>(p1.getMelody().subList(0, crossoverPoint));
        ArrayList<Note> m2 = new ArrayList<>(p2.getMelody().subList(0, crossoverPoint));

        m1.addAll(p2.getMelody().subList(crossoverPoint, p2.size()));
        m2.addAll(p1.getMelody().subList(crossoverPoint, p1.size()));

        return new Individual[]{new Individual(m1), new Individual(m2)};
    }
}
