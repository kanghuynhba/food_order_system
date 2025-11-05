package entity;

import java.sql.Timestamp;

/**
 * Product Entity - Sản phẩm/Món ăn
 * Path: Source Packages/entity/Product.java
 */
public class Product {
    
    private int productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private int available;  // 1: Available, 0: Unavailable
    private Timestamp createdAt;
    
    // ============ CONSTRUCTORS ============
    
    public Product() {}
    
    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = 1;
    }
    
    public Product(String name, String description, double price, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.available = 1;
    }
    
    public Product(int productId, String name, String description, double price, 
                   String category, String imageUrl, int available, Timestamp createdAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.available = available;
        this.createdAt = createdAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getAvailable() {
        return available;
    }
    
    public void setAvailable(int available) {
        this.available = available;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // ============ HELPER METHODS ============
    
    public boolean isAvailable() {
        return available == 1;
    }
    
    public void setAvailableStatus(boolean status) {
        this.available = status ? 1 : 0;
    }
    
    public String getAvailableStatus() {
        return available == 1 ? "Available" : "Unavailable";
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", available=" + getAvailableStatus() +
                '}';
    }
}