public class Manager extends Staff{
    public Manager(String lastName, String firstName, int employeeID, String password){
        super(lastName, firstName, employeeID);
    }
    @Override
    public String toString() { return super.toString() + "(Manager)";}
}   
