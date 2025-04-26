package database;

import java.util.ArrayList;
import java.util.HashSet;
import restaurant.*;


public class Database {
    // HashSet to look up things in constant time (I want pretty points)
    private HashSet<Staff> employees;
    private Payroll payroll;
    private ArrayList<Order> allOrders;

    public Database() {
        employees = new HashSet<Staff>();
        payroll = new Payroll();
        allOrders = new ArrayList<Order>();
    }

    private int generateUniqueID() {
        int employeeID = (int)(Math.random() * 90000000) + 10000000;
        for (Staff e: employees)     // If ID is not unique, regenerate
            if (e.getEmloyeeID() == employeeID) return generateUniqueID();
        return employeeID;      // We have unique ID
    }

    // Create and add employee (basically we hired someone)
    public Staff addEmployee(String name, String lastname, StaffType type, double salary) {
        int employeeID = generateUniqueID();
        Staff newEmployee;
        if (type.equals(StaffType.WAITER))           // Create waiter
            newEmployee = new Waiter(lastname, name, employeeID);
        else if (type.equals(StaffType.CHEF))        // Create chef
            newEmployee = new Chef(lastname, name, employeeID);
        else if (type.equals(StaffType.MANAGER))     // Create manager
            newEmployee = new Manager(lastname, name, employeeID);
        else newEmployee = new Staff(lastname, name, employeeID);   // Any other staff
        employees.add(newEmployee);                     // Add to employees
        payroll.addEmployee(newEmployee, salary);       // Add to payroll records
        return newEmployee;
    }

    // Will complete this later when we have a view
    public void fireEmployee(String name, String lastname) {
        ArrayList<Staff> matches = new ArrayList<Staff>();
        for (Staff s: employees) {
            if (s.getFirstName().equals(name) && s.getLastName().equals(lastname))
                matches.add(s);
        }
        if (matches.size() == 0) {
            System.err.println("Couldn't find employee " + name + ", " + lastname);
        } else if (matches.size() == 1) {
            fireEmployee(matches.get(0).getEmloyeeID());
        } else {
            // TODO: Find a way to number on the view or make them pick
        }
    }

    public void fireEmployee(int ID) {
        if (validateInputID(ID))
            for (Staff s: employees)
                if (s.getEmloyeeID() == ID) {
                    employees.remove(s);
                    payroll.removeEmployee(ID);
                    return;
                }
    }

    // Make sure ID exists in database (so we don't need to check later)
    public boolean validateInputID (int ID) {
        for (Staff s: employees)
            if (s.getEmloyeeID() == ID) return true;
        System.err.println("No employee with ID " + ID);
        return false;
    }

    // Method to pass info into Payroll class
    public void recordTip(int ID, double tipAmount) { 
        if (validateInputID(ID))
            payroll.recordTip(ID, tipAmount); 
    }

    public Double getTip(int ID) { 
        if (validateInputID(ID))
            return payroll.getTip(ID);
        else return null;
    }

    public String getPayrollInfo(int ID) {
        if (validateInputID(ID)) 
            return payroll.getPayrollRecord(ID);
        else return null;
    }

    // Get salaries that we need to pay each 'month'
    public double getTotalPayroll() { return payroll.totalSalariesExpense(); }

    public void updateSalary(int ID, double newSalary) { 
        if (validateInputID(ID))
            payroll.updateSalary(ID, newSalary);
    }

    public Double getSalary(int ID) { 
        if (validateInputID(ID))
            return payroll.getSalary(ID); 
        else return null;
    }

    public Double getCompensation(int ID) {
        if (validateInputID(ID))
            return payroll.getTotalCompensation(ID);
        else return null;
    }

    // Return only shallow copy cause staff is immutable
    public ArrayList<Staff> getEmployees() { return new ArrayList<Staff>(employees); }

    public Staff getEmployee(int ID) {
        for (Staff s: employees){
            if (s.getEmloyeeID() == ID)
                return s;
        }
        return null;
    }

    public ArrayList<Waiter> waiterWithMostTips() {
        ArrayList<Waiter> waiters = new ArrayList<Waiter>();
        double maxTips = 0.0;
        for (Staff s: employees) {
            if (s instanceof Waiter) {
                double tips = payroll.getTip(s.getEmloyeeID());
                if (tips > maxTips + 0.01) {  // New max, clear the list
                    maxTips = tips;
                    waiters.clear();
                    waiters.add((Waiter) s);
                } else if (Math.abs(tips - maxTips) <= 0.01) {  // Close enough to be considered equal
                    waiters.add((Waiter) s);
                }
            }
        }
        return waiters;
    }

    public ArrayList<Order> getOrders() { return new ArrayList<Order>(allOrders); }

    public void addOrder(Order o) { allOrders.add(o); }

    // Get all the salaries paid to all the existent employees
    public double getAllSalariesPaid() {
        double total = 0.0;
        for (Staff s: employees) {
            total += payroll.getAllEarnings(s.getEmloyeeID());
        }
        return total;
    }

    // Get the all time earnings for a specific employee
    public Double getEarningsForEmployee(int employeeID) {
        if (validateInputID(employeeID))
            for (Staff s: employees) {
                if (s.getEmloyeeID() == employeeID) 
                    return payroll.getAllEarnings(s.getEmloyeeID());
            }
        return null;
    }

    // Pay salaries to all the employees
    public void paySalaries() {
        for (Staff s: employees)
            payroll.payEmployee(s.getEmloyeeID());
    }

    // Register a tip that does not belong to any one waiter
    public void registerGeneralTip(double tipAmount) {
        payroll.registerGeneralTip(tipAmount);
    }

    // Get the total amount of money made from orders
    public double totalOrdersMoney() {
        double totalCash = 0;

        for (Order order : allOrders) {
            totalCash += order.getTotalPrice();
        }

        return totalCash;
    }

    // Get various datapoints from the database in string format
    public ArrayList<String> getStringData() {
        ArrayList<String> dataPoints = new ArrayList<String>();

        dataPoints.add("Total Orders Taken: " + allOrders.size() + " Orders");
        dataPoints.add("Total Money Made from Orders: $" + String.format("%.2f", totalOrdersMoney()));
        dataPoints.add("Total Money Made from Tips: $" + String.format("%.2f", payroll.totalTipsEarned()));
        dataPoints.add("Total Payroll Expense: $" + payroll.totalPayrollExpense());

        return dataPoints;
    }
}