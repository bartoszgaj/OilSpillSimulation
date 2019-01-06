package Presentation;

import Simulation.Type;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;


public class Tile extends StackPane {
    private static final int TILE_SIZE = 3;

    private int oilLevel;
    private Type  type;

    private Color oilColor = Color.web("rgb(0%, 0%, 0%)"); //black
    private Color oilColor1 = Color.web("rgb(0%, 0%, 10%)"); // black -> blue
    private Color oilColor2 = Color.web("rgb(0%, 0%, 30%)");
    private Color oilColor3 = Color.web("rgb(0%, 0%, 50%)");
    private Color oilColor4 = Color.web("rgb(0%, 0%, 70%)");
    private Color water = Color.web("rgb(0%, 0%, 100%)"); // blue


    public Tile(double oilLevel, Type type) {
        this.oilLevel = (int) oilLevel;
        this.type = type;

        Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);

        switch (this.type) {
            case WATER:
                border.setFill(this.water);
                break;
            case OIL:
                border.setFill(this.oilColor);
                if (oilLevel < 1.0E-15) border.setFill(this.water);
                else if (oilLevel < 1.0E-12) border.setFill(this.oilColor4);
                else if (oilLevel < 1.0E-9) border.setFill(this.oilColor3);
                else if (oilLevel < 1.0E-6) border.setFill(this.oilColor2);
                else if (oilLevel < 1.0E-3) border.setFill(this.oilColor1);
                break;
        }

        getChildren().add(border);
    }


}