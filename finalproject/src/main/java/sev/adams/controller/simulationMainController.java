package sev.adams.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import restaurant.Table;
import sev.adams.model.simulationMainModel;
import sev.adams.view.CustomersListView;
import sev.adams.view.TablesListView;

public class simulationMainController {
    private simulationMainModel model;

    public final String debugString = "mainController";

    @FXML
    private CustomersListView customersList;
    @FXML
    private Label tablesLabel;
    @FXML
    private TablesListView tablesList;
    @FXML
    private Button timeButton;

    public simulationMainController() {
        model = new simulationMainModel();
    }

    // We can guarantee everything is injected when this is called, but cannot guarantee the same when the constructor is called
    @FXML
    void initialize() {
        // Register all of the simulationMainModel's observers, and set the myController field of our view
        // If any of this garbage makes it into the final submission I will cry tears of pure sorrow
        model.registerTableListObserver(tablesList);
        customersList.setController(this);
        model.registerCustomerListObserver(customersList);

        // After the observers are filled in, we can fully initialize the model
        model.fullInit();
    }

    // Progress time by one unit
    @FXML
    public void doTimeButton() {
        model.progressTime();
    }

    // A button was pressed in the customerList listview, respond to it by...TODO
    //  cellInfo is a condensed string that represtents the customerGroup represented by the cell
    //  cell info is formatted like the following:
    //      "customerGroupID:customerGroupSize"
    public void CustomerListCellButtonPressed(String cellInfo) {
        if (cellInfo == null) {
            System.err.println("[!] Error! CustomerListButtonCell has no associated Data!");
            return;
        }

        // The ID this button represents
        String customerGroupID = cellInfo.split(":")[0];

        // Tell the model to assign this group to a table
        model.autoAssignGroupToTable(customerGroupID);
    }

    // A button was pressed in the customerList listview, requesting that a customerGroup be removed from the restaurant
    //  assignedTableID is allowed to be null here
    public void CustomerGroupRemovalRequsted(String customerGroupID, String assignedTableID) {
        // Let the model know that it needs to update its customerGroups
        model.removeCustomerGroup(customerGroupID, assignedTableID);
    }
}
