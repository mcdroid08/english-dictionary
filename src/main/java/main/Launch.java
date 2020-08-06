package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launch extends Application {
    @Override
    public void start(Stage stage) {
        Parent root = null; //root layout

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml")); //loader for main fxml file

        try {
            root = loader.load(); //load root layout
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainController controller = loader.getController();
        controller.setStage(stage); //give pointer of stage to maincontroller for listners
        controller.setListenersAndEvents(); //set listners

        assert root != null;
        stage.setScene(new Scene(root)); //add root to scene and set it on stage
        stage.show();  //show window for root loayout or home/main page
    }
}
