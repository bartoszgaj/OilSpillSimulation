package Presentation;

import Simulation.Area;
import Simulation.Controller;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


        Area area = new Area(100);
        controller.printGrid(area);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
