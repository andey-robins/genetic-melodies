package midi;

import javax.sound.midi.*;
import java.io.*;

public class MidiRecorder {

    private final Sequence sequence;
    private final Track track;
    private final MidiDevice device;
    private final Synthesizer synthesizer;
    private Receiver livePlaybackReceiver;
    private final int PPQ = 12;
    private final double TEMPO = 120;
    private int currentInstrument = 0; // Default to Piano

    public MidiRecorder(MidiDevice.Info selectedDeviceInfo) throws MidiUnavailableException, InvalidMidiDataException {
        sequence = new Sequence(Sequence.PPQ, PPQ);
        track = sequence.createTrack();
        device = MidiSystem.getMidiDevice(selectedDeviceInfo);
        device.open();
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        initLivePlayback();
    }

    private void initLivePlayback() throws MidiUnavailableException {
        livePlaybackReceiver = synthesizer.getReceiver();

        Transmitter transmitter = device.getTransmitter();
        transmitter.setReceiver(new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                livePlaybackReceiver.send(message, -1);
            }

            @Override
            public void close() {}
        });
    }

    public int changeInstrument(int change) {
        currentInstrument += change;

        if (currentInstrument < 0)
            currentInstrument = 127;
        else if (currentInstrument > 127)
            currentInstrument = 0;

        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, currentInstrument, 0);
            livePlaybackReceiver.send(sm, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentInstrument;
    }

    public int getCurrentInstrument() {
        return currentInstrument;
    }

    public void startRecording() throws MidiUnavailableException {
        device.getTransmitter().setReceiver(new Receiver() {
            private final long startTime = System.currentTimeMillis();

            @Override
            public void send(MidiMessage message, long timeStamp) {
                long currentTime = System.currentTimeMillis();
                long tick = (long) ((currentTime - startTime) * TEMPO * PPQ) / (60 * 1000);
                track.add(new MidiEvent(message, tick));
            }

            @Override
            public void close() {
            }
        });
        System.out.println("Recording started.");
    }

    public void stopRecording() {
        System.out.println("Recording stopped.");
    }

    public void saveToFile(String filename) throws IOException {
        System.out.println("print");
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(filename));
            System.out.println("MIDI file saved: " + filename);
        }
    }

    public Integer mapKeyToMidiNote(String key) {
        return switch (key) {
            case "a" -> 60; // C
            case "w" -> 61; // C#
            case "s" -> 62; // D
            case "e" -> 63; // D#
            case "d" -> 64; // E
            case "f" -> 65; // F
            case "t" -> 66; // F#
            case "g" -> 67; // G
            case "y" -> 68; // G#
            case "h" -> 69; // A
            case "u" -> 70; // A#
            case "j" -> 71; // B
            default -> null;
        };
    }
}
