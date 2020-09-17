package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;

public class SettingsController {

    private AppController mainController;
    private boolean isAnimationsEnabled;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private Button normalSkinButton;

    @FXML
    private Button darkModeSkinButton;

    @FXML
    private Label activeSkinLabel;

    @FXML
    private CheckBox enableAnimationsCheckBox;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public AnchorPane getMainRoot() {
        return mainRoot;
    }

    public CheckBox getEnableAnimationsCheckBox() {
        return enableAnimationsCheckBox;
    }

    @FXML
    void onActionSkinChange(ActionEvent event) {
        String skinChosen = ((Button)event.getSource()).getText();
        switch (skinChosen){
            case "Normal":
                setApplicationSkin("style.css");
                break;
            case "Dark Mode":
                setApplicationSkin("darkMode.css");
                break;
            default:
                break;
        }

        this.activeSkinLabel.setText(skinChosen);
    }

    private void setApplicationSkin(String cssFileName) {
        String cssFilePath = "/fxmls/home/" + cssFileName;
        mainController.getMainStage().getScene().getStylesheets().clear();
        mainController.getMainStage().getScene().getStylesheets().add(cssFilePath);
    }

    @FXML
    void onActionEnableAnimationsCheckBox(ActionEvent event) {
    }
}
