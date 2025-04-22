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

        // Now the information is sorted in the model, we need to let observers know so we can update info on the view
        notifyTableViewObserverOfAssignedCustomerGroup(assignedTable, relevantCustomerGroup.getID());
        notifyCustomerViewObserverOfAssignedCustomerGroup(assignedTable, relevantCustomerGroup.getID());

    }

    // Remove the customerGroup with customerGroupID from our restaurant representation
    // @pre There is a customerGroup with customerGroupID in our representation
    // assignedTableID is allowed to be null, that just means this group didn't have an assignedTableID
    public void removeCustomerGroup(String customerGroupID, String assignedTableID) {
        // First remove the customerGroup from our representation
        for (CustomerGroup thisGroup : customerGroups) {
            if (thisGroup.getID() == customerGroupID) {
                // Found the customerGroup that we want to remove
                customerGroups.remove(thisGroup);
                break;
            }
        }

        // Free the assigned table (if we have an assigned table)
        // TODO: Might not be the best idea to pass the assignedTableID around like this, we should make it so its stored in the restaurant?
        if (assignedTableID != null) {
            Table freedTable = restaurant.getTable(Integer.parseInt(assignedTableID));
            restaurant.freeTable(Integer.parseInt(assignedTableID));
            // Let the tablesListObserver know that the table was freed
            notifyTableViewObserverOfFreedTable(freedTable);
        }

        // Let our CustomerViewObserver that the customerGroup was removed
        notifyCustomerViewObserverOfRemovedCustomerGroup(customerGroupID);
    }

    //////////////////////////////////////////////////////////////
    /// Observer Methods
    //////////////////////////////////////////////////////////////

    // We only have one table list observer, so fill it in
    public void registerTableListObserver(TablesListView tableListView) {
        tableListObserver = tableListView; // This is the UI, we dont really care about encapsulation violations
    }

    // We only have one customer list observer, fill it in
    public void registerCustomerListObserver(CustomersListView customersListView) {
        customersListObserver = customersListView;
    }

    // Completely clear and set the tableListObserver with a new set of tables
    public void notifyTableListObserverFullUpdate() {
        tableListObserver.updateList(restaurant.allTables());
    }

    // Let the tableListObserver know that a new table was added
    public void notifyTableListObserverOfNewTable(Table newTable) {
        tableListObserver.addTable(newTable);
    }

    // Let the customersListObserver know that a customer group has been assigned to a table, and that it needs to update its view
    //  assignedTable is the table that has been assigned
    //  customerGroupID is the... well guess
    private void notifyCustomerViewObserverOfAssignedCustomerGroup(Table assignedTable, String customerGroupID) {
        customersListObserver.assignCustomerGroupToTable(assignedTable, customerGroupID);
    }

    // Let the customersListObserver know that a customer group has been assigned to a table, and that it needs to update its view
    //  assignedTable is the table that has been assigned
    //  customerGroupID is the... well guess
    private void notifyTableViewObserverOfAssignedCustomerGroup(Table assignedTable, String customerGroupID) {
        tableListObserver.assignCustomerGroupToTable(assignedTable, customerGroupID);
    }

    // Let the customerListObserver know that a customerGroup was removed from our representation
    //  @pre the customerViewList has a cell with a customerGroupID
    private void notifyCustomerViewObserverOfRemovedCustomerGroup(String customerGroupID) {
        customersListObserver.removeCustomerGroupCell(customerGroupID);
    }

    // Let the TableListObserver know that the table with tableID had its assigned customerGroup was unassigned, and so it should
    //  update itself
    private void notifyTableViewObserverOfFreedTable(Table table) {
        tableListObserver.tableUnassignCustomerGroup(table);
    }
    
}
