module org.example.watersimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.watersimulator to javafx.fxml;
    exports org.example.watersimulator;
}