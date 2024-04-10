package genetics.mutation;

import genetics.Individual;
import genetics.interfaces.IMutationMechanism;

import java.util.ArrayList;
import java.util.Random;

public class MultipleMutation implements IMutationMechanism {

    private static final Random RNG = new Random();

    private final ArrayList<IMutationMechanism> mutators;

    /**
     * This class allows for randomly selecting one of multiple possible
     * mutation operators from a list provided to this constructor
     * @param many an ArrayList of mutation mechanisms
     */
    public MultipleMutation(ArrayList<IMutationMechanism> many) {
        this.mutators = many;
    }

    @Override
    public void mutate(Individual i) {
        IMutationMechanism mutator = this.mutators.get(RNG.nextInt(this.mutators.size()));
        mutator.mutate(i);
    }
}
