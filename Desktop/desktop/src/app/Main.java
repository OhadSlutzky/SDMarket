package app;

import controllers.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/index.fxml");
        loader.setLocation(mainFXML);
        Pane root = loader.load();
        AppController appController = loader.getController();
        appController.setMainStage(primaryStage);
        primaryStage.setTitle("SDMarket");
        Scene scene = new Scene(root, 1245, 710);
        scene.getStylesheets().add("/fxmls/home/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
