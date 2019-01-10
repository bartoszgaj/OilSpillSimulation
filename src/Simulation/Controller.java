package Simulation;

import Presentation.Tile;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static java.lang.Thread.sleep;

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
    private Text temperatureText;
    @FXML
    private Slider temperatureSlider;
    @FXML
    private ChoiceBox<String> waterDirection;
    @FXML
    private ChoiceBox<String> windDirection;
    @FXML
    private Button startButton;
    @FXML
    private Button iterateButton;
    @FXML
    private Label position;


    public void initialize() {
        this.area = new Area(300);
        createGrid(this.area);

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

        this.temperatureSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                temperatureText.setText("Temperatura = " + String.format("%.0f", newValue) + " stopni Celsjusza");
            }
        });

        map.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg = "x: " + event.getX() + "   y: " + event.getY();
                position.setText(msg);
            }
        });
        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Wybrany punkt");
                if(area.getCell(((int) event.getY())/3,((int) event.getX())/3).getType() == Type.WATER) {
                    area.generateSpillSource(((int) event.getY()) / 3, ((int) event.getX()) / 3);
                    printGrid(area);
                }
            }
        });
    }

    public void setParameters() {
        double windSpeedSlider = this.windSpeedSlider.getValue() / 20;
        double waterSpeedSlider = this.waterSpeedSlider.getValue() / 20;

        this.area.setSimulationParameters(this.windDirection.getValue(), windSpeedSlider, this.waterDirection.getValue(), waterSpeedSlider, this.temperatureSlider.getValue());
        this.area.generateWindDireciontsPower();
    }

    @FXML
    void startSimulation(ActionEvent event) {

        setParameters();

        if (this.area.sourceX == -1) {
            this.area.generateRandomSpillSource();
        }
        printGrid(this.area);

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

        setParameters();

        this.windSpeedSlider.setDisable(true);
        this.waterSpeedSlider.setDisable(true);
        this.windDirection.setDisable(true);
        this.waterDirection.setDisable(true);
        this.temperatureSlider.setDisable(true);
        this.timestampSlider.setDisable(true);
        this.iterateButton.setDisable(true);

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
        try {
            sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                area.checkOilForCircle();
                printGrid(area);

                System.out.println("WYKONANO " + (ite) + " ITERACJI");

                if (numberOfInterations - 1 != 0) {
                    iterate(numberOfInterations - 1, ite);
                } else {
                    windSpeedSlider.setDisable(false);
                    waterSpeedSlider.setDisable(false);
                    windDirection.setDisable(false);
                    waterDirection.setDisable(false);
                    temperatureSlider.setDisable(false);
                    timestampSlider.setDisable(false);
                    iterateButton.setDisable(false);
                }
            }
        });
    }

    @FXML
    void resetSimulation(ActionEvent event) {
        this.area = new Area(300);
        printGrid(area);

        this.startButton.setDisable(false);
        this.iterateButton.setDisable(true);

        System.out.println("SYMULACJA ZOSTAŁA ZRESETOWANA");
    }

    public void createGrid(Area area) {
        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                map.add(new Tile(area.getCell(i, j).getOilLevel(), area.getCell(i, j).getType(), i, j), i, j);
            }
        }
    }


    public void printGrid(Area area) {
        ObservableList<Node> childrens = map.getChildren();
        for (Node node : childrens) {
            Tile tile = (Tile) node;
            tile.setFill(area.getCell(tile.y, tile.x).getOilLevel(), area.getCell(tile.y, tile.x).getType());
        }
    }


    private Label createMonitoredLabel(final Label reporter) {
        final Label monitored = new Label("Mouse Location Monitor");

        monitored.setStyle("-fx-background-color: forestgreen; -fx-text-fill: white; -fx-font-size: 20px;");

        monitored.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                String msg =
                        "(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " +
                                "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") -- " +
                                "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")";

                reporter.setText(msg);
            }
        });

        monitored.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                reporter.setText("POZA");
            }
        });

        return monitored;
    }
}
