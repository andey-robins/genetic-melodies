package app;

import genetics.crossover.UniformCrossover;
import genetics.fitness.SmoothFitness;
import genetics.fitness.ConsonanceFitness;
import genetics.mutation.NotewiseMutation;
import genetics.selection.TournamentSelection;
import genetics.stopping.BoundedGenerationStop;
import midi.MidiRecorderGUI;
import midi.MidiUtility;
import genetics.Individual;
import genetics.Population;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

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
    private void startGA() throws MidiUnavailableException, InvalidMidiDataException {
        //int populationCount = Integer.parseInt(populationCountField.getText());
        //int numberOfNotes = Integer.parseInt(numberOfNotesField.getText());
        //int numberOfGenerations = Integer.parseInt(numberOfGenerationsField.getText());
        //int generationsBetweenInteraction = Integer.parseInt(generationsForInteractionField.getText());
        //boolean elitism = elitismCheckBox.isSelected();

        /* Here you should have access to creating a population with the available mechanisms */
        Population pop = new Population(
                100,
                true,
                0.2,
                new ConsonanceFitness(),
                new TournamentSelection(5),
                new UniformCrossover(),
                new NotewiseMutation(),
                new BoundedGenerationStop(100)
        );
        // Then evolution is just this:
         pop.Evolve();
        // it will continue until the stopping condition is triggered
        // our workflow will thus be able to be:
        //   1. Prompt user for initial configuration
        //   2. Run until stopping condition is true
        //   3. Provide output to user, ask if they want to continue evolution
        //   4. Go to step 2 if they want to continue otherwise exit

        Individual[] smoothest = pop.getTopPerformers(10);
        System.out.println("Evolution complete. Playing smoothest melody.");
        smoothest[0].playMelody();


        // Cleanup
        // Destroy Sequencer
        // Doesn't work right now, it doesn't wait for the current melody to end. Might not need it tbh
        //MidiUtility.getInstance().cleanup();

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
