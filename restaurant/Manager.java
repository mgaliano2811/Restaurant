public class Manager extends Staff{
    public Manager(String lastName, String fistName, int employeeID, String password){
        super(lastName, fistName, employeeID, password);
    }
    @Override
    public String toString() { return super.toString() + "(Manager)";}
}   
