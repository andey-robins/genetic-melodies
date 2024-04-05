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
        C_5
    }

    public enum Rhythm {
        WHOLE,
        HALF,
        QUARTER,
        EIGHTH,
        SIXTEENTH
    }

    public int getPitch() {
        switch (this.pitch) {
            case C_3: return 48;
            case D_3: return 50;
            case E_3: return 52;
            case F_3: return 53;
            case G_3: return 55;
            case A_4: return 57;
            case B_4: return 59;
            case C_4: return 60;
            case D_4: return 62;
            case E_4: return 64;
            case F_4: return 65;
            case G_4: return 67;
            case A_5: return 69;
            case B_5: return 71;
            case C_5: return 72;
            default: return -1;
        }
    }
}