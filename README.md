# Inventory Tracker (JavaFX)

A desktop inventory management app written for **COP3330 (Object-Oriented Programming), Summer 2021, at UCF** (Assignment 5). This is archived coursework, kept for reference and not maintained.

## What it does

- Keeps a table of items, each with a value, a serial number, and a name
- Validates input: the name must be 2-256 characters, the value must be a number (shown as currency, for example `$15.68`), and the serial number must be exactly 10 letters or digits and unique
- Edit any field in place, remove items, and sort by column
- Search by name, value, or serial number
- Save the inventory as TSV, HTML, or JSON, and load it back from any of those formats

## Tools

- Java with JavaFX 16 (fetched automatically by the Gradle JavaFX plugin)
- Gradle (the included wrapper uses 8.14.3, which runs on JDK 17 through 21)
- Gson, org.json, and json-simple for JSON; jsoup for reading HTML
- JUnit 5
- PlantUML class diagram in `uml/InventoryTracker.puml`

## Build, test, and run

```bash
./gradlew build
./gradlew run
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Usage

- **Add an item:** enter a value, serial number, and name, then click "+". If the input is invalid or the serial number is already used, an error message appears.
- **Edit an item:** double-click a field and type. The change is saved when you click out of the field.
- **Remove an item:** select it and click "-".
- **Search:** type a name, value, or serial number in the search bar at the top.
- **Save:** File > Save, then save as a TSV, HTML, or JSON file.
- **Load:** File > Load, then pick a TSV, HTML, or JSON file.
