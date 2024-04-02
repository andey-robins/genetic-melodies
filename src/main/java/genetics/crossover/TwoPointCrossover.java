package genetics.crossover;

import genetics.Individual;
import genetics.interfaces.ICrossoverMechanism;
import midi.Note;

import java.util.ArrayList;
import java.util.Random;

public class TwoPointCrossover implements ICrossoverMechanism {

    private static final Random RNG = new Random();

    public TwoPointCrossover() {}

    @Override
    public Individual[] crossover(Individual p1, Individual p2) {

        // determine bounds since there is no guarantee about melody lengths
        int shortestIndividual = p1.size();
        if (p2.size() < shortestIndividual) {
            shortestIndividual = p2.size();
        }

        // select two crossover points which are different
        int crossoverPointOne = RNG.nextInt(shortestIndividual);
        int crossoverPointTwo;
        do {
            crossoverPointTwo = RNG.nextInt(shortestIndividual);
        } while (crossoverPointOne == crossoverPointTwo);

        // guarantee that crossoverPointOne is the smaller
        if (crossoverPointOne > crossoverPointTwo) {
            int tmp = crossoverPointOne;
            crossoverPointOne = crossoverPointTwo;
            crossoverPointTwo = tmp;
        }

        ArrayList<Note> m1 = new ArrayList<>(p1.getMelody().subList(0, crossoverPointOne));
        ArrayList<Note> m2 = new ArrayList<>(p2.getMelody().subList(0, crossoverPointOne));

        m1.addAll(p2.getMelody().subList(crossoverPointOne, crossoverPointTwo));
        m2.addAll(p1.getMelody().subList(crossoverPointOne, crossoverPointTwo));

        m1.addAll(p1.getMelody().subList(crossoverPointTwo, p1.getMelody().size()));
        m2.addAll(p2.getMelody().subList(crossoverPointTwo, p2.getMelody().size()));

        return new Individual[]{new Individual(m1), new Individual(m2)};
    }
}
