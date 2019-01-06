package Simulation;

import Presentation.Tile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private GridPane map;
    @FXML
    private Button start;
    @FXML
    private Button iterate;

    @FXML
    private TextField windx;
    @FXML
    private TextField windy;
    @FXML
    private TextField timestamp;
    // BUTTON

    public Button getStartButton(){
        return start;
    }

    public Button getIterate() {
        return iterate;
    }

    public Integer getWindX(){
        return Integer.valueOf(windx.getText());
    }

    public Integer getWindY(){
        return Integer.valueOf(windy.getText());
    }
    public Integer getTimestamp(){
        return Integer.valueOf(timestamp.getText());
    }




    public void printGrid (Area area){

        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                map.add(new Tile(area.getCell(i,j).getOilLevel(), area.getCell(i,j).getType()),i,j);
            }
        }
    }

}
