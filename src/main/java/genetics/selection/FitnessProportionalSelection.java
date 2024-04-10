package genetics.selection;

import genetics.Individual;
import genetics.Population;
import genetics.interfaces.ISelectionMechanism;
import midi.Note;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class FitnessProportionalSelection implements ISelectionMechanism {

    private static final Random RNG = new Random();

    @Override
    public Pair<Individual, Individual> selectOnce(Population p) {

        ArrayList<Individual> individuals = p.getIndividuals();
        individuals.sort(Comparator.comparing(Individual::getFitness));

        // scale fitness prior to proportional selection
        double totalFitness = 0.f;
        for (Individual i : individuals) {
            totalFitness += i.getFitness();
        }

        for (Individual i : individuals) {
            i.setFitness(i.getFitness() / totalFitness);
        }

        // we select the two most fit in the case something goes wrong and we don't randomly select
        // from the population properly
        Individual parent1 = individuals.get(0);
        Individual parent2 = individuals.get(1);

        for (int parentIdx = 0; parentIdx < 2; parentIdx++) {
            // selectionFitnessPoint will be a point somewhere in the normalized interval. we iteratively move towards
            // the point from the most fit to least fit, stopping when we have passed a normalized cumulative fitness
            // greater than the fitness selection point
            double selectionFitnessPoint = RNG.nextDouble();
            for (Individual i : individuals) {
                selectionFitnessPoint -= i.getFitness();
                if (selectionFitnessPoint <= 0) {
                    if (parentIdx == 0) {
                        parent1 = i;
                    } else {
                        parent2 = i;
                    }
                    break;
                }
            }
        }

        return new Pair<>(parent1, parent2);
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
