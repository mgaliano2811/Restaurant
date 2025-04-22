package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

// A cell that acts like a button, so we can react to it being pressed
//  The info for an individual cell is two values seperated by a colon
    //  tableId:tableCapacity (stored in this.item)
public class TablesListButtonCell extends ListCell<String> {
    // The button for this cell
    private Button cellButton;
    
    // The Table information of the table this cell represents is stored in its item
    //  It is formatted as a key value list in a string, like so:
    //  "TableID:(tableID),Capacity:(capacity),CustomerGroup:(customerGroup)"
    //  Though customerGroup may not be assigned
    public TablesListButtonCell() {
        cellButton = new Button(this.getText());
        // Set up what the button does when its pressed
        cellButton.setOnAction(event -> 
            {
            String item = this.getItem();
            System.out.println(item);
            }
        );
    }

    // Get the tableID field from this cell's item data, it is the first field
    // @pre this cell has an assigned tableID
    private String getTableID() {
        if (this.getItem() == null || this.isEmpty()) {
            System.err.println("[!] Error! Cannot getTableID from a table cell with no assigned Table ID!");
            return null;
        }

        return this.getItem().split(":")[0];
    }

    // Get the tableCapacity field from this cell's item data, it is the second field
    // @pre this cell has an assigned tableID and tableCapacity
    private String getTableCapacity() {
        if (this.getItem() == null || this.isEmpty()) {
            System.err.println("[!] Error! Cannot getTableCapacity from a table cell with no assigned Table ID or capacity!!");
            return null;
        }

        return this.getItem().split(":")[1];
    }

    // Get the assignedCustomerGroupID field from this cell's item data, it is the third field if it exists
    // If this table cell is not assigned a customerGroupID, return null instead
    private String getAssignedCustomerGroupID() {
        if (this.getItem() == null || this.isEmpty()) {
            return null;
        }

        String[] dataFields = this.getItem().split(":");
        if (dataFields.length >= 3) {
            return dataFields[2];
        } else {
            return null;
        }
    }


    // How we handle the info for this cell
    //  The info for an individual cell is two values seperated by a colon
    //  So tableData is: "tableId:tableCapacity:assignedCustomerID"
    @Override
    protected void updateItem(String tableData, boolean empty) {
        super.updateItem(tableData, empty);

        if (empty || tableData == null) {
            setText(null);
            setGraphic(null);
        } else {
            // At this point in the program, the cells item will already have been assigned to the new tableData
            String tableID = this.getTableID();
            String tableCapacity = this.getTableCapacity();
            String assignedCustomerGroupID = this.getAssignedCustomerGroupID();
            // The string this cell will display
            String cellText = "Table Number: " + tableID + " \nTable Capacity: " + tableCapacity;
            if (assignedCustomerGroupID != null) {
                cellText += " \nCustomer Group ID: " + assignedCustomerGroupID;
            }

            setText(cellText);
            cellButton.setText("Table Info");
            setGraphic(cellButton);
        }
    }
}
