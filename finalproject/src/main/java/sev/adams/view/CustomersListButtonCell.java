package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import sev.adams.controller.simulationMainController;

public class CustomersListButtonCell extends ListCell<String> {
    // The button for this cell
    private Button cellButton;
    // The controller that we reference
    private simulationMainController myController;
    
    public CustomersListButtonCell() {
        cellButton = new Button(this.getText());
        // Set up what the button does when its pressed
        cellButton.setOnAction(event -> 
            {
            String item = this.getItem();
            myController.CustomerListCellButtonPressed(item);
            }
        );
    }

    // Sets the reference for the controller for this
    public void setController(simulationMainController controller) {
        myController = controller;
    }

    // How we handle the info for this cell
    //  The info for an individual cell is 
    //  CustomerData is formatted like so:
    //      "customerGroupID:customerGroupSize"
    @Override
    protected void updateItem(String customerData, boolean empty) {
        super.updateItem(customerData, empty);

        if (empty || customerData == null) {
            //System.err.println("[!] Error! CustomerListButtonCell given null as customerData!");
            setText(null);
            setGraphic(null);
        } else {
            String customerGroupID = customerData.split(":")[0];
            String customerGroupSize = customerData.split(":")[1];
            setText("ID: " + customerGroupID + " | Size: " + customerGroupSize);
            cellButton.setText("Assign Group to Table");
            setGraphic(cellButton);
        }
    }
}
