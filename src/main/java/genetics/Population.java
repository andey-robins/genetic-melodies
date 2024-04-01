package genetics;

import genetics.interfaces.ICrossoverMechanism;
import genetics.interfaces.IFitnessFunction;
import genetics.interfaces.IMutationMechanism;
import genetics.interfaces.IStopCondition;

import java.util.*;

public class Population {

    IFitnessFunction fitness;
    ICrossoverMechanism xover;
    IMutationMechanism mutator;
    IStopCondition stopper;

    private final ArrayList<Individual> individuals;

    public Population(IFitnessFunction f, ICrossoverMechanism xmech, IMutationMechanism mmech, IStopCondition stopCond) {
        this.fitness = f;
        this.xover = xmech;
        this.mutator = mmech;
        this.stopper = stopCond;

        this.individuals = new ArrayList<>();
    }


    public void Evolve() {
        this.initialize();

        if (!this.stopper.shouldStop(this)) {
            this.evaluateFitness();
            this.select();
            this.combine();
            this.mutate();
        }
    }

    /**
     * getTopPerformers will return the top 10 most fit individuals.
     * This is a polymorphic entrypoint to the generic getTopPerformers/1 function
     * which accepts a parameter n and returns the top n elements
     * @return The top 10 most fit individuals
     */
    public Individual[] getTopPerformers() {
        return this.getTopPerformers(10);
    }

    /**
     * getTopPerformers/1
     * @param n, the count of individuals to get
     * @return the top n most fit individuals in the population
     */
    public Individual[] getTopPerformers(int n) {
        individuals.sort(Comparator.comparingDouble(Individual::getFitness));
        return this.individuals.subList(0, n).toArray(new Individual[n]);
    }

    private void initialize() {

    }

    private void evaluateFitness() {

    }

    private void select() {

    }

    private void combine() {

    }

    private void mutate() {

    }
}
