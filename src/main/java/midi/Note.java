package midi;

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
        C_3,
        D_3,
        E_3,
        F_3,
        G_3,
        A_4,
        B_4,
        C_4,
        D_4,
        E_4,
        F_4,
        G_4,
        A_5,
        B_5,
        C_5,
        REST
    }

    public enum Rhythm {
        WHOLE,
        HALF,
        QUARTER,
        EIGHTH,
        SIXTEENTH
    }

    public int getPitch() {
        return switch (this.pitch) {
            case C_3 -> 48;
            case D_3 -> 50;
            case E_3 -> 52;
            case F_3 -> 53;
            case G_3 -> 55;
            case A_4 -> 57;
            case B_4 -> 59;
            case C_4 -> 60;
            case D_4 -> 62;
            case E_4 -> 64;
            case F_4 -> 65;
            case G_4 -> 67;
            case A_5 -> 69;
            case B_5 -> 71;
            case C_5 -> 72;
            case REST -> 1;
            default -> -1;
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