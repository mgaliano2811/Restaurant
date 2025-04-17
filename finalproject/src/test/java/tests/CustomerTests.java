package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import restaurant.menu.ItemType;

public class CustomerTests {
    
    @Test
    public void testCustomerCreation() {
        restaurant.Customer customer = new restaurant.Customer();
        assertNotNull(customer.getId());
        assertEquals(3, customer.getFoodWant().size());
        assertEquals(ItemType.ENTREE, customer.getFoodWant().get(0).getItemType());
        assertEquals(ItemType.DRINK, customer.getFoodWant().get(1).getItemType());
        assertEquals(ItemType.DESSERT, customer.getFoodWant().get(2).getItemType());
    }

    @Test
    public void getCustomerOrderPrice() {
        restaurant.Customer customer = new restaurant.Customer();
        double totalPrice = customer.getTotalPrice();
        assertTrue(totalPrice > 0.0);
    }

    @Test
    public void testCustomerGroupCreation() {
        restaurant.CustomerGroup group = new restaurant.CustomerGroup();
        assertNotNull(group);
    }

    @Test
    public void testAddCustomerToGroup() {
        restaurant.CustomerGroup group = new restaurant.CustomerGroup();
        restaurant.Customer customer = new restaurant.Customer();
        group.addCustomer(customer);
        assertEquals(1, group.numCustomers());
        assertEquals(customer.getId(), group.iterator().next().getId());
    }

    @Test
    public void testAddManyCustomersToGroup() {
        restaurant.CustomerGroup group = new restaurant.CustomerGroup();
        restaurant.Customer customer1 = new restaurant.Customer();
        restaurant.Customer customer2 = new restaurant.Customer();
        restaurant.Customer customer3 = new restaurant.Customer();
        group.addCustomer(customer1);
        group.addCustomer(customer2);
        group.addCustomer(customer3);
        assertEquals(3, group.numCustomers());
        Iterator<restaurant.Customer> iterator = group.iterator();
        assertEquals(customer1.getId(), iterator.next().getId());
        assertEquals(customer2.getId(), iterator.next().getId());
        assertEquals(customer3.getId(), iterator.next().getId());
    }

    @Test
    public void testGetBill() {
        restaurant.CustomerGroup group = new restaurant.CustomerGroup();
        restaurant.Customer customer1 = new restaurant.Customer();
        restaurant.Customer customer2 = new restaurant.Customer();
        group.addCustomer(customer1);
        group.addCustomer(customer2);
        double totalBill = group.getBill();
        assertTrue(totalBill > 0.0);
        assertEquals(customer1.getTotalPrice() + customer2.getTotalPrice(), totalBill);
    }

}
