import java.util.ArrayList;

public class Waiter extends Staff{
    private ArrayList<Table> assignedTables;
    public Waiter(String lastName, String firstName, int employeeID){
        super(lastName, firstName, employeeID);
    }
    @Override
    public String toString() { return super.toString() + "(Waiter)";}
} 
