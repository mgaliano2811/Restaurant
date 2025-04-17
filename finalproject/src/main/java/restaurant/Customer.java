package restaurant;

import java.util.ArrayList;
import java.util.UUID;

import restaurant.menu.*;

// A basic customer class, they have something they want to eat
//  This should remain IMMUTABLE, please...
public class Customer {
    UUID id;
    ArrayList<MenuItem> foodWant;

    public Customer() {
        id = UUID.randomUUID();
        foodWant = makeFoodWant();
    }

    private ArrayList<MenuItem> makeFoodWant() {
        ArrayList<MenuItem> foodWant = new ArrayList<>();
        
        Menu menu = new Menu();

        foodWant.add(menu.getMenuByType(ItemType.ENTREE).values().iterator().next());
        foodWant.add(menu.getMenuByType(ItemType.DRINK).values().iterator().next());
        foodWant.add(menu.getMenuByType(ItemType.DESSERT).values().iterator().next());

        return foodWant;
    }

    public String getId() {
        return id.toString();
    }

    public ArrayList<MenuItem> getFoodWant() {
        return new ArrayList<>(foodWant);
    }
}
