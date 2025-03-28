import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.util.HashMap;

public class Restaurant {
    private final String restaurantName; // Final cause why not fuck it
    private ArrayList<Table> tables;        // Use ArrayList because number of tables is limited

    // Have the list of tables divided by their maxCapacity to make seating easier
    private HashMap<Integer, ArrayList<Table>> tablesByCapacity;

    // Make maxCapacity and currCapacity instance variables so we don't need to iterate
    // over all tables to get these numbers (more effective timewise). It also depends
    // on if we want a fixed max capacity or if we want it to depend on the tables.
    private int maxCapacity;
    private int currCapacity;
    
    public Restaurant() {
        tables = new ArrayList<Table>();
        restaurantName = "Whatever Name Restaurant";
        maxCapacity = 0; // For now
        currCapacity = 0;
    }
    
    // Will add prechecks depending on implementation of max capacity
    public void seatPeople(int people) {
        int possibleSeats = 0;
        for (Table t: tables)   // For loop to get all available seats 
            if (t.isFree()) possibleSeats += t.getMaxCapacity();
        if (possibleSeats < people) {
            System.err.println("There is not enough capasity to sit " + people + " people.");
            return;         // Return cause we can't do anything
        }
        actuallySeatPeople(people);
    }

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
                if (t.isFree()) {
                    t.seatPeople(noOfPeople);   // Seat the people in the table
                    currCapacity += noOfPeople; // Update current capacity
                    done = true;
                }
            count++;
        }
    }

    // People at the table standed up so we free it. However we might need some 
    // form to indetify the table other than ID, so we can overload the method
    // if needed for more simplicity.
    public void freeTable(int tableID) {
        sortTablesByID();       // Make sure tables are at expecter position
        if (tableID > 0 && tableID <= numberOfTables())
            tables.remove(tableID-1);   // TableID-1 = index the table is in
        else
            System.err.println("There is no table by that ID");
    }

    // Helper to sort the tables inside the ArrayList by ID, so we can assume their position in the
    // list without needing to search for them.
    private void sortTablesByID() {
        Collections.sort(tables, Comparator.comparingInt(Table::getTableNumber).reversed());
    }

    // Helper to count total number of tables
    private int numberOfTables() {
        int count = 0;
        for (Table t: tables) count++;
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
}
