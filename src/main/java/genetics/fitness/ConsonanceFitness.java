package genetics.fitness;

import genetics.Individual;
import genetics.interfaces.IFitnessFunction;
import midi.Note;

import java.util.Optional;

public class ConsonanceFitness implements IFitnessFunction {
    public ConsonanceFitness() {}

    @Override
    public double score(Individual i) {
        Note lastNote = null;
        double dissonance = 0.0;
        double consonance = 0.0;

        for (Note note : i.getMelody()) {
            if (lastNote != null) {
                Optional<Integer> lastNotePitch = lastNote.getPitch();
                Optional<Integer> notePitch = note.getPitch();

                // Only calculate distance if both notes are not rests
                if (lastNotePitch.isPresent() && notePitch.isPresent()) {
                    int semitones = lastNotePitch.get() - notePitch.get();
                    // Addresses compound intervals
                    if (semitones > 12) {
                        semitones = semitones % 12;
                    }
                    if ((semitones == 1 ) || (semitones == 2) || (semitones == 6) || (semitones == 10) || (semitones == 11)) {
                        dissonance += 1;
                    } else {
                        consonance += 1;
                    }
                }
            }
            lastNote = note;
        }
        return consonance / (consonance + dissonance);
    }
}
