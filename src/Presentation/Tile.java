package Presentation;

import Simulation.Type;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends StackPane {

    private static final int AREA_SIZE = 300;

    private static final int TILE_SIZE = 3;
    public int x;
    public int y;
    public Rectangle square;

    private Color coast = Color.web("rgb(86%, 54%, 4%)"); // orange
    private Color land = Color.web("rgb(0%, 100%, 0%)");
    private Color oilColor = Color.web("rgb(0%, 0%, 0%)");
    private Color oilColor1 = Color.web("rgb(0%, 0%, 10%)"); // black -> blue
    private Color oilColor2 = Color.web("rgb(0%, 0%, 30%)");
    private Color oilColor3 = Color.web("rgb(0%, 0%, 50%)");
    private Color oilColor4 = Color.web("rgb(0%, 0%, 70%)");
    private Color source = Color.web("rgb(100%, 0%, 70%)");
    private Color water = Color.web("rgb(0%, 0%, 100%)");


    public Tile(double oilLevel, Type type, int x, int y) {
        this.x = x;
        this.y = y;
        this.square = new Rectangle(TILE_SIZE, TILE_SIZE);
        this.setFill(oilLevel, type);
        getChildren().add(square);
    }

    public void setFill(double oilLevel, Type type) {
        if (x == 0 || y == 0 || y == AREA_SIZE - 1 || x == AREA_SIZE - 1) {
            square.setFill(Color.BLACK);
        } else {

            switch (type) {
                case COAST:
                    square.setFill(this.coast);
                    break;
                case LAND:
                    square.setFill(this.land);
                    break;
                case OIL:
                    square.setFill(this.oilColor);
                    if (oilLevel < 1.0E-14) {
                        square.setFill(this.water);
                    } else if (oilLevel < 1.0E-11) {
                        square.setFill(this.oilColor4);
                    } else if (oilLevel < 1.0E-8) {
                        square.setFill(this.oilColor3);
                    } else if (oilLevel < 1.0E-5) {
                        square.setFill(this.oilColor2);
                    } else if (oilLevel < 1.0E-2) {
                        square.setFill(this.oilColor1);
                    }
                    break;
                case SOURCE:
                    square.setFill(source);
                    break;
                case WATER:
                    square.setFill(this.water);
                    break;
            }
        }
    }
}
