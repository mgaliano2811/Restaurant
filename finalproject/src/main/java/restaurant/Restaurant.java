package restaurant;

import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;

public class Restaurant {
    private final String restaurantName; // Final cause why not fuck it
    private ArrayList<Table> freeTables;        // Use ArrayList because number of tables is limited
    private ArrayList<Table> occupiedTables;
    // Have the list of tables divided by their maxCapacity to make seating easier
    private HashMap<Integer, ArrayList<Table>> tablesByCapacity;

    private int maxCapacity;
    private int currCapacity;
    
    public Restaurant() {
        freeTables = new ArrayList<Table>();
        occupiedTables = new ArrayList<Table>();
        restaurantName = "Whatever Name Restaurant";
        maxCapacity = 0; // For now
        currCapacity = 0;
    }

    public String getRestaurantName() { return restaurantName; }    // Get name

    public int getMaxRestaurantCapacity() { return maxCapacity; }   // Get max capaciy

    public int getCurrRestaurantCapacity() { return currCapacity; } // Get people inside

    // Will add prechecks depending on implementation of max capacity
    public void seatPeople(int people) {
        int possibleSeats = 0;
        for (Table t: freeTables)   // For loop to get all available seats 
            if (occupied(t)) possibleSeats += t.getMaxCapacity();
        if (possibleSeats < people) {
            System.err.println("There is not enough capasity to sit " + people + " people.");
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
            }
            for (Table t: tablesByCapacity.get(count))
                if (!occupied(t)) {    // If Table is not occupied, remove from free & add to occupied
                    int i = freeTables.indexOf(t);
                    Table occTable = freeTables.get(i);
                    freeTables.remove(i);
                    occupiedTables.add(occTable);
                    currCapacity += noOfPeople;             // Update current capacity
                    done = true;
                }
            count++;
        }
    }

    // People at the table standed up so we free it. However we might need some 
    // form to indetify the table other than ID, so we can overload the method
    // if needed for more simplicity.
    public void freeTable(int tableID) {
        for (Table t: occupiedTables) {
            if (t.getTableNumber() == tableID) occupiedTables.remove(t);
            return;
        }
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
    }

    private int assignTableNumber() {
        ArrayList<Integer> tableIDs = new ArrayList<Integer>();
        for (Table t: freeTables) tableIDs.add(t.getTableNumber());
        for (Table t: occupiedTables) tableIDs.add(t.getTableNumber());
        for (int i = 1; i < numberOfTables(); i++) {
            if (!tableIDs.contains(i)) return i;    // If ID is available return
        }
        return numberOfTables();
    }

    public void removeTable(int tableNumber) { 
        Table t = getTable(tableNumber);
        if (t != null) {
            if (freeTables.contains(t)) freeTables.remove(t);
            else if (occupiedTables.contains(t)) occupiedTables.remove(t);
        }
    }
}
