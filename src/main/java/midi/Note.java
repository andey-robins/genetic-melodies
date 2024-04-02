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
}