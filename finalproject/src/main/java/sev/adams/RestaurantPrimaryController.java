package sev.adams;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    @FXML
    private TextField waiterField;
    @FXML
    private TextField chefField;
    @FXML
    private TextField managerField;
    @FXML
    private TextField waiterPayField;
    @FXML
    private TextField chefPayField;
    @FXML
    private TextField managerPayField;
    
    private String restaurantName;
    private int numTables;
    private int minTables;
    private int maxTables;
    private int numWaiters;
    private int numChefs;
    private int numManagers;
    private int waiterPay;
    private int chefPay;
    private int managerPay;

    @FXML
    void initialize() {
        // Default values
        restaurantName = restaurantNameField.getText();
        numTables = Integer.parseInt(tableField.getText());
        minTables = Integer.parseInt(minimumTableField.getText());
        maxTables = Integer.parseInt(maximumTableField.getText());
        numWaiters = Integer.parseInt(waiterField.getText());
        numChefs = Integer.parseInt(chefField.getText());
        numManagers = Integer.parseInt(managerField.getText());
        waiterPay = Integer.parseInt(waiterPayField.getText());
        chefPay = Integer.parseInt(chefPayField.getText());
        managerPay = Integer.parseInt(managerPayField.getText());
    }

    // Go into the main restaurant view
    @FXML
    private void StartRestaurant() throws IOException {
        // Create a save file, only start if it is successful
        if (createSaveFile()) {
            // Save important data that the next scene needs to know
            createDataTransferFile(getRestaurantSaveFilePath());
            RestaurantApp.setRoot("simulationMain");
        }
    }

    // Get the string of the save file path that this restaurant will/has created
    private String getRestaurantSaveFilePath() {
        return "data/" + restaurantNameField.getText() + ".txt";
    }

    // Make sure that the restaurantNameField is kosher
    @FXML
    private void checkRestaurantNameField() {
        String newName = restaurantNameField.getText();

        // Just a bunch of checks
        if (newName.contains(":")) { // Will break our key value sorting
            resetRestaurantName();
        } else if (newName.equals("dataTransfer")) { // NOT ALLOWED, dont try to overwrite my shit, yes this is a bad solution but better ones are more complicated
            resetRestaurantName();
        } else {
            // If we get here, its a valid restuarant name
            restaurantName = restaurantNameField.getText();
        }
    }

    // Helper for if we need to reset the restuarant name because the user put something in that they shouldnt have
    private void resetRestaurantName() {
        restaurantNameField.setText(restaurantName);
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

    // Next 3 functions are all similar, validate the number of managers, chefs, and waiters
    @FXML
    private void numWaitersChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(waiterField.getText());
            numWaiters = clamp(newNumber, 0, 77777);
            waiterField.setText(Integer.toString(numWaiters));
        } catch (Exception e) {
            waiterField.setText(Integer.toString(numWaiters));
        }
    }

    @FXML
    private void numChefsChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(chefField.getText());
            numChefs = clamp(newNumber, 0, 77777);
            chefField.setText(Integer.toString(numChefs));
        } catch (Exception e) {
            chefField.setText(Integer.toString(numChefs));
        }
    }

    @FXML
    private void numManagersChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(managerField.getText());
            numManagers = clamp(newNumber, 0, 77777);
            managerField.setText(Integer.toString(numManagers));
        } catch (Exception e) {
            managerField.setText(Integer.toString(numManagers));
        }
    }

    // Next 3 functions are similar and relate to employee pay
    @FXML
    private void waiterPayChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(waiterPayField.getText());
            waiterPay = clamp(newNumber, 0, 77777);
            waiterPayField.setText(Integer.toString(waiterPay));
        } catch (Exception e) {
            waiterPayField.setText(Integer.toString(waiterPay));
        }
    }

    @FXML
    private void chefPayChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(chefPayField.getText());
            chefPay = clamp(newNumber, 0, 77777);
            chefPayField.setText(Integer.toString(chefPay));
        } catch (Exception e) {
            chefPayField.setText(Integer.toString(chefPay));
        }
    }

    @FXML
    private void managerPayChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(managerPayField.getText());
            managerPay = clamp(newNumber, 0, 77777);
            managerPayField.setText(Integer.toString(managerPay));
        } catch (Exception e) {
            managerPayField.setText(Integer.toString(managerPay));
        }
    }


    // Check that the new minimum tables number is valid, and allow it to update if it is
    @FXML
    private void minTablesChanged() {
        int newNumber;
        try {
            newNumber = Integer.parseInt(minimumTableField.getText());
            minTables = clamp(newNumber, 1, maxTables);
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
            maxTables = clamp(newNumber, minTables, 77777);
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

            restaurantFile = new File(getRestaurantSaveFilePath());
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
            writer.write(numWaitersKeyValue() + "\n");
            writer.write(numChefsKeyValue() + "\n");
            writer.write(numManagersKeyValue() + "\n");
            writer.write(waiterPayKeyValue() + "\n");
            writer.write(chefPayKeyValue() + "\n");
            writer.write(managerPayKeyValue() + "\n");

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

    // Write info related to THIS RUN of the program to a file that everything knows, so that we can pass information between scenes
    //  An example of info we want to pass on is the path to the save file to use for this file
    //  we of course use key value pairs for this file
    private void createDataTransferFile(String restaurantSaveFilePath) {
        
        try {
            // We need this directory to put the dataTransfer file into
            createDataDirectory();

            File transferDataFile = new File(getDataTransferFilePath());
            transferDataFile.createNewFile();
            FileWriter writer = new FileWriter(transferDataFile);

            writer.write("restaurantSaveFilePath:" + restaurantSaveFilePath + "\n");

            writer.close();
        } catch (Exception e) {
            // We dont really have a recourse for if this fails, it wont be the users fault, so i guess just crash...
            System.err.println("[!] Error! Something bad happened and we couldnt create the data transfer file! Crashing...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // So that everybody knows the location of the dataTransfer file
    // This would be better as a global variable but im pretty sure that computer science professors are allergic to those
    public static String getDataTransferFilePath() {
        return "data/dataTransfer";
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

    private String numWaitersKeyValue() {
        return "numWaiters:" + numWaiters;
    }

    private String numChefsKeyValue() {
        return "numChefs:" + numChefs;
    }

    private String numManagersKeyValue() {
        return "numManagers:" + numManagers;
    }

    private String waiterPayKeyValue() {
        return "waiterPay:" + waiterPay;
    }

    private String chefPayKeyValue() {
        return "chefPay:" + chefPay;
    }

    private String managerPayKeyValue() {
        return "managerPay:" + managerPay;
    }
}
