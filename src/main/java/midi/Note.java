package midi;

import java.util.Optional;
import java.util.Random;

public class Note {

    Pitch pitch;
    Rhythm rhythm;

    private static final Random PRNG = new Random();

    public static Note randomNoteFactory() {
        Pitch[] noteOpts = Pitch.values();
        Rhythm[] rhythmOpts = Rhythm.values();
        Pitch pitch = noteOpts[PRNG.nextInt(noteOpts.length)];
        Rhythm rhythm = rhythmOpts[PRNG.nextInt(rhythmOpts.length)];
        return new Note(pitch, rhythm);
    }

    public Note(Pitch p, Rhythm r) {
        this.pitch = p;
        this.rhythm = r;
    }

    public enum Pitch {
        C_3, D_3, E_3, F_3, G_3, A_4, B_4, C_4, D_4, E_4, F_4, G_4, A_5, B_5, C_5, REST
    }

    public enum Rhythm {
        WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH
    }

    public Optional<Integer> getPitch() {
        return switch (this.pitch) {
            case C_3 -> Optional.of(48);
            case D_3 -> Optional.of(50);
            case E_3 -> Optional.of(52);
            case F_3 -> Optional.of(53);
            case G_3 -> Optional.of(55);
            case A_4 -> Optional.of(57);
            case B_4 -> Optional.of(59);
            case C_4 -> Optional.of(60);
            case D_4 -> Optional.of(62);
            case E_4 -> Optional.of(64);
            case F_4 -> Optional.of(65);
            case G_4 -> Optional.of(67);
            case A_5 -> Optional.of(69);
            case B_5 -> Optional.of(71);
            case C_5 -> Optional.of(72);
            case REST -> Optional.empty();
            default -> Optional.empty();
        };
    }

    public int getLength() {
        return switch (this.rhythm) {
            case WHOLE -> 16;
            case HALF -> 8;
            case QUARTER -> 4;
            case EIGHTH -> 2;
            case SIXTEENTH -> 1;
            default -> 0;
        };
    }
}
