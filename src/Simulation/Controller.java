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
    private Label positionText;

    //STATISTICS
    @FXML
    private Label oilStart;
    @FXML
    private Label waterStart;
    @FXML
    private Label coastStart;
    @FXML
    private Label landStart;
    @FXML
    private Label sourceStart;

    @FXML
    private Label oilEnd;
    @FXML
    private Label waterEnd;
    @FXML
    private Label coastEnd;
    @FXML
    private Label landEnd;
    @FXML
    private Label sourceEnd;

    @FXML
    private Label areaText;
    @FXML
    private Label volumeText;
    @FXML
    private Label evaporationText;



    public void initialize() {
        this.area = new Area(300);
        createGrid(this.area);
        positionText.setVisible(false);


        this.iterateButton.setDisable(true);

        this.waterSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                waterSpeedText.setText("Siła = " + String.format("%.1f", newValue) + " m/s");
            }
        });

        this.windSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windSpeedText.setText("Siła = " + String.format("%.1f", newValue) + " m/s");
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
                positionText.setVisible(true);
                String msg = "x: " + event.getX() + "   y: " + event.getY();
                positionText.setText(msg);
            }
        });
        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Wybrany punkt");
                if(area.getCell(((int) event.getY())/2,((int) event.getX())/2).getType() == Type.WATER) {
                    area.generateSpillSource(((int) event.getY()) / 2, ((int) event.getX()) / 2);
                    printGrid(area);
                }
            }
        });
        map.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                positionText.setVisible(false);
            }
        });
    }

    public void setParameters() {
        double windSpeedSlider = this.windSpeedSlider.getValue() / 100;
        double waterSpeedSlider = this.waterSpeedSlider.getValue() / 100;

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
        setStartStatistict();

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
                setEndStatistict();
                setOverallStatistics();
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
                map.add(new Tile(area.getCell(i, j).getOilLevel(), area.getCell(i, j).getType(), j, i), j, i);
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

    public void setStartStatistict(){
        int[] statistics = Statistics.getCellsTypeInfo(area);
        oilStart.setText(String.valueOf(statistics[0]));
        sourceStart.setText(String.valueOf(statistics[1]));
        waterStart.setText(String.valueOf(statistics[2]));
        coastStart.setText(String.valueOf(statistics[3]));
        landStart.setText(String.valueOf(statistics[4]));
    }
    public void setEndStatistict(){
        int[] statistics = Statistics.getCellsTypeInfo(area);
        oilEnd.setText(String.valueOf(statistics[0]));
        sourceEnd.setText(String.valueOf(statistics[1]));
        waterEnd.setText(String.valueOf(statistics[2]));
        coastEnd.setText(String.valueOf(statistics[3]));
        landEnd.setText(String.valueOf(statistics[4]));
    }

    public void setOverallStatistics(){
        areaText.setText("Powierzchnia rozlewu: " + oilEnd.getText());
        volumeText.setText("Objętośc oleju: " + String.format("%.2f", Statistics.getOverallOilLevel(area)));
        evaporationText.setText("Wyparowana objętość: " + String.format("%.2f", 400 - Statistics.getOverallOilLevel(area)));
    }
}
