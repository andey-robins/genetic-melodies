package app;

import genetics.crossover.UniformCrossover;
import genetics.fitness.ConsonanceFitness;
import genetics.mutation.NotewiseMutation;
import genetics.selection.TournamentSelection;
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

        /* Here you should have access to creating a population with the available mechanisms */
        Population pop = new Population(
                populationCount,
                elitism,
                0.2,
                new ConsonanceFitness(),
                new TournamentSelection(5),
                new UniformCrossover(),
                new NotewiseMutation(),
                new FitnessThresholdStop(1.0)
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
        smoothest[1].playMelody();
        drawStaffAndNotes(smoothest[0].getMelody());
        Individual[] smoothest = pop.getTopPerformers(1);
        System.out.println("Top Melody");
        System.out.println(smoothest[0].getFitness());
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

    public void drawStaffAndNotes(ArrayList<Note> melody) {
        GraphicsContext gc = staffCanvas.getGraphicsContext2D();
    
        // Clear previous drawings
        gc.clearRect(0, 0, staffCanvas.getWidth(), staffCanvas.getHeight());
    
        // Draw staff lines
        double lineSpacing = 10; // Adjust as necessary
        double startY = 50; // Start drawing staff lines a bit lower
        for (int i = 0; i < 5; i++) {
            double y = startY + lineSpacing * i;
            gc.strokeLine(10, y, staffCanvas.getWidth() - 10, y);
        }
    
        // Use an array to hold noteX so it can be modified inside lambda
        final double[] noteX = {10}; // Starting x position for notes
        double noteSpacing = 20; // Space between notes
        melody.forEach(note -> {
            Optional<Integer> pitchOpt = note.getPitch();
            if (pitchOpt.isPresent()) {
                // Simplified: convert pitch to a vertical position on the staff
                double noteY = pitchToPositionOnStaff(pitchOpt.get(), startY, lineSpacing);
    
                // Draw a simple circle for the note head
                gc.fillOval(noteX[0], noteY - 5, 10, 10); // Adjust size as needed
            } else {
                // Draw a rest symbol, here we use a simple "R" to represent rests
                gc.fillText("R", noteX[0] + 5, startY + 2 * lineSpacing); // You might want to adjust positioning
            }
            
            noteX[0] += noteSpacing; // Move to the next position regardless of note or rest
        });
    }
    
    private double pitchToPositionOnStaff(int pitch, double startY, double lineSpacing) {
        // This method needs to convert MIDI pitch numbers to y positions on the canvas
        // Placeholder: Implement the conversion based on musical rules
        // For simplicity, let's map pitches directly to staff positions as a placeholder
        return startY + pitch + lineSpacing - 50; // Placeholder: replace with actual logic
    }
    
}
