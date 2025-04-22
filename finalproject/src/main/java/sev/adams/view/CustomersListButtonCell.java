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
            // TODO: Add a good way to unassign tables from a group
            if (this.getAssignedTableID() == null) {
                myController.CustomerListCellButtonPressed(item);
            }
            }
        );
    }

    // Sets the reference for the controller for this
    public void setController(simulationMainController controller) {
        myController = controller;
    }

    // Gets the customerGroupID field from this.item
    //  @pre should have an assigned customerGroupID
    private String getCustomerGroupID() {
        if (this.getItem() == null || this.isEmpty()) {
            System.err.println("[!] Error! CustomerListViewCell has no assigned customerGroupID, cannot get it!");
            return null;
        } 
        return this.getItem().split(":")[0];
    }

    // Gets the customerGroupCapacity field from this.item
    //  @pre should have an assigned customerGroupID and customerGroupCapacity
    private String getCustomerCapacity() {
        if (this.getItem() == null || this.isEmpty()) {
            System.err.println("[!] Error! CustomerListViewCell has no assigned customerGroupCapacity, cannot get it!");
            return null;
        } 
        return this.getItem().split(":")[1];
    }

    // Get the assignedTableID field from this.item, if it has one
    //  otherwise returns null
    private String getAssignedTableID() {
        if (this.getItem() == null || this.isEmpty()) {
            return null;
        } 
        String[] splitData = this.getItem().split(":");
        if (splitData.length >= 3) {
            // We have an assigned table
            return splitData[2];
        } else {
            // We have no assigned table
            return null;
        }
    }

    // How we handle the info for this cell
    //  The info for an individual cell is 
    //  CustomerData is formatted like so:
    //      "customerGroupID:customerGroupSize:AssignedTableID(optional)"
    @Override
    protected void updateItem(String customerData, boolean empty) {
        super.updateItem(customerData, empty);

        if (empty || customerData == null) {
            //System.err.println("[!] Error! CustomerListButtonCell given null as customerData!");
            setText(null);
            setGraphic(null);
        } else {
            // At this point our item should already be updated, so we can use our getter methods
            String customerGroupID = this.getCustomerGroupID();
            String customerGroupSize = this.getCustomerCapacity();
            String assignedTableID = this.getAssignedTableID();
            // The string that will be displayed in this cell
            String cellText = "Group ID: " + customerGroupID + " \nSize: " + customerGroupSize;
            if (assignedTableID != null) {
                cellText += " \nTable: " + assignedTableID;
            }
            
            setText(cellText);
            cellButton.setText("Auto Assign");
            setGraphic(cellButton);
        }
    }
}
