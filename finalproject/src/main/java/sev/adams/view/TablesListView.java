package sev.adams.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import java.util.ArrayList;
import restaurant.Table;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class TablesListView extends ListView<String>{
    public TablesListView() {
        super();
        System.out.println("Hi");
    }

    // Updates the list using a new source list
    public void updateList(ArrayList<String> newList) {
        ObservableList<String> newObservableList = FXCollections.observableArrayList("hello", "world");
        // for (String tableString : newList) {
        //     newObservableList.add(tableString);
        // }
        this.setItems(newObservableList);
    }
}
