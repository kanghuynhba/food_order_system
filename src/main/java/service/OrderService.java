package service;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.CartDAO;
import dao.CartItemDAO;
import entity.Order;
import entity.OrderItem;
import entity.Cart;
import entity.CartItem;
import java.util.List;
import java.util.ArrayList;

/**
 * OrderService - Order Management Service
 * Path: Source Packages/service/OrderService.java
 * 
 * Chức năng:
 * - Tạo order từ cart
 * - Quản lý order lifecycle
 * - Cập nhật trạng thái order
 * - Tính toán tổng tiền
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class OrderService {
    
    private static OrderService instance;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private NotificationService notificationService;
    
    // ============ SINGLETON ============
    
    private OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.cartDAO = new CartDAO();
        this.cartItemDAO = new CartItemDAO();
        this.notificationService = NotificationService.getInstance();
    }
    
    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }
    
    // ============ ORDER CREATION ============
    
    /**
     * Tạo order từ cart
     */
    public Order createOrderFromCart(int customerId, String customerName, 
                                     String phoneNumber, int payMethod) {
        try {
            // Get active cart
            Cart cart = cartDAO.getActiveCart(customerId);
            if (cart == null || cart.isEmpty()) {
                System.err.println("❌ Cart is empty");
                return null;
            }
            
            // Get cart items
            List<CartItem> cartItems = cartItemDAO.getByCartId(cart.getCartId());
            if (cartItems.isEmpty()) {
                System.err.println("❌ No items in cart");
                return null;
            }
            
            // Create order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setPhoneNumber(phoneNumber);
            order.setTotalAmount(cart.getTotalAmount());
            order.setPayMethod(payMethod);
            order.setPaymentStatus(0); // Unpaid
            order.setStatus(0); // New
            
            boolean orderCreated = orderDAO.create(order);
            if (!orderCreated) {
                System.err.println("❌ Failed to create order");
                return null;
            }
            
            // Create order items
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getOrderId());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(cartItem.getUnitPrice());
                orderItem.setSubtotal(cartItem.getSubtotal());
                
                orderItemDAO.create(orderItem);
            }
            
            // Mark cart as checked out
            cartDAO.updateStatus(cart.getCartId(), 1);
            
            // Send notification
            notificationService.notifyNewOrder(order.getOrderId());
            
            System.out.println("✅ Order created: #" + order.getOrderId());
            return order;
            
        } catch (Exception e) {
            System.err.println("❌ Error creating order: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tạo order trực tiếp (không từ cart)
     */
    public Order createOrder(String customerName, String phoneNumber, 
                            List<OrderItem> items, int payMethod) {
        try {
            if (items == null || items.isEmpty()) {
                System.err.println("❌ No items provided");
                return null;
            }
            
            // Calculate total
            double totalAmount = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
            
            // Create order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setPhoneNumber(phoneNumber);
            order.setTotalAmount(totalAmount);
            order.setPayMethod(payMethod);
            order.setPaymentStatus(0);
            order.setStatus(0);
            
            boolean orderCreated = orderDAO.create(order);
            if (!orderCreated) {
                return null;
            }
            
            // Create order items
            for (OrderItem item : items) {
                item.setOrderId(order.getOrderId());
                orderItemDAO.create(item);
            }
            
            notificationService.notifyNewOrder(order.getOrderId());
            
            System.out.println("✅ Order created: #" + order.getOrderId());
            return order;
            
        } catch (Exception e) {
            System.err.println("❌ Error creating order: " + e.getMessage());
            return null;
        }
    }
    
    // ============ ORDER QUERY ============
    
    /**
     * Lấy order theo ID
     */
    public Order getOrderById(int orderId) {
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
    
    /**
     * Lấy tất cả orders
     */
    public List<Order> getAllOrders() {
        try {
            return orderDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all orders: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy orders theo status
     */
    public List<Order> getOrdersByStatus(int status) {
        try {
            return orderDAO.getByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Error getting orders by status: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy orders theo phone number
     */
    public List<Order> getOrdersByPhone(String phoneNumber) {
        try {
            return orderDAO.getByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            System.err.println("❌ Error getting orders by phone: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Tìm kiếm orders
     */
    public List<Order> searchOrders(String keyword) {
        try {
            return orderDAO.search(keyword);
        } catch (Exception e) {
            System.err.println("❌ Error searching orders: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ ORDER STATUS MANAGEMENT ============
    
    /**
     * Cập nhật status order
     */
    public boolean updateOrderStatus(int orderId, int newStatus) {
        try {
            boolean success = orderDAO.updateStatus(orderId, newStatus);
            
            if (success) {
                notificationService.notifyOrderUpdated(orderId);
                
                // Special notifications
                if (newStatus == 4) { // Ready
                    notificationService.notifyOrderReady(orderId);
                }
                
                System.out.println("✅ Order status updated: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xác nhận order
     */
    public boolean confirmOrder(int orderId) {
        return updateOrderStatus(orderId, 1); // 1 = Confirmed
    }
    
    /**
     * Gửi order cho bếp
     */
    public boolean sendToKitchen(int orderId) {
        return updateOrderStatus(orderId, 2); // 2 = Preparing
    }
    
    /**
     * Bắt đầu nấu
     */
    public boolean startCooking(int orderId) {
        return updateOrderStatus(orderId, 3); // 3 = Cooking
    }
    
    /**
     * Đánh dấu sẵn sàng
     */
    public boolean markAsReady(int orderId) {
        return updateOrderStatus(orderId, 4); // 4 = Ready
    }
    
    /**
     * Hoàn thành order
     */
    public boolean completeOrder(int orderId) {
        return updateOrderStatus(orderId, 5); // 5 = Completed
    }
    
    /**
     * Hủy order
     */
    public boolean cancelOrder(int orderId) {
        return updateOrderStatus(orderId, 6); // 6 = Cancelled
    }
    
    // ============ PAYMENT MANAGEMENT ============
    
    /**
     * Cập nhật payment status
     */
    public boolean updatePaymentStatus(int orderId, int paymentStatus) {
        try {
            boolean success = orderDAO.updatePaymentStatus(orderId, paymentStatus);
            
            if (success && paymentStatus == 1) {
                notificationService.notifyPaymentConfirmed(orderId);
                System.out.println("✅ Payment confirmed: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating payment status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đánh dấu đã thanh toán
     */
    public boolean markAsPaid(int orderId) {
        return updatePaymentStatus(orderId, 1);
    }
    
    // ============ ORDER ITEMS ============
    
    /**
     * Lấy items của order
     */
    public List<OrderItem> getOrderItems(int orderId) {
        try {
            return orderItemDAO.getByOrderId(orderId);
        } catch (Exception e) {
            System.err.println("❌ Error getting order items: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Thêm item vào order
     */
    public boolean addItemToOrder(int orderId, OrderItem item) {
        try {
            item.setOrderId(orderId);
            boolean success = orderItemDAO.create(item);
            
            if (success) {
                // Recalculate order total
                recalculateOrderTotal(orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding item to order: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa item khỏi order
     */
    public boolean removeItemFromOrder(int orderItemId) {
        try {
            OrderItem item = orderItemDAO.getById(orderItemId);
            if (item == null) return false;
            
            boolean success = orderItemDAO.delete(orderItemId);
            
            if (success) {
                recalculateOrderTotal(item.getOrderId());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error removing item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tính lại tổng tiền order
     */
    private void recalculateOrderTotal(int orderId) {
        try {
            List<OrderItem> items = getOrderItems(orderId);
            double total = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
            
            Order order = orderDAO.getById(orderId);
            if (order != null) {
                order.setTotalAmount(total);
                orderDAO.update(order);
            }
        } catch (Exception e) {
            System.err.println("❌ Error recalculating total: " + e.getMessage());
        }
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm orders theo status
     */
    public int countOrdersByStatus(int status) {
        return getOrdersByStatus(status).size();
    }
    
    /**
     * Tính tổng revenue
     */
    public double getTotalRevenue() {
        try {
            List<Order> orders = getAllOrders();
            return orders.stream()
                .filter(o -> o.isPaid() && !o.isCancelled())
                .mapToDouble(Order::getTotalAmount)
                .sum();
        } catch (Exception e) {
            System.err.println("❌ Error calculating revenue: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Đếm tổng số orders
     */
    public int getTotalOrderCount() {
        try {
            return orderDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting orders: " + e.getMessage());
            return 0;
        }
    }
}