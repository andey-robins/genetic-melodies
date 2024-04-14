package app;

import genetics.crossover.MeasureCrossover;
import genetics.crossover.OnePointCrossover;
import genetics.crossover.TwoPointCrossover;
import genetics.crossover.UniformCrossover;
import genetics.fitness.ConsonanceFitness;
import genetics.fitness.MultipleFitness;
import genetics.fitness.VarietyFitness;
import genetics.interfaces.IFitnessFunction;
import genetics.interfaces.IMutationMechanism;
import genetics.interfaces.ISelectionMechanism;
import genetics.mutation.MultipleMutation;
import genetics.mutation.NotewiseMutation;
import genetics.mutation.PitchMutation;
import genetics.mutation.RhythmicMutation;
import genetics.selection.FitnessProportionalSelection;
import genetics.selection.TournamentSelection;
import genetics.stopping.BoundedGenerationStop;
import genetics.stopping.FitnessThresholdStop;
import midi.MidiRecorderGUI;
import midi.MidiUtility;
import midi.Note;
import genetics.Individual;
import genetics.Population;
import genetics.interfaces.EvolutionStopListener;
import genetics.interfaces.ICrossoverMechanism;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;

public class PrimaryController  implements EvolutionStopListener {

    // Population Settings
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

    // Fitness Settings
    @FXML
    private CheckBox consonanceCheckBox;
    @FXML
    private CheckBox varietyCheckBox;
    @FXML
    private CheckBox smoothCheckBox;

    // Crossover Settings
    @FXML
    private ComboBox<String> crossoverMethodComboBox;

    // Mutation Settings
    @FXML
    private CheckBox pitchMutationCheckBox;
    @FXML
    private CheckBox rhythmicMutationCheckBox;
    @FXML
    private CheckBox notewiseMutationCheckBox;
    @FXML
    private TextField mutationRateField;
    
    // Selection Settings
    @FXML
    private ComboBox<String> selectionMethodComboBox;
    @FXML
    private TextField tournamentSizeTextField;
    
    @FXML
    private Button startGAButton;

    // Canvas to draw staff
    @FXML
    private Canvas staffCanvas;

    // Advanced Settings
    @FXML private TitledPane advancedSettings;

    // Melody Columns
    @FXML
    private ListView<Pair<String, Individual>> currentMelodyList;

    @FXML
    private ListView<Pair<String, Individual>> savedMelodyList;

    private Population pop;
    private ArrayList<IFitnessFunction> selectedFitness;
    private ICrossoverMechanism selectedXover;
    private ISelectionMechanism selectedSelection;
    private IMutationMechanism selectedMutation;

