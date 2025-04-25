package sev.adams.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import restaurant.Table;
import sev.adams.model.simulationMainModel;
import sev.adams.view.CustomersListView;
import sev.adams.view.EmployeeList;
import sev.adams.view.TableInfoListView;
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
    @FXML 
    private EmployeeList employeeList;
    @FXML
    private TableInfoListView tableInfoList;

    public simulationMainController() {
        model = new simulationMainModel();
    }

    // We can guarantee everything is injected when this is called, but cannot guarantee the same when the constructor is called
    @FXML
    void initialize() {
        // Register all of the simulationMainModel's observers, and set the myController field of our view
        tablesList.setController(this);
        customersList.setController(this);

        model.registerTableListObserver(tablesList);
        model.registerCustomerListObserver(customersList);
        model.registerEmployeeListObserver(employeeList);
        model.registerTableInfoListObserver(tableInfoList);

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

    // A button in the table list requested that the information related to its table be shown in more detail
    //  in the TableInfoListView panel.
    // We are given a string that represents all the information we need to find the data the cell
    //  that requested this represents, it is formatted as follows (customerGroup may not exist)
    // "(tableID):(capacity):(customerGroupID)"
    // @pre we have a tableID and capacity that is not null, this will not happen unless another error happens during
    //  runtime. All fields in the string should be valid integers
    public void tableInformationRequested(String itemInfo) {
        // Split the item into its tableID and customerGroupID
        int tableID = -1;
        int customerID = -1; // This may or may not be given, negative one is nonsense enough

        String[] dataFields = itemInfo.split(":");
        tableID = Integer.parseInt(dataFields[0]); // We assume we have this
        if (dataFields.length >= 3) {
            customerID = Integer.parseInt(dataFields[2]);
        }

        // Pass this info to the model, which will get the needed tables and customerGroups and then pass it to TableInfoListView to render
        model.renderTableInfo(tableID, customerID);
    }
}
