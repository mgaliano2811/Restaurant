package restaurant;

public class Chef extends Staff{
    public Chef(String lastName, String fistName, int employeeID){
        super(lastName, fistName, employeeID);
    }

    @Override       // Override getEmployeeType() to new subtype
    public StaffType getEmployeeType() { return StaffType.CHEF; }

    @Override
    public String toString() { return super.toString() + "(Chef)";}
}
