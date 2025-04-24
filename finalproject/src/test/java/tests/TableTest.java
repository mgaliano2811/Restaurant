package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import restaurant.Table;

public class TableTest {
    
    // Test the Table class
    // Test the constructor and getters
    @Test
    public void testTableConstructor() {
        Table table = new Table(4, 1);
        assertEquals(1, table.getMaxCapacity());
        assertEquals(4, table.getTableNumber());
    }

    // Test the addCustomer method
    @Test
    public void testTableCopy() {
        Table table1 = new Table(4, 1);
        Table table2 = new Table(table1);
        assertEquals(table1.getMaxCapacity(), table2.getMaxCapacity());
        assertEquals(table1.getTableNumber(), table2.getTableNumber());
    }

    @Test
    public void testTableToString() {
        Table table = new Table(4, 1);
        String expected = "TableID:4,Capacity:1";
        assertEquals(expected, table.toString());
    }

    @Test 
    void testTableHashCode() {
        Table table = new Table(4, 1);
        assertEquals(4, table.hashCode());
    }
    
    @Test
    public void testTableEqualSameVar() {
        Table table1 = new Table(4, 1);
        assertEquals(true, table1.equals(table1));
    }

    @Test
    public void testTableEqualDiffVar() {
        Table table1 = new Table(4, 1);
        Table table2 = new Table(4, 1);
        assertEquals(true, table1.equals(table2));
    }

    @Test
    public void testTableEqualDiffType() {
        Table table1 = new Table(4, 1);
        int table2 = 4;
        assertEquals(false, table1.equals(table2));
    }

}
