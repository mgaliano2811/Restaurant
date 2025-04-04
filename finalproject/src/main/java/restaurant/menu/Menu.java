package restaurant.menu;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {

    private HashMap<String, MenuItem> menuItems;

    // Constructor to initialize the menu from a default file path
    public Menu() {
        this.menuItems = new HashMap<>();
        parseMenuFile("./menu/Menu.txt");
    }

    // Constructor to initialize the menu from a specified file path
    public Menu(String filePath) {
        this.menuItems = new HashMap<>();
        parseMenuFile(filePath);
    }

    private void parseMenuFile(String filePath) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    String itemName = parts[0].trim();
                    String itemType = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();
                    menuItems.put(itemName, new MenuItem(itemName, itemType, price, description));
                }
            }
        } catch (java.io.IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, MenuItem> getMenu() {
        return new HashMap<>(menuItems);
    }

    public HashMap<String, MenuItem> getMenuByType(ItemType type) {
        HashMap<String, MenuItem> filteredMenu = new HashMap<>();
        for (MenuItem item : menuItems.values()) {
            if (item.getItemType() == type) {
                filteredMenu.put(item.getName(), item);
            }
        }
        return filteredMenu;
    }
    
    public void addMenuItem(String name, String itemString, double price, String description) {
        if (!menuItems.containsKey(name)) { // Check if the item already exists
            MenuItem newItem = new MenuItem(name, itemString, price, description);
            menuItems.put(name, newItem);
        } else {
            System.err.println("Menu item with name " + name + " already exists.");
        }
        updateMenuFile();
    }

    public void removeMenuItem(String name) {
        if (menuItems.containsKey(name)) {
            menuItems.remove(name);
        } else {
            System.err.println("Menu item with name " + name + " does not exist.");
        }
        updateMenuFile();
    }

    public void updateMenuItem(String name, String itemString, double price, String description) {
        if (menuItems.containsKey(name)) {
            MenuItem updatedItem = new MenuItem(name, itemString, price, description);
            menuItems.put(name, updatedItem);
        } else {
            System.err.println("Menu item with name " + name + " does not exist.");
        }
        updateMenuFile();
    }

    private void updateMenuFile() {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("./menu/Menu.txt"))) {
            for (MenuItem item : menuItems.values()) {
                writer.write(item.getName() + "," + item.getItemType().toString() + "," + item.getPrice() + "," + item.getDescription());
                writer.newLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        HashMap<String, ArrayList<MenuItem>> groupedMenu = new HashMap<>();
        String out = "";
        for (MenuItem item : menuItems.values()) {
            String type = item.getItemType().toString();
            if (!groupedMenu.containsKey(type)) {
                groupedMenu.put(type, new ArrayList<>());
            }
            groupedMenu.get(type).add(item);
        }
        for (String type : groupedMenu.keySet()) {
            out += "Type: " + type + "\n";
            for (MenuItem item : groupedMenu.get(type)) {
                out += "\t" + item + "\n";
            }
        }
        return out;
    }
}