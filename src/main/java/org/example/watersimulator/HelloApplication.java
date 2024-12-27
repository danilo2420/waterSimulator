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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.watersimulator.Tile.Tile;
import org.example.watersimulator.Tile.TileState;

import java.io.IOException;
import java.util.Random;

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
    Button btnClear;

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
        VBox root = new VBox(10);

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

        btnClear = new Button("Clear grid");

        // Add elements to and set up root
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.getChildren().setAll(lblTitle, VBinstructions, grid, btnStart, btnClear);

        // Set up UI and show
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setResizable(false);
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
                btnClear.setDisable(true);
            }else{
                isSimulationRunning = false;
                //checkKeys = true; (moved to end of simulation logic)
                btnStart.setText("Start simulation");
                btnClear.setDisable(false);
            }
        });

        btnClear.setOnAction(event -> {
            if(!isSimulationRunning){
                washGrid();
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
            runIteration();
            pause(1000);
        }
        washGrid();
        checkKeys = true;
    }

    public void runIteration(){
        Random random = new Random();
        for (int i = 0; i < tiles.length; i++) {
            if(random.nextBoolean()){
                for (int j = 0; j < tiles[i].length; j++) {
                    updateCell(i, j);
                }
            }else{
                for (int j = tiles[i].length - 1; j >= 0; j--) {
                    updateCell(i, j);
                }
            }
        }

    }

    // TODO: we have to take into account solids as well

    // this is the logic for an iteration of a particular cell
    public void updateCell(int i, int j){
        Tile current = tiles[i][j];
        if(current.getState() == TileState.WATER){
            // look down
            if(lookDown(i, j)) return;

            // look at one side
            Random random = new Random();
            if(random.nextBoolean()){
                if(lookOver(i, j, true)) return;
                lookOver(i, j, false);
            }else{
                if(lookOver(i, j, false)) return;
                lookOver(i, j, true);
            }
        }
    }

    public boolean lookDown(int i, int j){
        Tile current = tiles[i][j];
        if(i < tiles.length - 1 && tiles[i+1][j].getState() == TileState.EMPTY){
            swapCells(current, tiles[i+1][j]);
            return true;
        }
        return false;
    }

    public boolean lookOver(int i, int j, boolean lookRight){
        Tile current = tiles[i][j];
        if(lookRight){
            if(j >= tiles[i].length - 1) return false;

            Tile other = tiles[i][j+1];
            if(other.getState() == TileState.EMPTY){
                swapCells(current, other);
                return true;
            }
            return false;
        }else{
            if(j <= 0) return false;

            Tile other = tiles[i][j-1];
            if(other.getState() == TileState.EMPTY){
                swapCells(current, other);
                return true;
            }
            return false;
        }
    }

    public void swapCells(Tile tile1, Tile tile2){
        TileState aux = tile1.getState();
        tile1.setState(tile2.getState());
        tile2.setState(aux);
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