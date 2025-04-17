package sev.adams.model;

import java.util.ArrayList;

import restaurant.Customer;
import restaurant.CustomerGroup;
import restaurant.Restaurant;
import restaurant.Table;
import sev.adams.view.CustomersListView;
import sev.adams.view.TablesListView;

// The model holds all of our actual data
public class simulationMainModel {
    // core restaurant class
    Restaurant restaurant;

    // Observers
    TablesListView tableListObserver;
    CustomersListView customersListObserver;

    /// Model data
    // How many time units have "passed"
    int currentTime;
    // All of the customerGroups currently in the restaurant
    ArrayList<CustomerGroup> customerGroups;
    // The percentage chance for a customer group to come in per time unit. 0-1 double;
    double customerFrequency;

    public simulationMainModel() {
        // Create the restaurant
        restaurant = new Restaurant();

        // Add the tables
        for (int i = 2; i <= 7; i++) { restaurant.addTable(new Table(i - 1, i)); }
        restaurant.addTable(new Table(7, 7));
        restaurant.debugPrint();

        // Fill in our other data
        currentTime = 0;
        customerGroups = new ArrayList<CustomerGroup>();
        customerFrequency = 0.7;
    }

    // Progress time by one unit, and alert things that care
    public void progressTime() {
        currentTime += 1;
        
        // See if we want to add a new customerGroup
        if (Math.random() < customerFrequency) {
            this.newCustomerGroup();
        }
    }
    
    // Add a new customerGroup and let the customersList know
    private void newCustomerGroup() {
        CustomerGroup newCustomerGroup = new CustomerGroup();

        // Add a random number of customers to this group
        for (int i = 0; i < 3; i++) {
            newCustomerGroup.addCustomer(new Customer());
        }

        // Add the customer group to our groups
        customerGroups.add(newCustomerGroup);

        // Notify the customersListObserver know something changed
        //  The view only cares (only needs to know) about a string representing some identifying data for the customerGroup
        String customerGroupDataString = currentTime + ":" + newCustomerGroup.numCustomers();
        customersListObserver.newCustomerGroup(customerGroupDataString);
    }

    // We only have one table list observer, so fill it in
    public void registerTableListObserver(TablesListView tableListView) {
        tableListObserver = tableListView; // This is the UI, we dont really care about encapsulation violations
    }

    // We only have one customer list observer, fill it in
    public void registerCustomerListObserver(CustomersListView customersListView) {
        customersListObserver = customersListView;
    }

    // Let the tableListObserver know that something changed
    public void notifyTableListObserver() {
        tableListObserver.updateList(restaurant.tableStringList());
    }
}
