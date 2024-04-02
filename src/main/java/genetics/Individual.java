package genetics;

import midi.Note;

import java.util.ArrayList;

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
}
