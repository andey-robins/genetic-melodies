package genetics.fitness;

import genetics.Individual;
import genetics.interfaces.IFitnessFunction;
import stats.JaccardSimilarity;

public class SampleSimilarityFitness implements IFitnessFunction {

    Individual sample;

    public SampleSimilarityFitness(Individual sample) {
        this.sample = sample;
    }

    @Override
    public double score(Individual i) {
        return JaccardSimilarity.NoteSimilarity(sample.getMelody(), i.getMelody());
    }
}
