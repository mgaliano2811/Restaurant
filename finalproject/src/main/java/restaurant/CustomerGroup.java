package restaurant;

import java.util.ArrayList;
import java.util.Iterator;

// A grouping of customers that can be sat somewhere
public class CustomerGroup implements Iterable<Customer>{
    // All the customers in this customerGroup
    ArrayList<Customer> customers;
    // An ID that represents this customerGroup
    String ID;

    public CustomerGroup(String newID) {
        customers = new ArrayList<Customer>();
        ID = newID;
    }

    public String getID() {
        return ID;
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

    public double getBill() {
        double total = 0.0;
        for (Customer customer : customers) {
            total += customer.getTotalPrice();
        }
        return total;
    }

    @Override
    public Iterator<Customer> iterator() {
        return customers.iterator();
    }
    
}
