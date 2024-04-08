package genetics.mutation;

import genetics.Individual;
import genetics.interfaces.IMutationMechanism;
import midi.Note;

import java.util.Random;

public class NotewiseMutation implements IMutationMechanism {

    private static final Random RNG = new Random();

    @Override
    public void mutate(Individual i) {
        int mutationIdx = RNG.nextInt(i.size());
        i.setMelodyNote(mutationIdx, Note.randomNoteFactory());
    }
}
