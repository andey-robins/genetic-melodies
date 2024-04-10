package midi;

import java.util.Optional;
import java.util.Random;

public class Note {

    Pitch pitch;
    Rhythm rhythm;
    private static final Random RNG = new Random();

    public static Note randomNoteFactory() {
        Pitch[] noteOpts = Pitch.values();
        Rhythm[] rhythmOpts = Rhythm.values();
        Pitch pitch = noteOpts[RNG.nextInt(noteOpts.length)];
        Rhythm rhythm = rhythmOpts[RNG.nextInt(rhythmOpts.length)];
        return new Note(pitch, rhythm);
    }

    public Note(Pitch p, Rhythm r) {
        this.pitch = p;
        this.rhythm = r;
    }

    public void setPitch(Pitch p) {
        this.pitch = p;
    }

    public void setRhythm(Rhythm r) {
        this.rhythm = r;
    }

    public enum Pitch {
        C_3, Cs_3, D_3, Ds_3, E_3, F_3, Fs_3, G_3, Gs_3, A_4, As_4, B_4, C_4, Cs_4, D_4, Ds_4, E_4, F_4, Fs_4, G_4, Gs_4, A_5, As_5, B_5, C_5, REST
    }

    public enum Rhythm {
        WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH
    }

    public Optional<Integer> getPitch() {
        return switch (this.pitch) {
            case C_3  -> Optional.of(48);
            case Cs_3 -> Optional.of(49);
            case D_3  -> Optional.of(50);
            case Ds_3 -> Optional.of(51);
            case E_3  -> Optional.of(52);
            case F_3  -> Optional.of(53);
            case Fs_3 -> Optional.of(54);
            case G_3  -> Optional.of(55);
            case Gs_3 -> Optional.of(56);
            case A_4  -> Optional.of(57);
            case As_4 -> Optional.of(58);
            case B_4  -> Optional.of(59);
            case C_4  -> Optional.of(60);
            case Cs_4 -> Optional.of(61);
            case D_4  -> Optional.of(62);
            case Ds_4 -> Optional.of(63);
            case E_4  -> Optional.of(64);
            case F_4  -> Optional.of(65);
            case Fs_4 -> Optional.of(66);
            case G_4  -> Optional.of(67);
            case Gs_4 -> Optional.of(68);
            case A_5  -> Optional.of(69);
            case As_5 -> Optional.of(70);
            case B_5  -> Optional.of(71);
            case C_5  -> Optional.of(72);
            case REST -> Optional.empty();
        };
    }

    public int getLength() {
        return switch (this.rhythm) {
            case WHOLE -> 16;
            case HALF -> 8;
            case QUARTER -> 4;
            case EIGHTH -> 2;
            case SIXTEENTH -> 1;
        };
    }

    public Rhythm getRhythm() {
        return this.rhythm;
    }

    public boolean isSharp() {
        int relativePitch = (getPitch().get() - 48) % 12;
        return relativePitch == 1 || relativePitch == 3 || relativePitch == 6 || relativePitch == 8 || relativePitch == 10;
    }
}
