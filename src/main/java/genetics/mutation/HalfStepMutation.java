package genetics.mutation;

import genetics.Individual;
import genetics.interfaces.IMutationMechanism;
import midi.Note;

import java.util.Optional;
import java.util.Random;

public class HalfStepMutation implements IMutationMechanism {

    private static final Random RNG = new Random();

    public HalfStepMutation() {}

    @Override
    public void mutate(Individual i) {
        int mutationIdx = RNG.nextInt(i.size());
        int offset = -1;
        if (RNG.nextBoolean()) {
            offset = 1;
        }

        Note mutatingNote = i.getMelody().get(mutationIdx);
        Optional<Integer> mutatingPitch = mutatingNote.getPitch();
        if (mutatingPitch.isEmpty()) {
            // If there's just a rest, mutating by a half-step is not a meaningful operation
            return;
        }

        Note.Pitch steppedNote = Note.Pitch.values()[mutatingNote.getPitch().get() - 48 + offset];
        mutatingNote.setPitch(steppedNote);
        i.setMelodyNote(mutationIdx, i.getMelody().get(mutationIdx));
    }
}
