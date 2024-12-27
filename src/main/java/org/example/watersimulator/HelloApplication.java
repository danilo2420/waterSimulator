package org.example.watersimulator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.watersimulator.Tile.Tile;
import org.example.watersimulator.Tile.TileState;

import java.io.IOException;

public class HelloApplication extends Application {

    // VARIABLES
    // static variables
    // Note: Grid will be a square
    static final int GRID_WIDTH = 400; // (in pixels)
    static final int TILES_IN_ROW = 25;
    static final String TILE_STARTING_COLOR = "#ebebeb";

    // UI components that have to be global
    Scene scene;
    GridPane grid;
    Button btnStart;

    // Other variables
    Thread simulation;
    Tile[][] tiles;
    boolean checkKeys = true;
    boolean isWPressed = false;
    boolean isSPressed = false;
    boolean isSimulationRunning = false;

    // METHODS
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        createAndShowUI(stage);
        initializeEvents();
        // start of logic thread is in the event of btnStart
    }

    // UI CONSTRUCTION
    public void createAndShowUI(Stage stage){
        // Root basic setup
        VBox root = new VBox(15);

        // Creation of elements
        Label lblTitle = new Label("Water simulator");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        VBox VBinstructions = new VBox(5);
        Label lblInstruction1 = new Label("Hold W and drag to create water cells");
        Label lblInstruction2 = new Label("Hold S and drag to create solid cells");
        VBinstructions.setAlignment(Pos.CENTER);
        VBinstructions.getChildren().setAll(lblInstruction1, lblInstruction2);

        initializeGrid(); // grid is global

        btnStart = new Button("Start simulation");

        // Add elements to and set up root
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.getChildren().setAll(lblTitle, VBinstructions, grid, btnStart);

        // Set up UI and show
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    // Creates both UI grid and matrix with references to the tiles (for the logic later on)
    public void initializeGrid(){
        // Calculation for dynamic sizing
        double tileWidth = GRID_WIDTH / (TILES_IN_ROW*1.0);

        // Tiles matrix initialization
        tiles = new Tile[TILES_IN_ROW][TILES_IN_ROW];

        // Creation of grid
        grid = new GridPane();
        for (int i = 0; i < TILES_IN_ROW; i++) {
            for (int j = 0; j < TILES_IN_ROW; j++) {
                Tile tile = new Tile(tileWidth);
                // Construction of the UI
                grid.add(tile.getRectangle(), j, i);

                // Construction of matrix that holds the references to the tiles
                tiles[i][j] = tile;
            }
        }
    }

    // EVENTS
    public void initializeEvents(){
        // Events to check whether a key is pressed (necessary later)
        checkKeysPressed();

        // Events to change the state of the cells
        for(Node node : grid.getChildren()){
            int i = GridPane.getRowIndex(node);
            int j = GridPane.getColumnIndex(node);

            Tile tile = tiles[i][j];
            tile.getRectangle().setOnMouseEntered(event -> {
                if(isWPressed)
                    tile.setState(TileState.WATER);
                if(isSPressed)
                    tile.setState(TileState.SOLID);
            });
        }

        // Event for the start button
        btnStart.setOnAction(event -> {
            if(!isSimulationRunning){
                isSimulationRunning = true;
                checkKeys = false;
                simulation = new Thread(this::runSimulation);
                simulation.start();

                btnStart.setText("Stop simulation");
            }else{
                isSimulationRunning = false;
                //checkKeys = true; (moved to end of simulation logic)
                btnStart.setText("Start simulation");
            }
        });
    }

    // this method allows us to know if a key is pressed (the left click button doesnt work well for some reason)
    public void checkKeysPressed(){
        scene.setOnKeyPressed(event -> {
            if(checkKeys){
                switch(event.getCode()){
                    case KeyCode.W:
                        isWPressed = true;
                        System.out.println("W is pressed");
                        break;
                    case KeyCode S:
                        isSPressed = true;
                        System.out.println("S is pressed");
                        break;
                }
            }
        });
        scene.setOnKeyReleased(event -> {
            if(checkKeys){
                switch(event.getCode()){
                    case KeyCode.W:
                        isWPressed = false;
                        System.out.println("W is released");
                        break;
                    case KeyCode S:
                        isSPressed = false;
                        System.out.println("S is released");
                        break;
                }
            }
        });
    }

    // LOGIC
    public void runSimulation(){
        while(isSimulationRunning) {
            for (int i = 0; i < TILES_IN_ROW; i++) {
                for (int j = 0; j < TILES_IN_ROW; j++) {
                    Tile tile = tiles[i][j];
                    TileState originalState = tile.getState();
                    switch (originalState) {
                        case EMPTY:
                            tile.setState(TileState.WATER);
                            break;
                        case SOLID:
                            tile.setState(TileState.EMPTY);
                            break;
                        case WATER:
                            tile.setState(TileState.SOLID);
                    }

                /*
                washGrid();
                tiles[i][j].setState(TileState.WATER);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                 */
                }
            }
            pause(1000);
        }
        washGrid();
        checkKeys = true;
    }

    public void washGrid(){
        for(Tile[] row : tiles)
            for(Tile tile : row)
                tile.setState(TileState.EMPTY);
    }

    public void pause(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}