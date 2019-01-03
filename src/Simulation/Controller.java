package Simulation;

import Presentation.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private GridPane map;



    public void createGrid (int size){

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map.add(new Tile(),i,j);
            }
        }
    }

}
