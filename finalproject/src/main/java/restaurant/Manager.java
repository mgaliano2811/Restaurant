package restaurant;

public class Manager extends Staff{
    public Manager(String lastName, String firstName, int employeeID){
        super(lastName, firstName, employeeID);
    }

    @Override       // Override getEmployeeType() to new subtype
    public StaffType getEmployeeType() { return StaffType.MANAGER; }

    @Override
    public String toString() { return super.toString() + " (Manager)";}
}   
