package controllers;

import SDMImprovedFacade.Customer;
import SDMImprovedFacade.Store;
import SDMImprovedFacade.StoreItem;
import generatedClasses.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class UpdateInformationController {

    private final String ADD_ITEM_TO_STORE = "Add Item To Store";
    private final String REMOVE_ITEM_FROM_STORE = "Remove Item From Store";
    private final String UPDATE_ITEM_PRICE = "Update Item Price In Store";
    private final String ADD_NEW_STORE_TO_SYSTEM = "Add new store";
    private final String ADD_NEW_ITEM_TO_SYSTEM = "Add new item";

    private AppController mainController;
    private Store newlyAddedStore;
    private Map<Integer, StoreItem> newlyAddedStoreItemsBeingSold;
    private StoreItem newlyAddedStoreItem;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private ComboBox<String> chooseOperationComboBox;

    @FXML
    private ComboBox<Store> chooseStoreComboBox;

    @FXML
    private ComboBox<StoreItem> chooseItemComboBox;

    @FXML
    private TextField newPriceForItemTextField;

    @FXML
    private Button applyButton;

    @FXML
    private VBox updateInformationAddDynamicObjectVBox;

    @FXML
    private TextField addDynamicObjectIdTextField;

    @FXML
    private TextField addDynamicObjectNameTextField;

    @FXML
    private TextField addDynamicStorePpkTextField;

    @FXML
    private TextField addDynamicStoreLocationXTextField;

    @FXML
    private TextField addDynamicStoreLocationYTextField;

    @FXML
    private ComboBox<Store> addDynamicObjectChooseStoreComboBox;

    @FXML
    private ComboBox<StoreItem> addDynamicObjectChooseItemComboBox;

    @FXML
    private TextField addDynamicObjectItemPriceTextField;

    @FXML
    private Button addDynamicObjectAddItemToStoreButton;

    @FXML
    private Button addDynamicObjectAddButton;

    @FXML
    private HBox addNewObjectAddItemToStoreHBox;

    @FXML
    private Button AddNewObjectFinishButton;

    @FXML
    private HBox updateInformationOperationDashboardHBox;

    @FXML
    private ComboBox<String> chooseItemCategoryComboBox;

    @FXML
    private HBox inputNewObjectInformationHBox;


    public void initializeChooseStoreComboBox(boolean isNewStore) {
        ObservableList<Store> storesObservableList = FXCollections.observableArrayList();
        storesObservableList.addAll(this.mainController.getSDMLogic().getStores().values());

        if(isNewStore){
            this.addDynamicObjectChooseStoreComboBox.setItems(storesObservableList);
        }
        else {
            this.chooseStoreComboBox.setItems(storesObservableList);
        }
    }

    public void initializeChooseOperationComboBox() {
        ObservableList<String> operationsObservableList = FXCollections.observableArrayList();
        operationsObservableList.addAll(ADD_ITEM_TO_STORE, REMOVE_ITEM_FROM_STORE,
                UPDATE_ITEM_PRICE, ADD_NEW_STORE_TO_SYSTEM, ADD_NEW_ITEM_TO_SYSTEM);
        this.chooseOperationComboBox.setItems(operationsObservableList);
    }

    public void initializeChooseItemComboBox(boolean isAddNewStore) {
        ObservableList<StoreItem> storeItemsObservableList = FXCollections.observableArrayList();
        storeItemsObservableList.addAll(this.mainController.getSDMLogic().getItems().values());
        if(isAddNewStore){
            this.addDynamicObjectChooseItemComboBox.setItems(storeItemsObservableList);
        }
        else
        {
            this.chooseItemComboBox.setItems(storeItemsObservableList);
        }
    }

    @FXML
    void onActionApplyButton() {
        if (this.chooseOperationComboBox.getSelectionModel().getSelectedItem() != null) {
            String operation = this.chooseOperationComboBox.getSelectionModel().getSelectedItem();
            if(!operation.equals(ADD_NEW_STORE_TO_SYSTEM) && !operation.equals(ADD_NEW_ITEM_TO_SYSTEM)) {
                if (this.chooseStoreComboBox.getSelectionModel().getSelectedItem() != null) {
                    if (this.chooseItemComboBox.getSelectionModel().getSelectedItem() != null) {
                        StoreItem sItem = this.chooseItemComboBox.getSelectionModel().getSelectedItem();
                        Store store = this.chooseStoreComboBox.getSelectionModel().getSelectedItem();

                        switch (operation) {
                            case ADD_ITEM_TO_STORE:
                                addItemToStore(sItem, store);
                                break;
                            case REMOVE_ITEM_FROM_STORE:
                                removeItemFromStore(sItem, store);
                                break;
                            case UPDATE_ITEM_PRICE:
                                updateStoreItemPrice(sItem, store);
                                break;
                        }
                    } else {
                        displayUpdateInformationError("Please choose an item first");
                    }
                } else {
                    displayUpdateInformationError("Please choose a store first");
                }
            }
            else{
                if(operation.equals(ADD_NEW_STORE_TO_SYSTEM)){
                    enableAddNewStoreSectionAndInitialize();
                }
                else if(operation.equals(ADD_NEW_ITEM_TO_SYSTEM)){
                    enableAddNewItemSectionAndInitialize();
                }
            }
        } else {
            displayUpdateInformationError("Please choose an action first");
        }
    }

    private void enableAddNewItemSectionAndInitialize() {
        this.updateInformationAddDynamicObjectVBox.setVisible(true);
        this.updateInformationAddDynamicObjectVBox.setDisable(false);
        this.addNewObjectAddItemToStoreHBox.setVisible(false);
        this.addNewObjectAddItemToStoreHBox.setDisable(true);
        initializeNewItemComponents();
    }

    private void initializeNewItemComponents() {
        this.inputNewObjectInformationHBox.setDisable(false);
        this.addDynamicObjectIdTextField.setPromptText("Item ID");
        this.addDynamicObjectIdTextField.clear();
        this.addDynamicObjectIdTextField.setStyle("-fx-border-color: none");
        this.addDynamicObjectIdTextField.setDisable(false);
        this.addDynamicObjectNameTextField.setPromptText("Item Name");
        this.addDynamicObjectNameTextField.clear();
        this.addDynamicObjectNameTextField.setStyle("-fx-border-color: none");
        this.addDynamicObjectNameTextField.setDisable(false);
        this.addDynamicStorePpkTextField.setVisible(false);
        this.addDynamicStorePpkTextField.setDisable(true);
        this.addDynamicStoreLocationXTextField.setDisable(true);
        this.addDynamicStoreLocationXTextField.setVisible(false);
        this.addDynamicStoreLocationYTextField.setDisable(true);
        this.addDynamicStoreLocationYTextField.setVisible(false);
        this.chooseItemCategoryComboBox.setDisable(false);
        this.chooseItemCategoryComboBox.setVisible(true);
        this.chooseItemCategoryComboBox.setPromptText("Choose Category");
        this.addDynamicObjectAddButton.setDisable(false);
        initializeItemCategoryComboBox();
    }

    private void initializeAddItemToStoreSectionNewItem() {
        this.addNewObjectAddItemToStoreHBox.setVisible(true);
        this.addNewObjectAddItemToStoreHBox.setDisable(false);
        this.addDynamicObjectChooseStoreComboBox.setDisable(false);
        this.addDynamicObjectChooseStoreComboBox.setVisible(true);
        this.addDynamicObjectChooseStoreComboBox.setPromptText("Choose Store To Sell The New Item");
        this.addDynamicObjectItemPriceTextField.setPromptText("Item Price");
        this.addDynamicObjectItemPriceTextField.setStyle("-fx-border-color: none");
        this.addNewObjectAddItemToStoreHBox.setDisable(false);
        this.addNewObjectAddItemToStoreHBox.setVisible(true);
        this.inputNewObjectInformationHBox.setDisable(true);
        this.addDynamicObjectAddButton.setDisable(true);
        initializeChooseStoreComboBox(true);
        this.addDynamicObjectChooseItemComboBox.setDisable(true);
        this.addDynamicObjectChooseItemComboBox.setVisible(false);
    }


    private void initializeItemCategoryComboBox() {
        ObservableList<String> observableCategoryList = FXCollections.observableArrayList();
        observableCategoryList.addAll("Quantity", "Weight");
        this.chooseItemCategoryComboBox.setItems(observableCategoryList);
    }

    private void enableAddNewStoreSectionAndInitialize() {
        this.updateInformationAddDynamicObjectVBox.setVisible(true);
        this.updateInformationAddDynamicObjectVBox.setDisable(false);
        this.addNewObjectAddItemToStoreHBox.setVisible(false);
        this.addNewObjectAddItemToStoreHBox.setDisable(true);
        this.addDynamicObjectChooseItemComboBox.setVisible(false);
        this.addDynamicObjectChooseItemComboBox.setDisable(true);
        this.inputNewObjectInformationHBox.setVisible(true);
        this.inputNewObjectInformationHBox.setDisable(false);
        this.chooseItemCategoryComboBox.setDisable(true);
        this.chooseItemCategoryComboBox.setVisible(false);
        this.addDynamicObjectAddButton.setDisable(false);
        initializeNewStoreComponents();
    }

    private void initializeNewStoreComponents() {
        this.addDynamicObjectIdTextField.setPromptText("Store ID");
        this.addDynamicObjectNameTextField.setPromptText("Store Name");
        this.addDynamicStorePpkTextField.setPromptText("Store PPK");
        this.addDynamicStorePpkTextField.setDisable(false);
        this.addDynamicStorePpkTextField.setVisible(true);
        this.addDynamicStoreLocationXTextField.setPromptText("Store X Coordinate");
        this.addDynamicStoreLocationXTextField.setDisable(false);
        this.addDynamicStoreLocationXTextField.setVisible(true);
        this.addDynamicStoreLocationYTextField.setPromptText("Store Y Coordinate");
        this.addDynamicStoreLocationYTextField.setDisable(false);
        this.addDynamicStoreLocationYTextField.setVisible(true);
        //initializeAddItemToStoreSectionNewStore();
    }

    private void disableNewObjectSectionAfterAddButtonPressed(){
        this.inputNewObjectInformationHBox.setDisable(true);
        this.addDynamicObjectAddButton.setDisable(true);
        this.chooseItemCategoryComboBox.setDisable(true);
    }

    private void initializeAddItemToStoreSectionNewStore() {
        this.addDynamicObjectChooseStoreComboBox.setDisable(true);
        this.addDynamicObjectChooseStoreComboBox.setVisible(false);
        this.addDynamicObjectItemPriceTextField.setPromptText("Item Price");
        this.addNewObjectAddItemToStoreHBox.setVisible(true);
        this.addNewObjectAddItemToStoreHBox.setDisable(false);
        initializeChooseItemComboBox(true);
        this.addDynamicObjectChooseItemComboBox.setDisable(false);
        this.addDynamicObjectChooseItemComboBox.setVisible(true);
        this.addDynamicObjectChooseItemComboBox.setPromptText("Choose Item To sell In The Store");
    }

    private void updateStoreItemPrice(StoreItem sItem, Store store) {
        try {
            if(store.getItemsBeingSold().containsKey(sItem.getId())) {
                String newPriceString = this.newPriceForItemTextField.getText();
                if (isNumber(newPriceString)) {
                    if (Double.parseDouble(newPriceString) > 0) {
                        this.newPriceForItemTextField.setStyle("-fx-border-color: none;");
                        this.newPriceForItemTextField.setPromptText("New Price");
                        this.mainController.getSDMLogic().updatePriceOfAnItem(store.getId(), sItem.getId(), Double.parseDouble(newPriceString));
                        resetUpdateInformationScene();
                        displayOperationSuccessAlert(String.format("%s price was updated to %s in the store %s",
                                sItem.getName(), newPriceString, store.getName()));
                    } else {
                        displayVisualInvalidInput("are you serious?");
                    }
                } else {
                    displayVisualInvalidInput("no can do bro¯\\_(ツ)_/¯");
                }
            }
            else{ displayUpdateInformationError("You can not update an item that is not being sold by the store"); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeItemFromStore(StoreItem sItem, Store store) {
        boolean isDiscountDeletedFlag = false;
        if (store.getItemsBeingSold().containsKey(sItem.getId())) {
            if (store.getItemsBeingSold().size() != 1) {
                if (sItem.getAmountOfStoresSellingThisItem() != 1) {
                    this.mainController.getSDMLogic().removeItemFromStore(sItem, store);

                    if (store.getStoreDiscounts().get(sItem.getId()) != null && store.getStoreDiscounts().get(sItem.getId()).size() > 0) {
                        store.getStoreDiscounts().get(sItem.getId()).clear();
                        isDiscountDeletedFlag = true;
                    }

                    if (isDiscountDeletedFlag) {
                        displayOperationSuccessAlert(String.format("The item '%s' was removed successfully from %s along with the discount related to it",
                                sItem.getName(), store.getName()));
                    } else {
                        displayOperationSuccessAlert(String.format("The item '%s' was removed successfully from %s",
                                sItem.getName(), store.getName()));
                    }

                    resetUpdateInformationScene();
                } else {
                    displayUpdateInformationError("You can not delete an item that is being sold by only one store");
                }
            } else {
                displayUpdateInformationError("You can not delete the last item of this store");
            }
        } else {
            displayUpdateInformationError("You can not remove an item that is not being sold by the store");
        }
    }

    private void displayOperationSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operation Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addItemToStore(StoreItem sItem, Store store) {
        if (!store.getItemsBeingSold().containsKey(sItem.getId())) {
            this.mainController.getSDMLogic().addItemToStore(sItem, store);
            displayOperationSuccessAlert(String.format("The item '%s' was added successfully to '%s'", sItem.getName(), store.getName()));
            resetUpdateInformationScene();
        } else {
            displayUpdateInformationError("You can not add an item to a store that is already selling it");
        }
    }

    private void displayUpdateInformationError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Information Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    @FXML
    void onActionAddButton() {

        String objectName;
        int objectId;
        if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_STORE_TO_SYSTEM)){
            int storeXCoordinate;
            int storeYCoordinate;

            if(isObjectIdValid()){
                objectId = Integer.parseInt(this.addDynamicObjectIdTextField.getText());
                objectName = this.addDynamicObjectNameTextField.getText();

                if(isPpkValid()){
                    int storePpk = Integer.parseInt(this.addDynamicStorePpkTextField.getText());

                    if(isCoordinateValid(true)){
                        storeXCoordinate = Integer.parseInt(this.addDynamicStoreLocationXTextField.getText());

                        if(isCoordinateValid(false)){
                            storeYCoordinate = Integer.parseInt(this.addDynamicStoreLocationYTextField.getText());

                            if(!isLocationTaken(storeXCoordinate, storeYCoordinate)){
                                Location storeLocation = new Location();
                                storeLocation.setX(storeXCoordinate);
                                storeLocation.setY(storeYCoordinate);
                                newlyAddedStore = new Store(objectId, objectName, storePpk, storeLocation);
                                initializeAddItemToStoreSectionNewStore();
                                disableNewObjectSectionAfterAddButtonPressed();
                                initializeNewStoreComponents();
                                resetNewObjectTextFieldsBorderAndText();
                            }
                            else{
                                this.addDynamicStoreLocationXTextField.clear();
                                this.addDynamicStoreLocationXTextField.setPromptText("Location Taken");
                                this.addDynamicStoreLocationXTextField.setStyle("-fx-border-color: red");
                                this.addDynamicStoreLocationYTextField.clear();
                                this.addDynamicStoreLocationYTextField.setPromptText("Location Taken");
                                this.addDynamicStoreLocationYTextField.setStyle("-fx-border-color: red");
                            }
                        }
                    }
                }
            }
        }
        else if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_ITEM_TO_SYSTEM)){
            if(isObjectIdValid()){
                objectId = Integer.parseInt(this.addDynamicObjectIdTextField.getText());
                objectName = this.addDynamicObjectNameTextField.getText();
                String itemCategory = this.chooseItemCategoryComboBox.getSelectionModel().getSelectedItem();
                newlyAddedStoreItem = new StoreItem(objectId, objectName, itemCategory);
                initializeAddItemToStoreSectionNewItem();
                disableNewObjectSectionAfterAddButtonPressed();
                resetNewObjectTextFieldsBorderAndText();
            }
        }
    }

    private void resetNewObjectTextFieldsBorderAndText() {
        this.addDynamicObjectIdTextField.setStyle("-fx-border-color: none");
        this.addDynamicObjectIdTextField.clear();
        //this.addDynamicObjectIdTextField.setPromptText("");
        this.addDynamicObjectNameTextField.setStyle("-fx-border-color: none");
        this.addDynamicObjectNameTextField.clear();
        //this.addDynamicObjectNameTextField.setPromptText("");
        this.addDynamicStorePpkTextField.setStyle("-fx-border-color: none");
        this.addDynamicStorePpkTextField.clear();
        //this.addDynamicStorePpkTextField.setPromptText("");
        this.addDynamicStoreLocationXTextField.setStyle("-fx-border-color: none");
        this.addDynamicStoreLocationXTextField.clear();
        //this.addDynamicStoreLocationXTextField.setPromptText("");
        this.addDynamicStoreLocationYTextField.setStyle("-fx-border-color: none");
        this.addDynamicStoreLocationYTextField.clear();
        //this.addDynamicStoreLocationYTextField.setPromptText("");
    }

    @FXML
    void onActionAddItemToStoreButton(){
        if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_STORE_TO_SYSTEM)){
            initializeAddItemToStoreSectionNewStore();
            StoreItem itemToAdd = this.addDynamicObjectChooseItemComboBox.getSelectionModel().getSelectedItem();

            if(itemToAdd != null){
                if(isPriceValid()){
                    StoreItem newItemToAdd = new StoreItem(itemToAdd);
                    newItemToAdd.setPricePerUnit(Integer.parseInt(this.addDynamicObjectItemPriceTextField.getText()));

                    if(newlyAddedStoreItemsBeingSold == null){
                        newlyAddedStoreItemsBeingSold = new HashMap<>();
                    }

                    newlyAddedStoreItemsBeingSold.put(newItemToAdd.getId(), newItemToAdd);
                    this.addDynamicObjectItemPriceTextField.clear();
                    updateChooseItemToAddComboBox();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adding new Store Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose an Item first");
                alert.showAndWait();
            }
        }
        else if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_ITEM_TO_SYSTEM)){
            initializeAddItemToStoreSectionNewItem();
            Store storeToAddItemTo = this.addDynamicObjectChooseStoreComboBox.getSelectionModel().getSelectedItem();

            if(storeToAddItemTo != null){
                if(isPriceValid()){
                    newlyAddedStoreItem.setPricePerUnit(Integer.parseInt(this.addDynamicObjectItemPriceTextField.getText()));
                    newlyAddedStoreItem.setAveragePriceOfTheItem(newlyAddedStoreItem.getPricePerUnit());
                    this.mainController.getSDMLogic().addItemToStore(newlyAddedStoreItem, storeToAddItemTo);
                    updateChooseStoreToAddItemToComboBox();
                    displayOperationSuccessAlert(String.format("%s was add to  the store %s",
                            newlyAddedStoreItem.getName(), storeToAddItemTo.getName()));
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adding new Item Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a store first");
                alert.showAndWait();
            }
        }
    }

    private void updateChooseStoreToAddItemToComboBox() {
        ObservableList<Store> observableStoreList = FXCollections.observableArrayList();
        observableStoreList.addAll(this.addDynamicObjectChooseStoreComboBox.getItems());
        this.addDynamicObjectChooseStoreComboBox.getItems().clear();

        for (Store store: mainController.getSDMLogic().getStores().values()) {
            if(store.getItemsBeingSold().containsKey(newlyAddedStoreItem.getId())){
                observableStoreList.remove(store);
            }
        }

        this.addDynamicObjectChooseStoreComboBox.setItems(observableStoreList);
    }

    private void updateChooseItemToAddComboBox() {
        ObservableList<StoreItem> observableStoreItemsList =FXCollections.observableArrayList();
        this.addDynamicObjectChooseItemComboBox.getItems().clear();

        for (StoreItem sItem: mainController.getSDMLogic().getItems().values()) {
                if(!newlyAddedStoreItemsBeingSold.containsKey(sItem.getId())){
                    observableStoreItemsList.add(sItem);
                }
        }

        this.addDynamicObjectChooseItemComboBox.setItems(observableStoreItemsList);
    }

    private boolean isPriceValid() {
        try{
            int itemPrice = Integer.parseInt(this.addDynamicObjectItemPriceTextField.getText());

            if(0 < itemPrice){
                return true;
            }
            else{
                this.addDynamicObjectItemPriceTextField.clear();
                this.addDynamicObjectItemPriceTextField.setPromptText("Must Be Positive");
                this.addDynamicObjectItemPriceTextField.setStyle("-fx-border-color: red");
                return false;
            }
        }
        catch(Exception e){
            this.addDynamicObjectItemPriceTextField.clear();
            this.addDynamicObjectItemPriceTextField.setPromptText("Not An Integer");
            this.addDynamicObjectItemPriceTextField.setStyle("-fx-border-color: red");
            return false;
        }
    }

    @FXML
    void onActionFinishButton(){
        if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_STORE_TO_SYSTEM)){
            if(newlyAddedStoreItemsBeingSold != null){
                newlyAddedStore.setItemBeingSold(newlyAddedStoreItemsBeingSold);
                mainController.getSDMLogic().addStoreToSystem(newlyAddedStore);
                displayOperationSuccessAlert(String.format("The Store %s was added to the system successfully",
                        newlyAddedStore.getName()));
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adding new Store Error");
                alert.setHeaderText(null);
                alert.setContentText("You cannot add a store which sells nothing");
                alert.showAndWait();
            }
        }
        else{
            if(newlyAddedStoreItem.getAmountOfStoresSellingThisItem() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adding new Item Error");
                alert.setHeaderText(null);
                alert.setContentText("You cannot add an item which is not sold at any store");
                alert.showAndWait();
            }
        }

        resetUpdateInformationScene();
    }

    public void resetUpdateInformationScene() {
        this.updateInformationAddDynamicObjectVBox.setDisable(true);
        this.updateInformationAddDynamicObjectVBox.setVisible(false);
        this.updateInformationOperationDashboardHBox.setDisable(false);
        this.chooseItemCategoryComboBox.setDisable(true);
        this.chooseItemCategoryComboBox.setVisible(false);
        this.chooseOperationComboBox.getSelectionModel().clearSelection();
        this.chooseOperationComboBox.setPromptText("Choose an operation");
        initializeChooseOperationComboBox();
        this.chooseStoreComboBox.setPromptText("Choose Store");
        this.chooseStoreComboBox.getSelectionModel().clearSelection();
        this.chooseItemComboBox.setPromptText("Choose Item");
        this.chooseItemComboBox.getSelectionModel().clearSelection();
        this.newPriceForItemTextField.setVisible(false);
        this.newPriceForItemTextField.setDisable(true);
        this.addDynamicObjectChooseItemComboBox.setVisible(false);
        this.addDynamicObjectChooseItemComboBox.setDisable(true);
    }

    private boolean isLocationTaken(int storeXCoordinate, int storeYCoordinate) {
        for (Store store: mainController.getSDMLogic().getStores().values()) {
            if(store.getStoreLocation().getX() == storeXCoordinate && store.getStoreLocation().getY() == storeYCoordinate){
                return true;
            }
        }

        for (Customer customer: mainController.getSDMLogic().getCustomers().values()) {
            if(customer.getLocation().getX() == storeXCoordinate && customer.getLocation().getY() == storeYCoordinate){
                return true;
            }
        }

        return false;
    }

    private boolean isCoordinateValid(boolean isX) {
        try{
            int coordinate;

            if(isX){
                coordinate = Integer.parseInt(this.addDynamicStoreLocationXTextField.getText());
            }
            else{
                coordinate = Integer.parseInt(this.addDynamicStoreLocationYTextField.getText());
            }

            if(1 <= coordinate && coordinate <= 50){
                return true;
            }
            else{
                if(isX){
                    this.addDynamicStoreLocationXTextField.clear();
                    this.addDynamicStoreLocationXTextField.setPromptText("Not In Range");
                    this.addDynamicStoreLocationXTextField.setStyle("-fx-border-color: red");
                }
                else{
                    this.addDynamicStoreLocationYTextField.clear();
                    this.addDynamicStoreLocationYTextField.setPromptText("Not In Range");
                    this.addDynamicStoreLocationYTextField.setStyle("-fx-border-color: red");
                }

                return false;
            }
        }
        catch(Exception e){
            if(isX){
                this.addDynamicStoreLocationXTextField.clear();
                this.addDynamicStoreLocationXTextField.setPromptText("Not An Integer");
                this.addDynamicStoreLocationXTextField.setStyle("-fx-border-color: red");
            }
            else{
                this.addDynamicStoreLocationYTextField.clear();
                this.addDynamicStoreLocationYTextField.setPromptText("Not An Integer");
                this.addDynamicStoreLocationYTextField.setStyle("-fx-border-color: red");
            }

            return false;
        }
    }

    private boolean isPpkValid() {
        try{
            int storePpk = Integer.parseInt(this.addDynamicStorePpkTextField.getText());

            if(storePpk > 0){
                return true;
            }
            else {
                this.addDynamicStorePpkTextField.setPromptText("Must Be Positive");
                this.addDynamicStorePpkTextField.setStyle("-fx-border-color: red");
                return false;
            }
        }
        catch(Exception e){
            this.addDynamicStorePpkTextField.setPromptText("Not An Integer");
            this.addDynamicStorePpkTextField.setStyle("-fx-border-color: red");
            return false;
        }
    }

    private boolean isObjectIdValid() {
        String objectIdString = this.addDynamicObjectIdTextField.getText();
        try{
            int objectId = Integer.parseInt(objectIdString);

            if(!isIdAlreadyExist(objectId)){
                return true;
            }
            else{
                this.addDynamicObjectIdTextField.clear();
                this.addDynamicObjectIdTextField.setPromptText("ID Already Exist");
                this.addDynamicObjectIdTextField.setStyle("-fx-border-color: red");
                return false;
            }
        }
        catch(Exception e){
            this.addDynamicObjectIdTextField.clear();
            this.addDynamicObjectIdTextField.setPromptText("Not An Integer");
            this.addDynamicObjectIdTextField.setStyle("-fx-border-color: red");
            return false;
        }
    }

    private boolean isIdAlreadyExist(int objectId) {
        if(this.chooseOperationComboBox.getSelectionModel().getSelectedItem().equals(ADD_NEW_STORE_TO_SYSTEM)){
            return this.mainController.getSDMLogic().getStores().containsKey(objectId);
        }
        else{
            return this.mainController.getSDMLogic().getItems().containsKey(objectId);
        }
    }

    @FXML
    void onActionChooseOperation() {
        try {
            String operation = this.chooseOperationComboBox.getSelectionModel().getSelectedItem();
            if(operation != null){
                if(operation.equals(UPDATE_ITEM_PRICE)){
                    this.newPriceForItemTextField.setVisible(true);
                    this.newPriceForItemTextField.setDisable(false);
                }
                else if(operation.equals(ADD_NEW_STORE_TO_SYSTEM)){
                    enableAddNewStoreSectionAndInitialize();
                    this.updateInformationOperationDashboardHBox.setDisable(true);
                }
                else if(operation.equals(ADD_NEW_ITEM_TO_SYSTEM)){
                    enableAddNewItemSectionAndInitialize();
                    this.updateInformationOperationDashboardHBox.setDisable(true);
                }
                else{
                    this.newPriceForItemTextField.setVisible(false);
                    this.newPriceForItemTextField.setDisable(true);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadItemsToSelectItemsComboBox(Collection<StoreItem> storeItems) {
        ObservableList<StoreItem> storeItemsObservableList = FXCollections.observableArrayList();
        storeItemsObservableList.addAll(storeItems);
        this.chooseItemComboBox.setItems(storeItemsObservableList);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private boolean isNumber(String numberToCheck) {
        try {
            Double.parseDouble(numberToCheck);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void displayVisualInvalidInput(String promptText) {
        this.newPriceForItemTextField.setStyle("-fx-border-color: red;");
        this.newPriceForItemTextField.clear();
        this.newPriceForItemTextField.setPromptText(promptText);
    }

    public AnchorPane getMainRoot() {
        return mainRoot;
    }

}
