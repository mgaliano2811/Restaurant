package sev.adams.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import java.util.ArrayList;
import restaurant.Table;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TablesListView extends ListView<String>{

    public TablesListView() {
        super();
        
        // We override the cell factory to use our custom cell that is a button
        this.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                return new TablesListButtonCell();
            }
            
        });
    }

    // Updates the list using a new source list
    //  The info for an individual cell is two values seperated by a colon
    //  tableId:tableCapacity
    public void updateList(ArrayList<String> newList) {
        ObservableList<String> newObservableList = FXCollections.observableArrayList();
        for (String tableString : newList) {
            newObservableList.add(tableString);
        }
        this.setItems(newObservableList);
    }

    // When we are clicked, open up an info panel?
    @FXML
    public void onClick() {
        System.out.println("hi");
    }
}
