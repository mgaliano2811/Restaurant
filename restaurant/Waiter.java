public class Waiter extends Staff{
    private Table table;
    public Waiter(String lastName, String fistName, int employeeID, String password){
        super(lastName, fistName, employeeID, password);
    }
    @Override
    public String toString() { return super.toString() + "(Waiter)";}
} 
