package genetics.mutation;

import genetics.Individual;
import genetics.interfaces.IMutationMechanism;
import midi.Note;

import java.util.Random;

public class PitchMutation implements IMutationMechanism {
    private static final Random RNG = new Random();

    public PitchMutation() {}

    @Override
    public void mutate(Individual i) {
        int mutationIdx = RNG.nextInt(i.size());
        i.getMelody().get(mutationIdx).setPitch(Note.Pitch.values()[RNG.nextInt(Note.Pitch.values().length)]);
        i.setMelodyNote(mutationIdx, i.getMelody().get(mutationIdx));
    }

}
