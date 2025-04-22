package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import sev.adams.controller.simulationMainController;

public class CustomersListButtonCell extends ListCell<String> {
    // The button for this cell, can auto assign a table to a group or remove the group based on the context of b_assignedTable
    private Button cellButton;
    // The controller that we reference
    private simulationMainController myController;
    // If we have an assigned table ID to the customerGroup that this cell represents
    private boolean b_assignedTable;

    public CustomersListButtonCell() {
        cellButton = new Button(this.getText());
        b_assignedTable = this.getAssignedTableID() != null;

        // Set up what the button does when its pressed
        cellButton.setOnAction(event -> 
            {
            String item = this.getItem();
            // This button will auto assign a group to a table if no table is assigne
            if (this.b_assignedTable == false) {
                myController.CustomerListCellButtonPressed(item);
            } else { // If there is a table assigned the button will remove the group
                myController.CustomerGroupRemovalRequsted(this.getCustomerGroupID(), this.getAssignedTableID());
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
    // This will also update b_assignedTable to its correct value
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
                // Update the flag for if we have an assigned table
                b_assignedTable = true;
            } else {
                // Update the flag for if we have an assigned table
                b_assignedTable = false;
            }
            
            setText(cellText);

            // The main button has different effects based on if we have an assigned table, update the text to reflect this
            if (b_assignedTable == true) {
                cellButton.setText("Remove Group");
            } else {
                cellButton.setText("Auto Assign");
            }

            setGraphic(cellButton);
        }
    }
}
