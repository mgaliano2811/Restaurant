package sev.adams.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import java.util.ArrayList;
import restaurant.Table;
import sev.adams.controller.simulationMainController;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import restaurant.Table;

public class TablesListView extends ListView<String>{
    private simulationMainController myController;

    public TablesListView() {
        super();
        
        // We override the cell factory to use our custom cell that is a button
        this.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                TablesListButtonCell button = new TablesListButtonCell();
                button.setController(myController);
                return button;
            }
            
        });
    }
    
    // Sets the reference for the controller for this
    public void setController(simulationMainController controller) {
        myController = controller;
    }
    // Adds a new table to the listView
    //  @pre given a table with a unique ID that is not already in this TablesListView
    public void addTable(Table newTable) {
        this.getItems().add("" + newTable.getTableNumber() + ":" + newTable.getMaxCapacity());
    }

    // Updates the list using a new source list of tables
    //  The info for an individual cell is two values seperated by a colon
    //  tableId:tableCapacity
    public void updateList(ArrayList<Table> newTables) {
        ObservableList<String> newObservableList = FXCollections.observableArrayList();
        this.setItems(newObservableList); // Clear the previous tables in the lsit

        // Add all the tables anew
        for (Table newTable : newTables) {
            this.addTable(newTable);
        }
    }

    // When we are clicked, open up an info panel?
    @FXML
    public void onClick() {
        System.out.println("hi");
    }

    // A customerGroup with the ID of customerGroupID was assigned to a table, represent this in the list
    //  @pre assignedTable already has a representation cell inside this TablesListView
    public void assignCustomerGroupToTable(Table assignedTable, String customerGroupID) {

        // Find the assignedTable representation in our cells
        //  This is fairly slow, but we dont have a lot of flexibility in how we deal with our cells so alas
        for (int i = 0; i < this.getItems().size(); i++) {
            String thisItem = this.getItems().get(i);

            if (thisItem.startsWith(assignedTable.getTableNumber() + ":")) { // This avoids false positives so long as tableIDs are unique
                // Found the relevant table cell
                this.getItems().set(i, thisItem + ":" + customerGroupID);
                return;
            }
        }

        // Could not find a relevant table, this is an error
        System.err.println("[!] Error! Could not find the given table in this TablesListView! TableID: " + assignedTable.getTableNumber());
    }

    // The table with the given tableID had its assigned customerGroup unassigned, update the relevant cell to reflect this
    //  @pre a cell with the referenced tableID exists here
    public void tableUnassignCustomerGroup(Table table) {
        // Find the cell with the relevant table
        for (int i = 0; i < this.getItems().size(); i++) {

            String thisItem = this.getItems().get(i);
            if (thisItem.startsWith(table.getTableNumber() + ":")) {
                // Found our cell, update it without the assignedCustomerGroup
                this.getItems().set(i, "" + table.getTableNumber() + ":" + table.getMaxCapacity());
                return;
            }
        }

        // Could not find the relevant cell, this should not happen and is an error
        System.err.println("[!] Error! tableUnassignCustomerGroup() was given an invalid tableID:" + table.getTableNumber());
    }
}
