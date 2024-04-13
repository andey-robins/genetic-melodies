package genetics.stopping;

import genetics.Population;
import genetics.interfaces.IStopCondition;

public class BoundedGenerationStop implements IStopCondition {

    private final int stoppingPoint;

    public BoundedGenerationStop(int stoppingGeneration) {
        this.stoppingPoint = stoppingGeneration;
    }

    @Override
    public boolean shouldStop(Population p) {
        return p.generation % this.stoppingPoint == 0;
    }
}
