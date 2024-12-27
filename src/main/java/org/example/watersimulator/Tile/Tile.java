package org.example.watersimulator.Tile;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {

    private Rectangle rectangle;
    private double width;
    private TileState state;

    public Tile(double width) {
        this.width = width;
        this.rectangle = new Rectangle(width, width);
        this.state = TileState.EMPTY;
        updateRectangle();
    }

    // Setters and getters
    public void setState(TileState state) {
        this.state = state;
        updateRectangle();
    }
    public TileState getState() {return state;}
    public Rectangle getRectangle() {return rectangle;}

    // Other methods
    private void updateRectangle(){
        this.rectangle.setStroke(Color.web("#bfbfbf"));
        this.rectangle.setStrokeWidth(0);
        switch(this.state){
            case TileState.EMPTY:
                this.rectangle.setFill(Color.web("#ebebeb"));
                break;
            case TileState.WATER:
                this.rectangle.setFill(Color.web("#4782ff"));
                break;
            case TileState.SOLID:
                this.rectangle.setFill(Color.web("#000000"));
        }
    }

}
