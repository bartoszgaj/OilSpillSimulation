package Presentation;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Tile extends StackPane {
    private static final int TILE_SIZE = 3;

    public Tile(double oilLevel) {
        Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);

        border.setFill(Color.grayRgb((int)oilLevel));



        getChildren().add(border);
    }
}