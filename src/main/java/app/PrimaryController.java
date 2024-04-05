package app;

import midi.MidiRecorderGUI;
import genetics.Individual;
import genetics.Population;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private TextField populationCountField;
    @FXML
    private TextField numberOfNotesField;
    @FXML
    private TextField numberOfGenerationsField;
    @FXML
    private TextField generationsBetweenInteractionField;
    @FXML
    private CheckBox elitismCheckBox;
    @FXML
    private Button startGAButton;

    @FXML
    private void startGA() {
        //int populationCount = Integer.parseInt(populationCountField.getText());
        //int numberOfNotes = Integer.parseInt(numberOfNotesField.getText());
        //int numberOfGenerations = Integer.parseInt(numberOfGenerationsField.getText());
        //int generationsBetweenInteraction = Integer.parseInt(generationsForInteractionField.getText());
        //boolean elitism = elitismCheckBox.isSelected();

        // Testing Individual gene decoding
        Individual i = Individual.randomIndividualFactory();
        i.playMelody();

    }

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
