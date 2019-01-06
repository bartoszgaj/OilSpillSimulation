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

    private Color oilColor = Color.BLACK;
    private Color oilColor1 = Color.web("rgb(10%, 10%, 10%)");
    private Color oilColor2 = Color.web("rgb(20%, 20%, 20%)");
    private Color oilColor3 = Color.web("rgb(30%, 30%, 30%)");


    public Tile(double oilLevel, Type type) {
        this.oilLevel = (int) oilLevel;
        this.type = type;

        Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);


        switch (this.type) {
            case WATER:
                border.setFill(Color.BLUE);
                break;
            case OIL:
                border.setFill(oilColor);
                if (oilLevel < 1.0E-15) border.setFill(oilColor3);
                else if (oilLevel < 1.0E-8) border.setFill(oilColor2);
                else if (oilLevel < 1.0E-4) border.setFill(oilColor1);
                break;
        }
//        border.setFill(Color.grayRgb());



        getChildren().add(border);
    }


}