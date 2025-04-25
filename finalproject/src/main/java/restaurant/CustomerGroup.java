package restaurant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// A grouping of customers that can be sat somewhere
public class CustomerGroup implements Iterable<Customer>{
    // All the customers in this customerGroup
    ArrayList<Customer> customers;
    // An ID that represents this customerGroup
    String ID;
    int tipPercentage;

    public CustomerGroup(String newID) {
        Random rand = new Random();
        customers = new ArrayList<Customer>();
        ID = newID;
        tipPercentage = rand.nextInt(31);   // Tip between 0% - 30%
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

    public double getTip() {
        return getBill() * (tipPercentage / 100.0);
    }

    // Get all the orders from the customers, orders are immutable so this shouldn't be a problem
    public ArrayList<Order> getOrders() {
        ArrayList<Order> orderList = new ArrayList<Order>();

        for (Customer customer : this.customers) {
            orderList.add(customer.getOrder());
        }

        return orderList;
    }

    @Override
    public Iterator<Customer> iterator() {
        return customers.iterator();
    }
    
}
