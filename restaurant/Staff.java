public class Staff{
    //This class is desi
    //protected methods as child classes need access(inherence)
    protected final String lastName;
    protected final String fistName;
    protected final int employeeID;
    //????????protected String password;

    //constructor method
    public Staff(String lastName, String fistName, int employeeID, String password) {
        this.lastName = lastName;
        this.fistName = fistName;
        this.employeeID = employeeID;
        //??????this.password = password;
    }

    //getters
    public String getLastName(){
        return fistName;
    }

    public String getFirstName(){
        return fistName;
    }
    @Override
    public String toString(){
        return this.fistName + this.lastName + "," + this.employeeID;
    }
}  
