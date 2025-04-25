package sev.adams.view;

import java.util.ArrayList;

import javafx.scene.control.ListView;
import restaurant.CustomerGroup;
import restaurant.Order;
import restaurant.Table;

// Allows the user to look at the details of a specific table
public class TableInfoListView extends ListView<String>{

    // Given a table and customer group, construct what we want the listView to look like
    //  This is an encapsulation error, but the other way to do this would be to pass a string representing the customerGroup
    //  which would suck. Consider this a known/intentional encapsulation error, and we promise to not try to add anything to the
    //  customerGroup (thats the only way one can be modified);
    // @pre table != null, customerGroup may be null however
    public void renderTable(Table table, CustomerGroup customerGroup) {
        this.getItems().clear(); // Clear the previous table's render

        // Step by step create the render ofo the table and its contents
        this.getItems().add("Table Number: " + Integer.toString(table.getTableNumber()));
        this.getItems().add("Table Capacity: " + Integer.toString(table.getMaxCapacity()) + " People");
        this.getItems().add("");
        // Only do this stuff if we have a customerGroup
        if (customerGroup != null) {
            this.getItems().add("Assigned Customer Group ID: " + customerGroup.getID());
            this.getItems().add("Individual Customer Orders:");
            // Print out all the orders for each customer
            ArrayList<Order> orders = customerGroup.getOrders();
            if (orders != null) {
                for (Order order : orders) {
                    this.getItems().add("   " + order.toString());
                }
            }
            // Print out the total bill for this group
            this.getItems().add("");
            this.getItems().add("Total Bill: $" + String.format("%.2f", customerGroup.getBill()));
        }
        
    }

}
