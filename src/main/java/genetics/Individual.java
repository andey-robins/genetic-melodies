package genetics;

import midi.MidiUtility;
import midi.Note;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


import java.util.Optional;

public class Individual {

    ArrayList<Note> melody;
    private int length;

    public static Individual randomIndividualFactory() {
        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notes.add(Note.randomNoteFactory());
        }
        return new Individual(notes);
    }

    private double fitness;

    public Individual(ArrayList<Note> m) {
        this.melody = m;
        this.length = m.size();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int size() {
        return this.length;
    }

    public ArrayList<Note> getMelody() {
        return this.melody;
    }

    /**
     * Plays the melody of this individual.
     */
    public void playMelody() {
        try {
            // TODO: Replace these hardcoded values
            MidiUtility midiUtility = MidiUtility.getInstance();
            int startTick = 0;
            int duration = 2;
            int velocity = 64;
            int instrument = 0;
            
            // Ensure sequence is empty
            midiUtility.resetForNewMelody();

            for (Note note : melody) {
                Optional<Integer> pitch = note.getPitch();
                if (pitch.isPresent()) {
                    midiUtility.addNote(0, pitch.get(), velocity, startTick, note.getLength(), instrument);
                }
                startTick += note.getLength(); // Advance the start tick regardless of whether a note is played or not
            }
            displayMelody();
            midiUtility.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMelodyNote(int idx, Note n) {
        this.melody.set(idx, n);
    }

    /**
     * Displays a visual representation of the melody.
     */
    public void displayMelody() {
        StringBuilder melodyRepresentation = new StringBuilder("Melody: [");
        for (Note note : this.melody) {
            melodyRepresentation.append(noteToString(note)).append(", ");
        }
        if (!this.melody.isEmpty()) {
            melodyRepresentation.setLength(melodyRepresentation.length() - 2); // Trim the trailing comma and space
        }
        melodyRepresentation.append("]");
        System.out.println(melodyRepresentation.toString());
    }

    /**
     * Converts a note to a string representation.
     * 
     * @param note The note to convert.
     * @return A string representing the note's pitch (or "REST") and its rhythm.
     */
    private String noteToString(Note note) {
        String pitchName = note.getPitch().map(pitch -> {
            // This map operation converts MIDI pitch numbers back to note names.
            // Assuming pitch corresponds directly to the Note.Pitch enum ordering.
            return Note.Pitch.values()[pitch % Note.Pitch.values().length - 1].name();
        }).orElse("REST");
        
        // The rhythm is directly obtainable from the note's rhythm property.
        String rhythmName = note.getRhythm().name();
        return pitchName + " (" + rhythmName + ")";
    }
}
