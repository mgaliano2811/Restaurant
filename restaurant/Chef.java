public class Chef extends Staff{
    private Table table;
    public Chef(String lastName, String fistName, int employeeID, String password){
        super(lastName, fistName, employeeID, password);
    }
    @Override
    public String toString() { return super.toString() + "(Chef)";}
}
