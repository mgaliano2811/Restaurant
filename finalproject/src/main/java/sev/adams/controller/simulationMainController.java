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
        // Register all of the simulationMainModel's observers
        // none of this shit works right now, need to find a way to guarantee components are initialized before I start doing things with
        //  them
        model.registerTableListObserver(tablesList);
        model.notifyTableListObserver();

        model.registerCustomerListObserver(customersList);
    }

    // Progress time by one unit
    @FXML
    public void doTimeButton() {
        model.progressTime();
    }
}
