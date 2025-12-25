package listener;

import entity.Product;
import entity.CartItem;
import java.util.List;

// Save as MenuListener.java
public interface MenuListener {
    // When a user clicks a product in the menu
    void addToCart(Product p, int quality);
    void removeFromCart(CartItem item);
    void updateCartItemQuantity(CartItem item, int newQuantity);
    void clearCart();
    List<CartItem> getCartItems();     
    double getCartTotal();
    double getCartTax();  
    double getCartDiscount();     
    double getCartGrandTotal();     
    // ============ CHECKOUT ============
    void proceedToCheckout();
}
