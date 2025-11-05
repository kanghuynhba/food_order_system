package listener;

import entity.Cart;
import entity.CartItem;
import java.util.EventListener;

/**
 * CartListener - Shopping Cart Event Listener
 * Path: Source Packages/listener/CartListener.java
 * 
 * Interface for listening to cart change events
 * Implement this interface to receive notifications when cart changes
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public interface CartListener extends EventListener {
    
    /**
     * Called when a new cart is created
     * 
     * @param cart The newly created cart
     */
    void onCartCreated(Cart cart);
    
    /**
     * Called when an item is added to cart
     * 
     * @param cart The cart
     * @param item The item added
     */
    void onItemAdded(Cart cart, CartItem item);
    
    /**
     * Called when an item is removed from cart
     * 
     * @param cart The cart
     * @param item The item removed
     */
    void onItemRemoved(Cart cart, CartItem item);
    
    /**
     * Called when an item quantity is updated
     * 
     * @param cart The cart
     * @param item The item updated
     * @param oldQuantity Previous quantity
     * @param newQuantity New quantity
     */
    void onItemQuantityChanged(Cart cart, CartItem item, int oldQuantity, int newQuantity);
    
    /**
     * Called when cart total is updated
     * 
     * @param cart The cart
     * @param oldTotal Previous total amount
     * @param newTotal New total amount
     */
    void onCartTotalChanged(Cart cart, double oldTotal, double newTotal);
    
    /**
     * Called when cart is cleared (all items removed)
     * 
     * @param cart The cart that was cleared
     * @param itemCount Number of items that were removed
     */
    void onCartCleared(Cart cart, int itemCount);
    
    /**
     * Called when cart is checked out
     * 
     * @param cart The cart being checked out
     * @param orderId ID of order created from cart
     */
    void onCartCheckedOut(Cart cart, int orderId);
    
    /**
     * Called when cart is abandoned (not checked out)
     * 
     * @param cart The abandoned cart
     */
    void onCartAbandoned(Cart cart);
    
    /**
     * Called when cart is merged (combining items from another cart)
     * 
     * @param targetCart The cart receiving items
     * @param sourceCart The cart providing items
     * @param itemsMerged Number of items merged
     */
    void onCartMerged(Cart targetCart, Cart sourceCart, int itemsMerged);
    
    /**
     * Called when cart validation fails (e.g., product out of stock)
     * 
     * @param cart The cart
     * @param item The problematic item
     * @param reason Reason for validation failure
     */
    void onCartValidationFailed(Cart cart, CartItem item, String reason);
    
    /**
     * Called when discount is applied to cart
     * 
     * @param cart The cart
     * @param discountAmount Discount amount applied
     * @param discountCode Discount code used (if any)
     */
    void onDiscountApplied(Cart cart, double discountAmount, String discountCode);
    
    /**
     * Called when cart is updated (general)
     * 
     * @param cart The updated cart
     */
    void onCartUpdated(Cart cart);
}