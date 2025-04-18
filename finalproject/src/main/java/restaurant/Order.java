package restaurant;

import java.util.ArrayList;

import restaurant.menu.*;

public class Order {

    private final ArrayList<MenuItem> orderItems;

    public Order() {
        this.orderItems = new ArrayList<>();

        Menu menu = new Menu();

        this.orderItems.add(menu.getMenuByType(ItemType.ENTREE).values().iterator().next());
        this.orderItems.add(menu.getMenuByType(ItemType.DRINK).values().iterator().next());
        this.orderItems.add(menu.getMenuByType(ItemType.DESSERT).values().iterator().next());
    }

    // Constructor for creating an order with specific items, not sure if we want this instead but it's here if we do
    // public Order(ArrayList<MenuItem> orderItems) {
    //     this.orderItems = new ArrayList<>(orderItems);
    // }

    public ArrayList<MenuItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (MenuItem item : orderItems) {
            total += item.getPrice();
        }
        return total;
    }

}