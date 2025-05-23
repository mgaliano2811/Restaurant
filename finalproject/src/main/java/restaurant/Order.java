package restaurant;

import java.util.ArrayList;
import restaurant.menu.*;

public class Order {

    private final ArrayList<MenuItem> orderItems;
    private final int orderNumber;

    public Order(int orderNumber) {
        this.orderItems = new ArrayList<>();
        this.orderNumber = orderNumber;

        Menu menu = new Menu();

        // Customer randomly picks something on the menu
        this.orderItems.add(menu.getRandomMenuItemByType(ItemType.ENTREE));
        this.orderItems.add(menu.getRandomMenuItemByType(ItemType.DRINK));
        this.orderItems.add(menu.getRandomMenuItemByType(ItemType.DESSERT));
    }

    // Copy constructor but with a new orderNumber
    public Order(Order order, int orderNumber) {
        this.orderItems = order.getOrderItems();
        this.orderNumber = orderNumber;
    }

    // Constructor for creating an order with specific items, not sure if we want this instead but it's here if we do
    // public Order(ArrayList<MenuItem> orderItems) {
    //     this.orderItems = new ArrayList<>(orderItems);
    // }

    public ArrayList<MenuItem> getOrderItems() {
        return new ArrayList<MenuItem>(orderItems);
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (MenuItem item : orderItems) {
            total += item.getPrice();
        }
        return total;
    }

    public int getOrderNumber() { return orderNumber; }

    // Return a stringified version of everything in this order
    @Override
    public String toString() {
        String returnString = "";
        for (MenuItem item : orderItems) {
            returnString += item.toString() + "\n";
        }

        return returnString;
    }

    @Override
    public int hashCode() { return Integer.hashCode(orderNumber); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                // Same object
        if (obj == null || getClass() != obj.getClass()) return false;
        Order other = (Order) obj;
        return this.orderNumber == other.orderNumber;
    }
}