package sev.adams.model;

import java.util.ArrayList;

import restaurant.Restaurant;
import restaurant.Table;
import sev.adams.view.TablesListView;

// The model holds all of our actual data
public class simulationMainModel {
    // core restaurant class
    Restaurant restaurant;

    // Observers
    TablesListView tableListObserver;

    public simulationMainModel() {
        // Create the restaurant
        restaurant = new Restaurant();

        // Add the tables
        for (int i = 2; i <= 7; i++) { restaurant.addTable(new Table(i - 1, i)); }
        restaurant.addTable(new Table(7, 7));
        restaurant.debugPrint();
    }

    // We only have one table list observer, so fill it in
    public void registerTableListObserver(TablesListView tableListView) {
        tableListObserver = tableListView; // This is the UI, we dont really care about encapsulation violations
    }

    // Let the tableListObserver know that something changed
    public void notifyTableListObserver() {
        tableListObserver.updateList(restaurant.tableStringList());
    }
}
