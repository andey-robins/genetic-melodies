package genetics.mutation;

import genetics.Individual;
import genetics.interfaces.IMutationMechanism;
import midi.Note;

import java.util.Random;

public class RhythmicMutation implements IMutationMechanism {

    private static final Random RNG = new Random();

    public RhythmicMutation() {}

    @Override
    public void mutate(Individual i) {
        int mutationIdx = RNG.nextInt(i.size());
        i.getMelody().get(mutationIdx).setRhythm(Note.Rhythm.values()[RNG.nextInt(Note.Rhythm.values().length)]);
        i.setMelodyNote(mutationIdx, i.getMelody().get(mutationIdx));
    }
}
