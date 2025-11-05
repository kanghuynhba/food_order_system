package service;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.ProductDAO;
import entity.Cart;
import entity.CartItem;
import entity.Product;
import java.util.List;

/**
 * CartService - Shopping Cart Business Logic
 * Path: Source Packages/service/CartService.java
 * 
 * Chức năng:
 * - Quản lý giỏ hàng (thêm, xóa, sửa items)
 * - Tính toán tổng tiền
 * - Validate giỏ hàng trước checkout
 * - Clear cart sau checkout
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CartService {
    
    private static CartService instance;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;
    
    // ============ SINGLETON ============
    
    private CartService() {
        this.cartDAO = new CartDAO();
        this.cartItemDAO = new CartItemDAO();
        this.productDAO = new ProductDAO();
    }
    
    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }
    
    // ============ CART MANAGEMENT ============
    
    /**
     * Lấy hoặc tạo active cart cho customer
     */
    public Cart getOrCreateCart(int customerId) {
        try {
            return cartDAO.getOrCreateActiveCart(customerId);
        } catch (Exception e) {
            System.err.println("❌ Error getting/creating cart: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy active cart của customer
     */
    public Cart getActiveCart(int customerId) {
        try {
            return cartDAO.getActiveCart(customerId);
        } catch (Exception e) {
            System.err.println("❌ Error getting active cart: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy cart theo ID
     */
    public Cart getCartById(int cartId) {
        try {
            return cartDAO.getById(cartId);
        } catch (Exception e) {
            System.err.println("❌ Error getting cart: " + e.getMessage());
            return null;
        }
    }
    
    // ============ ITEM MANAGEMENT ============
    
    /**
     * Thêm product vào cart
     */
    public boolean addToCart(int customerId, int productId, int quantity) {
        try {
            // Validate product
            Product product = productDAO.getById(productId);
            if (product == null) {
                System.err.println("❌ Product not found: " + productId);
                return false;
            }
            
            if (!product.isAvailable()) {
                System.err.println("❌ Product unavailable: " + product.getName());
                return false;
            }
            
            if (quantity <= 0) {
                System.err.println("❌ Invalid quantity: " + quantity);
                return false;
            }
            
            // Get or create cart
            Cart cart = getOrCreateCart(customerId);
            if (cart == null) {
                System.err.println("❌ Cannot get/create cart");
                return false;
            }
            
            // Create cart item
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setProductId(productId);
            cartItem.setProductName(product.getName());
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setQuantity(quantity);
            cartItem.setImageUrl(product.getImageUrl());
            cartItem.calculateSubtotal();
            
            // Add or update item
            boolean success = cartItemDAO.addOrUpdate(cartItem);
            
            if (success) {
                // Recalculate cart total
                cartDAO.recalculateTotalAmount(cart.getCartId());
                System.out.println("✅ Added to cart: " + product.getName() + " x" + quantity);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thêm product vào cart với notes
     */
    public boolean addToCart(int customerId, int productId, int quantity, String notes) {
        boolean success = addToCart(customerId, productId, quantity);
        
        if (success && notes != null && !notes.trim().isEmpty()) {
            // Update notes
            Cart cart = getActiveCart(customerId);
            if (cart != null) {
                CartItem item = cartItemDAO.getByCartAndProduct(cart.getCartId(), productId);
                if (item != null) {
                    cartItemDAO.updateNotes(item.getCartItemId(), notes);
                }
            }
        }
        
        return success;
    }
    
    /**
     * Cập nhật số lượng item trong cart
     */
    public boolean updateQuantity(int customerId, int productId, int newQuantity) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                System.err.println("❌ No active cart found");
                return false;
            }
            
            if (newQuantity <= 0) {
                // Remove item if quantity <= 0
                return removeFromCart(customerId, productId);
            }
            
            boolean success = cartItemDAO.updateQuantityByCartAndProduct(
                cart.getCartId(), productId, newQuantity
            );
            
            if (success) {
                cartDAO.recalculateTotalAmount(cart.getCartId());
                System.out.println("✅ Updated quantity: " + newQuantity);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating quantity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa item khỏi cart
     */
    public boolean removeFromCart(int customerId, int productId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return false;
            }
            
            boolean success = cartItemDAO.deleteByCartAndProduct(cart.getCartId(), productId);
            
            if (success) {
                cartDAO.recalculateTotalAmount(cart.getCartId());
                System.out.println("✅ Removed from cart");
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error removing from cart: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa tất cả items trong cart
     */
    public boolean clearCart(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return false;
            }
            
            boolean success = cartDAO.clearCart(cart.getCartId());
            
            if (success) {
                System.out.println("✅ Cart cleared");
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error clearing cart: " + e.getMessage());
            return false;
        }
    }
    
    // ============ CART INFORMATION ============
    
    /**
     * Lấy danh sách items trong cart
     */
    public List<CartItem> getCartItems(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return List.of();
            }
            return cartItemDAO.getByCartId(cart.getCartId());
            
        } catch (Exception e) {
            System.err.println("❌ Error getting cart items: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Đếm số lượng items trong cart (số dòng)
     */
    public int getItemCount(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return 0;
            }
            return cartItemDAO.countByCartId(cart.getCartId());
            
        } catch (Exception e) {
            System.err.println("❌ Error counting items: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Tính tổng số lượng sản phẩm trong cart
     */
    public int getTotalQuantity(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return 0;
            }
            return cartItemDAO.getTotalQuantity(cart.getCartId());
            
        } catch (Exception e) {
            System.err.println("❌ Error getting total quantity: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Tính tổng tiền trong cart
     */
    public double getCartTotal(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return 0.0;
            }
            return cartItemDAO.calculateCartTotal(cart.getCartId());
            
        } catch (Exception e) {
            System.err.println("❌ Error calculating total: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Kiểm tra cart có rỗng không
     */
    public boolean isCartEmpty(int customerId) {
        return getItemCount(customerId) == 0;
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate cart trước khi checkout
     */
    public boolean validateCart(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            
            if (cart == null) {
                System.err.println("❌ Cart not found");
                return false;
            }
            
            if (!cart.isActive()) {
                System.err.println("❌ Cart is not active");
                return false;
            }
            
            List<CartItem> items = getCartItems(customerId);
            
            if (items.isEmpty()) {
                System.err.println("❌ Cart is empty");
                return false;
            }
            
            // Validate each item
            for (CartItem item : items) {
                Product product = productDAO.getById(item.getProductId());
                
                if (product == null) {
                    System.err.println("❌ Product not found: " + item.getProductId());
                    return false;
                }
                
                if (!product.isAvailable()) {
                    System.err.println("❌ Product unavailable: " + product.getName());
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error validating cart: " + e.getMessage());
            return false;
        }
    }
    
    // ============ CHECKOUT OPERATIONS ============
    
    /**
     * Đánh dấu cart là checked out
     */
    public boolean markAsCheckedOut(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return false;
            }
            
            return cartDAO.updateStatus(cart.getCartId(), 1); // 1 = Checked out
            
        } catch (Exception e) {
            System.err.println("❌ Error marking as checked out: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đánh dấu cart là abandoned
     */
    public boolean markAsAbandoned(int customerId) {
        try {
            Cart cart = getActiveCart(customerId);
            if (cart == null) {
                return false;
            }
            
            return cartDAO.updateStatus(cart.getCartId(), 2); // 2 = Abandoned
            
        } catch (Exception e) {
            System.err.println("❌ Error marking as abandoned: " + e.getMessage());
            return false;
        }
    }
    
    // ============ UTILITY ============
    
    /**
     * Format total amount
     */
    public String getFormattedTotal(int customerId) {
        double total = getCartTotal(customerId);
        return String.format("%,.0fđ", total);
    }
    
    /**
     * Get cart summary
     */
    public String getCartSummary(int customerId) {
        int itemCount = getItemCount(customerId);
        double total = getCartTotal(customerId);
        return String.format("%d items - %,.0fđ", itemCount, total);
    }
}