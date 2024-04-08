package genetics.selection;

import genetics.Individual;
import genetics.Population;
import genetics.interfaces.ISelectionMechanism;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class TournamentSelection implements ISelectionMechanism {

    private static final Random RNG = new Random();

    private final int tournamentSize;

    public TournamentSelection(int size) {
        this.tournamentSize = size;
    }

    @Override
    public Pair<Individual, Individual> selectOnce(Population p) {

        Individual[] tournament = new Individual[this.tournamentSize];

        for (int i = 0; i < this.tournamentSize; i++) {
            tournament[i] = p.getRandomIndividual();
        }

        Arrays.sort(tournament, Comparator.comparing(Individual::getFitness));

        return new Pair<>(tournament[0], tournament[1]);
    }

    @Override
    public ArrayList<Pair<Individual, Individual>> selectTop(Population p, int n) {

        ArrayList<Pair<Individual, Individual>> selected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            selected.add(this.selectOnce(p));
        }

        return selected;
    }
}
