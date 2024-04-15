package midi;

import javax.sound.midi.*;

public class MidiUtility {
    private static MidiUtility instance;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    private MidiUtility() throws MidiUnavailableException, InvalidMidiDataException {
        this.sequencer = MidiSystem.getSequencer();
        this.sequencer.open();
        initializeSequenceAndTrack(); // Initial setup
    }

    public static MidiUtility getInstance() throws MidiUnavailableException, InvalidMidiDataException {
        if (instance == null) {
            instance = new MidiUtility();
        }
        return instance;
    }

    // Initializes or reinitializes the sequence and track
    private void initializeSequenceAndTrack() throws InvalidMidiDataException {
        if (this.sequence == null) {
            this.sequence = new Sequence(Sequence.PPQ, 4);
        } else {
            // Remove existing tracks from the sequence to start fresh
            Track[] existingTracks = this.sequence.getTracks();
            for (Track t : existingTracks) {
                this.sequence.deleteTrack(t);
            }
        }
        // Create new track
        this.track = this.sequence.createTrack();
    }

    public void resetForNewMelody() throws InvalidMidiDataException {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
        }
        initializeSequenceAndTrack(); // Prepare for new melody
    }
    
    public void addNote(int channel, int note, int velocity, int startTick, long duration, int instrument)
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