    @FXML
    private void startGA() throws MidiUnavailableException, InvalidMidiDataException {

        interface Validator { String ensureParseableInt(String s); };
        Validator numberString = (String val) -> (NumberUtils.isParsable(val) ? val : "10");

        int populationCount = parseTextFieldOrDefault(populationCountField, 100);
        int numberOfNotes = parseTextFieldOrDefault(numberOfNotesField, 10);
        int numberOfGenerations = parseTextFieldOrDefault(numberOfGenerationsField, 100);
        int generationsBetweenInteraction = parseTextFieldOrDefault(generationsBetweenInteractionField, 25);
        double mutationRate = parseTextFieldOrDefaultDouble(mutationRateField, 0.2);
        boolean elitism = this.elitismCheckBox.isSelected();


        selectMutationMethod();
        

        /* Here you should have access to initalizing a population with the available mechanisms */
        this.pop = new Population(
                populationCount,
                numberOfNotes,
                elitism,
                mutationRate,
                new MultipleFitness(selectedFitness),
                selectedSelection,
                selectedXover,
                selectedMutation,
                new BoundedGenerationStop(numberOfGenerations)
        );
        
        // Update the start button
        startGAButton.setStyle("-fx-background-color: red;");
        startGAButton.setText("Running...");

        // Set the listener so the GUI can be triggered to respond to a stop
        pop.setEvolutionStopListener(this);
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

    /**
     * Styling Methods
     */

     @FXML
     private void initialize() {
        advancedSettings.setExpanded(false);
        customizeMelodyListView(currentMelodyList);
        customizeMelodyListView(savedMelodyList);

        selectedFitness = new ArrayList<>();
        // Add event handlers to play the selected melody and update staff visual
        currentMelodyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                playMelodyAndDrawStaff(newValue.getValue().getMelody()); // Assuming getMelody() method exists in the Individual class
            }
        });

        savedMelodyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                playMelodyAndDrawStaff(newValue.getValue().getMelody()); // Assuming getMelody() method exists in the Individual class
            }
        });
    }

    private int parseTextFieldOrDefault(TextField textField, int defaultValue) {
        String text = textField.getText();
        if (text.isEmpty()) {
            return defaultValue;
        } else {
            return Integer.parseInt(text);
        }
    }

    private double parseTextFieldOrDefaultDouble(TextField textField, double defaultValue) {
        String text = textField.getText();
        if (text.isEmpty()) {
            return defaultValue;
        } else {
            return Double.parseDouble(text);
        }
    }
    @FXML
    private void selectFitnessMethod() {
        if (this.consonanceCheckBox.isSelected()) {
            selectedFitness.add(new ConsonanceFitness());
        }
        if (this.varietyCheckBox.isSelected()) {
            selectedFitness.add(new VarietyFitness());
        }
        if (this.smoothCheckBox.isSelected()) {
            selectedFitness.add(new VarietyFitness());
        }
    }

    @FXML
    private void selectCrossoverMethod() {
        String selectedCrossoverMethod = crossoverMethodComboBox.getValue();
        switch (selectedCrossoverMethod) {
            case "One Point Crossover":
            selectedXover = new OnePointCrossover();
                break;
            case "Two Point Crossover":
            selectedXover = new TwoPointCrossover();
                break;
            case "Uniform Crossover":
            selectedXover = new UniformCrossover();
                break;
            case "Measure Crossover":
            selectedXover = new MeasureCrossover();
                break;
            default:
            selectedXover = new OnePointCrossover();
                break;
        }
    }
    
    @FXML
    private void selectMutationMethod() {
        ArrayList<IMutationMechanism> selectedMutations = new ArrayList<>();

        if (pitchMutationCheckBox.isSelected()) {
            selectedMutations.add(new PitchMutation());
        }
        if (rhythmicMutationCheckBox.isSelected()) {
            selectedMutations.add(new RhythmicMutation());
        }
        if (notewiseMutationCheckBox.isSelected()) {
            selectedMutations.add(new NotewiseMutation());
        }

        if (!selectedMutations.isEmpty()) {
            selectedMutation = new MultipleMutation(selectedMutations);
        } else {
            selectedMutation = new PitchMutation();
        }
    }

    @FXML
    private void selectSelectionMethod() {
        String selectedMethod = selectionMethodComboBox.getValue();
        switch (selectedMethod) {
            case "Fitness Proportional":
                selectedSelection = new FitnessProportionalSelection();
                tournamentSizeTextField.setVisible(false);
                break;
            case "Tournament":
                // Show the input field to input Tournament Size
                tournamentSizeTextField.setVisible(true);
                tournamentSizeTextField.setText("5");
                selectedSelection = new TournamentSelection(5);
                break;
            default:
                selectedSelection = new FitnessProportionalSelection();
                // Hide the input field by default
                tournamentSizeTextField.setVisible(false);
                break;
        }
    }


     @FXML
    private void toggleAdvancedSettings() {
        boolean isExpanded = advancedSettings.isExpanded();

        advancedSettings.setExpanded(!isExpanded);
    }
    
    @FXML
    private void moveMelodyRight() {
        if (currentMelodyList != null && savedMelodyList != null) {
            int selectedIndex = currentMelodyList.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                ObservableList<Pair<String, Individual>> selectedItems = currentMelodyList.getSelectionModel().getSelectedItems();
                int insertionIndex = savedMelodyList.getSelectionModel().isEmpty() ? savedMelodyList.getItems().size() : savedMelodyList.getSelectionModel().getSelectedIndex();
                savedMelodyList.getItems().addAll(insertionIndex, selectedItems);
                currentMelodyList.getItems().removeAll(selectedItems);

                // Sort the saved melodies by fitness value
                sortMelodiesByFitness(savedMelodyList);
            }
        }
    }

    @FXML
    private void moveMelodyLeft() {
        if (currentMelodyList != null && savedMelodyList != null) {
            int selectedIndex = savedMelodyList.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                ObservableList<Pair<String, Individual>> selectedItems = savedMelodyList.getSelectionModel().getSelectedItems();
                int insertionIndex = currentMelodyList.getSelectionModel().isEmpty() ? currentMelodyList.getItems().size() : currentMelodyList.getSelectionModel().getSelectedIndex();
                currentMelodyList.getItems().addAll(insertionIndex, selectedItems);
                savedMelodyList.getItems().removeAll(selectedItems);

                // Sort the current melodies by fitness value
                sortMelodiesByFitness(currentMelodyList);
            }
        }
    }

    private void sortMelodiesByFitness(ListView<Pair<String, Individual>> listView) {
        ObservableList<Pair<String, Individual>> items = listView.getItems();
        items.sort((pair1, pair2) -> Double.compare(pair2.getValue().getFitness(), pair1.getValue().getFitness()));
    }

    
    @Override
public void evolutionStopped() {
    ObservableList<Pair<String, Individual>> melodyItems = FXCollections.observableArrayList();

    ArrayList<Individual> individuals = pop.getIndividuals();

    // Populate the list with melodies
    for (int i = 0; i < individuals.size(); i++) {
        Individual individual = individuals.get(i);

        String melodyName = "Melody " + (i + 1);

        melodyItems.add(new Pair<>(melodyName, individual));
    }

    // Sort the melodies based on fitness value
    melodyItems.sort((pair1, pair2) -> Double.compare(pair2.getValue().getFitness(), pair1.getValue().getFitness()));

    currentMelodyList.setItems(melodyItems);

    // Update the start button
    startGAButton.setStyle("-fx-background-color: orange;");
    startGAButton.setText("Click to Continue...");
}



    @FXML
