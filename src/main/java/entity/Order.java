package entity;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Order Entity - Đơn hàng (FIXED)
 * Path: Source Packages/entity/Order.java
 * 
 * FIXED: Added missing getFormattedDateTime() method
 */
public class Order {
    
    private int orderId;
    private String customerName;
    private String phoneNumber;
    private double totalAmount;
    private int payMethod;  // 0: Cash, 1: Transfer, 2: Card, 3: MoMo, 4: VNPay
    private int paymentStatus=0;  // 0: Unpaid, 1: Paid, 2: Refunded, 3: Failed
    private int status=0;  // 0: NEW, 1: PREPARING, 2: COOKING, 3: READY, 4: COMPLETED, 5: CANCELLED
    private int assignedChefId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItem> items;
    
    // ============ CONSTRUCTORS ============
    
    public Order() {}
    
    public Order(String customerName, String phoneNumber, double totalAmount, int payMethod) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.totalAmount = totalAmount;
        this.payMethod = payMethod;
        if(payMethod>0) {
            this.paymentStatus=1;
            this.status = 1;  // NEW
        }
    }
    
    public Order(int orderId, String customerName, String phoneNumber, double totalAmount,
                 int payMethod, int paymentStatus, int status, int assignedChefId,
                 Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.totalAmount = totalAmount;
        this.payMethod = payMethod;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.assignedChefId = assignedChefId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getPayMethod() {
        return payMethod;
    }
    
    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }
    
    public int getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public int getAssignedChefId() {
        return assignedChefId;
    }
    
    public void setAssignedChefId(int assignedChefId) {
        this.assignedChefId = assignedChefId;
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
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    // ============ HELPER METHODS ============
    
    /**
     * Get status name (Vietnamese)
     */
    public String getStatusName() {
        return switch (status) {
            case 0 -> "Đơn mới";
            case 1 -> "Đã xác nhận";
            case 2 -> "Đang chuẩn bị";
            case 3 -> "Đang nấu";
            case 4 -> "Sẵn sàng";
            case 5 -> "Hoàn thành";
            case 6 -> "Đã hủy";
            default -> "Unknown";
        };
    }
    
    /**
     * Get payment method name
     */
    public String getPayMethodName() {
        return switch (payMethod) {
            case 0 -> "Tiền mặt";
            case 1 -> "Chuyển khoản";
            case 2 -> "Thẻ tín dụng";
            case 3 -> "MoMo";
            case 4 -> "VNPay";
            default -> "Unknown";
        };
    }
    
    /**
     * Get payment status name
     */
    public String getPaymentStatusName() {
        return switch (paymentStatus) {
            case 0 -> "Chưa thanh toán";
            case 1 -> "Đã thanh toán";
            case 2 -> "Đã hoàn tiền";
            case 3 -> "Thất bại";
            default -> "Unknown";
        };
    }
    
    /**
     * Check if order is paid
     */
    public boolean isPaid() {
        return paymentStatus == 1;
    }
    
    /**
     * Check status helpers
     */
    public boolean isNew() {
        return status == 0;
    }
    
    public boolean isPreparing() {
        return status == 1;
    }
    
    public boolean isCooking() {
        return status == 2;
    }
    
    public boolean isReady() {
        return status == 3;
    }
    
    public boolean isCompleted() {
        return status == 4;
    }
    
    public boolean isCancelled() {
        return status == 5;
    }
    
    // ============ FORMATTING METHODS (FIXED) ============
    
    /**
     * Get formatted time only (HH:mm)
     * Example: "10:30"
     */
    public String getFormattedTime() {
        if (createdAt == null) return "";
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return createdAt.toLocalDateTime().format(formatter);
        } catch (Exception e) {
            // Fallback to substring method
            return createdAt.toString().substring(11, 16);
        }
    }
    
    /**
     * Get formatted date and time (dd/MM/yyyy HH:mm)
     */
    public String getFormattedDateTime() {
        if (createdAt == null) return "";
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return createdAt.toLocalDateTime().format(formatter);
        } catch (Exception e) {
            // Fallback to toString
            return createdAt.toString();
        }
    }
    
    /**
     * Get formatted date and time with seconds (dd/MM/yyyy HH:mm:ss)
     * Example: "29/09/2024 10:30:45"
     */
    public String getFormattedDateTimeFull() {
        if (createdAt == null) return "";
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return createdAt.toLocalDateTime().format(formatter);
        } catch (Exception e) {
            return createdAt.toString();
        }
    }
    
    /**
     * Get formatted date only (dd/MM/yyyy)
     * Example: "29/09/2024"
     */
    public String getFormattedDate() {
        if (createdAt == null) return "";
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return createdAt.toLocalDateTime().format(formatter);
        } catch (Exception e) {
            return createdAt.toString().substring(0, 10);
        }
    }
    
    /**
     * Get time ago (e.g., "5 phút trước", "2 giờ trước")
     */
    public String getTimeAgo() {
        if (createdAt == null) return "";
        
        long diffInMillis = System.currentTimeMillis() - createdAt.getTime();
        long diffInMinutes = diffInMillis / (60 * 1000);
        long diffInHours = diffInMillis / (60 * 60 * 1000);
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
        
        if (diffInMinutes < 1) {
            return "Vừa xong";
        } else if (diffInMinutes < 60) {
            return diffInMinutes + " phút trước";
        } else if (diffInHours < 24) {
            return diffInHours + " giờ trước";
        } else {
            return diffInDays + " ngày trước";
        }
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + getStatusName() +
                ", paymentStatus=" + getPaymentStatusName() +
                ", createdAt=" + getFormattedDateTime() +
                '}';
    }
}
