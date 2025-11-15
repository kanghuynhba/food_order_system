package ui.cashier;

import dao.ProductDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import entity.Product;
import entity.Order;
import entity.OrderItem;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import config.AppConfig;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MenuPOSPanel - POS Menu with cart sidebar (Image 9 style)
 * Path: Source Packages/ui/cashier/MenuPOSPanel.java
 * 
 * Features:
 * - Product grid with categories
 * - Right sidebar cart
 * - Add/remove items
 * - Place order
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class MenuPOSPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private CashierMainFrame mainFrame;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    
    // UI Components
    private JPanel productsContainer;
    private JPanel cartPanel;
    private JLabel subtotalLabel;
    private JLabel taxLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JComboBox<String> customerCombo;
    
    // Data
    private List<Product> products;
    private Map<Integer, CartItem> cart; // productId -> CartItem
    private String selectedCategory = "All";
    
    // ============ CONSTRUCTOR ============
    
    public MenuPOSPanel(CashierMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.cart = new HashMap<>();
        
        initComponents();
        loadProducts();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        
        // Left: Products + Categories
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(new Color(250, 250, 250));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 0));
        
        // Categories tabs
        leftPanel.add(createCategoriesPanel(), BorderLayout.NORTH);
        
        // Products grid
        productsContainer = new JPanel(new GridLayout(0, 3, 15, 15));
        productsContainer.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right: Cart sidebar
        JPanel rightPanel = createCartPanel();
        
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    // ============ CATEGORIES PANEL ============
    
    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(new Color(250, 250, 250));
        
        String[] categories = {"All", "Burger", "Pizza", "Chicken", "Combo", "Sides", "Drinks"};
        String[] icons = {"🍽️", "🍔", "🍕", "🍗", "🎁", "🍟", "🥤"};
        
        for (int i = 0; i < categories.length; i++) {
            String cat = categories[i];
            String icon = icons[i];
            
            RoundedButton btn = new RoundedButton(icon + " " + cat, 8);
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setBackground(cat.equals(selectedCategory) ? ORANGE : Color.WHITE);
            btn.setForeground(cat.equals(selectedCategory) ? Color.WHITE : Color.BLACK);
            
            btn.addActionListener(e -> {
                selectedCategory = cat;
                filterProductsByCategory();
                // Update all category buttons
                panel.removeAll();
                panel.add(createCategoriesPanel());
                panel.revalidate();
                panel.repaint();
            });
            
            panel.add(btn);
        }
        
        return panel;
    }
    
    // ============ CART PANEL ============
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JLabel headerLabel = new JLabel("Đơn hàng");
        headerLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        
        // Customer selection
        JPanel customerPanel = new JPanel(new BorderLayout(10, 5));
        customerPanel.setBackground(Color.WHITE);
        
        JLabel customerLabel = new JLabel("Tên khách hàng");
        customerLabel.setFont(UIConstants.FONT_BODY_BOLD);
        
        customerCombo = new JComboBox<>(new String[]{"Ăn tại chỗ", "Mang về", "Giao hàng"});
        customerCombo.setFont(UIConstants.FONT_BODY);
        customerCombo.setPreferredSize(new Dimension(0, 36));
        
        customerPanel.add(customerLabel, BorderLayout.NORTH);
        customerPanel.add(customerCombo, BorderLayout.CENTER);
        
        // Cart items container
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(Color.WHITE);
        
        JScrollPane cartScroll = new JScrollPane(cartPanel);
        cartScroll.setBorder(null);
        cartScroll.getVerticalScrollBar().setUnitIncrement(10);
        
        // Summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 0, 10, 0)
        ));
        
        subtotalLabel = createSummaryRow("Subtotal:", "0đ");
        taxLabel = createSummaryRow("Thuế (10%):", "0đ");
        discountLabel = createSummaryRow("Giảm giá:", "-0đ");
        
        summaryPanel.add(subtotalLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(taxLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(discountLabel);
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel totalLabelText = new JLabel("Tổng cộng:");
        totalLabelText.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        
        totalLabel = new JLabel("0đ");
        totalLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        totalLabel.setForeground(ORANGE);
        
        totalPanel.add(totalLabelText, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        
        // Place order button
        RoundedButton placeOrderBtn = new RoundedButton("🛒 Đặt món", 8);
        placeOrderBtn.setBackground(ORANGE);
        placeOrderBtn.setPreferredSize(new Dimension(0, 50));
        placeOrderBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        placeOrderBtn.addActionListener(e -> placeOrder());
        
        // Assembly
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(headerLabel);
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(customerPanel);
        topPanel.add(Box.createVerticalStrut(15));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(summaryPanel);
        bottomPanel.add(totalPanel);
        bottomPanel.add(Box.createVerticalStrut(15));
        bottomPanel.add(placeOrderBtn);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(cartScroll, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JLabel createSummaryRow(String label, String value) {
        JLabel rowLabel = new JLabel(label + " " + value);
        rowLabel.setFont(UIConstants.FONT_BODY);
        rowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return rowLabel;
    }
    
    // ============ LOAD PRODUCTS ============
    
    private void loadProducts() {
        products = productDAO.getAllAvailable();
        displayProducts(products);
    }
    
    private void displayProducts(List<Product> productsToDisplay) {
        productsContainer.removeAll();
        
        for (Product product : productsToDisplay) {
            productsContainer.add(createProductCard(product));
        }
        
        productsContainer.revalidate();
        productsContainer.repaint();
    }
    
    private void filterProductsByCategory() {
        if (selectedCategory.equals("All")) {
            displayProducts(products);
        } else {
            List<Product> filtered = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(selectedCategory))
                .toList();
            displayProducts(filtered);
        }
    }
    
    // ============ PRODUCT CARD ============
    
    private RoundedPanel createProductCard(Product product) {
        RoundedPanel card = new RoundedPanel(10, true);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(0, 8));
        card.setPreferredSize(new Dimension(180, 200));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Image area
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBackground(new Color(245, 245, 245));
        imgPanel.setPreferredSize(new Dimension(0, 100));
        
        JLabel imgLabel = new JLabel(getEmojiForCategory(product.getCategory()));
        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgPanel.add(imgLabel, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        JLabel nameLabel = new JLabel(truncate(product.getName(), 20));
        nameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel(AppConfig.formatCurrency(product.getPrice()));
        priceLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 15));
        priceLabel.setForeground(ORANGE);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        RoundedButton addBtn = new RoundedButton("Thêm", 6);
        addBtn.setBackground(ORANGE);
        addBtn.setPreferredSize(new Dimension(0, 30));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        addBtn.addActionListener(e -> addToCart(product));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(addBtn);
        
        card.add(imgPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private String getEmojiForCategory(String category) {
        if (category == null) return "🍔";
        return switch (category.toLowerCase()) {
            case "burger" -> "🍔";
            case "pizza" -> "🍕";
            case "chicken" -> "🍗";
            case "sides" -> "🍟";
            case "drinks" -> "🥤";
            case "combo" -> "🎁";
            default -> "🍽️";
        };
    }
    
    private String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
    
    // ============ CART MANAGEMENT ============
    
    private void addToCart(Product product) {
        CartItem item = cart.get(product.getProductId());
        
        if (item == null) {
            item = new CartItem(product, 1);
            cart.put(product.getProductId(), item);
        } else {
            item.quantity++;
        }
        
        updateCartDisplay();
    }
    
    private void removeFromCart(int productId) {
        cart.remove(productId);
        updateCartDisplay();
    }
    
    private void updateQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(productId);
        } else {
            CartItem item = cart.get(productId);
            if (item != null) {
                item.quantity = quantity;
                updateCartDisplay();
            }
        }
    }
    
    private void updateCartDisplay() {
        cartPanel.removeAll();
        
        if (cart.isEmpty()) {
            JLabel emptyLabel = new JLabel("Chưa có món nào");
            emptyLabel.setFont(UIConstants.FONT_BODY);
            emptyLabel.setForeground(Color.GRAY);
            cartPanel.add(emptyLabel);
        } else {
            for (CartItem item : cart.values()) {
                cartPanel.add(createCartItemRow(item));
                cartPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        // Update summary
        double subtotal = cart.values().stream()
            .mapToDouble(item -> item.product.getPrice() * item.quantity)
            .sum();
        double tax = subtotal * 0.1;
        double discount = 0;
        double total = subtotal + tax - discount;
        
        subtotalLabel.setText("Subtotal: " + AppConfig.formatCurrency(subtotal));
        taxLabel.setText("Thuế (10%): " + AppConfig.formatCurrency(tax));
        discountLabel.setText("Giảm giá: -" + AppConfig.formatCurrency(discount));
        totalLabel.setText(AppConfig.formatCurrency(total));
        
        cartPanel.revalidate();
        cartPanel.repaint();
    }
    
    private JPanel createCartItemRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(truncate(item.product.getName(), 18));
        nameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        
        JLabel priceLabel = new JLabel(AppConfig.formatCurrency(item.product.getPrice()));
        priceLabel.setFont(UIConstants.FONT_CAPTION);
        priceLabel.setForeground(Color.GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        
        // Quantity controls
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        qtyPanel.setBackground(Color.WHITE);
        
        RoundedButton minusBtn = new RoundedButton("-", 4);
        minusBtn.setPreferredSize(new Dimension(30, 30));
        minusBtn.setBackground(new Color(240, 240, 240));
        minusBtn.setForeground(Color.BLACK);
        minusBtn.addActionListener(e -> updateQuantity(item.product.getProductId(), item.quantity - 1));
        
        JLabel qtyLabel = new JLabel(String.valueOf(item.quantity));
        qtyLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        qtyLabel.setPreferredSize(new Dimension(25, 30));
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        RoundedButton plusBtn = new RoundedButton("+", 4);
        plusBtn.setPreferredSize(new Dimension(30, 30));
        plusBtn.setBackground(ORANGE);
        plusBtn.addActionListener(e -> updateQuantity(item.product.getProductId(), item.quantity + 1));
        
        RoundedButton removeBtn = new RoundedButton("✕", 4);
        removeBtn.setPreferredSize(new Dimension(30, 30));
        removeBtn.setBackground(new Color(244, 67, 54));
        removeBtn.addActionListener(e -> removeFromCart(item.product.getProductId()));
        
        qtyPanel.add(minusBtn);
        qtyPanel.add(qtyLabel);
        qtyPanel.add(plusBtn);
        qtyPanel.add(removeBtn);
        
        row.add(infoPanel, BorderLayout.CENTER);
        row.add(qtyPanel, BorderLayout.EAST);
        
        return row;
    }
    
    // ============ PLACE ORDER ============
    
    private void placeOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món!");
            return;
        }
        
        double subtotal = cart.values().stream()
            .mapToDouble(item -> item.product.getPrice() * item.quantity)
            .sum();
        double total = subtotal * 1.1; // Include tax
        
        String customer = (String) customerCombo.getSelectedItem();
        
        // Create order
        Order order = new Order(customer, "0905999999", total, 0);
        order.setStatus(1); // Confirmed
        order.setPaymentStatus(0); // Unpaid
        
        if (orderDAO.create(order)) {
            // Create order items
            for (CartItem item : cart.values()) {
                OrderItem orderItem = new OrderItem(
                    order.getOrderId(),
                    item.product.getProductId(),
                    item.product.getName(),
                    item.quantity,
                    item.product.getPrice(),
                    item.product.getPrice() * item.quantity
                );
                orderItemDAO.create(orderItem);
            }
            
            JOptionPane.showMessageDialog(this, 
                "✅ Đặt món thành công!\n" +
                "Đơn hàng #" + order.getOrderId());
            
            // Clear cart
            cart.clear();
            updateCartDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi đặt món!");
        }
    }
    
    public void refreshProducts() {
        loadProducts();
    }
    
    // ============ CART ITEM CLASS ============
    
    private static class CartItem {
        Product product;
        int quantity;
        
        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }
}