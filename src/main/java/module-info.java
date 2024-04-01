module com.github.andeyrobins {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.andeyrobins to javafx.fxml;

    exports com.github.andeyrobins;
    exports com.github.ahmedmansour3548;
}
