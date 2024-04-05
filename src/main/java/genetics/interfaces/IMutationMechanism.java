package genetics.interfaces;

import genetics.Individual;

public interface IMutationMechanism {

    // Some Thoughts: Standard bitwise mutation will be more than enough here, but
    // if we would like to go above and beyond, we could explore music-specific mutations such as:
    // Modal Mutation: Mutate a whole melody to a different mode (i.e. from major to minor)
    // Note Duration Mutation: Changing a note from a quarter to a whole note, for example
    // Key Mutation: Randomly moving notes towards their nearest neighbor within the key defined by the tonal center
    
    // Important note: this *must* be an in-place mutation operation
    void mutate(Individual i);
}
