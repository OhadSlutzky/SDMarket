package controllers;

import SDMImprovedFacade.Customer;
import SDMImprovedFacade.Store;
import generatedClasses.Location;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadXMLController {

    private AppController mainController;
    private Integer lowestX = new Integer(0);
    private Integer highestX = new Integer(0);
    private Integer lowestY = new Integer(0);
    private Integer highestY = new Integer(0);

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private Button buttonChooseFile;

    @FXML
    private Button buttonLoadFile;

    @FXML
    private ProgressBar loadXMLProgressBar;

    @FXML
    private AnchorPane loadXMLMapAnchorPane;

    @FXML
    private Label pathToFileLabel;

    @FXML
    private Label amountOrdersLabel;

    @FXML
    private Label amountCustomersLabel;

    @FXML
    private Label amountStoresLabel;

    @FXML
    private Label amountItemsLabel;

    @FXML
    private GridPane mainMapGridPane;

    @FXML
    private Label mapObjectChosenIdLabel;

    @FXML
    private Label mapObjectChosenNameLabel;

    @FXML
    private Label mapObjectChosenAmountOrdersLabel;

    @FXML
    private Label mapObjectChosenPpkLabel;

    @FXML
    private Label mapObjectChosenLocationLabel;

    @FXML
    private VBox mapObjectInformationVBox;

    @FXML
    private VBox mapPpkLabelsVBox;

    @FXML
    void onActionChooseXMLFile(ActionEvent event) throws JAXBException {
        FileChooser dialog = new FileChooser();
        File file = dialog.showOpenDialog(mainController.getMainStage());

        if( file != null) {
            this.pathToFileLabel.textProperty().set(file.getAbsolutePath());
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void onActionLoadXML(ActionEvent event) throws Exception {
        LoadXMLTask task = new LoadXMLTask();

        clearSDMMapSection();

        if(!this.pathToFileLabel.textProperty().get().equals("-"))
        {

            Runnable target = new Runnable() {
                @Override
                public void run() {
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread th = new Thread(target);
            th.start();

            loadXMLProgressBar.progressProperty().bind(task.progressProperty());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("XML loading error");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a file first");
            alert.showAndWait();
        }
    }

    public void clearSDMMapSection() {
        this.loadXMLMapAnchorPane.getChildren().clear();
        this.loadXMLMapAnchorPane.setStyle("-fx-background-image: none");
    }

    public AnchorPane getRoot() {
        return this.mainRoot;
    }

    public void reloadSDMMap() {
        this.loadXMLMapAnchorPane.setStyle("-fx-background-image: url('/fxmls/images/map.jpg'); -fx-background-size: auto");
        double anchorPaneWidth = loadXMLMapAnchorPane.getWidth();
        double anchorPaneHeight = loadXMLMapAnchorPane.getHeight();
        double widthScale, heightScale;
        Map<Integer, Store> systemStores = mainController.getSDMLogic().getStores();
        Map<Integer, Customer> systemCustomers = mainController.getSDMLogic().getCustomers();

        calculateMapBorders(systemCustomers, systemStores);

        widthScale = anchorPaneWidth/(highestX - lowestX + 4);
        heightScale = anchorPaneHeight/(highestY - lowestY + 4);

        addObjectsToMap(systemCustomers, systemStores, widthScale, heightScale);
    }

    private void addObjectsToMap(Map<Integer, Customer> systemCustomers, Map<Integer, Store> systemStores,
                                 double widthScale, double heightScale) {
        addCustomersToMap(systemCustomers, widthScale, heightScale);
        addStoresToMap(systemStores, widthScale, heightScale);
    }

    private void addStoresToMap(Map<Integer, Store> systemStores, double widthScale, double heightScale) {
        int x, y;

        for (Store store: systemStores.values()) {
            x = store.getStoreLocation().getX();
            y = store.getStoreLocation().getY();
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/fxmls/images/store-pin.png")));
            img.setId(Integer.toString(store.getId()));
            img.setFitWidth(32);
            img.setFitHeight(32);
            img.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int storeId = Integer.parseInt(((ImageView)event.getSource()).getId());
                    Store store = mainController.getSDMLogic().getStores().get(storeId);
                    updateStoreInformationInLabels(store);
                }
            });

            AnchorPane.setTopAnchor(img, heightScale * y);
            AnchorPane.setLeftAnchor(img, widthScale * x);
            this.loadXMLMapAnchorPane.getChildren().add(img);
        }
    }

    private void updateStoreInformationInLabels(Store store) {
        this.mapObjectChosenAmountOrdersLabel.setText(Integer.toString(store.getStoreOrdersHistory().size()));
        this.mapObjectChosenIdLabel.setText(Integer.toString(store.getId()));
        this.mapObjectChosenNameLabel.setText(store.getName());
        this.mapObjectChosenPpkLabel.setText(Integer.toString(store.getDeliveryPpk()));
        this.mapObjectChosenLocationLabel.setText(String.format("(%d,%d)",
                store.getStoreLocation().getX(), store.getStoreLocation().getY()));
        this.mapPpkLabelsVBox.setVisible(true);
    }

    private void addCustomersToMap(Map<Integer, Customer> systemCustomers, double widthScale, double heightScale) {
        int x, y;

        for (Customer customer: systemCustomers.values()) {
            x = customer.getLocation().getX();
            y = customer.getLocation().getY();

            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/fxmls/images/customerPin.png")));
            img.setId(Integer.toString(customer.getId()));
            img.setFitWidth(32);
            img.setFitHeight(32);
            img.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int customerId = Integer.parseInt(((ImageView)event.getSource()).getId());
                    Customer customer = mainController.getSDMLogic().getCustomers().get(customerId);
                    updateCustomerInformationInLabels(customer);
                }
            });
            AnchorPane.setTopAnchor(img, heightScale * y);
            AnchorPane.setLeftAnchor(img, widthScale * x);
            this.loadXMLMapAnchorPane.getChildren().add(img);
        }
    }

    private void updateCustomerInformationInLabels(Customer customer) {
        this.mapObjectChosenLocationLabel.setText(String.format("(%d,%d)",
                customer.getLocation().getX(), customer.getLocation().getY()));
        this.mapPpkLabelsVBox.setVisible(false);
        this.mapObjectChosenNameLabel.setText(customer.getName());
        this.mapObjectChosenIdLabel.setText(Integer.toString(customer.getId()));
        this.mapObjectChosenAmountOrdersLabel.setText(Integer.toString(customer.getCustomerOrders().size()));
    }

    private void calculateMapBorders(Map<Integer, Customer> systemCustomers, Map<Integer, Store> systemStores) {
        List<Integer> allObjectsLocationsX = new ArrayList<>();
        List<Integer> allObjectsLocationsY = new ArrayList<>();
        systemCustomers.values().forEach(customer -> {
            allObjectsLocationsX.add(customer.getLocation().getX());
            allObjectsLocationsY.add(customer.getLocation().getY());
        });

        systemStores.values().forEach(store -> {
            allObjectsLocationsX.add(store.getStoreLocation().getX());
            allObjectsLocationsY.add(store.getStoreLocation().getY());
        });

        this.lowestX = allObjectsLocationsX.stream().min(Integer::compare).get();
        this.highestX = allObjectsLocationsX.stream().max(Integer::compare).get();
        this.lowestY = allObjectsLocationsY.stream().min(Integer::compare).get();
        this.highestY = allObjectsLocationsY.stream().max(Integer::compare).get();
    }

    public void bindLabelsToXmlLoadedProperty(){
        this.mapObjectInformationVBox.visibleProperty().bind(mainController.getIsXMLLoaded());
    }

    private class LoadXMLTask extends Task<Boolean> {
        boolean isValidXML = false;

        @Override
        protected Boolean call() throws Exception {
            StringBuilder sb = new StringBuilder();
            File file = new File(pathToFileLabel.textProperty().get().trim());
            isValidXML = mainController.getSDMLogic().loadData(file.getAbsolutePath().trim(), sb);

            delayProgress();
            if(!isValidXML) {
                Platform.runLater(() -> {
                    loadXMLProgressBar.setStyle("-fx-accent: red;");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("XML loading error");
                    alert.setHeaderText(null);
                    alert.setContentText(sb.toString());
                    alert.showAndWait();
                });
            }
            else if(sb.length() > 0){ // isValidXML = True ...
                Platform.runLater(() -> {
                    loadXMLProgressBar.setStyle("-fx-accent: red;");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("XML loading error");
                    alert.setHeaderText("The previous XML file is still loaded due to an error in the loading process of the new one");
                    alert.setContentText("Error detected: " + sb.toString());
                    alert.showAndWait();
                });
            }
            else{
                Platform.runLater(() -> {
                    amountCustomersLabel.textProperty().bind(mainController.getSDMLogic().getAmountCustomers());
                    amountStoresLabel.textProperty().bind(mainController.getSDMLogic().getAmountStoresStringProperty());
                    amountItemsLabel.textProperty().bind(mainController.getSDMLogic().getAmountItemsStringProperty());
                    amountOrdersLabel.textProperty().bind(mainController.getSDMLogic().getAmountOrdersStringProperty());
                    loadXMLProgressBar.setStyle("-fx-accent: green;");
                    mainController.enableMenuButtons();
                    reloadSDMMap();
                });
            }
            return Boolean.TRUE;
        }

        private void delayProgress() throws InterruptedException {
            loadXMLProgressBar.setStyle("-fx-accent: blue;");
            for (int i = 1; i <= 1000; i++) {
                this.updateProgress(i,1000);
                Thread.sleep(3);
            }
        }
    }
}

