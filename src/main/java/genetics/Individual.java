package genetics;

import midi.MidiUtility;
import midi.Note;

import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Creates a random Melody with 10 notes.
     * @return
     */
    public static Individual randomIndividualFactory() {
        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notes.add(Note.randomNoteFactory());
        }
        return new Individual(notes);
    }

    /**
     * Creates a random Melody with the specified number of notes.
     * @param numNotes
     * @return
     */
    public static Individual randomIndividualFactory(int numNotes) {
        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < numNotes; i++) {
            notes.add(Note.randomNoteFactory());
        }
        return new Individual(notes);
    }

    /**
     * Creates a Melody with the specified Notes.
     * @param numNotes
     * @return
     */
    public static Individual IndividualFactory(Note... notes) {
        return new Individual(new ArrayList<Note>(Arrays.asList(notes)));
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
    public void playMelody(int instrument, long tempoFactor) {
        try {
            // TODO: Replace these hardcoded values
            MidiUtility midiUtility = MidiUtility.getInstance();
            int currentTick = 0;
            int velocity = 64;
            
            // Ensure sequence is empty
            midiUtility.resetForNewMelody();

            for (Note note : melody) {
                Optional<Integer> pitch = note.getPitch();
                if (pitch.isPresent()) {
                    midiUtility.addNote(0, pitch.get(), velocity, currentTick, note.getLength() / tempoFactor, instrument);
                }
                currentTick += note.getLength() / tempoFactor; // Advance the current tick regardless of whether a note is played or not
            }
            printMelody();
            midiUtility.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMelodyNote(int idx, Note n) {
        this.melody.set(idx, n);
    }

    /**
     * Prints out a visual representation of the melody.
     */
    public void printMelody() {
        StringBuilder melodyRepresentation = new StringBuilder("Melody: [");
        for (Note note : this.melody) {
            melodyRepresentation.append(noteToString(note)).append(", ");
        }
        if (!this.melody.isEmpty()) {
            melodyRepresentation.setLength(melodyRepresentation.length() - 2);
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
            int pitchIndex = pitch - 48; // Subtract base MIDI number
            if (pitchIndex >= 0 && pitchIndex < Note.Pitch.values().length - 1) {
                return Note.Pitch.values()[pitchIndex].name();
            } else {
                return "UNKNOWN PITCH";
            }
        }).orElse("REST");
    
        String rhythmName = note.getRhythm().name();
        return pitchName + " (" + rhythmName + ")";
    }
}
