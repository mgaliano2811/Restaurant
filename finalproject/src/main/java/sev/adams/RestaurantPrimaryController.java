package sev.adams;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RestaurantPrimaryController {
    @FXML
    private TextField tableField;
    private int numTables;

    @FXML
    private void StartRestaurant() throws IOException {
        System.out.print("opening!");
    }

    // Look to see if the new number input is valid, update numTables if it is
    @FXML
    private void numTablesChanged(ActionEvent event) {
        int newNumber;
        try {
            newNumber = Integer.parseInt(tableField.getText());
            numTables = newNumber;
        } catch (Exception e) {
            tableField.setText(Integer.toString(numTables));
        }
    }
}
