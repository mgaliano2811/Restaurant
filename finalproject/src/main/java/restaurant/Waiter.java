package restaurant;

import java.util.ArrayList;

public class Waiter extends Staff {
    private ArrayList<Integer> assignedTables;
    public Waiter(String lastName, String firstName, int employeeID){
        super(lastName, firstName, employeeID);
        assignedTables = new ArrayList<Integer>();
    }

    @Override       // Override getEmployeeType() to new subtype
    public StaffType getEmployeeType() { return StaffType.WAITER; }

    // Assign a table to a waiter
    public void assignTable (Integer tableNumber) { assignedTables.add(tableNumber); }

    // Free a table form assigned tables of waiter
    public void unassignTable (Integer tableNumber) { 
        int i = assignedTables.indexOf(tableNumber);
        assignedTables.remove(i);
    }

    @Override
    public String toString() { return super.toString() + "(Waiter)"; }
} 
