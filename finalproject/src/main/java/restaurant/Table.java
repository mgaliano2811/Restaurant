package restaurant;

/* 
 * CLASS IS IMMUTABLE, NO PROBLEMATIC ESCAPING REFERENCES
 */

public class Table {
    private int maxCapacity;        // People we can sit on the table
    private final int tableNumber;        // Make it like an ID

    public Table(int tableNumber, int maxCapacity) {
        this.tableNumber = tableNumber;
        this.maxCapacity = maxCapacity;
    }

    public Table (Table table) {
        this.tableNumber = table.tableNumber;
        this.maxCapacity = table.maxCapacity;
    }

    public int getMaxCapacity() { return this.maxCapacity; }

    public int getTableNumber() { return this.tableNumber; }

    @Override
    public String toString() {
        // Do not change this if you are anybody but me -Sev
        //  We use these to form key value pairs
        return "TableID:" + tableNumber + ",Capacity:" + maxCapacity;
    }

    @Override
    public int hashCode() { return Integer.hashCode(tableNumber); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                // Same object
        if (obj == null || getClass() != obj.getClass()) return false;
        Table other = (Table) obj;
        return this.tableNumber == other.tableNumber;
    }
}
