package controllers;

import SDMImprovedFacade.Customer;
import SDMImprovedFacade.Store;
import SuperMarketLogic.SuperMarketLogic;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppController {
    private final SuperMarketLogic SDMLogic;
    private final SimpleBooleanProperty isXMLLoaded;
    private final SimpleBooleanProperty isAnimationEnabled;
    private Stage mainStage;

    //Controllers
    private final LoadXMLController loadXMLController;
    private final StoresController storesController;
    private final ItemsController itemsController;
    private final CustomersController customersController;
    private final PurchaseController purchaseController;
    private final UpdateInformationController updateInformationController;
    private final OrdersHistoryController ordersHistoryController;
    private final SettingsController settingsController;
    private final Map<Integer, StoreCardController> storeCardControllersMap = new HashMap<>();
    private final Map<Integer, CustomerCardController> customerCardControllersMap = new HashMap<>();
    private final Map<Integer, StoreCardController> storeCardControllerMapForPurchase = new HashMap<>();

    @FXML
    private Button buttonLoadXML;

    @FXML
    private Button buttonPurchase;

    @FXML
    private Button buttonCustomers;

    @FXML
    private Button buttonStores;

    @FXML
    private Button buttonItems;

    @FXML
    private Button buttonOrderHistory;

    @FXML
    private Button buttonSettings;

    @FXML
    private Button buttonUpdateInformation;

    @FXML
    private AnchorPane anchorPaneMainWindow;



    public AppController() throws IOException {
        isXMLLoaded = new SimpleBooleanProperty(false);
        isAnimationEnabled = new SimpleBooleanProperty(false);
        SDMLogic = new SuperMarketLogic();
        loadXMLController = initializeLoadXMLController();
        storesController = initializeStoresController();
        itemsController = initializeItemsController();
        customersController = initializeCustomersController();
        purchaseController = initializePurchaseController();
        updateInformationController = initializeUpdateInformationController();
        ordersHistoryController = initializeOrderHistoryController();
        settingsController = initializeSettingsController();
    }

    private SettingsController initializeSettingsController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/settings.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        SettingsController tempSettingsController = loader.getController();
        tempSettingsController.setMainController(this);
        isAnimationEnabled.bind(tempSettingsController.getEnableAnimationsCheckBox().selectedProperty());
        return tempSettingsController;
    }

    private OrdersHistoryController initializeOrderHistoryController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/ordersHistory.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        OrdersHistoryController tempOrdersHistoryController = loader.getController();
        tempOrdersHistoryController.setMainController(this);
        return tempOrdersHistoryController;
    }

    private UpdateInformationController initializeUpdateInformationController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/updateInformation.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        UpdateInformationController tempUpdateInformationController = loader.getController();
        tempUpdateInformationController.setMainController(this);
        return tempUpdateInformationController;
    }

    private PurchaseController initializePurchaseController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/purchase.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        PurchaseController tempPurchaseController = loader.getController();
        tempPurchaseController.setMainController(this);
        tempPurchaseController.bindRelevantLabelsToOrderProperties();
        return tempPurchaseController;
    }

    private CustomersController initializeCustomersController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/customers.fxml");
        loader.setLocation(mainFXML);
        loader.load();
        CustomersController customersController = loader.getController();
        customersController.setMainController(this);

        return customersController;
    }

    private ItemsController initializeItemsController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/items.fxml");
        loader.setLocation(mainFXML);
        loader.load();
        ItemsController itemsController = loader.getController();
        itemsController.setMainController(this);
        return itemsController;
    }

    private StoresController initializeStoresController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/stores.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        StoresController storesController = loader.getController();
        storesController.setMainController(this);
        return storesController;
    }

    private LoadXMLController initializeLoadXMLController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/fxmls/home/loadXML.fxml");
        loader.setLocation(mainFXML);
        loader.load(); //need to be done before loader.getController() !
        LoadXMLController xmlLoadController = loader.getController();
        xmlLoadController.setMainController(this);
        xmlLoadController.bindLabelsToXmlLoadedProperty();

        return xmlLoadController;
    }

    public SuperMarketLogic getSDMLogic() {
        return SDMLogic;
    }

    public SimpleBooleanProperty getIsXMLLoaded() {
        return isXMLLoaded;
    }

    public SimpleBooleanProperty isXMLLoadedProperty() {
        return isXMLLoaded;
    }

    public boolean getIsAnimationEnabled() {
        return isAnimationEnabled.get();
    }

    public SimpleBooleanProperty isAnimationEnabledProperty() {
        return isAnimationEnabled;
    }

    public void setIsXMLLoaded(boolean isXMLLoaded) {
        this.isXMLLoaded.set(isXMLLoaded);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize() {
        bindButtonsToIsXMLLoaded();
    }

    private void bindButtonsToIsXMLLoaded() {
        buttonCustomers.disableProperty().bind(isXMLLoaded.not());
        buttonItems.disableProperty().bind(isXMLLoaded.not());
        buttonPurchase.disableProperty().bind(isXMLLoaded.not());
        buttonSettings.disableProperty().bind(isXMLLoaded.not());
        buttonStores.disableProperty().bind(isXMLLoaded.not());
        buttonUpdateInformation.disableProperty().bind(isXMLLoaded.not());
        buttonOrderHistory.disableProperty().bind(isXMLLoaded.not());
    }

    @FXML
    void onActionCustomers() {
        try{
            FXMLLoader loader;
            if (!anchorPaneMainWindow.getChildren().contains(customersController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = customersController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);

                for(Customer customer : getSDMLogic().getCustomers().values()) {
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/home/customerCard.fxml"));
                    loader.load();
                    CustomerCardController customerCardController = loader.getController();
                    customerCardController.setCustomerIdCardLabelText(Integer.toString(customer.getId()));
                    customerCardController.setCustomerNameCardLabelText(customer.getName());
                    customerCardControllersMap.put(customer.getId(), customerCardController);
                }

                customersController.addCustomerCards(customerCardControllersMap);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void onActionItems() {
        if (!anchorPaneMainWindow.getChildren().contains(itemsController.getRoot())) {
            anchorPaneMainWindow.getChildren().clear();
            AnchorPane itemsControllerMainRoot = itemsController.getRoot();
            anchorPaneMainWindow.getChildren().add(itemsControllerMainRoot);
            setAnchorPaneInPlace(itemsControllerMainRoot);
            itemsController.updateSystemItemsScene(new ArrayList<>(this.getSDMLogic().getItems().values()));
        }
    }

    @FXML
    void onActionLoadXML() throws JAXBException, IOException {
        if (!anchorPaneMainWindow.getChildren().contains(loadXMLController.getRoot())) {
            anchorPaneMainWindow.getChildren().clear();
            anchorPaneMainWindow.getChildren().add(loadXMLController.getRoot());
        }

        if(isXMLLoaded.get()){
            loadXMLController.clearSDMMapSection();
            loadXMLController.reloadSDMMap();
        }
    }

    @FXML
    void onActionPurchase(ActionEvent event) {
        try{
            FXMLLoader loader;
            if (!anchorPaneMainWindow.getChildren().contains(purchaseController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = purchaseController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);

                for(Store store : getSDMLogic().getStores().values()) {
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/home/storeCard.fxml"));
                    loader.load();
                    StoreCardController storeCardController = loader.getController();
                    storeCardController.setStoreNameCardLabelText(store.getName());
                    storeCardController.setStoreIdCardLabelText(Integer.toString(store.getId()));
                    storeCardController.setLocationLabelText(String.format("(%d,%d)",
                            store.getStoreLocation().getX(), store.getStoreLocation().getY()));
                    storeCardControllerMapForPurchase.put(store.getId(), storeCardController);
                }

                purchaseController.addStoreCards(storeCardControllerMapForPurchase);
                purchaseController.activateReset();
                purchaseController.insertCustomersToComboBox();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void onActionSettings(ActionEvent event) {
        try {
            if (!anchorPaneMainWindow.getChildren().contains(settingsController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = settingsController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onActionStores(ActionEvent event) {
        try{
            FXMLLoader loader;
            if (!anchorPaneMainWindow.getChildren().contains(storesController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = storesController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);

                for(Store store : getSDMLogic().getStores().values()) {
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/home/storeCard.fxml"));
                    loader.load();
                    StoreCardController storeCardController = loader.getController();
                    storeCardController.setStoreIdCardLabelText(Integer.toString(store.getId()));
                    storeCardController.setStoreNameCardLabelText(store.getName());
                    storeCardController.setLocationLabelText(String.format("(%d,%d)",
                            store.getStoreLocation().getX(), store.getStoreLocation().getY()));
                    storeCardControllersMap.put(store.getId(), storeCardController);
                }

                storesController.addStoreCards(storeCardControllersMap);
                storesController.resetAllComponents();
            }
        }
        catch(IOException e){
                e.printStackTrace();
        }
    }

    @FXML
    void onActionUpdateInformation(ActionEvent event) {
        try {
            if (!anchorPaneMainWindow.getChildren().contains(updateInformationController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = updateInformationController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);
                updateInformationController.initializeChooseOperationComboBox();
                updateInformationController.initializeChooseStoreComboBox(false);
                updateInformationController.initializeChooseItemComboBox(false);
            }

            updateInformationController.resetUpdateInformationScene();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onActionOrderHistory(ActionEvent event) {
        try {
            if (!anchorPaneMainWindow.getChildren().contains(ordersHistoryController.getMainRoot())) {
                anchorPaneMainWindow.getChildren().clear();
                AnchorPane anchorPane = ordersHistoryController.getMainRoot();
                anchorPaneMainWindow.getChildren().add(anchorPane);
                setAnchorPaneInPlace(anchorPane);
                ordersHistoryController.loadOrdersToTableViews();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setAnchorPaneInPlace(AnchorPane anchorPane) {
        AnchorPane.setTopAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        AnchorPane.setBottomAnchor(anchorPane, 0.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
    }

    public void enableMenuButtons() {
        this.isXMLLoaded.set(true);
    }

    public AnchorPane getAnchorPaneMainWindow() {
        return anchorPaneMainWindow;
    }

    public Map<Integer, StoreCardController> getStoreCardControllersMap() {
        return storeCardControllersMap;
    }

    public Map<Integer, CustomerCardController> getCustomerCardControllersMap() {
        return customerCardControllersMap;
    }

    public Map<Integer, StoreCardController> getStoreCardControllerMapForPurchase() {
        return storeCardControllerMapForPurchase;
    }
}
