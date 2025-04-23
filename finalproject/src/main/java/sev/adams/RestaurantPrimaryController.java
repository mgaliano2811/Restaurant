package sev.adams;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class RestaurantPrimaryController {
    @FXML
    private TextField restaurantNameField; // The name of our restaurant field
    @FXML
    private CheckBox loadCheckBox;  // The checkbox that decides if we load previous info or just make a new file
    @FXML
    private TextField tableField; // How many tables will we have field
    @FXML
    private TextField minimumTableField; // Minimum capacity of the tables
    @FXML
    private TextField maximumTableField; // Maximum capacity of the tables
    @FXML
    private Slider customerFrequencySlider; // The chance for a customer to come in during a time progression
    
    private int numTables;
    private int minTables;
    private int maxTables;

    @FXML
    void initialize() {
        // Default values
        numTables = Integer.parseInt(tableField.getText());
        minTables = Integer.parseInt(minimumTableField.getText());
        maxTables = Integer.parseInt(maximumTableField.getText());
    }

    // Go into the main restaurant view
    @FXML
    private void StartRestaurant() throws IOException {
        // Create a save file, only start if it is successful
        if (createSaveFile()) {
            RestaurantApp.setRoot("simulationMain");
        }
    }

    // helper clamp method, why does java not have this in util...
    //  @pre min <= max
    private int clamp(int value, int min, int max) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }

    // Look to see if the new number input is valid, update numTables if it is
    @FXML
    private void numTablesChanged(ActionEvent event) {
        int newNumber;
        try {
            newNumber = Integer.parseInt(tableField.getText());
            numTables = clamp(newNumber, 0, 77777);
            tableField.setText(Integer.toString(numTables));
        } catch (Exception e) {
            tableField.setText(Integer.toString(numTables));
        }
    }

    // Check that the new minimum tables number is valid, and allow it to update if it is
    @FXML
    private void minTablesChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(minimumTableField.getText());
            minTables = clamp(newNumber, 1, 77777);
            minimumTableField.setText(Integer.toString(minTables));
        } catch (Exception e) {
            minimumTableField.setText(Integer.toString(minTables));
        }
    }

    // Check that the new maximum tables number is valid, and allow it to update if it is
    @FXML
    private void maxTablesChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(maximumTableField.getText());
            maxTables = clamp(newNumber, 1, 77777);
            maximumTableField.setText(Integer.toString(maxTables));
        } catch (Exception e) {
            maximumTableField.setText(Integer.toString(maxTables));
        }
    }

    // From all of our data, Create a text file that represents the restaurant so we can save it and pass it on
    //  to the next part of the program
    // The file is essentialy a list of key value pairs, which makes adding new values particularly easy because we dont need
    //  to worry about the order of the values, just they key.
    //  There is one key value pair per line
    //  returns true if a valid save file was created, false otherwise
    private boolean createSaveFile() {
        // We dont really need to do too much error checking, since the individual fields will error check when they recieve input

        // Check if we are just loading a file that already exists, or using our data to create/overwrite a save file
        boolean b_loadFile = loadCheckBox.selectedProperty().get();

        // Check if the data directory exists
        File restaurantFile = new File("This will be replaced if we dont have an exception"); // I hate java
        FileWriter writer = null;

        try {
            // Create the data directory if it doesnt already exist
            createDataDirectory();

            restaurantFile = new File("data/" + restaurantNameField.getText() + ".txt");
            boolean b_fileAlreadyExists = !restaurantFile.createNewFile(); // Wont actually create a new file if it already exists

            // If the file already exists and the user requested to load a previous file, then we are done!
            if (b_fileAlreadyExists && b_loadFile) {
                return true;
            }

            // Otherwise we need to write to the file
            writer = new FileWriter(restaurantFile); // something to write with
            // Get the data for every data field and write it with its key
            writer.write(restaurantNameKeyValue() + "\n");
            writer.write(numTablesKeyValue() + "\n");
            writer.write(minTableCapKeyValue() + "\n");
            writer.write(maxTableCapKeyValue() + "\n");
            writer.write(customerFreqKeyValue() + "\n");

            writer.close();
        } catch (Exception e) {
            System.err.println("[!] Error while trying to create/load a save file for the restaurant!");
            e.printStackTrace();
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e1) {
                    // wghta
                    return false;
                }
            }

            return false;
        }

        // At this point we will (should) have a valid file
        return true;
    }

    // Create the data directory if it does not already exist
    private void createDataDirectory() throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        File dataDirectory = new File(currentDirectory + File.separator + "data");
        dataDirectory.mkdir(); // This wont overwrite anything if it already exists, so were golden, hopefully
    }

    // Key Value getters for all of the data fields, ":" is what we use to seperate them so no funny business
    private String restaurantNameKeyValue() {
        return "restaurantName:" + restaurantNameField.getText().replaceAll(":", "");
    }

    private String numTablesKeyValue() {
        return "numTables:" + numTables;
    }

    private String minTableCapKeyValue() {
        return "minTableCap:" + minTables;
    }

    private String maxTableCapKeyValue() {
        return "maxTableCap:" + maxTables;
    }

    private String customerFreqKeyValue() {
        return "customerFrequency:" + customerFrequencySlider.getValue();
    }
}
