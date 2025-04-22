package sev.adams.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import restaurant.Table;
import sev.adams.controller.simulationMainController;

// A list of elements showing different groups of customers, who can be assigned to a table, and other things
public class CustomersListView extends ListView<String> {

    private simulationMainController myController;
    
    public CustomersListView() {
        super();
        
        // We override the cell factory to use our custom cell that is a button

        // Kill me
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                CustomersListButtonCell button = new CustomersListButtonCell();
                button.setController(myController);
                return button;
            }
        };

        this.setCellFactory(cellFactory);
    }

    // Sets the reference for the controller for this
    public void setController(simulationMainController controller) {
        myController = controller;
    }


    private void cellButtonPressed() {
        System.out.println("hi");
    }

    // A customerGroup has entered the restaurant, create a new button for it and connect the proper wires
    public void newCustomerGroup(String customerGroupData) {
        this.getItems().add(customerGroupData);
    }

    // Visually assign a customer group to one of our tables
    // @pre A there is something with the customerGroupID in our cells
    public void assignCustomerGroupToTable(Table assignedTable, String customerGroupID) {
        // Iterate through our items to find the related table
        String assignedTableString = assignedTable.toString();
        // This is horribly inefficient, but unfortunately listView sucks so bad and as far as I can tell this is the best way to update
        //  a cell. Luckily unless we have thousands of customerGroups it shouldn't actually matter.
        for (int i = 0; i < this.getItems().size(); i++) {

            String thisItem = this.getItems().get(i);
            if (thisItem.startsWith(customerGroupID + ":")) { // This shouldn't have false positives unless customerGroup ID's are non-unique, which they shouldn't be
                System.out.println("Found " + thisItem);
                this.getItems().set(i, thisItem + ":" + assignedTable.getTableNumber()); // update the cell, it gets its data from its item string
                return;
            }
        }

        // We didn't find a customer, this is an error
        System.err.println("[!] Error! assignCustomerGroupToTable was called with an invalid customerGroupID!");
    }
}
