package restaurant;

public class Table {
    private int maxCapacity;        // People we can sit on the table
    private int currCapacity;       // No of people actually seated in the table
    private int tableNumber;        // Make it like an ID

    public Table(int tableNumber, int maxCapacity) {
        this.tableNumber = tableNumber;
        this.maxCapacity = maxCapacity;
    }
    public Table (Table table) {
        this.tableNumber = table.tableNumber;
        this.maxCapacity = table.maxCapacity;
    }

    // Simulate seating people in the table
    public void seatPeople(int noOfPeople) { 
        if (noOfPeople < 1 || noOfPeople > maxCapacity)   // Check if people fit
            this.currCapacity = noOfPeople; 
    }

    public boolean isFree() { return currCapacity == 0; }  // Return if a table is free

    // Simulate freeing up the table
    public void freeTable() { this.currCapacity = 0; }

    public int getMaxCapacity() { return this.maxCapacity; }

    public int getTableNumber() { return this.tableNumber; }  
    
    public int getCurrCapacity() { return this.currCapacity; }
}
