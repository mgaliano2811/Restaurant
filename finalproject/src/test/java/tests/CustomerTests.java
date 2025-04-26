package tests;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import restaurant.*;

public class CustomerTests {
    
    @Test
    public void testCustomerCreationAndGetters() {
        Customer customer = new Customer();
        assertNotNull(customer.getId());
    }

    @Test
    public void getCustomerOrderPrice() {
        Customer customer = new Customer();
        double totalPrice = customer.getTotalPrice();
        assertTrue(totalPrice > 0.0);
    }

    @Test
    public void testCustomerGroupCreation() {
        CustomerGroup group = new CustomerGroup("123");
        assertNotNull(group);
    }

    @Test
    public void testAddCustomerToGroup() {
        CustomerGroup group = new CustomerGroup("123");
        Customer customer = new Customer();
        group.addCustomer(customer);
        assertEquals(1, group.numCustomers());
        assertEquals(customer.getId(), group.iterator().next().getId());
    }

    @Test
    public void testAddManyCustomersToGroup() {
        CustomerGroup group = new CustomerGroup("123");
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Customer customer3 = new Customer();
        group.addCustomer(customer1);
        group.addCustomer(customer2);
        group.addCustomer(customer3);
        assertEquals(3, group.numCustomers());
        Iterator<Customer> iterator = group.iterator();
        assertEquals(customer1.getId(), iterator.next().getId());
        assertEquals(customer2.getId(), iterator.next().getId());
        assertEquals(customer3.getId(), iterator.next().getId());
    }

    @Test
    public void testGetBill() {
        restaurant.CustomerGroup group = new restaurant.CustomerGroup("123");
        restaurant.Customer customer1 = new restaurant.Customer();
        restaurant.Customer customer2 = new restaurant.Customer();
        group.addCustomer(customer1);
        group.addCustomer(customer2);
        double totalBill = group.getBill();
        assertTrue(totalBill > 0.0);
        assertEquals(customer1.getTotalPrice() + customer2.getTotalPrice(), totalBill);
    }

}
