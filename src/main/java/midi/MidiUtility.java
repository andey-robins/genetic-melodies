package midi;

import javax.sound.midi.*;

public class MidiUtility {
    private final Sequence sequence;
    private final Sequencer sequencer;

    public MidiUtility() throws MidiUnavailableException, InvalidMidiDataException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequence = new Sequence(Sequence.PPQ, 4);
    }

    public void addNote(int channel, int note, int velocity, int startTick, int duration, int instrument)
            throws InvalidMidiDataException {
        Track track = sequence.createTrack();

        // Program Change to select the instrument
        ShortMessage instrumentChange = new ShortMessage();
        instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
        track.add(new MidiEvent(instrumentChange, 0));

        // Note ON
        ShortMessage startMessage = new ShortMessage();
        startMessage.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
        track.add(new MidiEvent(startMessage, startTick));

        // Note OFF
        ShortMessage stopMessage = new ShortMessage();
        stopMessage.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
        track.add(new MidiEvent(stopMessage, startTick + duration));
    }

    public void play() throws InterruptedException {
        try {
            sequencer.setSequence(sequence);
            sequencer.start();

            // Length of the song
            Thread.sleep(10000);

            sequencer.stop();
            sequencer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}