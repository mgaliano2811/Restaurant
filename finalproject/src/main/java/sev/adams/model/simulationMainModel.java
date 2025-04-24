package sev.adams.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import restaurant.Customer;
import restaurant.CustomerGroup;
import restaurant.Restaurant;
import restaurant.Table;
import sev.adams.RestaurantPrimaryController;
import sev.adams.view.CustomersListView;
import sev.adams.view.TablesListView;

// The model holds all of our actual data
public class simulationMainModel {
    // core restaurant class
    private Restaurant restaurant;

    // Observers
    private TablesListView tableListObserver;
    private CustomersListView customersListObserver;

    /// Model data
    // How many time units have "passed"
    private int currentTime;
    // All of the customerGroups currently in the restaurant
    private ArrayList<CustomerGroup> customerGroups;
    // The percentage chance for a customer group to come in per time unit. 0-1 double;
    private double customerFrequency;
    // The restaurant name
    private String restaurantName;
    // The maximum capacity of any table in our restaurant
    private int maxTableCap;

    public simulationMainModel() {
        customerGroups = new ArrayList<CustomerGroup>();
    }


    // This should be called right after actual construction
    //  The reason it isnt a constructor is because we need our observers to be filled in by the controller first
    public void fullInit() {
        // Get the save file we need to create the restaurant
        String restaurantSaveFilePath = getRestaurantSaveFilePath();

        // Create the restaurant
        restaurant = createRestaurantFromSaveFile(restaurantSaveFilePath);
        System.out.println("Created Restaurant: " + restaurantName);
    }

    // Get the path for the save file we need to make the restaurant
    private String getRestaurantSaveFilePath() {
        String restaurantSaveFilePath = null;

        try {
            // Get the file that we will read this data from
            File dataTransferFile = new File(RestaurantPrimaryController.getDataTransferFilePath());
            BufferedReader reader = new BufferedReader(new FileReader(dataTransferFile));

            // Read until we find the field that we want
            String thisLine = reader.readLine();
            while (thisLine != null) {
                // First is key, second is value
                String[] keyValue = thisLine.split(":");
                if (keyValue[0].equals("restaurantSaveFilePath")) {
                    // Found ya
                    restaurantSaveFilePath = keyValue[1];
                    break;
                }
                thisLine = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            // We dont have a recourse if this happens, just crash
            System.err.println("[!] Error! Could not get the restuarant save file! Crashing...");
            e.printStackTrace();
            System.exit(1);
        }

        if (restaurantSaveFilePath == null) {
            // This will probably cause some other crash, but thats not our problem,
            //  and luckily it will immediately happen
            System.err.println("[!] Error! Could not get the restuarant save file!");
        }

        return restaurantSaveFilePath;
    }

    // Create a restuarant from a given save file.
    //  Simpler than it seems, everything in the save file is just a key value pair, so we just piece together the information we need
    //  from that without regard to the order.
    // This will also fill in some of the variables on this model that are held in the save file
    // Returns the restaurant that was created
    // @pre The restaurantSaveFilePath references a save file that has all the required values, this should be
    //  guaranteed by RestaurantPrimaryController
    private Restaurant createRestaurantFromSaveFile(String restaurantSaveFilePath) {
        Restaurant newRestaurant = null;
        
        try {
            // All of the values that we will read in
            String restaurantName = "ERROR";
            int numTables = 7;
            int minTableCap = 1;
            int maxTableCap = 7;
            double customerFrequency = 0.7;

            // Read the save file and get all the values that we will need
            File restaurantSaveFile = new File(restaurantSaveFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(restaurantSaveFile));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                // The key is the first element, the second element is the value
                String[] keyValue = thisLine.split(":");
                String key = keyValue[0];
                String value = keyValue[1];

                if (key.equals("restaurantName")) {
                    restaurantName = value;
                } else if (key.equals("numTables")) {
                    numTables = Integer.parseInt(value);
                } else if (key.equals("minTableCap")) {
                    minTableCap = Integer.parseInt(value);
                } else if (key.equals("maxTableCap")) {
                    this.maxTableCap = Integer.parseInt(value);
                    maxTableCap = this.maxTableCap; //:trollface:
                } else if (key.equals("customerFrequency")) {
                    customerFrequency = Double.parseDouble(value);
                }

                thisLine = reader.readLine();
            }
            reader.close();

            // At this point we have all of the needed values, time to fill them in where needed
            this.restaurantName = restaurantName;
            newRestaurant = new Restaurant();
            this.addTables(newRestaurant, numTables, minTableCap, maxTableCap);
            this.customerFrequency = customerFrequency;

        } catch (Exception e) {
            // Another situation where I feel we have no recourse, and its best to just crash
            System.err.println("[!] Error! Could not create a restaurant! Crashing...");
            e.printStackTrace();
            System.exit(1);
        }

        if (newRestaurant == null) {
            // Another situation where I feel we have no recourse, and its best to just crash
            System.err.println("[!] Error! Could not create a restaurant! Crashing...");
            System.exit(1);
        }

        return newRestaurant;
    }


    // Create numTables tables with a capacity in range [minTableCap, maxTableCap]
    // This will notify observers that tables were created
    // @pre minTableCap <= maxTableCap, newRestaurant not null, 0 <= numTables
    private void addTables(Restaurant someRestaurant, int numTables, int minTableCap, int maxTableCap) {
        Random random = new Random();
        for (int i = 0; i < numTables; i++) {
            Table newTable = someRestaurant.addTable(random.ints(minTableCap, maxTableCap).findFirst().getAsInt());
            notifyTableListObserverOfNewTable(newTable);
        }
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
        // Choose how many customers will come in, based on the max capacity that a table can have
        int customerNum = new Random().ints(1, maxTableCap).findFirst().getAsInt();
        for (int i = 0; i < customerNum; i++) {
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
