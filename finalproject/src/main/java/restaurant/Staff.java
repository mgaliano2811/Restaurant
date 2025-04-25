package restaurant;

import java.util.Random;

public class Staff{
    //protected methods as child classes need access (inheritance)
    protected final String lastName;
    protected final String firstName;
    protected final int employeeID;

    // Constructor
    public Staff(String lastName, String firstName, int employeeID) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.employeeID = employeeID;
    }

    // Getters
    public String getLastName() { return lastName; }

    public String getFirstName() { return firstName; }

    public int getEmloyeeID() { return employeeID; }

    public StaffType getEmployeeType() { return StaffType.STAFF; }

    @Override
    public String toString(){
        return this.firstName + " " + this.lastName + ", Employee ID: " + this.employeeID;
    }

    // Override hashCode and base it on unique employeeID
    @Override
    public int hashCode() {
        return Integer.hashCode(employeeID);
    }

    // Override equals to consider two Staff objects equal if they have the same employeeID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                // Same object
        if (obj == null || getClass() != obj.getClass()) return false;
        Staff other = (Staff) obj;
        return this.employeeID == 
        other.employeeID;
    }

    // Fluff method for getting a random first name
    public static String randomFirstName() {
        String[] names = {"Severian", "Matteo", "Ian", "Paris"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }

    // Fluff method for getting a random last name
    public static String randomLastName() {
        String[] names = {"Garcia", "Galiano", "Sanchez", "Adams"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }
}  
