import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import java.io.IOException;
import javax.sound.midi.*;
import javafx.application.Platform;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.Node;
import java.util.Scanner;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import javafx.scene.control.Alert;

public class MidiRecorderGUI extends Application {
    private MidiRecorder recorder;
    private Label instrumentLabel;
    private Text statusText;
    private Synthesizer synthesizer;
    private MidiChannel[] midiChannels;
    private Rectangle[] pianoKeys;
    private int volume = 80;
    private static MidiDevice.Info selectedMidiDeviceInfo;
    
    @Override
    public void start(Stage primaryStage) {
        // Prompt user to select a MIDI device
        List<MidiDevice.Info> devices = List.of(MidiSystem.getMidiDeviceInfo());
        ChoiceDialog<MidiDevice.Info> dialog = new ChoiceDialog<>(devices.get(0), devices);
        dialog.setTitle("Select MIDI Device");
        dialog.setHeaderText("Choose your MIDI device:");
        dialog.setContentText("Available devices:");
        
        // JavaFX dialog
        Optional<MidiDevice.Info> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                selectedMidiDeviceInfo = result.get();
                recorder = new MidiRecorder(selectedMidiDeviceInfo);
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                midiChannels = synthesizer.getChannels();
                midiChannels[0].programChange(0);
                
                BorderPane root = new BorderPane();
                VBox topContainer = new VBox(5);
                setupUIComponents(topContainer);
                root.setTop(topContainer);
                root.setBottom(createCenteredPianoKeyboard());
    
                Scene scene = new Scene(root, 400, 300);
                prepareScene(scene);
                primaryStage.setTitle("MIDI Recorder");
                primaryStage.setScene(scene);
                primaryStage.show();
                
                root.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error initializing MIDI device", "An error occurred while initializing the MIDI device: " + e.getMessage());
                Platform.exit();
            }
        } else {
            System.out.println("No device selected!");
            Platform.exit();
        }
    }
    
    private void setupUIComponents(VBox topContainer) {
        if (instrumentLabel == null) {
            instrumentLabel = new Label("Instrument: 0");
            instrumentLabel.getStyleClass().add("instrument-text");
        }
        if (statusText == null) {
            statusText = new Text("Ready to record.");
            statusText.getStyleClass().add("status-text");
        }
        
        setupButtons(topContainer);
        
        if (!topContainer.getChildren().contains(instrumentLabel)) {
            topContainer.getChildren().add(instrumentLabel);
        }
        if (!topContainer.getChildren().contains(statusText)) {
            topContainer.getChildren().add(statusText);
        }
    }
    
    private void prepareScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);
        scene.getStylesheets().add("style.css");
    }
    
    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupButtons(VBox root) {
        Button startButton = new Button("Start Recording");
        Button stopButton = new Button("Stop Recording");
        stopButton.setDisable(true);
    
        Button incrementInstrumentButton = new Button("Next Instrument");
        Button decrementInstrumentButton = new Button("Previous Instrument");
    
        startButton.setOnAction(event -> {
            new Thread(() -> {
                try {
                    recorder.startRecording();
                    Platform.runLater(() -> {
                        statusText.setText("Recording...");
                        startButton.setDisable(true);
                        stopButton.setDisable(false);
                    });
                } catch (MidiUnavailableException e) {
                    Platform.runLater(() -> {
                        statusText.setText("Error: Failed to start recording.");
                        startButton.setDisable(false);
                    });
                }
            }).start();
        });
        stopButton.setOnAction(event -> {
            new Thread(() -> {
                recorder.stopRecording();
                try {
                    recorder.saveToFile("MyMidiRecording.mid");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                Platform.runLater(() -> {
                    statusText.setText("Stopped. Ready to record.");
                    startButton.setDisable(false);
                    stopButton.setDisable(true);
                });
            }).start();
        });
        incrementInstrumentButton.setOnAction(event -> {
            new Thread(() -> {
                midiChannels[0].programChange(recorder.changeInstrument(1));
                
                Platform.runLater(() -> {
                    updateInstrumentDisplay();
                });
            }).start();
        });
        decrementInstrumentButton.setOnAction(event -> {
            new Thread(() -> {
                midiChannels[0].programChange(recorder.changeInstrument(-1));
                Platform.runLater(() -> {
                    updateInstrumentDisplay();
                });
            }).start();
        });
        root.getChildren().addAll(instrumentLabel, startButton, stopButton, incrementInstrumentButton, decrementInstrumentButton, statusText);
    }

    private void updateInstrumentDisplay() {
        Platform.runLater(() -> {
            instrumentLabel.setText("Instrument: " + recorder.getCurrentInstrument());
        });
    }

    private Node createCenteredPianoKeyboard() {
        Pane keyboardPane = createPianoKeyboard();
        HBox wrapper = new HBox(keyboardPane);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }
    
    private Pane createPianoKeyboard() {
        Pane keyboard = new Pane();
        double whiteKeyWidth = 40;
        double whiteKeyHeight = 120;
        double blackKeyWidth = 30;
        double blackKeyHeight = 80;
        int numberOfKeys = 12;
        pianoKeys = new Rectangle[numberOfKeys];
        
        double xPosition = 0;
        
        // Create keys based on their color
        for (int i = 0; i < numberOfKeys; i++) {
            if (isWhiteKey(i)) {
                Rectangle whiteKey = new Rectangle(xPosition, 0, whiteKeyWidth, whiteKeyHeight);
                whiteKey.setFill(Color.WHITE);
                whiteKey.setStroke(Color.BLACK);
                whiteKey.getStyleClass().add("white-key");
                pianoKeys[i] = whiteKey;
                keyboard.getChildren().add(whiteKey);
                xPosition += whiteKeyWidth;
            } else {
                double blackKeyXPosition = xPosition - blackKeyWidth / 2;
                Rectangle blackKey = new Rectangle(blackKeyXPosition, 0, blackKeyWidth, blackKeyHeight);
                blackKey.setFill(Color.BLACK);
                blackKey.getStyleClass().add("black-key");
                pianoKeys[i] = blackKey;
                keyboard.getChildren().add(blackKey);
            }
        }
        
        keyboard.setPrefSize(xPosition, whiteKeyHeight);
        return keyboard;
    }

    public boolean isWhiteKey(int index) {
        // The order of white keys on a keyboard for an octave
        boolean[] octavePattern = {true, false, true, false, true, true, false, true, false, true, false, true};
        return octavePattern[index % 12];
    }

    public boolean isBlackKey(int index) {
        return !isWhiteKey(index);
    }

    private void handleKeyPress(KeyEvent event) {
        Integer midiNote = recorder.mapKeyToMidiNote(event.getText().toLowerCase());
        if (midiNote != null) {
            int noteIndex = midiNoteToIndex(midiNote);
            if (noteIndex != -1) {
                Platform.runLater(() -> {
                    Rectangle key = pianoKeys[noteIndex];
                    if (key != null) key.setFill(Color.GRAY);
                });
                midiChannels[0].noteOn(midiNote, volume);
            }
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        Integer midiNote = recorder.mapKeyToMidiNote(event.getText().toLowerCase());
        if (midiNote != null) {
            int noteIndex = midiNoteToIndex(midiNote);
            if (noteIndex != -1) {
                Platform.runLater(() -> {
                    Rectangle key = pianoKeys[noteIndex];
                    if (key != null) {
                        key.setFill(isBlackKey(noteIndex) ? Color.BLACK : Color.WHITE);
                    }
                });
                midiChannels[0].noteOff(midiNote);
            }
        }
    }

        private int midiNoteToIndex(int midiNote) {
        int relativeNote = (midiNote - 60) % 12;
        return relativeNote;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
