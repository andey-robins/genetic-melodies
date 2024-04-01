package genetics.interfaces;

import genetics.Population;

public interface IStopCondition {

    boolean shouldStop(Population p);
}
