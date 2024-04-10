package app;

import genetics.crossover.UniformCrossover;
import genetics.fitness.ConsonanceFitness;
import genetics.fitness.MultipleFitness;
import genetics.fitness.VarietyFitness;
import genetics.interfaces.IFitnessFunction;
import genetics.mutation.NotewiseMutation;
import genetics.selection.TournamentSelection;
import genetics.stopping.BoundedGenerationStop;
import genetics.stopping.FitnessThresholdStop;
import midi.MidiRecorderGUI;
import midi.MidiUtility;
import midi.Note;
import genetics.Individual;
import genetics.Population;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Optional;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;

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
    private CheckBox consonanceCheckBox;
    @FXML
    private CheckBox varietyCheckBox;
    @FXML
    private Button startGAButton;

    // Canvas to draw staff
    @FXML
    private Canvas staffCanvas;

    @FXML
    private void startGA() throws MidiUnavailableException, InvalidMidiDataException {

        interface Validator { String ensureParseableInt(String s); };
        Validator numberString = (String val) -> (NumberUtils.isParsable(val) ? val : "10");

        int populationCount = Integer.parseInt(numberString.ensureParseableInt(this.populationCountField.getText()));
        int numberOfNotes = Integer.parseInt(numberString.ensureParseableInt(this.numberOfNotesField.getText()));
        int numberOfGenerations = Integer.parseInt(numberString.ensureParseableInt(this.numberOfGenerationsField.getText()));
//        int generationsBetweenInteraction = Integer.parseInt(generationsForInteractionField.getText());
        boolean elitism = this.elitismCheckBox.isSelected();

        ArrayList<IFitnessFunction> fitnesses = new ArrayList<>();
        if (this.consonanceCheckBox.isSelected()) {
            fitnesses.add(new ConsonanceFitness());
        }
        if (this.varietyCheckBox.isSelected()) {
            fitnesses.add(new VarietyFitness());
        }

        /* Here you should have access to creating a population with the available mechanisms */
        Population pop = new Population(
                populationCount,
                elitism,
                0.2,
                new MultipleFitness(fitnesses),
                new TournamentSelection(5),
                new UniformCrossover(),
                new NotewiseMutation(),
                new BoundedGenerationStop(numberOfGenerations)
        );
        // Then evolution is just this:
         pop.Evolve();
        // it will continue until the stopping condition is triggered
        // our workflow will thus be able to be:
        //   1. Prompt user for initial configuration
        //   2. Run until stopping condition is true
        //   3. Provide output to user, ask if they want to continue evolution
        //   4. Go to step 2 if they want to continue otherwise exit

        
        Individual[] smoothest = pop.getTopPerformers(1);
        System.out.println("Top Melody");
        System.out.println(smoothest[0].getFitness());
        smoothest[0].playMelody();
        drawStaffAndNotes(smoothest[0].getMelody());
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

    public void drawStaffAndNotes(ArrayList<Note> melody) {
        GraphicsContext gc = staffCanvas.getGraphicsContext2D();
    
        // Clear previous drawings
        gc.clearRect(0, 0, staffCanvas.getWidth(), staffCanvas.getHeight());
    
        double lineSpacing = 10;
        double startY = 50;
        for (int i = 0; i < 5; i++) {
            double y = startY + lineSpacing * i;
            gc.strokeLine(10, y, staffCanvas.getWidth() - 10, y);
        }
    
        final double[] noteX = {10}; // Starting x position for notes
        double noteSpacing = 20; // Space between notes
        melody.forEach(note -> {
            Optional<Integer> pitchOpt = note.getPitch();
            if (pitchOpt.isPresent()) {
                int pitch = pitchOpt.get();
                boolean isSharp = note.isSharp();
                double noteY = pitchToPositionOnStaff(pitch, startY, lineSpacing);
        
                // Adjust noteX[0] if the note is sharp to make room for the sharp symbol
                if (isSharp) {
                    gc.fillText("#", noteX[0], noteY); // Draw sharp symbol
                    noteX[0] += 5; // Adjust for sharp symbol width
                }
        
                // Draw the note
                gc.fillOval(noteX[0], noteY - 5, 10, 10);
        
                if (isSharp) {
                    noteX[0] -= 5; // Reset adjustment for sharp symbol width
                }
            } else {
                // Draw a rest symbol
                gc.fillText("R", noteX[0] + 5, startY + 2 * lineSpacing);
            }
        
            noteX[0] += noteSpacing; // Move to the next position
        });
    }
    
    private double pitchToPositionOnStaff(int pitch, double startY, double lineSpacing) {
        int stepsFromC3 = (pitch - 48);
        return 100 + startY - (stepsFromC3 * (lineSpacing / 2));
    }
    

}
