package menu;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {

    private HashMap<String, MenuItem> menuItems;

    public Menu() {
        this.menuItems = new HashMap<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("./menu/Menu.txt"))) {
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

    public void printMenu() {
        HashMap<String, ArrayList<MenuItem>> groupedMenu = new HashMap<>();
        for (MenuItem item : menuItems.values()) {
            String type = item.getItemType().toString();
            if (!groupedMenu.containsKey(type)) {
                groupedMenu.put(type, new ArrayList<>());
            }
            groupedMenu.get(type).add(item);
        }
        for (String type : groupedMenu.keySet()) {
                System.out.println("Type: " + type);
            for (MenuItem item : groupedMenu.get(type)) {
                System.out.println("\t" + item);
            }
        }
    }

    // Main Method just to show how the Menu class can be used in a simple console application.
    // This is not part of the Menu class itself but included here for demonstration.
    public static void main(String[] args) {
    Menu menu = new Menu();
    java.util.Scanner scanner = new java.util.Scanner(System.in);

    while (true) {
        System.out.println("\nMenu Management System");
        System.out.println("1. View Full Menu");
        System.out.println("2. View Menu by Type");
        System.out.println("3. Add Menu Item");
        System.out.println("4. Remove Menu Item");
        System.out.println("5. Update Menu Item");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                menu.printMenu();
                break;
            case 2:
                System.out.print("Enter item type: ");
                String type = scanner.nextLine();
                try {
                    ItemType itemType = ItemType.valueOf(type.toUpperCase());
                    HashMap<String, MenuItem> filteredMenu = menu.getMenuByType(itemType);
                    filteredMenu.values().forEach(System.out::println);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid item type.");
                }
                break;
            case 3:
                System.out.print("Enter item name: ");
                String name = scanner.nextLine();
                System.out.print("Enter item type: ");
                String itemType = scanner.nextLine();
                System.out.print("Enter item price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter item description: ");
                String description = scanner.nextLine();
                menu.addMenuItem(name, itemType, price, description);
                break;
            case 4:
                System.out.print("Enter item name to remove: ");
                String removeName = scanner.nextLine();
                menu.removeMenuItem(removeName);
                break;
            case 5:
                System.out.print("Enter item name to update: ");
                String updateName = scanner.nextLine();
                System.out.print("Enter new item type: ");
                String newItemType = scanner.nextLine();
                System.out.print("Enter new item price: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter new item description: ");
                String newDescription = scanner.nextLine();
                menu.updateMenuItem(updateName, newItemType, newPrice, newDescription);
                break;
            case 6:
                System.out.println("Exiting...");
                scanner.close();
                return;
            default:
                System.err.println("Invalid choice. Please try again.");
        }
    }
}
}