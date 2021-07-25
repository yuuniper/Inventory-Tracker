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

    ObservableList<Item> observableList = FXCollections.observableArrayList(
            new Item("Sam", "1234567890", "$45.17")
    );

    List<String> serialNumbersList = new ArrayList<String>();

    @FXML
    void addButtonClicked(ActionEvent event) {

        // Clear previous error messages
        errorMsg.setText("");

        // Check if valid input
        boolean isValid = validateInput(nameText.getText(), moneyText.getText(), serialNumberText.getText(), serialNumbersList);

        if (isValid){
            // format the money
            String formattedMoney = formatMoney(moneyText.getText());
            // Make Item
            Item item = new Item(nameText.getText(), serialNumberText.getText(), formattedMoney);
            // Add item to table
            itemTable.getItems().add(item);
            // Add serial Number to List Array
            serialNumbersList.add(serialNumberText.getText());

        }
        else{
            errorMsg.setText("Invalid Input");
        }

        // Clear all fields
        clearTextBoxes();
    }

    private String formatMoney(String initial) {
        // Parse money into double
        String noDollarSign =  initial.replace("$","");
        double d = Double.parseDouble(noDollarSign);

        // Format into currency with 2 decimal places with $
        String decimal = String.format("%.2f", d);
        String withDollarSign = "$" + decimal;
        return withDollarSign;
    }

    private void clearTextBoxes() {
        // Clear all fields
         nameText.setText("");
         serialNumberText.setText("");
         moneyText.setText("");
    }


    public boolean validateInput(String name, String money, String serialNumber, List<String> itemsList) {
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

        }catch(NumberFormatException nfe){
            System.out.println("Not valid currency");
            isValid = false;
        }

        // Check Serial Number is [2, 256] characters, only contains numbers and letters,
        // and does not match any current serial Numbers
        if (serialNumber.length() != 10 || !serialNumber.matches("^[a-zA-Z0-9]*$")
                                        || itemsList.contains(serialNumber)){
            isValid = false;
        }

        return isValid;
    }

    @FXML
    void loadButtonClicked(ActionEvent event) {

    }

    @FXML
    void removeButtonClicked(ActionEvent event) {

    }

    @FXML
    void saveButtonClicked(ActionEvent event) {

    }

    @FXML
    void searchButtonClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //itemTable = new TableView<Item>();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        //nameColumn = new TableColumn<Item, String>("name");
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        //serialNumberColumn = new TableColumn<Item, String>("serialNumber");
        moneyColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("money"));
        //moneyColumn = new TableColumn<Item, String>("money");

        //itemTable.getColumns().addAll(nameColumn, serialNumberColumn, moneyColumn);
        itemTable.setItems(observableList);

        itemTable.setEditable(true);
        editTable();
    }

    private void editTable() {
        //Set properties for the column to be edited
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //nameColumn.setCellValueFactory(TextFieldTableCell.forTableColumn());
        //nameColumn.setOnEditCommit(e->(e.getTableView().getItems().get(e.getTablePosition().getRow().setName(e.getNewValue()))));
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        moneyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @FXML
    void serialNumberEditCommitted(TableColumn.CellEditEvent<Item, String> itemStringCellEditEvent) {
        // Clear previous error message
        errorMsg.setText("");
        // Get Item
        Item item = itemTable.getSelectionModel().getSelectedItem();


        boolean isValid = editSerialNumber(item.getName(), item.getMoney(), itemStringCellEditEvent.getNewValue(), serialNumbersList, item, item.getSerialNumber());
        /*// Validate input
        boolean isValid = validateInput(item.getName(), item.getMoney(), itemStringCellEditEvent.getNewValue(), serialNumbersList);

        if (isValid){
            // Set serial number to new value
            item.setSerialNumber(itemStringCellEditEvent.getNewValue());
        } else{
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Serial Number");
            itemTable.refresh();
        }*/
        //System.out.println(item.getSerialNumber());
        if (!isValid){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Serial Number");
            itemTable.refresh();
        }

    }

    public boolean editSerialNumber(String name, String money, String serialNumberEdited, List<String> serialNumbersList,
                                    Item item, String originalSerialNumber) {
        // Validate input
        boolean isValid = validateInput(item.getName(), item.getMoney(), serialNumberEdited, serialNumbersList);

        if (isValid){
            // Set serial number to new value
            item.setSerialNumber(serialNumberEdited);
            // Remove original serial in Serial Number list and replace with new serial list
            serialNumbersList.add(serialNumberEdited);
            serialNumbersList.remove(originalSerialNumber);
        }
        return isValid;
    }

    @FXML
    void valueEditCommitted(TableColumn.CellEditEvent<Item, String> itemStringCellEditEvent) {
        // Clear previous error message
        errorMsg.setText("");
        // Select Item
        Item item = itemTable.getSelectionModel().getSelectedItem();
        // Validate input
        boolean isValid = editValue(item.getName(), itemStringCellEditEvent.getNewValue(), item.getSerialNumber(), serialNumbersList, item);

        /*// Format money
        String formattedMoney = formatMoney(itemStringCellEditEvent.getNewValue());

        if (isValid){
            // Set value number to new value
            item.setMoney(formattedMoney);
        } else{
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Value");
        }
        // Refresh Display
        itemTable.refresh();*/
        if (!isValid){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Value");
        }
        // Refresh Display
        itemTable.refresh();

    }

    public boolean editValue(String name, String valueEdited, String serialNumber, List<String> serialNumbersList, Item item) {
        // Validate input
        boolean isValid = validateInput(item.getName(), valueEdited, item.getSerialNumber(), serialNumbersList);

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

        boolean isValid = editName(itemStringCellEditEvent.getNewValue(), item.getMoney(), item.getSerialNumber(), serialNumbersList, item);
        // Validate input
       /* boolean isValid = validateInput(itemStringCellEditEvent.getNewValue(), item.getMoney(), item.getSerialNumber(), serialNumbersList);

        if (isValid){
            // Set name to new value
            item.setName(itemStringCellEditEvent.getNewValue());
        } else{
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Name");
            itemTable.refresh();
        }*/
        if (!isValid){
            // Don't save new value, revert back to original (valid) value
            errorMsg.setText("Invalid Name");
            itemTable.refresh();
        }

    }

    public boolean editName(String nameEdited, String money, String serialNumber, List<String> serialNumbersList, Item item){
        // Validate input
        boolean isValid = validateInput(nameEdited, item.getMoney(), item.getSerialNumber(), serialNumbersList);
        if (isValid) {
            // Set name to new value
            item.setName(nameEdited);
        }
        return isValid;
    }
}
