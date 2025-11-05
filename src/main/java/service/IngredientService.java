package service;

import dao.IngredientDAO;
import entity.Ingredient;
import java.sql.Date;
import java.util.List;

/**
 * IngredientService - Ingredient/Inventory Management Service
 * Path: Source Packages/service/IngredientService.java
 * 
 * Chức năng:
 * - CRUD nguyên liệu
 * - Quản lý tồn kho
 * - Cập nhật số lượng (nhập/xuất)
 * - Kiểm tra hạn sử dụng
 * - Cảnh báo nguyên liệu sắp hết
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class IngredientService {
    
    private static IngredientService instance;
    private IngredientDAO ingredientDAO;
    
    // ============ SINGLETON ============
    
    private IngredientService() {
        this.ingredientDAO = new IngredientDAO();
    }
    
    public static IngredientService getInstance() {
        if (instance == null) {
            instance = new IngredientService();
        }
        return instance;
    }
    
    // ============ CRUD OPERATIONS ============
    
    /**
     * Thêm nguyên liệu mới
     */
    public boolean addIngredient(String name, double quantity, String unit, 
                                 Date expiryDate, String supplier) {
        try {
            if (name == null || name.trim().isEmpty()) {
                System.err.println("❌ Name is required");
                return false;
            }
            
            if (quantity < 0) {
                System.err.println("❌ Quantity cannot be negative");
                return false;
            }
            
            if (unit == null || unit.trim().isEmpty()) {
                System.err.println("❌ Unit is required");
                return false;
            }
            
            Ingredient ingredient = new Ingredient();
            ingredient.setName(name);
            ingredient.setQuantity(quantity);
            ingredient.setUnit(unit);
            ingredient.setExpiryDate(expiryDate);
            ingredient.setSupplier(supplier);
            ingredient.updateStatus();
            
            boolean success = ingredientDAO.create(ingredient);
            
            if (success) {
                System.out.println("✅ Ingredient added: " + name);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding ingredient: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật nguyên liệu
     */
    public boolean updateIngredient(Ingredient ingredient) {
        try {
            if (ingredient == null || ingredient.getIngredientId() <= 0) {
                System.err.println("❌ Invalid ingredient");
                return false;
            }
            
            ingredient.updateStatus();
            boolean success = ingredientDAO.update(ingredient);
            
            if (success) {
                System.out.println("✅ Ingredient updated: " + ingredient.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating ingredient: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa nguyên liệu
     */
    public boolean deleteIngredient(int ingredientId) {
        try {
            Ingredient ingredient = ingredientDAO.getById(ingredientId);
            if (ingredient == null) {
                System.err.println("❌ Ingredient not found");
                return false;
            }
            
            boolean success = ingredientDAO.delete(ingredientId);
            
            if (success) {
                System.out.println("✅ Ingredient deleted: " + ingredientId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting ingredient: " + e.getMessage());
            return false;
        }
    }
    
    // ============ QUERY OPERATIONS ============
    
    /**
     * Lấy tất cả nguyên liệu
     */
    public List<Ingredient> getAllIngredients() {
        try {
            return ingredientDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all ingredients: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy nguyên liệu theo ID
     */
    public Ingredient getIngredientById(int ingredientId) {
        try {
            return ingredientDAO.getById(ingredientId);
        } catch (Exception e) {
            System.err.println("❌ Error getting ingredient: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy nguyên liệu theo status
     */
    public List<Ingredient> getIngredientsByStatus(String status) {
        try {
            return ingredientDAO.getByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Error getting ingredients by status: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Tìm kiếm nguyên liệu
     */
    public List<Ingredient> searchIngredients(String keyword) {
        try {
            return ingredientDAO.search(keyword);
        } catch (Exception e) {
            System.err.println("❌ Error searching ingredients: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ INVENTORY MANAGEMENT ============
    
    /**
     * Nhập thêm nguyên liệu
     */
    public boolean addStock(int ingredientId, double amount) {
        try {
            if (amount <= 0) {
                System.err.println("❌ Amount must be positive");
                return false;
            }
            
            Ingredient ingredient = ingredientDAO.getById(ingredientId);
            if (ingredient == null) {
                System.err.println("❌ Ingredient not found");
                return false;
            }
            
            boolean success = ingredientDAO.addQuantity(ingredientId, amount);
            
            if (success) {
                System.out.println("✅ Added " + amount + " " + ingredient.getUnit() + 
                                 " to " + ingredient.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding stock: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xuất nguyên liệu (sử dụng)
     */
    public boolean removeStock(int ingredientId, double amount) {
        try {
            if (amount <= 0) {
                System.err.println("❌ Amount must be positive");
                return false;
            }
            
            Ingredient ingredient = ingredientDAO.getById(ingredientId);
            if (ingredient == null) {
                System.err.println("❌ Ingredient not found");
                return false;
            }
            
            if (ingredient.getQuantity() < amount) {
                System.err.println("❌ Insufficient stock: " + ingredient.getName());
                return false;
            }
            
            boolean success = ingredientDAO.reduceQuantity(ingredientId, amount);
            
            if (success) {
                System.out.println("✅ Removed " + amount + " " + ingredient.getUnit() + 
                                 " from " + ingredient.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error removing stock: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật số lượng trực tiếp
     */
    public boolean updateQuantity(int ingredientId, double newQuantity) {
        try {
            if (newQuantity < 0) {
                System.err.println("❌ Quantity cannot be negative");
                return false;
            }
            
            boolean success = ingredientDAO.updateQuantity(ingredientId, newQuantity);
            
            if (success) {
                System.out.println("✅ Quantity updated");
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating quantity: " + e.getMessage());
            return false;
        }
    }
    
    // ============ STATUS CHECKS ============
    
    /**
     * Lấy nguyên liệu sắp hết
     */
    public List<Ingredient> getLowStockIngredients() {
        try {
            return ingredientDAO.getLowStockIngredients();
        } catch (Exception e) {
            System.err.println("❌ Error getting low stock ingredients: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy nguyên liệu đã hết hạn
     */
    public List<Ingredient> getExpiredIngredients() {
        try {
            return ingredientDAO.getExpiredIngredients();
        } catch (Exception e) {
            System.err.println("❌ Error getting expired ingredients: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Kiểm tra nguyên liệu có đủ không
     */
    public boolean checkAvailability(int ingredientId, double requiredAmount) {
        try {
            Ingredient ingredient = ingredientDAO.getById(ingredientId);
            if (ingredient == null) return false;
            
            return ingredient.getQuantity() >= requiredAmount;
            
        } catch (Exception e) {
            System.err.println("❌ Error checking availability: " + e.getMessage());
            return false;
        }
    }
    
    // ============ ALERTS & NOTIFICATIONS ============
    
    /**
     * Đếm số nguyên liệu sắp hết
     */
    public int getLowStockCount() {
        return getLowStockIngredients().size();
    }
    
    /**
     * Đếm số nguyên liệu hết hạn
     */
    public int getExpiredCount() {
        return getExpiredIngredients().size();
    }
    
    /**
     * Lấy danh sách cảnh báo
     */
    public List<String> getAlerts() {
        List<String> alerts = new java.util.ArrayList<>();
        
        // Low stock alerts
        List<Ingredient> lowStock = getLowStockIngredients();
        for (Ingredient ing : lowStock) {
            alerts.add("⚠️ Low stock: " + ing.getName() + " (" + ing.getQuantity() + " " + ing.getUnit() + ")");
        }
        
        // Expired alerts
        List<Ingredient> expired = getExpiredIngredients();
        for (Ingredient ing : expired) {
            alerts.add("❌ Expired: " + ing.getName());
        }
        
        return alerts;
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm tổng số nguyên liệu
     */
    public int getTotalIngredientCount() {
        try {
            return ingredientDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting ingredients: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Tính tổng giá trị kho (estimate)
     */
    public double getTotalInventoryValue() {
        // TODO: Add price field to Ingredient entity
        return 0.0;
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate ingredient data
     */
    public boolean validateIngredient(Ingredient ingredient) {
        if (ingredient == null) return false;
        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) return false;
        if (ingredient.getQuantity() < 0) return false;
        if (ingredient.getUnit() == null || ingredient.getUnit().trim().isEmpty()) return false;
        
        return true;
    }
}