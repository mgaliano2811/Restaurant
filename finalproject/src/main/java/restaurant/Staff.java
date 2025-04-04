package restaurant;

public class Staff{
    //protected methods as child classes need access(inherence)
    protected final String lastName;
    protected final String firstName;
    protected final int employeeID;

    //constructor method
    public Staff(String lastName, String firstName, int employeeID) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.employeeID = employeeID;
    }

    //getters
    public String getLastName(){
        return firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public int getEmloyeeID(){
        return employeeID;
    }

    @Override
    public String toString(){
        return this.firstName + this.lastName + "," + this.employeeID;
    }
}  
