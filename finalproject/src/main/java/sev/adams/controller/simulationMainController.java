package sev.adams.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import restaurant.Table;
import sev.adams.model.simulationMainModel;
import sev.adams.view.TablesListView;

public class simulationMainController {
    private simulationMainModel model;

    @FXML
    private Label tablesLabel;
    @FXML
    private TablesListView tablesList;
    @FXML
    private Button button;

    public simulationMainController() {
        model = new simulationMainModel();

        // Register all of the simulationMainModel's observers
        // none of this shit works right now, need to find a way to guarantee components are initialized before I start doing things with
        //  them
        model.registerTableListObserver(tablesList);
        model.notifyTableListObserver();
    }

    @FXML
    public void doButton() {
        button.setText("BUTTON");
        System.out.print("button");
    }
}
