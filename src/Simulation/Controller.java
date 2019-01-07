package Simulation;

import Presentation.Tile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Controller {

    Area area;

    @FXML
    private GridPane map;

    @FXML
    private Text windSpeedText;
    @FXML
    private Text waterSpeedText;
    @FXML
    private Slider windSpeedSlider;
    @FXML
    private Slider waterSpeedSlider;
    @FXML
    private Text timestampText;
    @FXML
    private Slider timestampSlider;
    @FXML
    private ChoiceBox<String> waterDirection;
    @FXML
    private ChoiceBox<String> windDirection;


    public void initialize() {
        area = new Area(100);
        printGrid(area);

        waterSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                waterSpeedText.setText("Siła = " + String.format("%.1f", newValue));
            }
        });

        windSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windSpeedText.setText("Siła = " + String.format("%.1f", newValue));
            }
        });

        timestampSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                timestampText.setText("Liczba kroków czasowych = " + String.format("%.0f", newValue));
            }
        });
    }

    @FXML
    void startSimulation(ActionEvent event) {
        area.generateRandomSpillSource();
        printGrid(area);
        windSpeedSlider.setDisable(true);
        waterSpeedSlider.setDisable(true);
        windDirection.setDisable(true);
        waterDirection.setDisable(true);

        System.out.println("SYMULACJA ZOSTAŁA WYSTARTOWANA");
    }

    @FXML
    void iterateSimulation(ActionEvent event) {
        area.checkOilForCircle();
        printGrid(area);

        System.out.println("USTALONY TIMESTAMP ROZCHODZENIA");
    }

    @FXML
    void resetSimulation(ActionEvent event) {
        area = new Area(100);
        printGrid(area);

        windSpeedSlider.setDisable(false);
        waterSpeedSlider.setDisable(false);
        windDirection.setDisable(false);
        waterDirection.setDisable(false);

        System.out.println("SYMULACJA ZOSTAŁA ZRESETOWANA");
    }






    public void printGrid(Area area) {

        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                map.add(new Tile(area.getCell(i, j).getOilLevel(), area.getCell(i, j).getType()), i, j);
            }
        }
    }


}
