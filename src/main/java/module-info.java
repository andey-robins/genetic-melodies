module geneticMelodies {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javatuples;
    requires javafx.graphics;

    opens app to javafx.fxml;

    exports app;
    exports midi;
}
