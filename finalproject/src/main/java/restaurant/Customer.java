package restaurant;

import java.util.ArrayList;

import restaurant.menu.*;

// A basic customer class, they have something they want to eat
//  This should remain IMMUTABLE, please...
public class Customer {
    int id;
    Order foodWant;

    public Customer() {
        this.id = generateID();
        foodWant = new Order(1);
    }

    public String getId() {
        return id+"";
    }

    // Generate a random 8 digit unique ID
    private int generateID() {
        return (int)(Math.random() * 90000000) + 10000000;
    }

    public Order getOrder() {
        return foodWant; // Order is immutable so this is fine
    }

    public double getTotalPrice() {
        return foodWant.getTotalPrice();
    }
}
