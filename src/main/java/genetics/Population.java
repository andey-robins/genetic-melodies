package genetics;

import genetics.interfaces.*;
import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {

    public int generation;

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


        this.generation = 0;
    }


    public void Evolve() {
        this.initialize();
        System.out.println("Beginning evolution");
        while (!this.stopper.shouldStop(this)) {
            ArrayList<Pair<Individual, Individual>> survivors;

            
            survivors = this.select();
            this.individuals = this.combine(survivors);
            this.mutate();
            this.generation++;
            this.evaluateFitness();
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
        individuals.sort(Comparator.comparingDouble(Individual::getFitness).reversed());
        return this.individuals.subList(0, n).toArray(new Individual[n]);
    }

    /**
     * getRandomIndividual will retrieve a random individual from the population
     * @return an individual sampled uniformly from the population
     */
    public Individual getRandomIndividual() {
        return this.individuals.get(this.RNG.nextInt(this.individuals.size()));
    }

    public ArrayList<Individual> getIndividuals() { return this.individuals; }

    private void initialize() {
        for (int i = 0; i < this.size; i++) {
            this.individuals.add(Individual.randomIndividualFactory());
        }
        this.evaluateFitness();
    }

    private void evaluateFitness() {
        for (Individual individual : this.individuals) {
            double fitness = this.fitness.score(individual);
            individual.setFitness(fitness);
        }
    }

    private ArrayList<Pair<Individual, Individual>> select() {
        ArrayList<Pair<Individual, Individual>> top = this.selector.selectTop(this, 20);
        // we recalculate true fitness here. This allows any selection function to use the fitness as a working
        // piece of memory. For instance, fitness proportional selection is able to normalize the fitness values
        // without worrying about side effects down the chain of methods in the Evolve workflow
        this.evaluateFitness();
        return top;
    }

    private ArrayList<Individual> combine(ArrayList<Pair<Individual, Individual>> parents) {
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
