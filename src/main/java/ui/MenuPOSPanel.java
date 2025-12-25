package ui.cashier;

import listener.MenuListener;
import entity.CartItem;
import entity.Product;
import ui.components.RoundedButton;
import util.ColorScheme;
import config.UIConstants;
import ui.components.MenuPanel;
import ui.components.CartPanel;
import ui.components.CheckoutDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List; 
import javax.swing.border.EmptyBorder;
/**
//  * MenuPOSPanel - POS Menu with cart sidebar (Image 9 style)
//  * Path: Source Packages/ui/cashier/MenuPOSPanel.java
//  * 
//  * Features:
//  * - Product grid with categories
//  * - Right sidebar cart
//  * - Add/remove items
//  * - Place order
//  * 
//  * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
//  */
public class MenuPOSPanel extends JPanel implements MenuListener {
    
    // UI Components
    private CashierMainFrame mainFrame;
    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private List<CartItem> cartItems;

    
    // ============ CONSTRUCTOR ============
    
    public MenuPOSPanel(CashierMainFrame mainFrame) {
        this.mainFrame=mainFrame;
        cartItems = new ArrayList<>();
        menuPanel = new MenuPanel(this);
        cartPanel = new CartPanel(this);
        
        initComponents();
   }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        
        // Left: Products + Categories
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(new Color(250, 250, 250));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 0));
        
        leftPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Right: Cart sidebar
        JPanel rightPanel = cartPanel;
        
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
 
    @Override
    public void addToCart(Product product, int quantity) {
        // Check if product already in cart
        for (CartItem item : cartItems) {
            if (item.getProductId() == product.getProductId()) {
                item.increaseQuantity(quantity);
                cartPanel.refreshCart();
                showNotification("Đã cập nhật số lượng!");
                return;
            }
        }
        
        // Add new item
        CartItem newItem = new CartItem(
            product.getProductId(),
            product.getName(),
            product.getPrice(),
            quantity,
            product.getImageUrl()
        );
        
        cartItems.add(newItem);
        cartPanel.refreshCart();
        showNotification("✅ Đã thêm vào giỏ hàng!");
    }
    
    public void removeFromCart(CartItem item) {
        cartItems.remove(item);
        cartPanel.refreshCart();
        showNotification("Đã xóa khỏi giỏ hàng");
    }
    
    public void updateCartItemQuantity(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(item);
        } else {
            item.updateQuantity(newQuantity);
            cartPanel.refreshCart();
        }
    }
    
    public void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            cartPanel.refreshCart();
            showNotification("Đã xóa giỏ hàng");
        }
    }
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public double getCartTotal() {
        return cartItems.stream()
            .mapToDouble(CartItem::getSubtotal)
            .sum();
    }
    
    public double getCartTax() {
        return getCartTotal() * 0.1; // 10% VAT
    }
    
    public double getCartDiscount() {
        return getCartTotal() >= 200000 ? 20000 : 0; // Discount if > 200k
    }
    
    public double getCartGrandTotal() {
        return getCartTotal() + getCartTax() - getCartDiscount();
    }
    
    // ============ CHECKOUT ============
    
    public void proceedToCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Giỏ hàng trống! Vui lòng thêm sản phẩm.",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        CheckoutDialog dialog = new CheckoutDialog(mainFrame, cartItems, getCartGrandTotal());
        dialog.setVisible(true);
        
        // If checkout successful, clear cart
        if (dialog.isCheckoutSuccessful()) {
            cartItems.clear();
            cartPanel.refreshCart();
        }
    }
    
    private void showNotification(String message) {
        // Simple toast notification
        JLabel notification = new JLabel(message);
        notification.setFont(UIConstants.FONT_BODY_BOLD);
        notification.setForeground(Color.WHITE);
        notification.setBackground(ColorScheme.SUCCESS);
        notification.setOpaque(true);
        notification.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JOptionPane pane = new JOptionPane(notification, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(this);
        
        Timer timer = new Timer(1500, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }
    

}
