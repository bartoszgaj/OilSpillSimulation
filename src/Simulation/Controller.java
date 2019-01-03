package Simulation;

import Presentation.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private GridPane map;



    public void printGrid (Area area){

        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                map.add(new Tile(area.getCell(i,j).getOilLevel()),i,j);
            }
        }
    }

}
