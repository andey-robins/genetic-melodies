package app;

import midi.MidiRecorderGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrimaryController {

    public Button midiPianoBtn;

    @FXML
    private void launchMidiPianoRoll() {
        MidiRecorderGUI midiRecorder = new MidiRecorderGUI();
        Stage stage = new Stage();
        try {
            midiRecorder.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
