package entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Receipt Entity - Hóa đơn
 * Path: Source Packages/entity/Receipt.java
 */
public class Receipt {
    
    private int receiptId;
    private int orderId;
    private String customerName;
    private String phoneNumber;
    private List<OrderItem> items;
    private double subtotal;
    private double tax;
    private double total;
    private int paymentMethod;  // 0: Cash, 1: Transfer
    private String notes;
    private Timestamp createdAt;
    
    // ============ CONSTRUCTORS ============
    
    public Receipt() {}
    
    public Receipt(int orderId, String customerName, List<OrderItem> items, double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.total = total;
        calculateSubtotal();
    }
    
    public Receipt(int orderId, String customerName, String phoneNumber, 
                   List<OrderItem> items, double subtotal, double tax, double total,
                   int paymentMethod) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.paymentMethod = paymentMethod;
    }
    
    public Receipt(int receiptId, int orderId, String customerName, String phoneNumber,
                   List<OrderItem> items, double subtotal, double tax, double total,
                   int paymentMethod, String notes, Timestamp createdAt) {
        this.receiptId = receiptId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.createdAt = createdAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getReceiptId() {
        return receiptId;
    }
    
    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }
    
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
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getTax() {
        return tax;
    }
    
    public void setTax(double tax) {
        this.tax = tax;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public int getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // ============ HELPER METHODS ============
    
    private void calculateSubtotal() {
        if (items == null || items.isEmpty()) {
            subtotal = 0;
            return;
        }
        subtotal = items.stream()
                        .mapToDouble(OrderItem::getSubtotal)
                        .sum();
    }
    
    public void calculateTotal() {
        calculateSubtotal();
        total = subtotal + tax;
    }
    
    public String getPaymentMethodName() {
        return paymentMethod == 0 ? "Cash" : "Transfer";
    }
    
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    public String generateReceiptText() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== HÓA ĐƠN ==========\n");
        sb.append("Đơn hàng #").append(orderId).append("\n");
        sb.append("Khách hàng: ").append(customerName).append("\n");
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            sb.append("Điện thoại: ").append(phoneNumber).append("\n");
        }
        sb.append("-----------------------------\n");
        sb.append("CHI TIẾT ĐƠN HÀNG:\n");
        
        if (items != null) {
            for (OrderItem item : items) {
                sb.append("- ").append(item.getProductName())
                  .append(" x").append(item.getQuantity())
                  .append(" = ").append(item.getSubtotal()).append("đ\n");
            }
        }
        
        sb.append("-----------------------------\n");
        sb.append("Tổng cộng: ").append(subtotal).append("đ\n");
        sb.append("Thuế (VAT): ").append(tax).append("đ\n");
        sb.append("TỔNG TIỀN: ").append(total).append("đ\n");
        sb.append("Phương thức: ").append(getPaymentMethodName()).append("\n");
        sb.append("========== CẢM ƠN ==========\n");
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId=" + receiptId +
                ", orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", total=" + total +
                ", paymentMethod='" + getPaymentMethodName() + '\'' +
                '}';
    }
}