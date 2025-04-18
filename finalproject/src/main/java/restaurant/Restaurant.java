package restaurant;

import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;
import java.util.List;

public class Restaurant {
    private final String restaurantName; // Final cause why not fuck it
    private ArrayList<Table> freeTables;        // Use ArrayList because number of tables is limited
    private ArrayList<Table> occupiedTables;
    // Have the list of tables divided by their maxCapacity to make seating easier
    private HashMap<Integer, ArrayList<Table>> tablesByCapacity;
    private HashMap<Waiter, ArrayList<Table>> waitersToTable;   // Tables assigned to each waiter

    private int maxCapacity;
    private int currCapacity;
    
    public Restaurant() {
        freeTables = new ArrayList<Table>();
        occupiedTables = new ArrayList<Table>();
        restaurantName = "Whatever Name Restaurant";
        maxCapacity = 200; // For now
        currCapacity = 0;
        tablesByCapacity = new HashMap<Integer, ArrayList<Table>>();
        waitersToTable   = new HashMap<Waiter, ArrayList<Table>>();
    }

    public String getRestaurantName() { return restaurantName; }    // Get name

    public int getMaxRestaurantCapacity() { return maxCapacity; }   // Get max capaciy

    public int getCurrRestaurantCapacity() { return currCapacity; } // Get people inside

    // Returns a list of strings representing all of the tables
    public ArrayList<String> tableStringList() {
        ArrayList<String> returnList = new ArrayList<String>();
        for (int key : tablesByCapacity.keySet()) {
            for (Table table : tablesByCapacity.get(key)) {
                returnList.add(table.toString());
            }
        }
        return returnList;
    }

    // Will add prechecks depending on implementation of max capacity
    public void seatPeople(int people) {
        int possibleSeats = 0;
        for (Table t: freeTables)   // For loop to get all available seats 
            if (!occupied(t)) possibleSeats += t.getMaxCapacity();
        if (possibleSeats < people) {
            System.err.println("There is not enough capacity to sit " + people + " people.");
            return;         // Return cause we can't do anything
        }
        actuallySeatPeople(people);
    }

    public boolean occupied (Table t) { return occupiedTables.contains(t); }

    // Loop to seat people in the seat (shit name but it is what it is)
    private void actuallySeatPeople(int noOfPeople) {
        boolean done = false;
        int count = noOfPeople;
        while (!done) {
            if (count > biggestTable()) {
                int group1 = ((Double)Math.floor(noOfPeople/2)).intValue(); // Take the floor of people/2
                int group2 = ((Double)Math.ceil(noOfPeople/2)).intValue();  // Take the ceiling of people/2
                actuallySeatPeople(group1);
                actuallySeatPeople(group2);
                return;
            }
            List<Table> bucket = tablesByCapacity.get(count);
            if (bucket != null)
                for (Table t: bucket) {
                    if (!occupied(t)) {    // If Table is not occupied, remove from free & add to occupied
                        int i = freeTables.indexOf(t);
                        Table occTable = freeTables.get(i);
                        freeTables.remove(i);           // Remove from free tables
                        occupiedTables.add(occTable);   // Add to occupied tables
                        assignWaiter(occTable);         // Assign waiter to table
                        currCapacity += noOfPeople;     // Update current capacity
                        done = true;
                    }
                }
            count++;
        }   
    }

    // People at the table left up so we free it
    public void freeTable(int tableID) {
        Table tableToFree = null;
        for (Table t: occupiedTables) {
            if (t.getTableNumber() == tableID) {
                tableToFree = t;
            }
        }
        if (tableToFree != null) {      // If there is a match
            occupiedTables.remove(tableToFree);
            freeTables.add(tableToFree);        // Free table
            for (ArrayList<Table> assigned : waitersToTable.values()) {
                if (assigned.contains(tableToFree)) {
                    assigned.remove(tableToFree);     // Deassign table from waiter
                    return;
                }
            }
        }
        // If we get here there is no match
        for (Table t: freeTables) {
            if (t.getTableNumber() == tableID){
                System.err.println("Table is already free");
                return;
            }
        }
        System.err.println("There is no table by that ID");
    }

