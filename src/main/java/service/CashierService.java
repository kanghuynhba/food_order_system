package service;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.PaymentDAO;
import entity.Order;
import entity.OrderItem;
import entity.Payment;
import java.util.List;

/**
 * CashierService - Cashier Operations Service
 * Path: Source Packages/service/CashierService.java
 * 
 * Chức năng:
 * - Xem danh sách orders
 * - Xác nhận thanh toán
 * - In hóa đơn
 * - Gửi order cho bếp
 * - Quản lý orders theo trạng thái
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CashierService {
    
    private static CashierService instance;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private PaymentDAO paymentDAO;
    
    // ============ SINGLETON ============
    
    private CashierService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.paymentDAO = new PaymentDAO();
    }
    
    public static CashierService getInstance() {
        if (instance == null) {
            instance = new CashierService();
        }
        return instance;
    }
    
    // ============ ORDER MANAGEMENT ============
    
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
     * Lấy pending orders (status = 0)
     */
    public List<Order> getPendingOrders() {
        return getOrdersByStatus(0);
    }
    
    /**
     * Lấy confirmed orders (status = 1)
     */
    public List<Order> getConfirmedOrders() {
        return getOrdersByStatus(1);
    }
    
    /**
     * Lấy order theo ID
     */
    public Order getOrderById(int orderId) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order != null) {
                // Load order items
                order.setItems(orderItemDAO.getByOrderId(orderId));
            }
            return order;
        } catch (Exception e) {
            System.err.println("❌ Error getting order: " + e.getMessage());
            return null;
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
    
    // ============ PAYMENT OPERATIONS ============
    
    /**
     * Xác nhận thanh toán tiền mặt
     */
    public boolean confirmCashPayment(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.isPaid()) {
                System.err.println("❌ Order already paid");
                return false;
            }
            
            // Update payment status
            boolean paymentSuccess = orderDAO.updatePaymentStatus(orderId, 1); // 1 = Paid
            
            if (paymentSuccess) {
                // Create payment record
                Payment payment = new Payment(orderId, order.getTotalAmount(), 0, 1, "Thanh toán tiền mặt");
                paymentDAO.create(payment);
                
                System.out.println("✅ Cash payment confirmed for order #" + orderId);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error confirming cash payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xác nhận thanh toán chuyển khoản
     */
    public boolean confirmTransferPayment(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.isPaid()) {
                System.err.println("❌ Order already paid");
                return false;
            }
            
            // Update payment status
            boolean paymentSuccess = orderDAO.updatePaymentStatus(orderId, 1); // 1 = Paid
            
            if (paymentSuccess) {
                // Create payment record
                Payment payment = new Payment(orderId, order.getTotalAmount(), 1, 1, "Thanh toán chuyển khoản");
                paymentDAO.create(payment);
                
                System.out.println("✅ Transfer payment confirmed for order #" + orderId);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error confirming transfer payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xác nhận thanh toán (auto detect method)
     */
    public boolean confirmPayment(int orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return false;
        
        if (order.getPayMethod() == 0) {
            return confirmCashPayment(orderId);
        } else {
            return confirmTransferPayment(orderId);
        }
    }
    
    // ============ ORDER STATUS MANAGEMENT ============
    
    /**
     * Xác nhận order (status 0 -> 1)
     */
    public boolean confirmOrder(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.getStatus() != 0) {
                System.err.println("❌ Order is not pending");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 1); // 1 = Confirmed
            
            if (success) {
                System.out.println("✅ Order confirmed: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error confirming order: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gửi order cho bếp (status 1 -> 2)
     */
    public boolean sendToKitchen(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (!order.isPaid()) {
                System.err.println("❌ Order must be paid before sending to kitchen");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 2); // 2 = Preparing
            
            if (success) {
                System.out.println("✅ Order sent to kitchen: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error sending to kitchen: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Hủy order
     */
    public boolean cancelOrder(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.getStatus() >= 3) {
                System.err.println("❌ Cannot cancel order in cooking/ready/completed status");
                return false;
            }
            
            boolean success = orderDAO.updateStatus(orderId, 6); // 6 = Cancelled
            
            if (success) {
                System.out.println("✅ Order cancelled: #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error cancelling order: " + e.getMessage());
            return false;
        }
    }
    
    // ============ INVOICE/RECEIPT ============
    
    /**
     * Tạo invoice text
     */
    public String generateInvoice(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                return "Order not found";
            }
            
            StringBuilder invoice = new StringBuilder();
            invoice.append("========================================\n");
            invoice.append("         HÓA ĐƠN BÁN HÀNG\n");
            invoice.append("========================================\n\n");
            invoice.append("Đơn hàng: #").append(orderId).append("\n");
            invoice.append("Khách hàng: ").append(order.getCustomerName()).append("\n");
            if (order.getPhoneNumber() != null) {
                invoice.append("Điện thoại: ").append(order.getPhoneNumber()).append("\n");
            }
            invoice.append("Thời gian: ").append(order.getCreatedAt()).append("\n");
            invoice.append("----------------------------------------\n\n");
            invoice.append("CHI TIẾT ĐƠN HÀNG:\n\n");
            
            List<OrderItem> items = order.getItems();
            if (items != null) {
                for (OrderItem item : items) {
                    invoice.append(String.format("%-20s x%d\n", item.getProductName(), item.getQuantity()));
                    invoice.append(String.format("  %,.0fđ x %d = %,.0fđ\n\n", 
                        item.getUnitPrice(), item.getQuantity(), item.getSubtotal()));
                }
            }
            
            invoice.append("----------------------------------------\n");
            invoice.append(String.format("TỔNG CỘNG: %,.0fđ\n", order.getTotalAmount()));
            invoice.append("Thanh toán: ").append(order.getPayMethodName()).append("\n");
            invoice.append("Trạng thái: ").append(order.getPaymentStatusName()).append("\n");
            invoice.append("========================================\n");
            invoice.append("     CẢM ƠN QUÝ KHÁCH!\n");
            invoice.append("========================================\n");
            
            return invoice.toString();
            
        } catch (Exception e) {
            System.err.println("❌ Error generating invoice: " + e.getMessage());
            return "Error generating invoice";
        }
    }
    
    /**
     * In hóa đơn (simulate)
     */
    public boolean printInvoice(int orderId) {
        try {
            String invoice = generateInvoice(orderId);
            System.out.println("\n📄 PRINTING INVOICE:\n");
            System.out.println(invoice);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error printing invoice: " + e.getMessage());
            return false;
        }
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm số orders theo payment status
     */
    public int countOrdersByPaymentStatus(int paymentStatus) {
        try {
            return orderDAO.getByPaymentStatus(paymentStatus).size();
        } catch (Exception e) {
            System.err.println("❌ Error counting orders: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Tính tổng revenue hôm nay
     */
    public double getTodayRevenue() {
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
     * Đếm số orders hôm nay
     */
    public int getTodayOrderCount() {
        try {
            return getAllOrders().size();
        } catch (Exception e) {
            System.err.println("❌ Error counting orders: " + e.getMessage());
            return 0;
        }
    }
}