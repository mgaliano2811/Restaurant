package restaurant.menu;

public class MenuItem {
    
    private String name;
    private ItemType itemType; 
    private double price;
    private String description;
    private int cookTime; 

    public MenuItem(String name, String itemString, double price, String description, int cookTime){
        this.name = name;
        switch (itemString.toLowerCase()) {
            case "entree":
            this.itemType = ItemType.ENTREE;
            break;
            case "drink":
            this.itemType = ItemType.DRINK;
            break;
            case "dessert":
            this.itemType = ItemType.DESSERT;
            break;
            default:
            throw new IllegalArgumentException("Invalid item type: " + itemString + ". Must be one of: entree, drink, dessert.");
        }
        this.price = price;
        this.description = description;
        this.cookTime = cookTime;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public String toString() {
        return name + " - $" + price + ": " + description;
    }

}
