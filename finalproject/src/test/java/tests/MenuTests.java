package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurant.menu.ItemType;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;

import java.util.HashMap;

public class MenuTests {
    private Menu menu;

    @BeforeEach
    public void setUp() {
        menu = new Menu("./src/test/java/tests/TestMenu.txt");
    }
    
    @Test
    public void TestMenuLoading() {
    	menu = new Menu("./src/test/java/tests/TestMenuLoading.txt");
    	assertEquals(1, menu.getMenu().size());
    }

    @Test
    public void testMenuItemConstruction() {
        MenuItem item = new MenuItem("Test Item", "entree", 10.99, "A test item description.");
        assertEquals("Test Item", item.getName());
        assertEquals(ItemType.ENTREE, item.getItemType());
        assertEquals(10.99, item.getPrice(), 0.01);
        assertEquals("A test item description.", item.getDescription());
    }

    @Test
    public void testInvalidMenuItemConstruction() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MenuItem("Test Item", "notAType", 10.99, "A test item description.");
        });
    }

    @Test
    public void testAddMenuItem() {
        menu.addMenuItem("New Item", "drink", 5.99, "A new drink item.");
        assertTrue(menu.getMenu().containsKey("New Item"));
        assertEquals(5.99, menu.getMenu().get("New Item").getPrice(), 0.01);
    }

    @Test
    public void testAddDuplicateMenuItem() {
        menu.addMenuItem("Duplicate Item", "entree", 7.99, "A duplicate item.");
        menu.addMenuItem("Duplicate Item", "entree", 7.99, "A duplicate item.");
        assertEquals(1, menu.getMenu().size()); // Should not add the duplicate
    }

    @Test
    public void testRemoveMenuItem() {
        menu.addMenuItem("Item to Remove", "dessert", 3.99, "A dessert item.");
        menu.removeMenuItem("Item to Remove");
        assertFalse(menu.getMenu().containsKey("Item to Remove"));
    }
    
    @Test
    public void testRemoveNonExistentMenuItem() {
        menu.removeMenuItem("Non Existent Item");
        assertFalse(menu.getMenu().containsKey("Non Existent Item"));
    }

    @Test
    public void testUpdateMenuItem() {
        menu.addMenuItem("Item to Update", "entree", 8.99, "An entree item.");
        menu.updateMenuItem("Item to Update", "drink", 9.99, "Updated drink item.");
        assertEquals(9.99, menu.getMenu().get("Item to Update").getPrice(), 0.01);
        assertEquals(ItemType.DRINK, menu.getMenu().get("Item to Update").getItemType());
    }

    @Test
    public void testUpdateNonExistentMenuItem() {
        menu.updateMenuItem("Non Existent Item", "entree", 7.99, "This item does not exist.");
        assertFalse(menu.getMenu().containsKey("Non Existent Item"));
    }

    @Test
    public void testGetMenuByType() {
        menu.addMenuItem("Drink Item", "drink", 2.99, "A drink item.");
        menu.addMenuItem("Dessert Item", "dessert", 4.99, "A dessert item.");
        HashMap<String, MenuItem> drinks = menu.getMenuByType(ItemType.DRINK);
        assertTrue(drinks.containsKey("Drink Item"));
        assertFalse(drinks.containsKey("Dessert Item"));
    }

    @Test
    public void testGetMenu() {
        menu.addMenuItem("Test Item 1", "entree", 10.99, "Description 1");
        menu.addMenuItem("Test Item 2", "drink", 5.99, "Description 2");
        HashMap<String, MenuItem> allItems = menu.getMenu();
        assertTrue(allItems.containsKey("Test Item 1"));
        assertTrue(allItems.containsKey("Test Item 2"));
        assertEquals(2, menu.getMenu().size());
    }

    @Test
    public void testMenuItemToString() {
        MenuItem item = new MenuItem("Test Item", "entree", 10.99, "A test item description.");
        assertEquals("Test Item - $10.99: A test item description.", item.toString());
    }

    @Test 
    public void testMenuToString() {
        menu.addMenuItem("Test Item 1", "entree", 10.99, "Description 1");
        menu.addMenuItem("Test Item 2", "drink", 5.99, "Description 2");
        String expected = "Type: ENTREE\n" +
                          "\tTest Item 1 - $10.99: Description 1\n" +
                          "Type: DRINK\n" +
                          "\tTest Item 2 - $5.99: Description 2\n";
        assertEquals(expected, menu.toString());
    }
}
