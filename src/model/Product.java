package model;

public class Product {

    private String itemID;
    private String name;
    private String description;
    private String brand;
    private String category;
    private int quantity;
    private double price;
    private String imageUrl;

    public Product() {}

    public Product(String itemID, String name, String description,
                   String category, String brand, int quantity, double price) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
