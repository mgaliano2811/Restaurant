package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import database.Database;
import restaurant.*;
import restaurant.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Comprehensive JUnit tests for Database to achieve >90% coverage.
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

    @Test
    public void testAddEmployeeTypesAndPayrollExpense() {
        Staff waiter  = db.addEmployee("John",   "Doe",     StaffType.WAITER,  1000.0);
        Staff chef    = db.addEmployee("Jane",   "Smith",   StaffType.CHEF,    2000.0);
        Staff manager = db.addEmployee("Alice",  "Jones",   StaffType.MANAGER, 3000.0);
        Staff staff   = db.addEmployee("Bob",    "Brown",   StaffType.STAFF,   4000.0);

        List<Staff> emps = db.getEmployees();
        assertEquals(4, emps.size());
        // Unique IDs
        HashSet<Integer> ids = new HashSet<>();
        for (Staff s : emps) {
            ids.add(s.getEmloyeeID());
        }
        assertEquals(4, ids.size());
        // Types
        assertTrue(waiter  instanceof Waiter);
        assertTrue(chef    instanceof Chef);
        assertTrue(manager instanceof Manager);
        assertFalse(staff  instanceof Waiter || staff instanceof Chef || staff instanceof Manager);
        assertEquals(1000.0 + 2000.0 + 3000.0 + 4000.0, db.getTotalPayroll(), 1e-6);
    }

    @Test
    public void testValidateInputID() {
        Staff s = db.addEmployee("Test", "User", StaffType.STAFF, 1234.56);
        int validId = s.getEmloyeeID();
        assertTrue(db.validateInputID(validId));
        assertFalse(db.validateInputID(-99999));
    }

    @Test
    public void testFireEmployeeByNameSingle() {
        db.addEmployee("Tom", "Jerry", StaffType.STAFF, 1500.0);
        assertEquals(1, db.getEmployees().size());
        db.fireEmployee("Tom", "Jerry");
        assertEquals(0, db.getEmployees().size());
        assertEquals(0.0, db.getTotalPayroll(), 1e-6);
    }

    @Test
    public void testFireEmployeeById() {
        Staff s = db.addEmployee("Anna", "Bell", StaffType.STAFF, 1800.0);
        int id = s.getEmloyeeID();
        assertEquals(1, db.getEmployees().size());
        db.fireEmployee(id);
        assertTrue(db.getEmployees().isEmpty());
        assertEquals(0.0, db.getTotalPayroll(), 1e-6);
    }

    @Test
    public void testFireEmployeeByNameNoMatchDoesNothing() {
        Staff s = db.addEmployee("U", "N", StaffType.STAFF, 500.0);
        double before = db.getTotalPayroll();
        db.fireEmployee("Non", "Existent");
        assertEquals(1, db.getEmployees().size());
        assertEquals(before, db.getTotalPayroll(), 1e-6);
    }

    @Test
    public void testFireEmployeeMultipleMatchesDoesNothing() {
        db.addEmployee("Sam", "Same", StaffType.STAFF, 100.0);
        db.addEmployee("Sam", "Same", StaffType.STAFF, 200.0);
        assertEquals(2, db.getEmployees().size());
        db.fireEmployee("Sam", "Same");  // two matches → no action
        assertEquals(2, db.getEmployees().size());
        assertEquals(300.0, db.getTotalPayroll(), 1e-6);
    }

    @Test
    public void testGetEmployeeLookup() {
        Staff added = db.addEmployee("Foo", "Bar", StaffType.STAFF, 750.0);
        int id = added.getEmloyeeID();
        assertEquals(added, db.getEmployee(id));
        assertNull(db.getEmployee(-1));
    }

    @Test
    public void testGetEmployeesShallowCopy() {
        db.addEmployee("X", "Y", StaffType.STAFF, 600.0);
        List<Staff> copy = db.getEmployees();
        copy.clear();
        // original list unaffected
        assertEquals(1, db.getEmployees().size());
    }

    @Test
    public void testRecordTipGetTipAndCompensation_WaiterVsManager() {
        // Waiter should get tip
        Waiter w = (Waiter) db.addEmployee("Will", "Wait", StaffType.WAITER,  800.0);
        int wid = w.getEmloyeeID();
        db.recordTip(wid, 50.0);
        assertEquals(50.0, db.getTip(wid),      1e-6);
        assertEquals(850.0, db.getCompensation(wid), 1e-6);

        // Manager should never get tip credit
        Manager m = (Manager) db.addEmployee("Mighty", "Man", StaffType.MANAGER, 900.0);
        int mid = m.getEmloyeeID();
        db.recordTip(mid, 75.0);
        assertEquals(0.0, db.getTip(mid),        1e-6);
        assertEquals(900.0, db.getCompensation(mid), 1e-6);
    }

    @Test
    public void testUpdateSalaryAndGetSalary() {
        Staff s = db.addEmployee("Sal", "Ary", StaffType.STAFF, 1200.0);
        int id = s.getEmloyeeID();
        db.updateSalary(id, 1500.25);
        assertEquals(1500.25, db.getSalary(id), 1e-6);

        // invalid ID: no exception, salary remains unchanged
        db.updateSalary(-5, 2000.0);
        assertNull(db.getSalary(-5));
    }

    @Test
    public void testPaySalariesAndGetEarnings() {
        Staff s1 = db.addEmployee("One", "Two", StaffType.STAFF, 1000.0);
        Staff s2 = db.addEmployee("Three", "Four", StaffType.STAFF, 2000.0);
        int id1 = s1.getEmloyeeID(), id2 = s2.getEmloyeeID();

        // before paying, earnings are zero
        assertNull(db.getEarningsForEmployee(-1));
        assertEquals(0.0, db.getAllSalariesPaid(), 1e-6);

        db.paySalaries();
        assertEquals(1000.0, db.getEarningsForEmployee(id1), 1e-6);
        assertEquals(2000.0, db.getEarningsForEmployee(id2), 1e-6);
        assertEquals(3000.0, db.getAllSalariesPaid(), 1e-6);
    }

    @Test
    public void testStringDataInitialAndAfterTipsAndOrders() {
        // Initial (no orders, no tips, no payroll)
        ArrayList<String> data0 = db.getStringData();
        assertEquals("Total Orders Taken: 0 Orders", data0.get(0));
        assertEquals("Total Money Made from Orders: $0,00", data0.get(1));
        assertEquals("Total Money Made from Tips: $0,00", data0.get(2));
        assertEquals("Total Payroll Expense: $0.0", data0.get(3));

        // Record an individual tip and a general tip
        Waiter w = (Waiter) db.addEmployee("Tip", "Master", StaffType.WAITER, 1000.0);
        db.recordTip(w.getEmloyeeID(), 20.0);
        db.registerGeneralTip(30.0);

        ArrayList<String> data1 = db.getStringData();
        assertEquals("Total Money Made from Tips: $50,00",   data1.get(2));  // 20 + 30
        assertEquals("Total Payroll Expense: $1000.0",      data1.get(3));
    }

    @Test
    public void testWaiterWithMostTipsTolerance() {
        // Set up three waiters
        Waiter a = (Waiter) db.addEmployee("A", "A", StaffType.WAITER, 500.0);
        Waiter b = (Waiter) db.addEmployee("B", "B", StaffType.WAITER, 500.0);
        Waiter c = (Waiter) db.addEmployee("C", "C", StaffType.WAITER, 500.0);
        // give tips: a=10, b=15, c=15.009 (within tolerance)
        db.recordTip(a.getEmloyeeID(), 10.0);
        db.recordTip(b.getEmloyeeID(), 15.0);
        db.recordTip(c.getEmloyeeID(), 15.009);

        List<Waiter> top = db.waiterWithMostTips();
        assertEquals(2, top.size());
        assertTrue(top.contains(b));
        assertTrue(top.contains(c));
    }

    @Test
    public void testInvalidOperationsDoNotThrow() {
        // recordTip, updateSalary, fireEmployee, etc., with invalid IDs
        db.recordTip(-10, 5.0);
        db.updateSalary(-20, 5.0);
        db.fireEmployee(-30);
        db.fireEmployee("No", "One");
        assertNull(db.getTip(-40));
        assertNull(db.getSalary(-50));
        assertNull(db.getCompensation(-60));
        assertNull(db.getPayrollInfo(-70));
    }
}