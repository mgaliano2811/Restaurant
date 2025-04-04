package restaurant;

public class Chef extends Staff{
    private Table table;
    public Chef(String lastName, String fistName, int employeeID){
        super(lastName, fistName, employeeID);
    }
    @Override
    public String toString() { return super.toString() + "(Chef)";}
}
