package genetics.stopping;

import genetics.Individual;
import genetics.Population;
import genetics.interfaces.IStopCondition;

public class FitnessThresholdStop implements IStopCondition {

    double stopThreshold;

    /**
     * stops when the top performing individual reaches a threshold of fitness
     * @param threshold the threshold which fitness must exceed
     */
    public FitnessThresholdStop(double threshold) {
        this.stopThreshold = threshold;
    }

    @Override
    public boolean shouldStop(Population p) {
        Individual[] top = p.getTopPerformers(1);
        return top[0].getFitness() >= this.stopThreshold;
    }
}
