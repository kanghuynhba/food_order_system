package ui.manager;

import entity.Product;
import service.ProductService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private ProductService productService;
    private JPanel productsGrid;
    private JTextField searchField;
    
    public ProductPanel() {
        productService = ProductService.getInstance();
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Products grid
        productsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        productsGrid.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(productsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel title = new JLabel("Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Search & Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField("🔍 Tìm món nhanh...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(250, 35));
        
        String[] categories = {"All Categories", "All Status", "All Warehouses"};
        for (String cat : categories) {
            JComboBox<String> combo = new JComboBox<>(new String[]{cat});
            combo.setPreferredSize(new Dimension(140, 35));
            filterPanel.add(combo);
        }
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadProducts() {
        productsGrid.removeAll();
        List<Product> products = productService.getAllProducts();
        
        for (Product product : products) {
            productsGrid.add(createProductCard(product));
        }
        
        productsGrid.revalidate();
        productsGrid.repaint();
    }
    
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Image
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(200, 150));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(245, 245, 245));
        imgLabel.setText("🍔"); // Placeholder
        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        // Badge
        if (!product.isAvailable()) {
            JLabel badge = new JLabel("Hết hàng");
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(Color.WHITE);
            badge.setBackground(Color.RED);
            badge.setOpaque(true);
            badge.setBorder(new EmptyBorder(3, 8, 3, 8));
            imgLabel.add(badge);
        }
        
        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel priceLabel = new JLabel(String.format("%,dđ", (int) product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(ORANGE);
        
        JLabel descLabel = new JLabel(product.getDescription() != null ? 
            product.getDescription() : product.getCategory());
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        btnPanel.setBackground(Color.WHITE);
        
        JButton addBtn = new JButton("Thêm");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(ORANGE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(addBtn);
        
        card.add(imgLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
}