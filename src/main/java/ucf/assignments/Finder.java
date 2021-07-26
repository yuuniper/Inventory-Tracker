/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Finder {
    public void search(ObservableList<Item> observableList, TextField searchBox, TableView<Item> itemTable) {
        FilteredList<Item> filteredList = new FilteredList<>(observableList, b-> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) ->  {
            filteredList.setPredicate(item -> {
                return searchTable(newValue, item);
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Item> sortedData = new SortedList<>(filteredList);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        itemTable.setItems(sortedData);
    }

    public boolean searchTable(String newValue, Item item) {
        // If filter text is empty, display all items.
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }
        // Compare first name and last name of every item with filter text.
        String lowerCaseFilter = newValue.toLowerCase();

        if (item.getSerialNumber().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches serial number.
        } else if (item.getName().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches name.
        } else if  (item.getMoney().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches value.
        } else {
            return false; // Does not match.
        }
    }
}
