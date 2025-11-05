package listener;

import entity.Order;
import java.util.EventListener;

/**
 * OrderUpdateListener - Order Update Event Listener
 * Path: Source Packages/listener/OrderUpdateListener.java
 * 
 * Interface for listening to order update events
 * Implement this interface to receive notifications when orders change
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public interface OrderUpdateListener extends EventListener {
    
    /**
     * Called when a new order is created
     * 
     * @param order The newly created order
     */
    void onOrderCreated(Order order);
    
    /**
     * Called when an order status is updated
     * 
     * @param order The updated order
     * @param oldStatus Previous status
     * @param newStatus New status
     */
    void onOrderStatusChanged(Order order, int oldStatus, int newStatus);
    
    /**
     * Called when an order is confirmed by cashier
     * 
     * @param order The confirmed order
     */
    void onOrderConfirmed(Order order);
    
    /**
     * Called when an order is sent to kitchen
     * 
     * @param order The order sent to kitchen
     */
    void onOrderSentToKitchen(Order order);
    
    /**
     * Called when an order starts cooking
     * 
     * @param order The order being cooked
     * @param chefId ID of chef assigned
     */
    void onOrderCookingStarted(Order order, int chefId);
    
    /**
     * Called when an order is ready for pickup
     * 
     * @param order The ready order
     */
    void onOrderReady(Order order);
    
    /**
     * Called when an order is completed/delivered
     * 
     * @param order The completed order
     */
    void onOrderCompleted(Order order);
    
    /**
     * Called when an order is cancelled
     * 
     * @param order The cancelled order
     * @param reason Reason for cancellation
     */
    void onOrderCancelled(Order order, String reason);
    
    /**
     * Called when an order is updated (general)
     * 
     * @param order The updated order
     */
    void onOrderUpdated(Order order);
    
    /**
     * Called when an order is deleted
     * 
     * @param orderId ID of deleted order
     */
    void onOrderDeleted(int orderId);
}