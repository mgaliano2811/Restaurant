package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import database.Database;
import restaurant.Staff;
import restaurant.StaffType;
import restaurant.Waiter;

/**
 * Testcases for Database and Payroll
 */
public class DatabaseTests {

    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
    }

    @AfterEach
    public void tearDown() {
        db = null;
    }

    // Test adding a Waiter, recording a tip, and getting full compensation
    @Test
    public void testAddEmployeeWaiter() throws Exception {
        db.addEmployee("Jhonny", "Longcock", StaffType.WAITER, 30000);
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(1, employees.size());
        Staff s = employees.get(0);
        assertEquals("Jhonny",s.getFirstName());
        assertEquals("Longcock",s.getLastName());
        assertEquals(StaffType.WAITER, s.getEmployeeType());
        int ID = s.getEmloyeeID();
        db.recordTip(ID, 30);
        assertEquals(30, db.getTip(ID));
        assertEquals(db.getCompensation(ID), 30030);
    }

    // Test adding a Chef & updating its salary
    @Test
    void testAddEmployeeChef() {
        db.addEmployee("Anna", "Smith", StaffType.CHEF, 35000);
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(1, employees.size());
        Staff s = employees.get(0);
        assertEquals("Anna", s.getFirstName());
        assertEquals("Smith", s.getLastName());
        assertEquals(StaffType.CHEF, s.getEmployeeType());

        db.updateSalary(s.getEmloyeeID(), 40000.5);
        assertEquals(40000.5, db.getSalary(s.getEmloyeeID()), 0.001);
    }

    // Test adding a manager & test recording tip
    @Test
    void testAddEmployeeManager() {
        db.addEmployee("Bob", "Brown", StaffType.MANAGER, 50000);
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(1, employees.size());
        Staff s = employees.get(0);
        assertEquals("Bob", s.getFirstName());
        assertEquals("Brown", s.getLastName());
        assertEquals(StaffType.MANAGER, s.getEmployeeType());
        db.recordTip(s.getEmloyeeID(), 40.5);
        assertEquals(0.0, db.getTip(s.getEmloyeeID()), 0.001);
    }

    // Test adding staff
    @Test
    void testAddEmployeeStaff() {
        db.addEmployee("Alice", "Green", StaffType.STAFF, 25000);
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(1, employees.size());
        Staff s = employees.get(0);
        assertEquals("Alice", s.getFirstName());
        assertEquals("Green", s.getLastName());
        assertEquals(StaffType.STAFF, s.getEmployeeType());
    }

    // Test hiring multiple employees (should all have different IDs)
    // and also get the payroll expense
    @Test
    void testHiringMultipleEmployeesGeneratesUniqueIDs() {
        double totalPayroll = 0.0;
        for (int i = 0; i < 10; i++){
            db.addEmployee("F" + i, "L" + i, StaffType.STAFF, 20000 + i);
            totalPayroll += (20000 + i);
        }
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(10, employees.size());

        // Now verify no two employees have same ID
        for (int i = 0; i < employees.size(); i++) {
            for (int j = i + 1; j < employees.size(); j++) {
                int id1 = employees.get(i).getEmloyeeID();
                int id2 = employees.get(j).getEmloyeeID();
                assertNotEquals(id1, id2);
            }
        }

        assertEquals(totalPayroll, db.getTotalPayroll(), 0.001);
    }

    // Test ID handling and validation
    @Test
    public void testValidateInputID() {
        db.addEmployee("Test", "User", StaffType.STAFF, 12345);
        int validId = db.getEmployees().get(0).getEmloyeeID();
        assertTrue(db.validateInputID(validId));        // Existing ID
        assertFalse(db.validateInputID(99999999));   // Nonexisting ID
    }

    // Test firing by name, lastname & by ID
    @Test
    public void testFireEmployeeByName() {
        db.addEmployee("Tom", "Jerry", StaffType.STAFF, 20000);
        assertEquals(1, db.getEmployees().size());

        db.fireEmployee("Tom", "Jerry");
        assertEquals(0, db.getEmployees().size());
        assertEquals(0.0, db.getTotalPayroll(), 0.001);

        db.addEmployee("Pepe", "Perez", StaffType.STAFF, 1000000);
        ArrayList<Staff> employees = db.getEmployees();
        assertEquals(1, employees.size());

        db.fireEmployee(employees.get(0).getEmloyeeID());
        assertEquals(0, db.getEmployees().size());
        assertEquals(0.0, db.getTotalPayroll(), 0.001);

    }

    // Test payroll record toString
    @Test
    public void testPayrollRecordToString() {
        db.addEmployee("Last", "First", StaffType.STAFF, 20000);
        ArrayList<Staff> employees = db.getEmployees();
        Staff s = employees.get(0);
        int ID = s.getEmloyeeID();
        assertEquals("PayrollRecord [Employee: " + s.toString() + ", Salary: 20000.0, Tips: 0.0]"
                , db.getPayrollInfo(ID));
    }

    // Test invalid IDs 
    @Test
    public void testInvalidIDs() {
        db.recordTip(-2, 40);
        db.updateSalary(-2, 40);
        assertNull(db.getTip(-30));
        assertNull(db.getCompensation(-30));
        assertNull(db.getSalary(-30));
        assertNull(db.getPayrollInfo(-30));
        db.fireEmployee("Pepe", "Perez");
        assertNull(db.getEmployee(-40));
        db.fireEmployee(-50);
    }

    // Test get waiter with the most tips
    @Test
    public void testWaiterWithMostTips() {
        db.addEmployee("Pepe", "Perez", StaffType.WAITER, 30000);
        db.addEmployee("Ana", "Garcia", StaffType.WAITER, 30000);
        db.addEmployee("Luis", "Torres", StaffType.WAITER, 30000);
        db.addEmployee("Gabriela", "Vargas", StaffType.MANAGER, 5000);

        ArrayList<Staff> employees = db.getEmployees();
        int pepeID = employees.get(0).getEmloyeeID();
        int anaID = employees.get(1).getEmloyeeID();
        int luisID = employees.get(2).getEmloyeeID();

        db.recordTip(pepeID, 50.0);
        db.recordTip(anaID, 75.0);   // highest
        db.recordTip(luisID, 75.00); // equal due to tolerance

        ArrayList<Waiter> topWaiters = db.waiterWithMostTips();

        assertEquals(2, topWaiters.size());

        assertTrue(topWaiters.contains(db.getEmployee(anaID)));
        assertTrue(topWaiters.contains(db.getEmployee(luisID)));
    }
}