private void openCredits() {
    Alert creditsAlert = new Alert(AlertType.INFORMATION);
    creditsAlert.setTitle("Credits");
    creditsAlert.setHeaderText("Designed for... TODO");
    creditsAlert.setContentText("This TODO WAS TODO'D:\n\n"
        + "- TODO\n"
        + "- TODO\n"
        + "- And... TODO\n\n"
        + "TODO");

    creditsAlert.showAndWait();
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

    private void playMelodyAndDrawStaff(ArrayList<Note> melody) {
        // Play the melody
        Individual individual = new Individual(melody);
        individual.playMelody();
    
        // Draw the staff visual
        drawStaffAndNotes(melody);
    }

    public void drawStaffAndNotes(ArrayList<Note> melody) {
        GraphicsContext gc = staffCanvas.getGraphicsContext2D();
    
        // Clef Images
        InputStream trebleStream = getClass().getResourceAsStream("/assets/treble.png");
        Image trebleClef = new Image(trebleStream);
        InputStream bassStream = getClass().getResourceAsStream("/assets/bass.png");
        Image bassClef = new Image(bassStream);

        // Clear previous drawings
        gc.clearRect(0, 0, staffCanvas.getWidth(), staffCanvas.getHeight());
    
        double lineSpacing = 10;
        double startY = 50;
        double bassClefStartY = startY + 5.7 * lineSpacing;
        double clefX = 0;
        double clefYOffset = 20;

        // Draw treble clef staff lines
        for (int i = 0; i < 5; i++) {
            double y = startY + lineSpacing * i;
            gc.strokeLine(35, y, staffCanvas.getWidth() - 10, y);
        }
        gc.drawImage(trebleClef, clefX, startY + clefYOffset - trebleClef.getHeight() / 2);

        // Draw bass clef staff lines
        for (int i = 0; i < 5; i++) {
            double y = bassClefStartY + lineSpacing * i;
            gc.strokeLine(35, y, staffCanvas.getWidth() - 10, y);
        }
        gc.drawImage(bassClef, clefX, bassClefStartY + clefYOffset - bassClef.getHeight() / 2);


        final double[] noteX = {40}; // Has to be defined this way to be used in the lambda
        double noteSpacing = 20; // Horizontal space between notes
        melody.forEach(note -> {
            Optional<Integer> pitchOpt = note.getPitch();
            if (pitchOpt.isPresent()) {
                int pitch = pitchOpt.get();
                boolean isSharp = note.isSharp();
                double staffStartY = pitch >= 60 ? startY : bassClefStartY;
                double noteY = pitchToPositionOnStaff(pitch, staffStartY, lineSpacing, pitch >= 60);
        
                if (isSharp) {
                    gc.fillText("#", noteX[0], noteY);
                    noteX[0] += 5; // Adjust for sharp
                    // Draw the note
                    gc.fillOval(noteX[0], noteY - 5, 10, 10);
                    noteX[0] -= 5;
                }
                else {
                    gc.fillOval(noteX[0], noteY - 5, 10, 10);
                }
            } else {
                // Draw a rest symbol
                gc.fillText("R", noteX[0] + 5, startY + 2 * lineSpacing);
            }
        
            noteX[0] += noteSpacing; // Move to the next position
        });
    }
    
    private double pitchToPositionOnStaff(int pitch, double startY, double lineSpacing, boolean isTreble) {
        double trebleStepsFromC4 = (pitch - 60) * 0.6; // Calculate steps from Middle C
        double bassStepsFromC4 = (pitch - 60) * 0.45;
        if (isTreble) {
            // For treble, move up for higher pitches from Middle C
            return startY + 5.1 * lineSpacing - (trebleStepsFromC4 * (lineSpacing / 2));
        } else {
            // For bass, start below Middle C and move down
            return startY - 0.27 * lineSpacing - (bassStepsFromC4 * (lineSpacing / 2));
        }
    }


    // Custom cell factory to customize melodies in list
    private void customizeMelodyListView(ListView<Pair<String, Individual>> listView) {
        listView.setCellFactory(new Callback<ListView<Pair<String, Individual>>, ListCell<Pair<String, Individual>>>() {
            @Override
            public ListCell<Pair<String, Individual>> call(ListView<Pair<String, Individual>> param) {
                return new ListCell<Pair<String, Individual>>() {
                    @Override
                    protected void updateItem(Pair<String, Individual> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            // Set the text of the cell to the melody name and fitness value
                            setText(item.getKey() + ": (" + String.format("%.2f", item.getValue().getFitness()) + ")");
                        }
                    }
                };
            }
        });
    }
    

}
