package entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Order Entity - Đơn hàng (ENHANCED)
 * Path: Source Packages/entity/Order.java
 */
public class Order {
    
    private int orderId;
    private String customerName;
    private String phoneNumber;
    private double totalAmount;
    private int payMethod;  // 0: Cash, 1: Transfer
    private int paymentStatus;  // 0: Unpaid, 1: Paid
    private int status;  // 1: Pending, 2: Sent to Kitchen, 3: Cooking, 4: Ready, 5: Delivered, 6: Cancelled
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
        this.paymentStatus = 0;
        this.status = 1;
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
    
    public String getStatusName() {
        return switch (status) {
            case 1 -> "Pending (Cashier)";
            case 2 -> "Sent to Kitchen";
            case 3 -> "Cooking";
            case 4 -> "Ready";
            case 5 -> "Delivered";
            case 6 -> "Cancelled";
            default -> "Unknown";
        };
    }
    
    public String getPayMethodName() {
        return payMethod == 0 ? "Cash" : "Transfer";
    }
    
    public String getPaymentStatusName() {
        return paymentStatus == 1 ? "Paid" : "Unpaid";
    }
    
    public boolean isPaid() {
        return paymentStatus == 1;
    }
    
    public boolean isPending() {
        return status == 1;
    }
    
    public boolean isCooking() {
        return status == 3;
    }
    
    public boolean isReady() {
        return status == 4;
    }
    
    public boolean isDelivered() {
        return status == 5;
    }
    
    public boolean isCancelled() {
        return status == 6;
    }
    
    public String getFormattedTime() {
        if (createdAt == null) return "";
        return createdAt.toString().substring(11, 16);
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + getStatusName() +
                ", paymentStatus=" + getPaymentStatusName() +
                '}';
    }
}