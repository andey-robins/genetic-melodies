module geneticMelodies {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    opens app to javafx.fxml;

    exports app;
    exports midi;
}
