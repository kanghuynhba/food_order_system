package ui.customer;

import entity.CartItem;
import entity.Product;
import ui.components.RoundedButton;
import util.ColorScheme;
import config.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMainFrame extends JFrame {
    
    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private List<CartItem> cartItems;
    
    public CustomerMainFrame() {
        cartItems = new ArrayList<>();
        
        setTitle("FastFood Pro - Đặt hàng");
        setSize(UIConstants.SIZE_CUSTOMER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BG_SECONDARY);
        
        // Header
        JPanel headerPanel = createHeader();
        
        // Content area with menu and cart
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(ColorScheme.BG_SECONDARY);
        
        // Left: Menu panel
        menuPanel = new MenuPanel(this);
        
        // Right: Cart panel
        cartPanel = new CartPanel(this);
        cartPanel.setPreferredSize(new Dimension(380, 0));
        
        contentPanel.add(menuPanel, BorderLayout.CENTER);
        contentPanel.add(cartPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left: Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🍔");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel titleLabel = new JLabel("FastFood Pro");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);
        
        // Right: Dashboard button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        
        RoundedButton dashboardBtn = new RoundedButton("📊 Dashboard", 8);
        dashboardBtn.setBackground(ColorScheme.ACCENT);
        dashboardBtn.setPreferredSize(new Dimension(140, 40));
        dashboardBtn.addActionListener(e -> showDashboard());
        
        rightPanel.add(dashboardBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    // ============ CART OPERATIONS ============
    
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
        
        CheckoutDialog dialog = new CheckoutDialog(this, cartItems, getCartGrandTotal());
        dialog.setVisible(true);
        
        // If checkout successful, clear cart
        if (dialog.isCheckoutSuccessful()) {
            cartItems.clear();
            cartPanel.refreshCart();
        }
    }
    
    // ============ HELPERS ============
    
    private void showDashboard() {
        JOptionPane.showMessageDialog(
            this,
            "Dashboard feature coming soon!",
            "Info",
            JOptionPane.INFORMATION_MESSAGE
        );
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
    
    // ============ MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            CustomerMainFrame frame = new CustomerMainFrame();
            frame.setVisible(true);
        });
    }
}