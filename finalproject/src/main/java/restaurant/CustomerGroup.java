package restaurant;

import java.util.ArrayList;
import java.util.Iterator;

// A grouping of customers that can be sat somewhere
public class CustomerGroup implements Iterable<Customer>{
    ArrayList<Customer> customers;

    public CustomerGroup() {
        customers = new ArrayList<Customer>();
    }

    // Adds a customer to this group
    public void addCustomer(Customer newCustomer) {
        // Customers are immutable (or at least they better be)
        this.customers.add(newCustomer);
    }

    // Returns how many customers are in this group
    public int numCustomers() {
        return customers.size();
    }

    @Override
    public Iterator<Customer> iterator() {
        return customers.iterator();
    }

    
}
