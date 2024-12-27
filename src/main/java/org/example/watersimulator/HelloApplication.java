package org.example.watersimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        createUI(stage);
    }

    public void createUI(Stage stage){
        // Root basic setup
        VBox root = new VBox(20);

        // Creation of elements
        Label label = new Label("Water simulator");
        // TODO: Gridpane

        // Add elements to and set up root
        root.getChildren().setAll(label);
        root.setPadding(new Insets(20));

        // Set up UI and show
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}