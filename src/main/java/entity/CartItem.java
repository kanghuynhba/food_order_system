package entity;

/**
 * CartItem Entity - Item trong giỏ hàng
 * Path: Source Packages/entity/CartItem.java
 * 
 * Represents a single product in the shopping cart
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CartItem {
    
    private int cartItemId;
    private int cartId;
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subtotal;
    private String imageUrl;
    private String notes;  // Ghi chú đặc biệt (VD: "Ít cay", "Không hành")
    
    // ============ CONSTRUCTORS ============
    
    public CartItem() {
        this.quantity = 1;
        this.subtotal = 0.0;
    }
    
    public CartItem(int productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public CartItem(int productId, String productName, double unitPrice, 
                    int quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        calculateSubtotal();
    }
    
    public CartItem(int cartItemId, int cartId, int productId, String productName,
                    double unitPrice, int quantity, double subtotal, 
                    String imageUrl, String notes) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.imageUrl = imageUrl;
        this.notes = notes;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getCartItemId() {
        return cartItemId;
    }
    
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
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
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Tính lại subtotal = unitPrice * quantity
     */
    public void calculateSubtotal() {
        this.subtotal = this.unitPrice * this.quantity;
    }
    
    /**
     * Tăng số lượng
     */
    public void increaseQuantity() {
        this.quantity++;
        calculateSubtotal();
    }
    
    /**
     * Tăng số lượng với amount
     */
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
            calculateSubtotal();
        }
    }
    
    /**
     * Giảm số lượng
     */
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            calculateSubtotal();
        }
    }
    
    /**
     * Giảm số lượng với amount
     */
    public void decreaseQuantity(int amount) {
        if (amount > 0 && this.quantity > amount) {
            this.quantity -= amount;
            calculateSubtotal();
        } else if (amount >= this.quantity) {
            this.quantity = 1;
            calculateSubtotal();
        }
    }
    
    /**
     * Cập nhật số lượng mới
     */
    public void updateQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
            calculateSubtotal();
        }
    }
    
    /**
     * Thêm notes/ghi chú
     */
    public void addNote(String note) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = note;
        } else {
            this.notes += "; " + note;
        }
    }
    
    /**
     * Xóa notes
     */
    public void clearNotes() {
        this.notes = null;
    }
    
    /**
     * Kiểm tra có notes không
     */
    public boolean hasNotes() {
        return notes != null && !notes.isEmpty();
    }
    
    // ============ CONVERSION METHODS ============
    
    /**
     * Convert CartItem sang OrderItem
     * Dùng khi checkout để tạo order
     */
    public OrderItem toOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(this.productId);
        orderItem.setProductName(this.productName);
        orderItem.setQuantity(this.quantity);
        orderItem.setUnitPrice(this.unitPrice);
        orderItem.setSubtotal(this.subtotal);
        return orderItem;
    }
    
    /**
     * Clone CartItem
     */
    public CartItem clone() {
        return new CartItem(
            this.cartItemId,
            this.cartId,
            this.productId,
            this.productName,
            this.unitPrice,
            this.quantity,
            this.subtotal,
            this.imageUrl,
            this.notes
        );
    }
    
    // ============ VALIDATION ============
    
    /**
     * Kiểm tra item có hợp lệ không
     */
    public boolean isValid() {
        return productId > 0 
            && productName != null && !productName.isEmpty()
            && unitPrice > 0 
            && quantity > 0;
    }
    
    // ============ DISPLAY HELPERS ============
    
    /**
     * Format giá tiền
     */
    public String getFormattedUnitPrice() {
        return String.format("%,.0fđ", unitPrice);
    }
    
    /**
     * Format subtotal
     */
    public String getFormattedSubtotal() {
        return String.format("%,.0fđ", subtotal);
    }
    
    /**
     * Display name với quantity
     */
    public String getDisplayName() {
        return quantity + "x " + productName;
    }
    
    /**
     * Display full info
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(productName);
        sb.append(" x").append(quantity);
        sb.append(" = ").append(getFormattedSubtotal());
        
        if (hasNotes()) {
            sb.append(" (").append(notes).append(")");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                (hasNotes() ? ", notes='" + notes + '\'' : "") +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CartItem cartItem = (CartItem) o;
        return productId == cartItem.productId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}