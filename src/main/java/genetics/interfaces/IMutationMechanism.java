package genetics.interfaces;

import genetics.Individual;

public interface IMutationMechanism {

    // Important note: this *must* be an in-place mutation operation
    void mutate(Individual i);
}
