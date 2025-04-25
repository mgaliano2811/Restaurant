package restaurant;

import java.util.ArrayList;
import java.util.UUID;

import restaurant.menu.*;

// A basic customer class, they have something they want to eat
//  This should remain IMMUTABLE, please...
public class Customer {
    UUID id;
    Order foodWant;

    public Customer() {
        id = UUID.randomUUID();
        foodWant = new Order(1);
    }

    public String getId() {
        return id.toString();
    }

    public ArrayList<MenuItem> getFoodWant() {
        return foodWant.getOrderItems();
    }

    public double getTotalPrice() {
        return foodWant.getTotalPrice();
    }
}
