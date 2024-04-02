package genetics;

import genetics.interfaces.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {

    private final Random RNG = new Random();

    ISelectionMechanism selector;
    IFitnessFunction fitness;
    ICrossoverMechanism xover;
    IMutationMechanism mutator;
    IStopCondition stopper;

    private ArrayList<Individual> individuals;

    // hyperparams
    private final int size;
    private final boolean elitism;
    private final double mutationChance;

    /**
     * Population is a large scale object which holds and evolves a solution
     * @param size - the number of individuals
     * @param elitist - if the top 4 individuals should be kept from each generation
     * @param mutChance -  0.0-1.0, the percentage chance of any one individual mutating
     * @param f - fitness function
     * @param smech - selection mechanism
     * @param xmech - crossover mechanism
     * @param mmech - mutation mechanism
     * @param stopCond - condition to stop evolution
     */
    public Population(
            int size,
            boolean elitist,
            double mutChance,
            IFitnessFunction f,
            ISelectionMechanism smech,
            ICrossoverMechanism xmech,
            IMutationMechanism mmech,
            IStopCondition stopCond
    ) {
        this.fitness = f;
        this.selector = smech;
        this.xover = xmech;
        this.mutator = mmech;
        this.stopper = stopCond;

        this.elitism = elitist;
        this.size = size;
        this.mutationChance = mutChance;

        this.individuals = new ArrayList<>();
    }


    public void Evolve() {
        this.initialize();

        if (!this.stopper.shouldStop(this)) {
            Pair<Individual, Individual>[] survivors;

            this.evaluateFitness();
            survivors = this.select();
            this.individuals = this.combine(survivors);
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
        for (int i = 0; i < this.size; i++) {
            this.individuals.add(Individual.randomIndividualFactory());
        }
    }

    private void evaluateFitness() {
        for (Individual individual : this.individuals) {
            double fitness = this.fitness.score(individual);
            individual.setFitness(fitness);
        }
    }

    private Pair<Individual, Individual>[] select() {
        return this.selector.selectTop(this, 20);
    }

    private ArrayList<Individual> combine(Pair<Individual, Individual>[] parents) {
        ArrayList<Individual> nextIndividuals = new ArrayList<>();

        for (Pair<Individual, Individual> couple : parents) {
            Individual p1, p2;
            Individual[] children;

            p1 = couple.getValue0();
            p2 = couple.getValue1();

            children = this.xover.crossover(p1, p2);

            Collections.addAll(nextIndividuals, children);
        }

        // for now, we hardcode the number of elite individuals to keep
        if (this.elitism) {
            Collections.addAll(nextIndividuals, this.getTopPerformers(4));
        }

        return nextIndividuals;
    }

    private void mutate() {
        for (Individual ind : this.individuals) {
            if (this.RNG.nextDouble() <= this.mutationChance) {
                this.mutator.mutate(ind);
            }
        }
    }
}