    // Helper to count total number of tables
    private int numberOfTables() {
        int count = 0;
        for (Table t: freeTables) count++;
        for (Table t: occupiedTables) count++;
        return count;
    }

    // Helper to get the biggest Table in the restaurant
    private int biggestTable() {
        int max = 0;
        for (Integer i: tablesByCapacity.keySet()) {
            if (i > max) max = i;
        }
        return max;
    }

    // Debug funciton
    public void debugPrint() {
        for (int i : tablesByCapacity.keySet()) {
            System.out.print("Tables with " + i + " Capacity: ");
            for (Table t : tablesByCapacity.get(i)) {
                System.out.println(t.toString());
                System.out.print("|");
            }
            System.out.print("\n");
        }
    }

    /* Design by contract:
     * @pre: Don't pass in a negative
     */
    public Table getTable(int tableNumber) {
        for (Table t: occupiedTables) {
            if (t.getTableNumber() == tableNumber)
                return t;
        }
        for (Table t: freeTables) {
            if (t.getTableNumber() == tableNumber)
                return t;
        }
        System.err.println("Invalid table number");
        return null;
    }

    /* Design by contract:
     * @pre: Don't pass in a negative
     */
    public boolean tableOccupied (int tableNumber) {
        return occupiedTables.contains(getTable(tableNumber));
    }

    public boolean tableOccupied (Table t) { return occupiedTables.contains(t); }

    /* Design by contract:
     * @pre: Don't pass in a negative
     */
    public void addTable(int capacity) {
        Table t = new Table (assignTableNumber(), capacity);
        freeTables.add(t);
        if (tablesByCapacity.keySet().contains(capacity)) {
            ArrayList<Table> listToAdd = tablesByCapacity.get(capacity);
            listToAdd.add(t);
        } else {      // If there are no tables with that capcity, create list first
            tablesByCapacity.put(capacity, new ArrayList<Table>());
            ArrayList<Table> listToAdd = tablesByCapacity.get(capacity);
            listToAdd.add(t);
        }
    }

    // Helper to get the minimum table number available 
    private int assignTableNumber() {
        ArrayList<Integer> tableIDs = new ArrayList<Integer>();
        for (Table t: freeTables) tableIDs.add(t.getTableNumber());
        for (Table t: occupiedTables) tableIDs.add(t.getTableNumber());
        for (int i = 1; ; i++) {
            if (!tableIDs.contains(i)) return i;
        }
    }

    // Remove a table
    public void removeTable(int tableNumber) { 
        Table t = getTable(tableNumber);
        if (t != null) {
            if (!occupied(t)) {
                freeTables.remove(t);
                ArrayList<Table> list = tablesByCapacity.get(t.getMaxCapacity());
                if (list != null) list.remove(t);
            }
            else System.err.println("Cannot remove an occupied table");
        }
    }

    // Assign waiter with the least tables currently assigned the table
    // @pre: Don't pass in null Table
    public void assignWaiter(Table table) {
        if (waitersToTable == null || waitersToTable.isEmpty()) {
            System.err.println("No waiters available to assign table " + table.getTableNumber());
            return;
        }
        int minNoOfTables = Integer.MAX_VALUE;      // Minimum num of tables assigned to a waiter
        Waiter waiterToAssign = null;     // Waiter with the minimum tables assigned
        for (Waiter w: waitersToTable.keySet()) {
            ArrayList<Table> assigned = waitersToTable.get(w);
            if (minNoOfTables > assigned.size()) {
                minNoOfTables = assigned.size();
                waiterToAssign = w;
            }
        }
        waitersToTable.get(waiterToAssign).add(table);      // Assign the table
    }

    /*
     * This will be used in the view to add waiter to database and
     * to the restaurant, so we can assign tables (has to be done in parallel)
     * We will pass in the same waiter object because its immutable, so we will not
     * have any duplicates
     */
    public void addWaiter(Waiter w) { waitersToTable.put(w, new ArrayList<Table>()); }

}
