package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurant.*;

public class RestaurantTests {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream originalErr;

    @BeforeEach
    public void setUpStreams() {
        originalErr = System.err;
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
    }

    /**
     * Helper method to set the private "tables" field of Restaurant.
     */
    private void setRestaurantTables(Restaurant restaurant, ArrayList<Table> tables) throws Exception {
        Field f = Restaurant.class.getDeclaredField("tables");
        f.setAccessible(true);
        f.set(restaurant, tables);
    }

    /**
     * Helper method to set the private "tablesByCapacity" field of Restaurant.
     */
    private void setRestaurantTablesByCapacity(Restaurant restaurant, HashMap<Integer, ArrayList<Table>> map)
            throws Exception {
        Field f = Restaurant.class.getDeclaredField("tablesByCapacity");
        f.setAccessible(true);
        f.set(restaurant, map);
    }

    /**
     * Helper method to retrieve the private "tables" field from Restaurant.
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Table> getRestaurantTables(Restaurant restaurant) throws Exception {
        Field f = Restaurant.class.getDeclaredField("tables");
        f.setAccessible(true);
        return (ArrayList<Table>) f.get(restaurant);
    }

    @Test
    public void testInitialState() {
        Restaurant r = new Restaurant();
        assertEquals("Whatever Name Restaurant", r.getRestaurantName());
        assertEquals(0, r.getMaxRestaurantCapacity());
        assertEquals(0, r.getCurrRestaurantCapacity());
        System.out.println("Executed");
    }

    @Test
    public void testSeatPeopleInsufficientCapacity() throws Exception {
        Restaurant r = new Restaurant();

        // Create one table with capacity 4.
        Table table = new Table(1, 4);
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(table);
        setRestaurantTables(r, tables);

        // Build tablesByCapacity map with key 4.
        HashMap<Integer, ArrayList<Table>> map = new HashMap<>();
        ArrayList<Table> listFor4 = new ArrayList<>();
        listFor4.add(table);
        map.put(4, listFor4);
        setRestaurantTablesByCapacity(r, map);

        // Attempt to seat 5 people (only 4 free seats available).
        r.seatPeople(5);

        // As seating is not possible, the current restaurant capacity should remain 0.
        assertEquals(0, r.getCurrRestaurantCapacity());
        assertTrue(errContent.toString().contains("There is not enough capasity to sit 5 people."));
        System.out.println("Executed");
    }

    @Test
    public void testOverloadedConstructorTable() {
        Table table = new Table(1, 4);
        Table testTable = new Table(table);
        assertEquals(table.getMaxCapacity(), testTable.getMaxCapacity());
        assertEquals(table.getTableNumber(), testTable.getTableNumber());
    }

    @Test
    public void testSeatPeopleSufficientCapacity() throws Exception {
        Restaurant r = new Restaurant();

        // Create one table with capacity 4.
        Table table = new Table(1, 4);
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(table);
        setRestaurantTables(r, tables);

        // Build a map for keys 1 through 4 (only key 4 contains the table).
        HashMap<Integer, ArrayList<Table>> map = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            ArrayList<Table> list = new ArrayList<>();
            if (i == 4) {
                list.add(table);
            }
            map.put(i, list);
        }
        setRestaurantTablesByCapacity(r, map);

        // Seat 4 people.
        r.seatPeople(4);

        // The current capacity should now be updated to 4.
        assertEquals(4, r.getCurrRestaurantCapacity());
    }

    @Test
    public void testFreeTableValid() throws Exception {
        Restaurant r = new Restaurant();

        // Create two tables: table1 (ID 1) and table2 (ID 2).
        Table table1 = new Table(1, 4);
        Table table2 = new Table(2, 6);
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(table1);
        tables.add(table2);
        setRestaurantTables(r, tables);

        // Build tablesByCapacity map for the two tables.
        HashMap<Integer, ArrayList<Table>> map = new HashMap<>();
        ArrayList<Table> listFor4 = new ArrayList<>();
        listFor4.add(table1);
        map.put(4, listFor4);
        ArrayList<Table> listFor6 = new ArrayList<>();
        listFor6.add(table2);
        map.put(6, listFor6);
        setRestaurantTablesByCapacity(r, map);

        // Call freeTable with table ID 1.
        // Since freeTable sorts tables in descending order by their IDs,
        // table2 (ID 2) becomes the first in the list and is removed.
        r.freeTable(1);

        ArrayList<Table> remaining = getRestaurantTables(r);
        assertEquals(1, remaining.size());
        // The remaining table should be table1 (ID 1).
        assertEquals(1, remaining.get(0).getTableNumber());
    }

    @Test
    public void testFreeTableInvalid() throws Exception {
        Restaurant r = new Restaurant();

        // Create one table.
        Table table = new Table(1, 4);
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(table);
        setRestaurantTables(r, tables);

        // Build tablesByCapacity map.
        HashMap<Integer, ArrayList<Table>> map = new HashMap<>();
        ArrayList<Table> listFor4 = new ArrayList<>();
        listFor4.add(table);
        map.put(4, listFor4);
        setRestaurantTablesByCapacity(r, map);

        // Call freeTable with an invalid ID (0).
        r.freeTable(0);
        assertTrue(errContent.toString().contains("There is no table by that ID"));

        // Reset error output.
        errContent.reset();

        // Call freeTable with an invalid ID (2) when only one table exists.
        r.freeTable(2);
        assertTrue(errContent.toString().contains("There is no table by that ID"));

        // Verify that the table list size remains unchanged.
        ArrayList<Table> remaining = getRestaurantTables(r);
        assertEquals(1, remaining.size());
    }

    @Test
    public void testSeatPeopleRecursionException() throws Exception {
        Restaurant r = new Restaurant();

        // Create two tables with capacity 4 each.
        Table table1 = new Table(1, 4);
        Table table2 = new Table(2, 4);
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(table1);
        tables.add(table2);
        setRestaurantTables(r, tables);

        // Build tablesByCapacity map with only the key 4.
        HashMap<Integer, ArrayList<Table>> map = new HashMap<>();
        ArrayList<Table> listFor4 = new ArrayList<>();
        listFor4.add(table1);
        listFor4.add(table2);
        map.put(4, listFor4);
        // Intentionally, do not add further keys to force a null return during recursion.
        setRestaurantTablesByCapacity(r, map);

        // Since the key for count (which becomes > 4) doesn't exist,
        // a NullPointerException is expected during the seating process.
        assertThrows(NullPointerException.class, () -> {
            r.seatPeople(5);
        });
    }

    @Test
    public void testTableMethods() {
        Table table = new Table(1, 4);
        assertTrue(table.isFree());
        assertEquals(4, table.getMaxCapacity());
        assertEquals(1, table.getTableNumber());
        assertEquals(0, table.getCurrCapacity());

        // Calling seatPeople with exactly the max capacity should leave the table unchanged.
        table.seatPeople(4);
        assertEquals(0, table.getCurrCapacity());
        assertTrue(table.isFree());

        // Calling seatPeople with a value exceeding maxCapacity should update currCapacity.
        table.seatPeople(5);
        assertEquals(5, table.getCurrCapacity());
        assertFalse(table.isFree());

        // Free the table.
        table.freeTable();
        assertEquals(0, table.getCurrCapacity());
        assertTrue(table.isFree());
    }
}