package service;

import dao.PaymentDAO;
import dao.OrderDAO;
import entity.Payment;
import entity.Order;
import java.util.List;

/**
 * PaymentService - Payment Processing Service
 * Path: Source Packages/service/PaymentService.java
 * 
 * Chức năng:
 * - Xử lý thanh toán
 * - Tạo payment records
 * - Quản lý payment methods
 * - Thống kê revenue
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class PaymentService {
    
    private static PaymentService instance;
    private PaymentDAO paymentDAO;
    private OrderDAO orderDAO;
    private NotificationService notificationService;
    
    // ============ SINGLETON ============
    
    private PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
        this.notificationService = NotificationService.getInstance();
    }
    
    public static PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }
    
    // ============ PAYMENT PROCESSING ============
    
    /**
     * Xử lý thanh toán tiền mặt
     */
    public boolean processCashPayment(int orderId, String notes) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.isPaid()) {
                System.err.println("❌ Order already paid");
                return false;
            }
            
            // Create payment record
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setAmount(order.getTotalAmount());
            payment.setMethod(0); // 0 = Cash
            payment.setStatus(1); // 1 = Success
            payment.setNotes(notes != null ? notes : "Thanh toán tiền mặt");
            
            boolean paymentCreated = paymentDAO.create(payment);
            if (!paymentCreated) {
                System.err.println("❌ Failed to create payment record");
                return false;
            }
            
            // Update order payment status
            boolean orderUpdated = orderDAO.updatePaymentStatus(orderId, 1);
            if (!orderUpdated) {
                System.err.println("❌ Failed to update order payment status");
                return false;
            }
            
            // Send notification
            notificationService.notifyPaymentConfirmed(orderId);
            
            System.out.println("✅ Cash payment processed: Order #" + orderId);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error processing cash payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xử lý thanh toán chuyển khoản
     */
    public boolean processTransferPayment(int orderId, String notes) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (order.isPaid()) {
                System.err.println("❌ Order already paid");
                return false;
            }
            
            // Create payment record
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setAmount(order.getTotalAmount());
            payment.setMethod(1); // 1 = Transfer
            payment.setStatus(1); // 1 = Success
            payment.setNotes(notes != null ? notes : "Thanh toán chuyển khoản");
            
            boolean paymentCreated = paymentDAO.create(payment);
            if (!paymentCreated) {
                System.err.println("❌ Failed to create payment record");
                return false;
            }
            
            // Update order payment status
            boolean orderUpdated = orderDAO.updatePaymentStatus(orderId, 1);
            if (!orderUpdated) {
                System.err.println("❌ Failed to update order payment status");
                return false;
            }
            
            // Send notification
            notificationService.notifyPaymentConfirmed(orderId);
            
            System.out.println("✅ Transfer payment processed: Order #" + orderId);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error processing transfer payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xử lý thanh toán (auto detect method)
     */
    public boolean processPayment(int orderId, int method, String notes) {
        if (method == 0) {
            return processCashPayment(orderId, notes);
        } else if (method == 1) {
            return processTransferPayment(orderId, notes);
        } else {
            System.err.println("❌ Invalid payment method: " + method);
            return false;
        }
    }
    
    /**
     * Xử lý thanh toán thất bại
     */
    public boolean processFailedPayment(int orderId, int method, String reason) {
        try {
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setAmount(0);
            payment.setMethod(method);
            payment.setStatus(0); // 0 = Failed
            payment.setNotes("Failed: " + reason);
            
            boolean success = paymentDAO.create(payment);
            
            if (success) {
                System.out.println("⚠️ Failed payment recorded: Order #" + orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error recording failed payment: " + e.getMessage());
            return false;
        }
    }
    
    // ============ REFUND ============
    
    /**
     * Hoàn tiền
     */
    public boolean refundPayment(int orderId, String reason) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                System.err.println("❌ Order not found");
                return false;
            }
            
            if (!order.isPaid()) {
                System.err.println("❌ Order not paid yet");
                return false;
            }
            
            // Create refund record
            Payment refund = new Payment();
            refund.setOrderId(orderId);
            refund.setAmount(-order.getTotalAmount()); // Negative amount for refund
            refund.setMethod(order.getPayMethod());
            refund.setStatus(1); // Success
            refund.setNotes("Refund: " + reason);
            
            boolean refundCreated = paymentDAO.create(refund);
            if (!refundCreated) {
                return false;
            }
            
            // Update order payment status
            orderDAO.updatePaymentStatus(orderId, 2); // 2 = Refunded
            
            System.out.println("✅ Refund processed: Order #" + orderId);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error processing refund: " + e.getMessage());
            return false;
        }
    }
    
    // ============ QUERY OPERATIONS ============
    
    /**
     * Lấy payment theo ID
     */
    public Payment getPaymentById(int paymentId) {
        try {
            return paymentDAO.getById(paymentId);
        } catch (Exception e) {
            System.err.println("❌ Error getting payment: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy payment theo order ID
     */
    public Payment getPaymentByOrderId(int orderId) {
        try {
            return paymentDAO.getByOrderId(orderId);
        } catch (Exception e) {
            System.err.println("❌ Error getting payment by order: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả payments
     */
    public List<Payment> getAllPayments() {
        try {
            return paymentDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all payments: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy payments theo status
     */
    public List<Payment> getPaymentsByStatus(int status) {
        try {
            return paymentDAO.getByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Error getting payments by status: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy payments theo method
     */
    public List<Payment> getPaymentsByMethod(int method) {
        try {
            return paymentDAO.getByMethod(method);
        } catch (Exception e) {
            System.err.println("❌ Error getting payments by method: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy successful payments
     */
    public List<Payment> getSuccessfulPayments() {
        return getPaymentsByStatus(1);
    }
    
    /**
     * Lấy failed payments
     */
    public List<Payment> getFailedPayments() {
        return getPaymentsByStatus(0);
    }
    
    // ============ STATISTICS ============
    
    /**
     * Tính tổng revenue
     */
    public double getTotalRevenue() {
        try {
            return paymentDAO.getTotalRevenue();
        } catch (Exception e) {
            System.err.println("❌ Error calculating total revenue: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Tính revenue theo method
     */
    public double getRevenueByMethod(int method) {
        try {
            return paymentDAO.getTotalRevenueByMethod(method);
        } catch (Exception e) {
            System.err.println("❌ Error calculating revenue by method: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Tính cash revenue
     */
    public double getCashRevenue() {
        return getRevenueByMethod(0);
    }
    
    /**
     * Tính transfer revenue
     */
    public double getTransferRevenue() {
        return getRevenueByMethod(1);
    }
    
    /**
     * Đếm successful payments
     */
    public int getSuccessfulPaymentCount() {
        return getSuccessfulPayments().size();
    }
    
    /**
     * Đếm failed payments
     */
    public int getFailedPaymentCount() {
        return getFailedPayments().size();
    }
    
    /**
     * Đếm tổng số payments
     */
    public int getTotalPaymentCount() {
        try {
            return paymentDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting payments: " + e.getMessage());
            return 0;
        }
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate payment amount
     */
    public boolean validatePaymentAmount(double amount) {
        return amount > 0;
    }
    
    /**
     * Validate payment method
     */
    public boolean validatePaymentMethod(int method) {
        return method >= 0 && method <= 4; // 0-4: Cash, Transfer, Card, MoMo, VNPay
    }
    
    /**
     * Kiểm tra order có thể thanh toán không
     */
    public boolean canProcessPayment(int orderId) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) return false;
            if (order.isPaid()) return false;
            if (order.isCancelled()) return false;
            
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error checking payment eligibility: " + e.getMessage());
            return false;
        }
    }
    
    // ============ UTILITY ============
    
    /**
     * Format amount
     */
    public String formatAmount(double amount) {
        return String.format("%,.0fđ", amount);
    }
    
    /**
     * Get payment method name
     */
    public String getMethodName(int method) {
        return switch (method) {
            case 0 -> "Tiền mặt";
            case 1 -> "Chuyển khoản";
            case 2 -> "Thẻ tín dụng";
            case 3 -> "MoMo";
            case 4 -> "VNPay";
            default -> "Unknown";
        };
    }
}