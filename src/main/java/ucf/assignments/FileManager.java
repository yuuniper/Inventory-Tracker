/*
 *
 *  *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  *  Copyright 2021 Alice Yu
 *
 */

package ucf.assignments;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
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
            if (fileType.equals("txt")){
                saveAsTSV(file, saveItems);
            } else if (fileType.equals("html")){
                saveAsHTML(file, saveItems);
            } else if(fileType.equals("json")){
                saveAsJSON(file, saveItems);
            } else{
                System.out.println("File type not found");
            }
        }
    }

    private void saveAsJSON(File file, List<Item> saveItems) {
        JSONObject jsonObject = new JSONObject();

        /*for (Item i : saveItems){
            JSONObject tempObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            // Iterate through all item objects
            tempObject.add("Value", i.getMoney());
            tempObject.add("Serial Number", i.getSerialNumber());
            tempObject.add("Name", i.getName());
            jsonArray.put(tempObject);
            // Put array into JSON Object
            jsonObject.put("Item", jsonArray);
        }*/


        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAsHTML(File file, List<Item> saveItems) {
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
            }
            writer.write(HTMLEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveAsTSV(File file, List<Item> saveItems) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (fileType.equals("txt")){
            loadedItems = loadTSV(file.getAbsolutePath());
        } else if (fileType.equals("html")){
            loadedItems = loadHTML(file.getAbsolutePath());
        }

        return loadedItems;
    }

    private List<Item> loadHTML(String filePath) {
        List<Item> loadedItems = new ArrayList<>();
        // Convert to HTML file to string
        String htmlString = gethtmlString(filePath);

        // Use Jsoup to parse
        Document html = Jsoup.parse(htmlString);
        Element table = html.selectFirst("table");

        Iterator<Element> row = table.select("tr").iterator();
        row.next();

        while (row.hasNext())
        {
            // Get data from each row
            Iterator<Element> ite = ((Element)row.next()).select("td").iterator();
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
        String content = contentBuilder.toString();
        return content;
    }

    private List<Item> loadTSV(String filePath) {
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
            Item item = new Item(name, serialNumber, value);
            loadedItems.add(item);
        }
        lineScanner.close();
        return loadedItems;
    }

}
