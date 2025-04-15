package database;

import java.util.HashSet;
import restaurant.*;


public class Database {
    // HashSet to look up things in constant time (I want pretty points)
    private HashSet<Staff> employees;
    private Payroll payroll;

    public Database() {
        employees = new HashSet<Staff>();
        payroll = new Payroll();
    }

    // Create and add employee (basically we hired someone)
    public void addEmployee(String name, String lastname, String type, double salary) {
        int employeeID = (int)(Math.random() * 90000000) + 10000000;
        Staff newEmployee;
        if (type.toLowerCase().contains("waiter"))      // Create waiter
            newEmployee = new Waiter(lastname, name, employeeID);
        else if (type.toLowerCase().contains("chef"))   // Create chef
            newEmployee = new Chef(lastname, name, employeeID);
        else if (type.toLowerCase().contains("manager")) // Create manager
            newEmployee = new Manager(lastname, name, employeeID);
        else newEmployee = new Staff(lastname, name, employeeID);   // Any other staff
        employees.add(newEmployee);                     // Add to employees
        payroll.addEmployee(newEmployee, salary);       // Add to payroll records
    }


}
