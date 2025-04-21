package sev.adams.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
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
}
