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
        for (int i = 2; i <= 7; i++) { restaurant.addTable(i); }
        restaurant.addTable(7);
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
        String thisCustomerGroupID = Integer.toString(currentTime); // Convienient, though might need to be changed at some point

        CustomerGroup newCustomerGroup = new CustomerGroup(thisCustomerGroupID);

        // Add a random number of customers to this group
        for (int i = 0; i < 3; i++) {
            newCustomerGroup.addCustomer(new Customer());
        }

        // Add the customer group to our groups
        customerGroups.add(newCustomerGroup);

        // Notify the customersListObserver know something changed
        //  The view only cares (only needs to know) about a string representing some identifying data for the customerGroup
        String customerGroupDataString = thisCustomerGroupID + ":" + newCustomerGroup.numCustomers();
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

    // Return the customer group that has the given ID
    // @pre customerGroupID is in the customergroups
    private CustomerGroup getCustomerGroupFromID(String customerGroupID) {
        for (CustomerGroup thisGroup : customerGroups) {
            if (customerGroupID.equals(thisGroup.getID())) {
                return thisGroup;
            }
        }

        // Couldnt find a customer group... this shouldn't happen unless something else went wrong
        System.err.println("[!] Error! Could not find customerGroup with ID: " + customerGroupID);
        return null;
    }

    // Given a customerGroupID, automatically assign a customer group to an open table in the restaurant
    public void autoAssignGroupToTable(String customerGroupID) {
        System.out.println("Assigning CustomerGroup " + customerGroupID);
        // Get the customerGroup associated with this ID
        CustomerGroup relevantCustomerGroup = this.getCustomerGroupFromID(customerGroupID);
        // Automatically assign a table to this group
        Table assignedTable = restaurant.seatPeople(relevantCustomerGroup.numCustomers());
        if (assignedTable != null) {
            System.out.println("Assigned Table ID: " + assignedTable.getTableNumber());
        } else {
            // Couldnt find a valid table
            System.out.println("No valid table for this customer group!");
            return;
        }

        // Let the relevant observers know that this customerGroup was assigned a table

    }
}
