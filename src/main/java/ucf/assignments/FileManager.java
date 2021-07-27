/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class FileManager {
    public void saveFile(List<Item> saveItems) {
        // Save List to file
        FileChooser fileChooser = new FileChooser();
        // Add extensions
        FileChooser.ExtensionFilter jsonFile = new FileChooser.ExtensionFilter("Json file", "*.json");
        FileChooser.ExtensionFilter htmlFile = new FileChooser.ExtensionFilter("HTML file", "*.html");
        FileChooser.ExtensionFilter tsvFile = new FileChooser.ExtensionFilter("TSV file", "*.txt");

        fileChooser.getExtensionFilters().addAll(jsonFile, htmlFile, tsvFile);
        // Show window
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null){
            // Determine file type
            String fileType = getFileType(file.getAbsolutePath());
            // Create a testString for testing purposes
            String testString = "";
            switch (fileType) {
                case "txt" -> testString = saveAsTSV(file, saveItems);
                case "html" -> testString = saveAsHTML(file, saveItems);
                case "json" -> testString = saveAsJSON(file, saveItems);
                default -> System.out.println("File type not found");
            }
        }
    }

    public String saveAsJSON(File file, List<Item> saveItems) {
        String testString; // for JUNIT testing
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Item i : saveItems){
            JSONObject tempObject = new JSONObject();
            // Iterate through all item objects
            tempObject.put("Value", i.getMoney());
            tempObject.put("Serial Number", i.getSerialNumber());
            tempObject.put("Name", i.getName());
            jsonArray.add(tempObject);
        }
        // Put array into JSON Object
        jsonObject.put("Inventory", jsonArray);

        // For JUNIT testing
        testString = jsonObject.toString();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return testString;
    }

    public String saveAsHTML(File file, List<Item> saveItems) {
        String HTMLBegin = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Inventory</title>"
                + "</head>"
                + "<body>"
                + "<table border = \"1\">"
                + "<tr> " +
                "<th>Value</th>" +
                "<th>Serial Number</th> " +
                "<th>Name</th> " +
                "</tr>";
        String HTMLEnd = "</table>"
                + "</body>"
                + "</html>";
        String htmlTestingString = HTMLBegin;
        try (FileWriter writer = new FileWriter(file)) {
            // Write beginning tags
            writer.write(HTMLBegin + "\n");
            // Iterate through all item objects
            for (Item i : saveItems){
                // Create individual strings
                String serialNumber = "<td>" + i.getSerialNumber() + "</td>";
                String name = "<td>" + i.getName() + "</td>";
                String value = "<td>" + i.getMoney() + "</td>";
                // Combine strings into a table row
                String tableRow = "<tr>" + value  + serialNumber + name + "</tr>\n";
                // Write to file
                writer.write(tableRow);
                htmlTestingString = htmlTestingString.concat(tableRow);
            }
            writer.write(HTMLEnd);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlTestingString + HTMLEnd;
    }


    public String saveAsTSV(File file, List<Item> saveItems) {
        // Create a test string for JUnit Testing purposes
        String testString = "";
        try (FileWriter writer = new FileWriter(file)) {
            // Iterate through all item objects
            for (Item i : saveItems){
                // Get individual strings
                String serialNumber = i.getSerialNumber();
                String name = i.getName();
                String value = i.getMoney();
                // Combine strings by using tab delimiters
                String makeLine = value + "\t" + serialNumber + "\t" + name + "\n";
                // Write to file
                writer.write(makeLine);
                // Save makeLine to testString
                testString = testString.concat(makeLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testString;
    }

    private String getFileType(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) { extension = fileName.substring(i+1); }
        return extension;
    }

    public List<Item> loadFile() {
        List<Item> loadedItems = new ArrayList<>();
        // Make fileChooser
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        // Launch
        File file = chooser.showOpenDialog(new Stage());
        // Get File type
        String fileType = getFileType(file.getAbsolutePath());

        switch (fileType) {
            case "txt" -> loadedItems = loadTSV(file.getAbsolutePath());
            case "html" -> loadedItems = loadHTML(file.getAbsolutePath());
            case "json" -> loadedItems = loadJSON(file.getAbsolutePath());
            default -> System.out.println("Invalid file");
        }

        return loadedItems;
    }

    public List<Item> loadJSON(String absolutePath) {
        List<Item> loadedItems = new ArrayList<>();
        //Creating a JSONParser object
        JSONParser jsonParser = new JSONParser();
        try {
            //Parsing the contents of the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(absolutePath));
            //Retrieving the array
            JSONArray jsonArray = (JSONArray) jsonObject.get("Inventory");
            //Iterating the contents of the array
            for (Object item : jsonArray){
                JSONObject temp = (JSONObject) item;
                String serialNumber = (String) temp.get("Serial Number");
                String value = (String) temp.get("Value");
                String name = (String) temp.get("Name");
                // Load Item into list
                Item loadItem = new Item(name, serialNumber, value);
                loadedItems.add(loadItem);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return loadedItems;
    }
       public List<Item> loadHTML(String filePath){
            List<Item> loadedItems = new ArrayList<>();
            // Convert to HTML file to string
            String htmlString = gethtmlString(filePath);

            // Use Jsoup to parse
            Document html = Jsoup.parse(htmlString);
            Element table = html.selectFirst("table");

           assert table != null;
           Iterator<Element> row = table.select("tr").iterator();
            row.next();

            while (row.hasNext()) {
                // Get data from each row
                Iterator<Element> ite = row.next().select("td").iterator();
                String value = ite.next().text();
                String serialNumber = ite.next().text();
                String name = ite.next().text();
                // Put into loaded Items list
                Item item = new Item(name, serialNumber, value);
                loadedItems.add(item);
            }

            return loadedItems;
        }
    private String gethtmlString(String filePath) {
        // Convert HTML file to string
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public List<Item> loadTSV(String filePath) {
        List<Item> loadedItems = new ArrayList<>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));

            // Check if there is another line of input
            while(sc.hasNextLine()){
                String str = sc.nextLine();
                // parse each line using delimiter
                loadedItems = parseTSVData(str, loadedItems);
            }
        } catch (IOException  exp) {
            // TODO Auto-generated catch block
            exp.printStackTrace();
        } finally{
            if(sc != null)
                sc.close();
        }
        return loadedItems;
    }

    private List<Item> parseTSVData(String str, List<Item> loadedItems) {
        String value, serialNumber, name;
        Scanner lineScanner = new Scanner(str);
        lineScanner.useDelimiter("\t");
        while(lineScanner.hasNext()){
            value = lineScanner.next();
            serialNumber = lineScanner.next();
            name = lineScanner.next();
            // Add items into list
            Item item = new Item(name, serialNumber, value);
            loadedItems.add(item);
        }
        lineScanner.close();
        return loadedItems;
    }

}
