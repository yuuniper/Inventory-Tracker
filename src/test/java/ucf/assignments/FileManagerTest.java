/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileManagerTest {

    @Test
    void saveAsHTML() throws IOException {
        FileManager test = new FileManager();
        List<Item> itemsList;

        // Add items to itemsList (helper function)
        itemsList = itemsListAdd();
        // Get Directory of Test file
        String directory = createDirectory("\\save\\CreateHTML.html");
        // Create file
        File testFile = new File(directory);
        boolean isValid = testFile.createNewFile();
        System.out.println(isValid);
        // Test string match
        String correctString = """
                <!DOCTYPE html><html><head><title>Inventory</title></head><body><table border = "1"><tr> <th>Value</th><th>Serial Number</th> <th>Name</th> </tr><tr><td>$45.17</td><td>1234567890</td><td>Sam</td></tr>
                <tr><td>$67.80</td><td>abcdefghi0</td><td>Laila</td></tr>
                </table></body></html>""";
        // Test function
        String htmlString = test.saveAsHTML(testFile, itemsList);
        assertEquals(correctString, htmlString);
    }

    private List<Item> itemsListAdd() {
        // This is a helper function that creates a list of items
        List<Item> itemsList = new ArrayList<>();
        itemsList.add(new Item("Sam", "1234567890", "$45.17"));
        itemsList.add(new Item("Laila", "abcdefghi0", "$67.80"));
        return itemsList;
    }

    @Test
    void saveAsTSV() {
        FileManager test = new FileManager();
        List<Item> itemsList;

        // Add items to itemsList (helper function)
        itemsList = itemsListAdd();

        // Get Directory of Test file
        String directory = createDirectory("\\save\\CreateTSV.txt");
        // Create file
        File testFile = new File(directory);

        // Test string match
        String correctString = """
                $45.17\t1234567890\tSam
                $67.80\tabcdefghi0\tLaila
                """;
        // Test function
        String TSVString = test.saveAsTSV(testFile, itemsList);
        assertEquals(correctString, TSVString);
    }

    @Test
    void saveAsJSON() {
        FileManager test = new FileManager();
        List<Item> itemsList;

        // Add items to itemsList (helper function)
        itemsList = itemsListAdd();

        // Get Directory of Test file
        String directory = createDirectory("\\save\\CreateJSON.json");
        // Create file
        File testFile = new File(directory);

        // Test string match
        String correctString = "{\"Inventory\":[{\"Value\":\"$45.17\",\"Serial Number\":\"1234567890\",\"Name\":\"Sam\"}," +
                "{\"Value\":\"$67.80\",\"Serial Number\":\"abcdefghi0\",\"Name\":\"Laila\"}]}";
        // Test function
        String JSONString = test.saveAsJSON(testFile, itemsList);
        System.out.println(JSONString);
        assertEquals(correctString, JSONString);
    }

    private String createDirectory(String endPath) {
        String userPath = System.getProperty("user.dir");

        return userPath +"\\src\\test\\resources" + endPath;
    }

    private void iterateListItems(List<Item> loadedItems, List<String> loadedNames, List<String> loadedSerialNumbers, List<String> loadedValues) {
        for(Item i : loadedItems){
            loadedNames.add(i.getName());
            loadedSerialNumbers.add(i.getSerialNumber());
            loadedValues.add(i.getMoney());
        }
    }
    @Test
    void loadHTML(){
        // Test if loaded HTML file contains correct fields
        FileManager test = new FileManager();
        List<Item> loadedItems;
        List<String> loadedNames = new ArrayList<>();
        List<String> loadedSerialNumbers = new ArrayList<>();
        List<String> loadedValues = new ArrayList<>();
        // Get Directory of Test file
        String directory = createDirectory("\\load\\TestFileHTML.html");
        // Test loadHTML
        loadedItems = test.loadHTML(directory);

        // Iterate through list to check if contains various fields
        iterateListItems(loadedItems, loadedNames, loadedSerialNumbers, loadedValues);

        assertTrue(loadedNames.contains("Sneakers"));
        assertTrue(loadedSerialNumbers.contains("qwerty78K0"));
        assertTrue(loadedValues.contains("$15.68"));
    }

    @Test
    void loadJSON(){
        // Test if loaded JSON file contains correct fields
        FileManager test = new FileManager();
        List<Item> loadedItems;
        List<String> loadedNames = new ArrayList<>();
        List<String> loadedSerialNumbers = new ArrayList<>();
        List<String> loadedValues = new ArrayList<>();
        // Get Directory of Test file
        String directory = createDirectory("\\load\\TestFileJSON.json");
        // Test loadJSON
        loadedItems = test.loadJSON(directory);

        // Iterate through list to check if contains various fields
        iterateListItems(loadedItems, loadedNames, loadedSerialNumbers, loadedValues);

        assertTrue(loadedNames.contains("Candy"));
        assertTrue(loadedSerialNumbers.contains("NIum87123d"));
        assertTrue(loadedValues.contains("$2.33"));
    }

    @Test
    void loadTSV(){
        // Test if loaded TSV file contains correct field items
        FileManager test = new FileManager();
        List<Item> loadedItems;
        List<String> loadedNames = new ArrayList<>();
        List<String> loadedSerialNumbers = new ArrayList<>();
        List<String> loadedValues = new ArrayList<>();
        // Get Directory of Test file
        String directory = createDirectory("\\load\\TestFileTSV.txt");
        // Test loadTSV
        loadedItems = test.loadTSV(directory);

        // Iterate through list to check if contains various fields
        iterateListItems(loadedItems, loadedNames, loadedSerialNumbers, loadedValues);

        assertTrue(loadedNames.contains("Candy"));
        assertTrue(loadedSerialNumbers.contains("NIum87123d"));
        assertTrue(loadedValues.contains("$2.33"));
    }

}