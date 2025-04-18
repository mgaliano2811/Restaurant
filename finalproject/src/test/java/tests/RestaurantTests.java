package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import restaurant.Restaurant;
import restaurant.Table;

public class RestaurantTests {

    private Restaurant r;

    @BeforeEach
    public void setUp() { r = new Restaurant(); }

    @AfterEach
    public void tearDown() { r = null; }

    // TODO: Will do this when we have a more stable project

    // This is just to test overloaded constructor
    public class TableTests {
        private Table t;

        @BeforeEach
        void setUp() {
            t = new Table(1, 4);
        }

        @AfterEach
        void tearDown() {
            t = null;
        }

        @Test
        void testConstructorAndGetters() {
            assertEquals(1, t.getTableNumber());
            assertEquals(4, t.getMaxCapacity());
            Table t2 = new Table (t);
            assertEquals(t.getMaxCapacity(), t2.getMaxCapacity());
            assertEquals(t.getTableNumber(), t2.getTableNumber());
        }
    }
}