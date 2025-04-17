package sev.adams.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

// A list of elements showing different groups of customers, who can be assigned to a table, and other things
public class CustomersListView extends ListView<String> {
    
    public CustomersListView() {
        super();
        
        // We override the cell factory to use our custom cell that is a button
        this.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                return new CustomersListButtonCell();
            }
        });
    }

    // A customerGroup has entered the restaurant, create a new button for it and connect the proper wires
    public void newCustomerGroup(String customerGroupData) {
        this.getItems().add(customerGroupData);
    }
}
