package midi;

import javax.sound.midi.*;

// Singleton Pattern
public class MidiUtility {
    private static MidiUtility instance;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    // Private constructor to prevent external instantiation
    private MidiUtility() throws MidiUnavailableException, InvalidMidiDataException {
        this.sequencer = MidiSystem.getSequencer();
        this.sequencer.open();
        this.sequence = new Sequence(Sequence.PPQ, 4);
        this.track = sequence.createTrack();
    }

    // Public method to get instance
    public static MidiUtility getInstance() throws MidiUnavailableException, InvalidMidiDataException {
        if (instance == null) {
            instance = new MidiUtility();
        }
        return instance;
    }

    // Resets the sequence and track
    public void resetSequence() throws InvalidMidiDataException {
        this.sequence = new Sequence(Sequence.PPQ, 4);
        this.track = sequence.createTrack();
    }
    
    public void addNote(int channel, int note, int velocity, int startTick, int duration, int instrument)
            throws InvalidMidiDataException {
        ShortMessage instrumentChange = new ShortMessage();
        instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
        MidiEvent changeEvent = new MidiEvent(instrumentChange, startTick);
        this.track.add(changeEvent);

        // Note ON
        ShortMessage startMessage = new ShortMessage();
        startMessage.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
        MidiEvent startEvent = new MidiEvent(startMessage, startTick);
        this.track.add(startEvent);

        // Note OFF
        ShortMessage stopMessage = new ShortMessage();
        stopMessage.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
        MidiEvent stopEvent = new MidiEvent(stopMessage, startTick + duration);
        this.track.add(stopEvent);
    }

    public void play() {
        new Thread(() -> {
            try {
                this.sequencer.setSequence(this.sequence);
                this.sequencer.start();

                while (this.sequencer.isRunning()) {
                    Thread.sleep(100);
                }
                this.sequencer.stop();
                this.sequence = new Sequence(Sequence.PPQ, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Clean up Sequencer
    public void cleanup() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.stop();
            sequencer.close();
        }
    }
}
