package midi;

import javax.sound.midi.*;
import java.io.*;

public class MidiPlayer {


    /*
     * playMidiFile will do what it says on the tin
     * @param filename - the filename of the midi file to be played
     */
    public static void playMidiFile(String filename) {
        try {
            Sequence sequence = MidiSystem.getSequence(new File(filename));
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);

            // Start playing
            sequencer.start();

            while (sequencer.isRunning()) {
                Thread.sleep(1000); // Check every second
            }

            sequencer.stop();
            sequencer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
