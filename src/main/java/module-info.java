module geneticMelodies {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javatuples;

    opens app to javafx.fxml;

    exports app;
    exports midi;
}
