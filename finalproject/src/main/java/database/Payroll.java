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

    // Returns payroll record for the given employee ID.
    public PayrollRecord getPayrollRecord(int employeeID) {
        return payrollRecords.get(employeeID);
    }
    
    // Updates the salary for given employee
    public void updateSalary(int employeeID, double newSalary) {
        PayrollRecord record = payrollRecords.get(employeeID);
        if (record != null) {
            record.setSalary(newSalary);
        }
    }
    
    // Records a tip for employee. We check if its waiter inside addTip()
    public void recordTip(int employeeID, double tipAmount) {
        PayrollRecord record = payrollRecords.get(employeeID);
        if (record != null) {
            record.addTip(tipAmount);
        }
    }
    
    // Calculates the total fixed payroll expense adding base salaries for employees
    public double totalPayrollExpense() {
        double total = 0;
        for (PayrollRecord record : payrollRecords.values()) {
            total += record.getSalary();
        }
        return total;
    }
    
    // Returns all payroll records.
    public HashMap<Integer, PayrollRecord> getPayrollRecords() {
        return payrollRecords;
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
        
        public Staff getEmployee() {
            return employee;
        }
        
        public double getSalary() {
            return salary;
        }
        
        public void setSalary(double salary) {
            this.salary = salary;
        }
        
        public double getTips() {
            return tips;
        }
        
        public void addTip(double tipAmount) {
            if (this.employee instanceof Waiter)
                this.tips += tipAmount;
            else System.err.println("Can only add tip to waiters");
        }
        
        // Get the total compensation (base salary plus tips)
        public double getTotalCompensation() {
            return salary + tips;
        }
        
        @Override
        public String toString() {
            return "PayrollRecord [Employee=" + employee.toString() + 
                   ", Salary=" + salary + ", Tips=" + tips + "]";
        }
    }
}