import javax.sound.midi.*;

public class MidiTest {
    public static void main(String[] args) {
        try {
            MidiUtility midiUtility = new MidiUtility();
            
            int[][] notes = {
                {1, noteLetterToMidiNumber("F", 3), 127, 0, 1, 120},
                {1, 52, 127, 4, 1, 0},
                {0, 54, 127, 5, 16, 0},
                {0, 57, 127, 12, 16, 0},
                {0, 59, 127, 16, 48, 0},
            };

            for (int[] note : notes) {
                midiUtility.addNote(note[0], // channel
                                    note[1], // note
                                    note[2], // velocity
                                    note[3], // startTick
                                    note[4], // duration
                                    note[5]); // instrument
            }

            midiUtility.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int noteLetterToMidiNumber(String note, int octave) {
        int offset = switch (note.toUpperCase().charAt(0)) {
            case 'C' -> 0;
            case 'D' -> 2;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 7;
            case 'A' -> 9;
            case 'B' -> 11;
            default -> throw new IllegalArgumentException("Invalid note: " + note);
        };

        // Sharp (#) increases by 1, flat (b) decreases by 1
        if (note.length() > 1) {
            if (note.charAt(1) == '#') {
                offset += 1;
            } else if (note.charAt(1) == 'b') {
                offset -= 1;
            }
        }

        // Edge case
        if (offset < 0) {
            offset += 12;
            octave -= 1;
        } else if (offset > 11) {
            offset -= 12;
            octave += 1;
        }

        int midiNumber = 12 * (octave + 1) + offset;
        return midiNumber;
    }
}

