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

import java.math.BigDecimal;
import java.net.URL;
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
            new Item("Sam", "xxxxxxxxxx", "45.17")
    );

    @FXML
    void addButtonClicked(ActionEvent event) {
        // Check if valid input
        boolean isValid = validateInput(nameText.getText(), moneyText.getText(), serialNumberText.getText());

        if (isValid){
            Item item = new Item(nameText.getText(), serialNumberText.getText(), moneyText.getText());
            itemTable.getItems().add(item);
        }
        else{
            errorMsg.setText("Invalid Input");
        }
    }
    public boolean validateInput(String name, String money, String serialNumber) {
        // validate input
        boolean isValid = true;

        // Check name length
        if (name.length() < 2 || name.length() > 256){
            System.out.println("Invalid length");
            isValid = false;
        }

        // Check money
        BigDecimal amount = new BigDecimal(money);

        if (amount == null){
            System.out.println("Invalid amount/currency.");
            isValid = false;
        }

        // Check Serial Number
        // Need to check for same serial number
        if (serialNumber.length() != 10 || !serialNumber.matches("^[a-zA-Z0-9]*$")){

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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));

        itemTable.setItems(observableList);
        itemTable.getColumns().addAll(nameColumn, serialNumberColumn, moneyColumn);

    }
}
