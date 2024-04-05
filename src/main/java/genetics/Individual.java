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
            MidiUtility midiUtility = new MidiUtility();
            int startTick = 0;
            int duration = 2; // fixed duration (for now... 😈)
            int velocity = 64;
            int instrument = 0;
            
            for (Note note : melody) {
                midiUtility.addNote(0, note.getPitch(), velocity, startTick, duration, instrument);
                startTick += duration; // Move to the next note's start time
            }
            
            midiUtility.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
