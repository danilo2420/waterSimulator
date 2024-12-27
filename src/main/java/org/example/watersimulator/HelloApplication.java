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

    // Note: Grid will be a square
    static final int GRID_WIDTH = 400; // (in pixels)
    static final int TILES_IN_ROW = 25;

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
        // Calculation for dynamic sizing
        double tileWidth = GRID_WIDTH / (TILES_IN_ROW*1.0);

        // Creation of grid
        GridPane grid = new GridPane();
        for (int i = 0; i < TILES_IN_ROW; i++) {
            for (int j = 0; j < TILES_IN_ROW; j++) {
                Rectangle tile = new Rectangle(tileWidth, tileWidth, Color.web("#ebebeb"));
                tile.setStroke(Color.web("#bfbfbf"));
                tile.setStrokeWidth(0.5);
                grid.add(tile, j, i);
            }
        }
        return grid;
    }

}