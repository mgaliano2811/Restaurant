package restaurant;

import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;
import java.util.List;

import database.Database;

public class Restaurant {
    private final String restaurantName; // Final cause why not fuck it
    private ArrayList<Table> freeTables;        // Use ArrayList because number of tables is limited
    private ArrayList<Table> occupiedTables;
    // Have the list of tables divided by their maxCapacity to make seating easier
    private HashMap<Integer, ArrayList<Table>> tablesByCapacity;
    private HashMap<Waiter, ArrayList<Table>> waitersToTable;   // Tables assigned to each waiter
    private ArrayList<Order> activeOrders;

    private int maxCapacity;
    private int currCapacity;
    private Database db;
    
    public Restaurant() {
        freeTables = new ArrayList<Table>();
        occupiedTables = new ArrayList<Table>();
        restaurantName = "Whatever Name Restaurant";
        maxCapacity = 200; // For now
        currCapacity = 0;
        tablesByCapacity = new HashMap<Integer, ArrayList<Table>>();
        waitersToTable = new HashMap<Waiter, ArrayList<Table>>();
        db = new Database();
    }

    public String getRestaurantName() { return restaurantName; }    // Get name

    public int getMaxRestaurantCapacity() { return maxCapacity; }   // Get max capaciy

    public int getCurrRestaurantCapacity() { return currCapacity; } // Get people inside

    // Returns a list of all the tables in this restaurant
    public ArrayList<Table> allTables() {
        ArrayList<Table> returnList = new ArrayList<Table>();
        for (int key : tablesByCapacity.keySet()) {
            for (Table table : tablesByCapacity.get(key)) {
                returnList.add(table);
            }
        }
        return returnList;
    }

    // Will add prechecks depending on implementation of max capacity
    // Returns the table that was chosen to seat people at, or null if we could not find a table
    public Table seatPeople(int people) {
        int possibleSeats = 0;
        for (Table t: freeTables)   // For loop to get all available seats 
            if (!occupied(t)) possibleSeats += t.getMaxCapacity();
        if (possibleSeats < people) {
            System.err.println("There is not enough capacity to sit " + people + " people.");
            return null;         // Return cause we can't do anything
        }
        return actuallySeatPeople(people);
    }

    public boolean occupied (Table t) { return occupiedTables.contains(t); }

    // Loop to seat people in the seat (shit name but it is what it is)
    //  Returns the table that was chosen
    private Table actuallySeatPeople(int noOfPeople) {
        int count = noOfPeople;
        while (true) {
            if (count > biggestTable()) {
                int group1 = ((Double)Math.floor(noOfPeople/2.0)).intValue(); // Take the floor of people/2
                int group2 = ((Double)Math.ceil(noOfPeople/2.0)).intValue();  // Take the ceiling of people/2
                actuallySeatPeople(group1);
                actuallySeatPeople(group2);
                return null;
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
                        return t;
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


    // THIS IS AN UNUSED FUNCTION, COMMENTING IT SO COVERAGE IS NOT AFFECTED
    // Helper to count total number of tables
    // private int numberOfTables() {
    //     int count = 0;
    //     for (Table t: freeTables) count++;
    //     for (Table t: occupiedTables) count++;
    //     return count;
    // }

    // Helper to get the biggest Table in the restaurant
    private int biggestTable() {
        int max = 0;
        for (Integer i: tablesByCapacity.keySet()) {
            if (i > max) max = i;
        }
        return max;
    }

    // COMMENTED OUT FOR COVERAGE REASONSR COVERAGE REASONS
    // Debug funciton
    // public void debugPrint() {
    //     for (int i : tablesByCapacity.keySet()) {
    //         System.out.print("Tables with " + i + " Capacity: ");
    //         for (Table t : tablesByCapacity.get(i)) {
    //             System.out.println(t.toString());
    //             System.out.print("|");
    //         }
    //         System.out.print("\n");
    //     }
    // }

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
    public Table addTable(int capacity) {
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

        return t; // Tables are immutable baby, I can do what I want baby
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

    public void addEmployee(String name, String lastname, StaffType type, double salary) {
        Staff newEmployee = db.addEmployee(name, lastname, type, salary);
        if (type.equals(StaffType.WAITER) && newEmployee instanceof Waiter)
            waitersToTable.put((Waiter)newEmployee, new ArrayList<Table>());
    }

    public ArrayList<Waiter> getWaiterWithMostTips() {
        ArrayList<Waiter> waiters = db.waiterWithMostTips();
        if (waiters.size() == 0) {
            System.err.println("Waiters are useless they all have 0 tips");
        }
        return waiters;
    }

    public void makeOrder() {
        Integer newNumber = db.getOrders().size() + 1;
        Order newOrder = new Order(newNumber);
        activeOrders.add(newOrder);
    }

    /* Do this when we have received payment. Here we add the order to the 
     * database, since we know now it cannot change.
     * @pre: Don't pass in a null
     */
    public void closeOrder(Order o) {
        if (activeOrders.contains(o)) {
            activeOrders.remove(o);
            db.addOrder(o);
        } else
            System.err.println("Order doesn't exist or has already been closed");
    }
    /* records the total bill in the database.
     * Design by contract:
     * @pre: Don't pass a null group
     */
    public void recordGroupBill(CustomerGroup group){
        double total = group.getBill();
        database.recordBill(total);
    }
}
