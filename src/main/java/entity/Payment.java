package entity;

import java.sql.Timestamp;

/**
 * Payment Entity - Thanh toán
 * Path: Source Packages/entity/Payment.java
 */
public class Payment {
    
    private int paymentId;
    private int orderId;
    private double amount;
    private int method;  // 0: Cash, 1: Transfer
    private int status;  // 0: Failed, 1: Success
    private String notes;
    private Timestamp paidAt;
    
    // ============ CONSTRUCTORS ============
    
    public Payment() {}
    
    public Payment(int orderId, double amount, int method) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = 1;
    }
    
    public Payment(int orderId, double amount, int method, int status, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.notes = notes;
    }
    
    public Payment(int paymentId, int orderId, double amount, int method, 
                   int status, String notes, Timestamp paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.notes = notes;
        this.paidAt = paidAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public int getMethod() {
        return method;
    }
    
    public void setMethod(int method) {
        this.method = method;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Timestamp getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
    
    // ============ HELPER METHODS ============
    
    public String getMethodName() {
        return method == 0 ? "Cash" : "Transfer";
    }
    
    public String getStatusName() {
        return switch (status) {
            case 0 -> "Failed";
            case 1 -> "Success";
            default -> "Unknown";
        };
    }
    
    public boolean isSuccess() {
        return status == 1;
    }
    
    public boolean isFailed() {
        return status == 0;
    }
    
    public boolean isCash() {
        return method == 0;
    }
    
    public boolean isTransfer() {
        return method == 1;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", method='" + getMethodName() + '\'' +
                ", status='" + getStatusName() + '\'' +
                '}';
    }
}