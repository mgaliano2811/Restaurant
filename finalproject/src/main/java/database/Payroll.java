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

    public Payroll() {
        payrollRecords = new HashMap<>();   // Initialize hashmap
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

    public double getTip(int ID) { return payrollRecords.get(ID).getTips(); }

    public double getTotalCompensation(int ID) { 
        return payrollRecords.get(ID).getTotalCompensation(); 
    }

    public String getPayrollRecord(int ID) { return payrollRecords.get(ID).toString(); }
    
    // Calculates the total fixed payroll expense adding base salaries for employees
    public double totalPayrollExpense() {
        double total = 0;
        for (PayrollRecord record : payrollRecords.values()) {
            total += record.getSalary();
        }
        return total;
    }
    
    /**
     * Internal class:
     *  PayrollRecord class has payroll information for one employee. We get here
     *  We get here by mapping from employeeID
     */
    public class PayrollRecord {
        private Staff employee;     // The staff type
        private double salary;
        private double tips;        // Will be 0 if employee is not a waiter
        
        public PayrollRecord(Staff employee, double salary) {
            this.employee = employee;
            this.salary = salary;
            this.tips = 0.0;
        }
                
        public double getSalary() { return salary; }
        
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