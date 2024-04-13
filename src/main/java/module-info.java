module geneticMelodies {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javatuples;
    requires javafx.graphics;
    requires org.apache.commons.lang3;
    requires javafx.base;

    opens app to javafx.fxml;

    exports app;
    exports midi;
}
