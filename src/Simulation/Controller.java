package Simulation;

import Presentation.Tile;
import javafx.application.Platform;
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
    @FXML
    private Button startButton;
    @FXML
    private Button iterateButton;


    public void initialize() {
        this.area = new Area(100);
        printGrid(this.area);

        this.iterateButton.setDisable(true);

        this.waterSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                waterSpeedText.setText("Siła = " + String.format("%.1f", newValue));
            }
        });

        this.windSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windSpeedText.setText("Siła = " + String.format("%.1f", newValue));
            }
        });

        this.timestampSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                timestampText.setText("Liczba kroków czasowych = " + String.format("%.0f", newValue));
            }
        });
    }

    @FXML
    void startSimulation(ActionEvent event) {


        this.area.setSimulationParameters(this.windDirection.getValue(), this.windSpeedSlider.getValue(), this.waterDirection.getValue(), this.waterSpeedSlider.getValue());

        this.area.generateRandomSpillSource();
        printGrid(this.area);
        this.windSpeedSlider.setDisable(true);
        this.waterSpeedSlider.setDisable(true);
        this.windDirection.setDisable(true);
        this.waterDirection.setDisable(true);
        this.startButton.setDisable(true);
        this.iterateButton.setDisable(false);

//        area.printSimulationParameters();

        System.out.println("SYMULACJA ZOSTAŁA WYSTARTOWANA");
    }

    @FXML
    void iterateSimulation(ActionEvent event) {
        System.out.println(this.timestampSlider.getValue());
        Integer numberOfInterations = (int) this.timestampSlider.getValue();
        System.out.println(numberOfInterations);


        int iteration = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                iterate(numberOfInterations, iteration);
            }
        }).start();

        System.out.println("SYMULUJE " + numberOfInterations + " TIMESTAMPÓW ROZCHODZENIA");
    }

    public void iterate(Integer numberOfInterations, int iteration) {
        int ite = iteration + 1;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                area.checkOilForCircle();
                printGrid(area);

                System.out.println("WYKONANO " + (ite) + " ITERACJI");

                if (numberOfInterations - 1 != 0) {
                    iterate(numberOfInterations - 1, ite);
                }
            }
        });
    }

    @FXML
    void resetSimulation(ActionEvent event) {
        this.area = new Area(100);
        printGrid(area);

        this.windSpeedSlider.setDisable(false);
        this.waterSpeedSlider.setDisable(false);
        this.windDirection.setDisable(false);
        this.waterDirection.setDisable(false);
        this.startButton.setDisable(false);
        this.iterateButton.setDisable(true);

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
