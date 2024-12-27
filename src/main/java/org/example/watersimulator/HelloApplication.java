package org.example.watersimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Number to decide the number of tiles in the grid

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        createUI(stage);
    }

    public void createUI(Stage stage){
        // Root basic setup
        VBox root = new VBox(30);

        // Creation of elements
        Label label = new Label("Water simulator");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        // Creation of elements: Gridpane
        GridPane grid = initializeGrid();

        // Add elements to and set up root
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.getChildren().setAll(label, grid);

        // Set up UI and show
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public GridPane initializeGrid(){
        GridPane grid = new GridPane();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle tile = new Rectangle(40, 40, Color.web("#ebebeb"));
                tile.setStroke(Color.web("#bfbfbf"));
                tile.setStrokeWidth(1);
                grid.add(tile, j, i);
            }
        }

        return grid;
    }

}