package Presentation;

import Simulation.Area;
import Simulation.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent) loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Oil Spill Simulation");
        primaryStage.setScene(new Scene(root, 600, 375));

        Button start = controller.getStartButton();
        Area area = new Area(100);
        //TODO move it to controller
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("SYMULACJA ZOSTAŁA WYSTARTOWANA");

                area.generateArea();
                area.generateDefaultParamsAs0();
                area.generateRandomSpillSource();
                controller.printGrid(area);
                primaryStage.show();
            }
        });

        Button iterate = controller.getIterate();
        iterate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("POJEDYŃCZY TIMESTAMP ROZCHODZENIA");
                area.generateRandomSpillSource();
                controller.printGrid(area);
                primaryStage.show();
            }
        });

//        Area area = new Area(100);
        controller.printGrid(area);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
