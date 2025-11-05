package listener;

import entity.Ingredient;
import java.util.EventListener;

/**
 * InventoryListener - Inventory/Stock Change Event Listener
 * Path: Source Packages/listener/InventoryListener.java
 * 
 * Interface for listening to inventory and stock change events
 * Implement this interface to receive notifications when inventory changes
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public interface InventoryListener extends EventListener {
    
    /**
     * Called when a new ingredient is added to inventory
     * 
     * @param ingredient The newly added ingredient
     */
    void onIngredientAdded(Ingredient ingredient);
    
    /**
     * Called when an ingredient is updated
     * 
     * @param ingredient The updated ingredient
     */
    void onIngredientUpdated(Ingredient ingredient);
    
    /**
     * Called when an ingredient is deleted from inventory
     * 
     * @param ingredientId ID of deleted ingredient
     * @param ingredientName Name of deleted ingredient
     */
    void onIngredientDeleted(int ingredientId, String ingredientName);
    
    /**
     * Called when stock is added to an ingredient
     * 
     * @param ingredient The ingredient
     * @param amountAdded Amount added to stock
     * @param newQuantity New total quantity
     */
    void onStockAdded(Ingredient ingredient, double amountAdded, double newQuantity);
    
    /**
     * Called when stock is removed/used from an ingredient
     * 
     * @param ingredient The ingredient
     * @param amountRemoved Amount removed from stock
     * @param newQuantity New remaining quantity
     */
    void onStockRemoved(Ingredient ingredient, double amountRemoved, double newQuantity);
    
    /**
     * Called when ingredient quantity is directly updated
     * 
     * @param ingredient The ingredient
     * @param oldQuantity Previous quantity
     * @param newQuantity New quantity
     */
    void onQuantityChanged(Ingredient ingredient, double oldQuantity, double newQuantity);
    
    /**
     * Called when ingredient stock reaches low level
     * 
     * @param ingredient The low stock ingredient
     * @param currentQuantity Current quantity
     * @param threshold Low stock threshold
     */
    void onLowStockAlert(Ingredient ingredient, double currentQuantity, double threshold);
    
    /**
     * Called when ingredient is out of stock
     * 
     * @param ingredient The out of stock ingredient
     */
    void onOutOfStock(Ingredient ingredient);
    
    /**
     * Called when ingredient has expired
     * 
     * @param ingredient The expired ingredient
     */
    void onIngredientExpired(Ingredient ingredient);
    
    /**
     * Called when ingredient is about to expire (within warning period)
     * 
     * @param ingredient The ingredient
     * @param daysUntilExpiry Days until expiration
     */
    void onExpirationWarning(Ingredient ingredient, int daysUntilExpiry);
    
    /**
     * Called when ingredient status changes
     * 
     * @param ingredient The ingredient
     * @param oldStatus Previous status
     * @param newStatus New status
     */
    void onStatusChanged(Ingredient ingredient, String oldStatus, String newStatus);
    
    /**
     * Called when inventory validation fails
     * 
     * @param ingredient The problematic ingredient
     * @param reason Reason for validation failure
     */
    void onInventoryValidationFailed(Ingredient ingredient, String reason);
    
    /**
     * Called when a batch of ingredients is received (bulk add)
     * 
     * @param count Number of ingredients received
     * @param totalValue Total value of received inventory
     */
    void onInventoryReceived(int count, double totalValue);
    
    /**
     * Called when inventory is adjusted (manual correction)
     * 
     * @param ingredient The ingredient
     * @param adjustment Adjustment amount (can be positive or negative)
     * @param reason Reason for adjustment
     */
    void onInventoryAdjusted(Ingredient ingredient, double adjustment, String reason);
}