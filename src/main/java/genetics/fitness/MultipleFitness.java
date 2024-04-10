package genetics.fitness;

import genetics.Individual;
import genetics.interfaces.IFitnessFunction;

import java.util.ArrayList;

public class MultipleFitness implements IFitnessFunction {

    ArrayList<IFitnessFunction> fitnesses;

    public MultipleFitness(ArrayList<IFitnessFunction> many) { this.fitnesses = many; }

    @Override
    public double score(Individual i) {
        double compositeScore = 0.0;
        if (fitnesses.isEmpty()){
            fitnesses.add(new SmoothFitness());
        }
        for (IFitnessFunction fitness : this.fitnesses) {
            compositeScore += fitness.score(i);
        }

        return compositeScore;
    }
}
