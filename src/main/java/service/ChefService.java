package service;

import dao.OrderDAO;
import dao.OrderItemDAO;
import entity.Order;
import entity.OrderItem;
import java.util.List;

/**
 * ChefService - Chef/Kitchen Operations Service
 * Path: Source Packages/service/ChefService.java
 * 
 * Chức năng:
 * - Xem orders cần chế biến
 * - Cập nhật trạng thái chế biến
 * - Quản lý queue orders
 * - Đánh dấu order hoàn thành
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ChefService {
    
    private static ChefService instance;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    
    // ============ SINGLETON ============
    
    private ChefService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
    }
    
    public static ChefService getInstance() {
        if (instance == null) {
            instance = new ChefService();
        }
        return instance;
    }
    
    // ============ ORDER QUEUE ============
    
    /**
     * Lấy orders chưa nấu (status = 2: Preparing)
     */
    public List<Order> getWaitingOrders() {
        try {
            return orderDAO.getByStatus(2); // 2 = Preparing (chưa nấu)
        } catch (Exception e) {
            System.err.println("❌ Error getting waiting orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy orders đang nấu (status = 3: Cooking)
     */
    public List<Order> getCookingOrders() {
        try {
            return orderDAO.getByStatus(3); // 3 = Cooking
        } catch (Exception e) {
            System.err.println("❌ Error getting cooking orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy orders đã sẵn sàng (status = 4: Ready)
     */
    public List<Order> getReadyOrders() {
        try {
            return orderDAO.getByStatus(4); // 4 = Ready
        } catch (Exception e) {
            System.err.println("❌ Error getting ready orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy orders đã hoàn thành (status = 5: Completed)
     */
    public List<Order> getCompletedOrders() {
        try {
            return orderDAO.getByStatus(5); // 5 = Completed
        } catch (Exception e) {
            System.err.println("❌ Error getting completed orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy order theo ID với items
     */
    public Order getOrderWithItems(int orderId) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order != null) {
                order.setItems(orderItemDAO.getByOrderId(orderId));
            }
            return order;
        } catch (Exception e) {
            System.err.println("❌ Error getting order: " + e.getMessage());
            return null;
        }
    }
    
    // ============ ORDER STATUS UPDATES ============
    
    /**
     * Bắt đầu nấu order (status 2 -> 3)
     */
    public boolean startCooking(int orderId) {
        try {
            Order order = getOrderWithItems(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.getStatus() != 2) {
                System.err.println("❌ Order is not in preparing status");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 3); // 3 = Cooking
            
            if (success) {
                System.out.println("🔥 Started cooking order #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error starting cooking: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đánh dấu order đã sẵn sàng (status 3 -> 4)
     */
    public boolean markAsReady(int orderId) {
        try {
            Order order = getOrderWithItems(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.getStatus() != 3) {
                System.err.println("❌ Order is not cooking");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 4); // 4 = Ready
            
            if (success) {
                System.out.println("✅ Order ready: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error marking as ready: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Hoàn thành order (status 4 -> 5)
     */
    public boolean completeOrder(int orderId) {
        try {
            Order order = getOrderWithItems(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.getStatus() != 4) {
                System.err.println("❌ Order is not ready");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 5); // 5 = Completed
            
            if (success) {
                System.out.println("🎉 Order completed: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error completing order: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Assign chef to order
     */
    public boolean assignChef(int orderId, int chefId) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            order.setAssignedChefId(chefId);
            boolean success = orderDAO.update(order);
            
            if (success) {
                System.out.println("👨‍🍳 Chef assigned to order #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error assigning chef: " + e.getMessage());
            return false;
        }
    }
    
    // ============ CHEF STATISTICS ============
    
    /**
     * Đếm số orders đang chờ
     */
    public int getWaitingOrderCount() {
        return getWaitingOrders().size();
    }
    
    /**
     * Đếm số orders đang nấu
     */
    public int getCookingOrderCount() {
        return getCookingOrders().size();
    }
    
    /**
     * Đếm số orders đã sẵn sàng
     */
    public int getReadyOrderCount() {
        return getReadyOrders().size();
    }
    
    /**
     * Đếm số orders đã hoàn thành hôm nay
     */
    public int getTodayCompletedCount() {
        return getCompletedOrders().size();
    }
    
    /**
     * Lấy orders của chef cụ thể
     */
    public List<Order> getOrdersByChef(int chefId) {
        try {
            List<Order> allOrders = orderDAO.getAll();
            return allOrders.stream()
                .filter(o -> o.getAssignedChefId() == chefId)
                .toList();
        } catch (Exception e) {
            System.err.println("❌ Error getting chef orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Đếm số orders chef đã hoàn thành
     */
    public int getChefCompletedCount(int chefId) {
        try {
            List<Order> chefOrders = getOrdersByChef(chefId);
            return (int) chefOrders.stream()
                .filter(o -> o.getStatus() == 5) // 5 = Completed
                .count();
        } catch (Exception e) {
            System.err.println("❌ Error counting chef completions: " + e.getMessage());
            return 0;
        }
    }
    // ============ UTILITY === =========
    
    /**
     * Lấy thời gian chờ trung bình (estimate)
     */
    public int getAverageWaitTime() {
        int waitingCount = getWaitingOrderCount();
        int cookingCount = getCookingOrderCount();
        
        // Estimate: 5 phút/order
        return (waitingCount + cookingCount) * 5;
    }
    
    /**
     * Kiểm tra order có thể bắt đầu nấu không
     */
    public boolean canStartCooking(int orderId) {
        Order order = getOrderWithItems(orderId);
        return order != null && order.getStatus() == 2;
    }
    
    /**
     * Kiểm tra order có thể đánh dấu ready không
     */
    public boolean canMarkAsReady(int orderId) {
        Order order = getOrderWithItems(orderId);
        return order != null && order.getStatus() == 3;
    }
    
    /**
     * Format order info cho chef
     */
    public String getOrderInfo(int orderId) {
        Order order = getOrderWithItems(orderId);
        if (order == null) return "Order not found";
        
        StringBuilder info = new StringBuilder();
        info.append("Đơn #").append(orderId).append("\n");
        info.append("Khách: ").append(order.getCustomerName()).append("\n");
        info.append("Trạng thái: ").append(order.getStatusName()).append("\n");
        info.append("Món ăn:\n");
        
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                info.append("- ").append(item.getProductName())
                    .append(" x").append(item.getQuantity()).append("\n");
            }
        }
        
        return info.toString();
    }
}