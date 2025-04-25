package database;

import java.util.HashMap;
import restaurant.*;

/**
 * Basic payroll class that has all employee payroll related expenses and information.
 *
 * Could also include tax deductions, benefits, overtime rules, and integration
 * with a persistent database (things if we have time)
 */

 public class Payroll {

    // Map employee ID to payroll record
    private HashMap<Integer, PayrollRecord> payrollRecords;
    // Total unspecified tips that the restaurant has recieved
    private double totalUnspecifiedTipAmount;

    public Payroll() {
        payrollRecords = new HashMap<>();   // Initialize hashmap
        totalUnspecifiedTipAmount = 0;
    }

    // Register a payment to an employee
    public void payEmployee(int employeeID) {
        PayrollRecord p = payrollRecords.get(employeeID);
        p.registerPayment();
    }

    // Get all time earnings of a certain employee
    public double getAllEarnings(int employeeID) {
        PayrollRecord p = payrollRecords.get(employeeID);
        return p.getAllTimeEarnings();
    }
    
    // Adds an employee to the payroll with specified base salary
    public void addEmployee(Staff employee, double salary) {
        PayrollRecord record = new PayrollRecord(employee, salary);
        payrollRecords.put(employee.getEmloyeeID(), record);
    }
    
    // Updates the salary for given employee
    public void updateSalary(int employeeID, double newSalary) {
        PayrollRecord record = payrollRecords.get(employeeID);
        record.setSalary(newSalary);
    }

    public double getSalary(int ID) {
        PayrollRecord record = payrollRecords.get(ID);
        return record.getSalary();

    }
    // Records a tip for employee. We check if its waiter inside addTip()
    public void recordTip(int employeeID, double tipAmount) {
        PayrollRecord record = payrollRecords.get(employeeID);
        record.addTip(tipAmount);
    }

    // Records a tip that does not belong to any single employee, and is instead tallied up seperately
    public void registerGeneralTip(double tipAmount) {
        totalUnspecifiedTipAmount += tipAmount;
    }

    public double getTip(int ID) { return payrollRecords.get(ID).getTips(); }

    public double getTotalCompensation(int ID) { 
        return payrollRecords.get(ID).getTotalCompensation(); 
    }

    public String getPayrollRecord(int ID) { return payrollRecords.get(ID).toString(); }
    
    // Calculates the total amount paid to all the employees during this run
    public double totalPayrollExpense() {
        double total = 0;
        for (PayrollRecord record : payrollRecords.values()) {
            total += record.getAllTimeEarnings();
        }
        return total;
    }

    // Calculates the total amount of TIPS paid to all employees, including the unspecified tips given
    public double totalTipsEarned() {
        double totalTips = totalUnspecifiedTipAmount;
        for (PayrollRecord record : payrollRecords.values()) {
            totalTips += record.getTips();
        }

        return totalTips;
    }
    
    /**
     * Internal class:
     *  PayrollRecord class has payroll information for one employee. We get here
     *  We get here by mapping from employeeID
     */
    public class PayrollRecord {
        private Staff employee;     // The staff type
        private double allTimeEarnings;
        private double salary;
        private double tips;        // Will be 0 if employee is not a waiter
        
        public PayrollRecord(Staff employee, double salary) {
            this.employee = employee;
            this.salary = salary;
            this.tips = 0.0;
            this.allTimeEarnings = 0.0;
        }
                
        public double getSalary() { return salary; }

        public void registerPayment() { allTimeEarnings += salary; }

        public double getAllTimeEarnings() { return this.allTimeEarnings; }
        
        public void setSalary(double salary) { this.salary = salary; }
        
        public double getTips() { return tips; }
        
        public void addTip(double tipAmount) {
            if (this.employee instanceof Waiter)
                this.tips += tipAmount;
            else System.err.println("Can only add tip to waiters");
        }
        
        // Get the total compensation (base salary plus tips)
        public double getTotalCompensation() { return salary + tips; }
        
        @Override
        public String toString() {
            return "PayrollRecord [Employee: " + employee.toString() + 
                ", Salary: " + salary + ", Tips: " + tips + "]";
        }
    }

    // Remove employee (case we fire someone)
    public void removeEmployee(Integer ID) { payrollRecords.remove(ID); }
}