package service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * NotificationService - Real-time Notification Service
 * Path: Source Packages/service/NotificationService.java
 * 
 * Chức năng:
 * - Gửi thông báo real-time giữa các UI
 * - Quản lý listeners
 * - Observer pattern để cập nhật UI
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class NotificationService {
    
    private static NotificationService instance;
    private List<NotificationListener> listeners;
    
    // Notification types
    public static final String NEW_ORDER = "NEW_ORDER";
    public static final String ORDER_UPDATED = "ORDER_UPDATED";
    public static final String ORDER_READY = "ORDER_READY";
    public static final String PAYMENT_CONFIRMED = "PAYMENT_CONFIRMED";
    public static final String LOW_STOCK = "LOW_STOCK";
    public static final String EXPIRED_INGREDIENT = "EXPIRED_INGREDIENT";
    
    // ============ SINGLETON ============
    
    private NotificationService() {
        this.listeners = new CopyOnWriteArrayList<>();
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    // ============ LISTENER MANAGEMENT ============
    
    /**
     * Đăng ký listener
     */
    public void addListener(NotificationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("📢 Listener registered: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Hủy đăng ký listener
     */
    public void removeListener(NotificationListener listener) {
        if (listener != null) {
            listeners.remove(listener);
            System.out.println("📢 Listener unregistered: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Xóa tất cả listeners
     */
    public void clearListeners() {
        listeners.clear();
        System.out.println("📢 All listeners cleared");
    }
    
    /**
     * Lấy số lượng listeners
     */
    public int getListenerCount() {
        return listeners.size();
    }
    
    // ============ NOTIFICATION SENDING ============
    
    /**
     * Gửi thông báo chung
     */
    public void notify(String type, Object data) {
        System.out.println("📢 Notification sent: " + type);
        
        for (NotificationListener listener : listeners) {
            try {
                listener.onNotification(type, data);
            } catch (Exception e) {
                System.err.println("❌ Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gửi thông báo với message
     */
    public void notify(String type, String message) {
        notify(type, message);
    }
    
    /**
     * Gửi thông báo không có data
     */
    public void notify(String type) {
        notify(type, null);
    }
    
    // ============ SPECIFIC NOTIFICATIONS ============
    
    /**
     * Thông báo có đơn hàng mới
     */
    public void notifyNewOrder(int orderId) {
        notify(NEW_ORDER, orderId);
        System.out.println("🔔 New order notification: #" + orderId);
    }
    
    /**
     * Thông báo đơn hàng được cập nhật
     */
    public void notifyOrderUpdated(int orderId) {
        notify(ORDER_UPDATED, orderId);
        System.out.println("🔔 Order updated notification: #" + orderId);
    }
    
    /**
     * Thông báo đơn hàng đã sẵn sàng
     */
    public void notifyOrderReady(int orderId) {
        notify(ORDER_READY, orderId);
        System.out.println("🔔 Order ready notification: #" + orderId);
    }
    
    /**
     * Thông báo thanh toán được xác nhận
     */
    public void notifyPaymentConfirmed(int orderId) {
        notify(PAYMENT_CONFIRMED, orderId);
        System.out.println("🔔 Payment confirmed notification: #" + orderId);
    }
    
    /**
     * Thông báo nguyên liệu sắp hết
     */
    public void notifyLowStock(String ingredientName) {
        notify(LOW_STOCK, ingredientName);
        System.out.println("🔔 Low stock alert: " + ingredientName);
    }
    
    /**
     * Thông báo nguyên liệu hết hạn
     */
    public void notifyExpiredIngredient(String ingredientName) {
        notify(EXPIRED_INGREDIENT, ingredientName);
        System.out.println("🔔 Expired ingredient alert: " + ingredientName);
    }
    
    // ============ BROADCAST MESSAGES ============
    
    /**
     * Broadcast message to all
     */
    public void broadcast(String message) {
        System.out.println("📢 Broadcasting: " + message);
        
        for (NotificationListener listener : listeners) {
            try {
                listener.onBroadcast(message);
            } catch (Exception e) {
                System.err.println("❌ Error broadcasting: " + e.getMessage());
            }
        }
    }
    
    /**
     * Broadcast with type and message
     */
    public void broadcast(String type, String message) {
        notify(type, message);
    }
    
    // ============ UTILITY ============
    
    /**
     * Check if has listeners
     */
    public boolean hasListeners() {
        return !listeners.isEmpty();
    }
    
    /**
     * Get active listeners info
     */
    public List<String> getActiveListeners() {
        List<String> active = new ArrayList<>();
        for (NotificationListener listener : listeners) {
            active.add(listener.getClass().getSimpleName());
        }
        return active;
    }
    
    // ============ LISTENER INTERFACE ============
    
    /**
     * Interface for notification listeners
     */
    public interface NotificationListener {
        void onNotification(String type, Object data);
        
        default void onBroadcast(String message) {
            // Default implementation
        }
    }
}