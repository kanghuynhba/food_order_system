package entity;

/**
 * OrderItem Entity - Chi tiết từng mục trong đơn hàng
 * Path: Source Packages/entity/OrderItem.java
 */
public class OrderItem {
    
    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    
    // ============ CONSTRUCTORS ============
    
    public OrderItem() {}
    
    public OrderItem(int productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }
    
    public OrderItem(int orderId, int productId, String productName, int quantity, 
                     double unitPrice, double subtotal) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    
    public OrderItem(int orderItemId, int orderId, int productId, String productName,
                     int quantity, double unitPrice, double subtotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getOrderItemId() {
        return orderItemId;
    }
    
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    // ============ HELPER METHODS ============
    
    private void calculateSubtotal() {
        this.subtotal = quantity * unitPrice;
    }
    
    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        calculateSubtotal();
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
