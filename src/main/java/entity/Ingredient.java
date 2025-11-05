package entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Ingredient Entity - Nguyên liệu/Kho
 * Path: Source Packages/entity/Ingredient.java
 */
public class Ingredient {
    
    private int ingredientId;
    private String name;
    private double quantity;
    private String unit;
    private Date expiryDate;
    private String supplier;
    private String status;  // available, low, out_of_stock, expired
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    public Ingredient() {}
    
    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.status = "available";
    }
    
    public Ingredient(String name, double quantity, String unit, Date expiryDate, String supplier) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.status = "available";
    }
    
    public Ingredient(int ingredientId, String name, double quantity, String unit,
                      Date expiryDate, String supplier, String status,
                      Timestamp createdAt, Timestamp updatedAt) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getIngredientId() {
        return ingredientId;
    }
    
    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ HELPER METHODS ============
    
    public void addQuantity(double amount) {
        this.quantity += amount;
        updateStatus();
    }
    
    public void removeQuantity(double amount) {
        this.quantity -= amount;
        if (this.quantity < 0) {
            this.quantity = 0;
        }
        updateStatus();
    }
    
    public void updateStatus() {
        if (quantity <= 0) {
            status = "out_of_stock";
        } else if (quantity < 10) {
            status = "low";
        } else if (isExpired()) {
            status = "expired";
        } else {
            status = "available";
        }
    }
    
    public boolean isExpired() {
        if (expiryDate == null) return false;
        return expiryDate.before(new Date(System.currentTimeMillis()));
    }
    
    public boolean isLowStock() {
        return quantity < 10;
    }
    
    public boolean isOutOfStock() {
        return quantity <= 0;
    }
    
    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientId=" + ingredientId +
                ", name='" + name + '\'' +
                ", quantity=" + quantity + unit +
                ", status='" + status + '\'' +
                '}';
    }
}