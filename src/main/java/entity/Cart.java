package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart Entity - Giỏ hàng
 * Path: Source Packages/entity/Cart.java
 * 
 * Status:
 * 0: Active - Giỏ hàng đang hoạt động
 * 1: Checked out - Đã thanh toán
 * 2: Abandoned - Đã bỏ qua
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class Cart {
    
    private int cartId;
    private int customerId;
    private double totalAmount;
    private int status;  // 0: Active, 1: Checked out, 2: Abandoned
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CartItem> items;  // Danh sách items trong giỏ hàng
    
    // ============ CONSTRUCTORS ============
    
    public Cart() {
        this.items = new ArrayList<>();
        this.status = 0;  // Active by default
        this.totalAmount = 0.0;
    }
    
    public Cart(int customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.status = 0;
        this.totalAmount = 0.0;
    }
    
    public Cart(int customerId, double totalAmount, int status) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = new ArrayList<>();
    }
    
    public Cart(int cartId, int customerId, double totalAmount, int status,
                Timestamp createdAt, Timestamp updatedAt) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = new ArrayList<>();
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
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
    
    public List<CartItem> getItems() {
        return items;
    }
    
    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotalAmount();
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Thêm item vào giỏ hàng
     * Nếu sản phẩm đã có thì tăng số lượng
     */
    public void addItem(CartItem newItem) {
        if (items == null) {
            items = new ArrayList<>();
        }
        
        // Kiểm tra xem product đã có trong cart chưa
        boolean found = false;
        for (CartItem item : items) {
            if (item.getProductId() == newItem.getProductId()) {
                // Nếu đã có thì cộng thêm số lượng
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                item.calculateSubtotal();
                found = true;
                break;
            }
        }
        
        // Nếu chưa có thì thêm mới
        if (!found) {
            items.add(newItem);
        }
        
        calculateTotalAmount();
    }
    
    /**
     * Xóa item khỏi giỏ hàng
     */
    public boolean removeItem(int productId) {
        if (items == null) return false;
        
        boolean removed = items.removeIf(item -> item.getProductId() == productId);
        
        if (removed) {
            calculateTotalAmount();
        }
        
        return removed;
    }
    
    /**
     * Xóa item theo cart_item_id
     */
    public boolean removeItemById(int cartItemId) {
        if (items == null) return false;
        
        boolean removed = items.removeIf(item -> item.getCartItemId() == cartItemId);
        
        if (removed) {
            calculateTotalAmount();
        }
        
        return removed;
    }
    
    /**
     * Cập nhật số lượng của item
     */
    public boolean updateItemQuantity(int productId, int newQuantity) {
        if (items == null) return false;
        
        for (CartItem item : items) {
            if (item.getProductId() == productId) {
                if (newQuantity <= 0) {
                    // Nếu quantity <= 0 thì xóa item
                    return removeItem(productId);
                } else {
                    item.setQuantity(newQuantity);
                    item.calculateSubtotal();
                    calculateTotalAmount();
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Lấy item theo product_id
     */
    public CartItem getItemByProductId(int productId) {
        if (items == null) return null;
        
        for (CartItem item : items) {
            if (item.getProductId() == productId) {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Kiểm tra xem product đã có trong cart chưa
     */
    public boolean hasProduct(int productId) {
        return getItemByProductId(productId) != null;
    }
    
    /**
     * Tính lại tổng tiền
     */
    public void calculateTotalAmount() {
        if (items == null || items.isEmpty()) {
            totalAmount = 0.0;
            return;
        }
        
        totalAmount = items.stream()
                           .mapToDouble(CartItem::getSubtotal)
                           .sum();
    }
    
    /**
     * Xóa tất cả items trong giỏ hàng
     */
    public void clearCart() {
        if (items != null) {
            items.clear();
        }
        totalAmount = 0.0;
    }
    
    /**
     * Lấy số lượng items trong giỏ hàng (số dòng)
     */
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    /**
     * Lấy tổng số lượng sản phẩm (tổng quantity)
     */
    public int getTotalQuantity() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        
        return items.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
    }
    
    /**
     * Kiểm tra giỏ hàng có rỗng không
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
    
    // ============ STATUS HELPERS ============
    
    public String getStatusName() {
        return switch (status) {
            case 0 -> "Active";
            case 1 -> "Checked Out";
            case 2 -> "Abandoned";
            default -> "Unknown";
        };
    }
    
    public boolean isActive() {
        return status == 0;
    }
    
    public boolean isCheckedOut() {
        return status == 1;
    }
    
    public boolean isAbandoned() {
        return status == 2;
    }
    
    public void markAsCheckedOut() {
        this.status = 1;
    }
    
    public void markAsAbandoned() {
        this.status = 2;
    }
    
    public void markAsActive() {
        this.status = 0;
    }
    
    // ============ VALIDATION ============
    
    /**
     * Kiểm tra giỏ hàng có hợp lệ để checkout không
     */
    public boolean isValidForCheckout() {
        return !isEmpty() && isActive() && totalAmount > 0;
    }
    
    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", status=" + getStatusName() +
                ", itemCount=" + getItemCount() +
                ", totalQuantity=" + getTotalQuantity() +
                '}';
    }
}