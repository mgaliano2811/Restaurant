package tests;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurant.Restaurant;
import restaurant.StaffType;

public class RestaurantTests {

    private Restaurant r;

    @BeforeEach
    public void setUp() { 
        r = new Restaurant(); 
        r.addEmployee("Gus", "Fring", StaffType.WAITER, 30000);
    }

    @AfterEach
    public void tearDown() { r = null; }

    @Test
    public void testRestrauntConstruction() {
        assertEquals("Whatever Name Restaurant", r.getRestaurantName());
        assertEquals(0, r.getCurrRestaurantCapacity());
        assertEquals(200, r.getMaxRestaurantCapacity());
    }

    @Test
    public void testRestrauntDefaultTables() {
        assertEquals(0, r.allTables().size());
    }

    @Test
    public void testRestrauntAddTable() {
        r.addTable(4);
        assertEquals(1, r.allTables().size());
        assertEquals(4, r.allTables().get(0).getMaxCapacity());
    }

    @Test
    public void testRestrauntAddMultipleTables() {
        r.addTable(4);
        r.addTable(2);
        assertEquals(2, r.allTables().size());
        assertEquals(2, r.allTables().get(0).getMaxCapacity());
        assertEquals(4, r.allTables().get(1).getMaxCapacity());
    }

    @Test
    public void testRestrauntRemoveTable() {
        r.addTable(4);
        r.removeTable(r.allTables().get(0).getTableNumber());
        assertEquals(0, r.allTables().size());
    }

    @Test
    public void testRestrauntRemoveTableOccupied() {
        r.addTable(4);
        r.seatPeople(3);
        r.removeTable(r.allTables().get(0).getTableNumber());
        assertEquals(1, r.allTables().size());
    }

    @Test
    public void testRestrauntAddTableWithSameCapacity() {
        r.addTable(4);
        r.addTable(4);
        assertEquals(2, r.allTables().size());
        assertEquals(4, r.allTables().get(0).getMaxCapacity());
        assertEquals(4, r.allTables().get(1).getMaxCapacity());
    }

    @Test
    public void testRestrauntSeatPeople() {
        r.addTable(4);
        r.seatPeople(3);
        assertEquals(3, r.getCurrRestaurantCapacity());
    }

    @Test
    public void testRestrauntSeatPeopleError() {
        r.addTable(2);
        r.seatPeople(3);
        assertEquals(0, r.getCurrRestaurantCapacity());
    }
    @Test
    public void testRestrauntSeatPeopleMutliTable() {
        r.addTable(3);
        r.addTable(3);
        r.seatPeople(5);
        assertEquals(0, r.getCurrRestaurantCapacity());
    }

    @Test
    public void testRestrauntSeatPeopleMutliTableError() {
        r.addTable(3);
        r.addTable(3);
        r.seatPeople(7);
        assertEquals(0, r.getCurrRestaurantCapacity());
    }

    @Test
    public void testRestrauntFreeTable() {
        r.addTable(4);
        r.seatPeople(3);
        int tableNumber = r.allTables().get(0).getTableNumber();
        assertEquals(true, r.tableOccupied(tableNumber));
        r.freeTable(tableNumber);
        assertEquals(false, r.tableOccupied(tableNumber));
    }

    @Test
    public void testRestrauntFreeTableIsFree() {
        r.addTable(4);
        int tableNumber = r.allTables().get(0).getTableNumber();
        assertEquals(false, r.tableOccupied(tableNumber));
        r.freeTable(tableNumber);
        assertEquals(false, r.tableOccupied(tableNumber));
    }

    @Test
    public void testRestrauntFreeTableInvalidTableNum() {
        r.addTable(4);
        int tableNumber = r.allTables().get(0).getTableNumber();
        assertEquals(false, r.tableOccupied(tableNumber));
        r.freeTable(tableNumber + 1);
        assertEquals(false, r.tableOccupied(tableNumber));
    }

    @Test
    public void testRegisterGeneralTip() {
        r.registerGeneralTip(50.5);
        ArrayList<String> info = r.getDatabaseInfoInString();
        assertEquals("Total Money Made from Tips: $50,50", info.get(2));
    }

    @Test
    public void testOrder() {
        List<String> before = r.getDatabaseInfoInString();
        assertEquals("Total Orders Taken: 0 Orders", before.get(0));
    }
}