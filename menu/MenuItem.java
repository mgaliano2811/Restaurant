package menu;

public class MenuItem {
    
    private String name;
    private ItemType itemType; 
    private double price;
    private String description;

    public MenuItem(String name, String itemString, double price, String description) {
        this.name = name;
        this.itemType = itemString.equalsIgnoreCase("entree") ? ItemType.ENTREE :
                        itemString.equalsIgnoreCase("drink") ? ItemType.DRINK :
                        itemString.equalsIgnoreCase("dessert") ? ItemType.DESSERT :
                        null; // Handle invalid type, could throw an exception instead
        this.price = price;
        this.description = description;
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

    @Override
    public String toString() {
        return name + " - $" + price + ": " + description;
    }

}
