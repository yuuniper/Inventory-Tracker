/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private TextField searchBox;

    @FXML
    private TextField serialNumberText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField moneyText;

    @FXML
    private Label errorMsg;

    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, String> serialNumberColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, String> moneyColumn;

    @FXML
    private Label addValueError;

    @FXML
    private Label serialNumberError;

    @FXML
    private Label nameError;

    ObservableList<Item> observableList = FXCollections.observableArrayList(
            //new Item("Sam", "1234567890", "$45.17"),
            //new Item("Laila", "abcdefghi0", "$67.80")
    );

    List<String> serialNumbersList = new ArrayList<>();
    List<Item> itemsList = new ArrayList<>();

    @FXML
    void addButtonClicked(ActionEvent event) {

        // Clear previous error messages
        errorMsg.setText("");
        clearErrorMsgs();

        // Check if valid input
        boolean isValid = validateInput(nameText.getText(), moneyText.getText(), serialNumberText.getText());
        // Check for no duplicate serial numbers
        boolean noDuplicate = checkSerialNumberDuplicate(serialNumbersList, serialNumberText.getText());

        if (isValid && noDuplicate){
            // format the money
            String formattedMoney = formatMoney(moneyText.getText());
            // Make Item
            Item item = new Item(nameText.getText(), serialNumberText.getText(), formattedMoney);
            // Add item to table
            itemTable.getItems().add(item);
            // Add serial Number to List Array
            serialNumbersList.add(serialNumberText.getText());
            itemTable.refresh();
            // Clear all fields
            clearTextBoxes();
        }
        else{
            errorMsg.setText("Invalid Input");
        }
    }

    public boolean checkSerialNumberDuplicate(List<String> serialNumbersList, String serialNumber) {
        if (serialNumbersList.contains(serialNumber)){
            // Invalid serial number
            System.out.println("Duplicate serial number");
            setErrorMsg("Duplicate Serial Number");
            return false;
        }else{
            return true;
        }
    }

    private String formatMoney(String initial) {
        // Parse money into double
        String noDollarSign =  initial.replace("$","");
        double d = Double.parseDouble(noDollarSign);

        // Format into currency with 2 decimal places with $
        String decimal = String.format("%.2f", d);
        return "$" + decimal;
    }

    private void clearTextBoxes() {
        // Clear all fields
         nameText.setText("");
         serialNumberText.setText("");
         moneyText.setText("");
    }


    public boolean validateInput(String name, String money, String serialNumber) throws NumberFormatException {
        clearErrorMsgs();
        // validate input
        boolean isValid = true;

        // Check name length
        if (name.length() < 2 || name.length() > 256){
            System.out.println("Invalid length");
            setErrorMsg("name");
            isValid = false;
        }
        // Check money
        try{
            String noDollarSign =  money.replace("$","");
            double d = Double.parseDouble(noDollarSign);
        }catch(NumberFormatException e){
            System.out.println("Not valid currency");
            setErrorMsg("value");
            isValid = false;
        }

        // Check Serial Number is [2, 256] characters, only contains numbers and letters,
        if (serialNumber.length() != 10 || !serialNumber.matches("^[a-zA-Z0-9]*$")){
            System.out.println("Not valid serial number");
            isValid = false;
            setErrorMsg("serialNumber");
        }
        return isValid;
    }

    public boolean testValidateInput(String name, String money, String serialNumber) throws NumberFormatException {
        // This function is the exact same as validateInput, but because of the FXML Labels for error
        // Messages, I couldn't JUnit Test it
        // validate input
        boolean isValid = true;

        // Check name length
        if (name.length() < 2 || name.length() > 256){
            System.out.println("Invalid length");
            isValid = false;
        }
        // Check money
        try{
            String noDollarSign =  money.replace("$","");
            double d = Double.parseDouble(noDollarSign);
        }catch(NumberFormatException e){
            System.out.println("Not valid currency");
            isValid = false;
        }

        // Check Serial Number is [2, 256] characters, only contains numbers and letters,
        if (serialNumber.length() != 10 || !serialNumber.matches("^[a-zA-Z0-9]*$")){
            System.out.println("Not valid serial number");
            isValid = false;
        }
        return isValid;
    }

    private void clearErrorMsgs() {
        nameError.setText("");
        addValueError.setText("");
        serialNumberError.setText("");
    }

    private void setErrorMsg(String errorMsg) {
        if (errorMsg.equals("name")){
            nameError.setText("Incorrect Name Format");
        } else if (errorMsg.equals("value")){
            addValueError.setText("Incorrect Value Format");
        }  else if (errorMsg.equals("Duplicate Serial Number")){
            serialNumberError.setText("Duplicate Serial Number");
        } else if (errorMsg.equals("serialNumber")){
            serialNumberError.setText("Incorrect Serial Number");
        } else{
            nameError.setText("");
            addValueError.setText("");
            serialNumberError.setText("");
        }
    }

    @FXML
    void loadButtonClicked(ActionEvent event) {
        FileManager makeFiles = new FileManager();
        List<Item> loadedItems;

        loadedItems = makeFiles.loadFile();

        serialNumbersList.clear();
        // Load into serial number list
        for(Item i : loadedItems){
            serialNumbersList.add(i.getSerialNumber());
        }

        // Display to table
        observableList.clear();
        observableList.addAll(loadedItems);

    }

    @FXML
    void removeButtonClicked(ActionEvent event) {
        itemTable.getItems().removeAll(itemTable.getSelectionModel().getSelectedItem());
        removeItem(itemTable.getSelectionModel().getSelectedItem());
    }

    public void removeItem(Item selectedItem) {
        itemsList.remove(selectedItem);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        FileManager makeFiles = new FileManager();
        List<Item> saveItems = new ArrayList<>(observableList);
        makeFiles.saveFile(saveItems);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){

        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("money"));

        itemTable.setItems(observableList);

        itemTable.setEditable(true);
        editTable();
    }

    private void editTable() {
        //Set properties for the column to be edited
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        moneyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }


    @FXML
    void serialNumberEditCommitted(TableColumn.CellEditEvent<Item, String> itemStringCellEditEvent) {
        // Clear previous error message
        errorMsg.setText("");
        // Get Item
        Item item = itemTable.getSelectionModel().getSelectedItem();

        boolean isValidAndNoDuplicate = editSerialNumber(item.getName(), item.getMoney(), itemStringCellEditEvent.getNewValue(),
                serialNumbersList, item, item.getSerialNumber());

        if (!isValidAndNoDuplicate){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Serial Number");
            itemTable.refresh();
        }

    }

    public boolean editSerialNumber(String name, String money, String serialNumberEdited, List<String> serialNumbersList,
                                    Item item, String originalSerialNumber) {
        System.out.println(serialNumberEdited);

        // Validate input
        boolean isValid = validateInput(item.getName(), item.getMoney(), serialNumberEdited);
        // Check for no duplicate serial numbers
        boolean noDuplicate  = checkSerialNumberDuplicate(serialNumbersList, serialNumberEdited);
        System.out.println(noDuplicate);
        if (isValid && noDuplicate){
            // Set serial number to new value
            item.setSerialNumber(serialNumberEdited);
            // Remove original serial in Serial Number list and replace with new serial list
            serialNumbersList.add(serialNumberEdited);
            serialNumbersList.remove(originalSerialNumber);

        }
        return isValid && noDuplicate;
    }


    @FXML
    void valueEditCommitted(TableColumn.CellEditEvent<Item, String> itemStringCellEditEvent) {
        // Clear previous error message
        errorMsg.setText("");
        // Select Item
        Item item = itemTable.getSelectionModel().getSelectedItem();

        // Edit Value
        boolean isValid = editValue(item.getName(), itemStringCellEditEvent.getNewValue(),
                item.getSerialNumber(), item);


        if (!isValid){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Value");
        }
        // Refresh Display
        itemTable.refresh();

    }

    public boolean editValue(String name, String valueEdited, String serialNumber, Item item) {
        // Validate input
        boolean isValid = validateInput(item.getName(), valueEdited, item.getSerialNumber());

        // Format money
        String formattedMoney = formatMoney(valueEdited);

        if (isValid){
            // Set value number to new value
            item.setMoney(formattedMoney);
        }
        return isValid;
    }

    @FXML
    void nameEditCommitted(TableColumn.CellEditEvent<Item, String> itemStringCellEditEvent) {
        // Clear previous error message
        errorMsg.setText("");
        // Select Item
        Item item = itemTable.getSelectionModel().getSelectedItem();


        // Edit name
        boolean isValid = editName(itemStringCellEditEvent.getNewValue(), item.getMoney(),
                item.getSerialNumber(), item);


        if (!isValid){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Name");
            itemTable.refresh();
        }

    }

    public boolean editName(String nameEdited, String money, String serialNumber, Item item){
        // Validate input
        boolean isValid = validateInput(nameEdited, item.getMoney(), item.getSerialNumber());
        if (isValid) {
            // Set name to new value
            item.setName(nameEdited);
        }
        return isValid;
    }
    public boolean testEditName(String nameEdited, String money, String serialNumber, Item item){
        // Same as exact above but for Junit testing (couldn't think of a way to get out of using FXML
        // Elements for the validateInput function)
        // Validate input
        boolean isValid = testValidateInput(nameEdited, item.getMoney(), item.getSerialNumber());
        if (isValid) {
            // Set name to new value
            item.setName(nameEdited);
        }
        return isValid;
    }

    public boolean testEditSerialNumber(String name, String money, String serialNumberEdited, List<String> serialNumbersList,
                                        Item item, String originalSerialNumber) {
        // Same as exact editserialnumber above but for Junit testing (couldn't think of a way to get out of using FXML
        // Elements for the validateInput function)

        // Validate input
        boolean isValid = testValidateInput(item.getName(), item.getMoney(), serialNumberEdited);
        // Check for no duplicate serial numbers
        boolean noDuplicate  = checkSerialNumberDuplicate(serialNumbersList, serialNumberEdited);
        System.out.println(noDuplicate);
        if (isValid && noDuplicate){
            // Set serial number to new value
            item.setSerialNumber(serialNumberEdited);
            // Remove original serial in Serial Number list and replace with new serial list
            serialNumbersList.add(serialNumberEdited);
            serialNumbersList.remove(originalSerialNumber);

        }
        return isValid && noDuplicate;
    }

    public boolean testEditValue(String name, String valueEdited, String serialNumber, Item item) {
        // Same as exact editValue above but for Junit testing (couldn't think of a way to get out of using FXML
        // Elements for the validateInput function)
        // Validate input
        boolean isValid = testValidateInput(item.getName(), valueEdited, item.getSerialNumber());

        // Format money
        String formattedMoney = formatMoney(valueEdited);

        if (isValid){
            // Set value number to new value
            item.setMoney(formattedMoney);
        }
        return isValid;
    }

    public void searchInputted(javafx.scene.input.KeyEvent keyEvent) {
        // On key press, search for value in entire table
        Finder findItem = new Finder();
        findItem.search(observableList, searchBox, itemTable);
    }
}
