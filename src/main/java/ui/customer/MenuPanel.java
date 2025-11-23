package ui.customer;

import entity.Product;
import dao.ProductDAO;
import ui.components.ProductCard;
import ui.components.RoundedButton;
import ui.components.SearchField;
import util.ColorScheme;
import config.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuPanel - Product browsing with categories and grid layout
 * Based on Image 9 design
 * 
 * Load products from database
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class MenuPanel extends JPanel {
    
    private CustomerMainFrame parentFrame;
    private JPanel categoriesPanel;
    private JPanel productsContainer;
    private SearchField searchField;
    private List<Product> allProducts;
    private String selectedCategory = "All";
    private ProductDAO productDAO;
    
    public MenuPanel(CustomerMainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.allProducts = new ArrayList<>();
        this.productDAO = new ProductDAO();
        
        setLayout(new BorderLayout(0, 15));
        setBackground(ColorScheme.BG_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        
        initComponents();
        loadProductsFromDatabase(); // Load từ DB
        displayProducts(allProducts);
    }
    
    private void initComponents() {
        // Top panel: Search and categories
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setOpaque(false);
        
        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setOpaque(false);
        
        searchField = new SearchField("Tìm món ăn...");
        searchField.setPreferredSize(new Dimension(350, 45));
        searchField.getTextField().addActionListener(e -> searchProducts());
        
        searchPanel.add(searchField);
        
        // Categories
        categoriesPanel = createCategoriesPanel();
        
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(categoriesPanel, BorderLayout.CENTER);
        
        // Products grid
        productsContainer = new JPanel(new GridLayout(0, 4, 20, 20));
        productsContainer.setBackground(ColorScheme.BG_SECONDARY);
        
        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(ColorScheme.BG_SECONDARY);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        
        String[] categories = {"Burger", "Pizza", "Chicken", "Sides", "Drinks", "Combo"};
        
        // All button
        RoundedButton allBtn = new RoundedButton("Tất cả", 20);
        allBtn.setBackground(ColorScheme.PRIMARY);
        allBtn.setPreferredSize(new Dimension(100, 40));
        allBtn.addActionListener(e -> filterByCategory("All"));
        panel.add(allBtn);
        
        // Category buttons
        for (String category : categories) {
            RoundedButton btn = new RoundedButton(category, 20);
            btn.setBackground(ColorScheme.SECONDARY);
            btn.setPreferredSize(new Dimension(130, 40));
            btn.addActionListener(e -> filterByCategory(category.substring(2).trim())); // Remove emoji
            panel.add(btn);
        }
        
        return panel;
    }
    
    /**
     * Load products từ database
     * Nếu DB trống, tạo sample data
     */
    private void loadProductsFromDatabase() {
        try {
            // Load tất cả products available từ DB
            List<Product> dbProducts = productDAO.getAllAvailable();
            if (dbProducts != null && !dbProducts.isEmpty()) {
                allProducts = dbProducts;
                System.out.println("✅ Loaded " + dbProducts.size() + " products from database");
            } else {
                System.out.println("⚠️ No products in database...");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading products from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
       
    private void displayProducts(List<Product> products) {
        productsContainer.removeAll();
        
        if (products == null || products.isEmpty()) {
            showEmptyState();
        } else {
            for (Product product : products) {
                ProductCard card = new ProductCard(product);
                
                // Add to cart button action
                card.getAddButton().addActionListener(e -> {
                    parentFrame.addToCart(product, 1);
                });
                
                productsContainer.add(card);
            }
            
            // Fill remaining slots with empty panels
            fillEmptySlots(products.size());
        }
        
        productsContainer.revalidate();
        productsContainer.repaint();
    }
    
    /**
     * Fill empty slots in grid to maintain alignment
     */
    private void fillEmptySlots(int totalProducts) {
        int columns = 4;
        int remainder = totalProducts % columns;
        if (remainder != 0) {
            int emptySlots = columns - remainder;
            for (int i = 0; i < emptySlots; i++) {
                JPanel empty = new JPanel();
                empty.setOpaque(false);
                productsContainer.add(empty);
            }
        }
    }
    
    /**
     * Show empty state when no products
     */
    private void showEmptyState() {
        JLabel emptyLabel = new JLabel("<html><center>🍽️<br><br>Không có sản phẩm<br>" +
            "<small>Vui lòng thử tìm kiếm khác</small></center></html>");
        emptyLabel.setFont(UIConstants.FONT_BODY);
        emptyLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setOpaque(false);
        emptyPanel.add(emptyLabel, BorderLayout.CENTER);
        
        productsContainer.add(emptyPanel);
    }
    
    private void filterByCategory(String category) {
        selectedCategory = category;
        
        if (category.equals("All")) {
            displayProducts(allProducts);
        } else {
            List<Product> filtered = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(p);
                }
            }
            displayProducts(filtered);
        }
    }
    
    private void searchProducts() {
        String keyword = searchField.getText().toLowerCase().trim();
        
        if (keyword.isEmpty()) {
            filterByCategory(selectedCategory);
            return;
        }
        
        List<Product> results = new ArrayList<>();
        for (Product p : allProducts) {
            String name = p.getName() != null ? p.getName().toLowerCase() : "";
            String desc = p.getDescription() != null ? p.getDescription().toLowerCase() : "";
            String category = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
            
            if (name.contains(keyword) || desc.contains(keyword) || category.contains(keyword)) {
                results.add(p);
            }
        }
        
        displayProducts(results);
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Không tìm thấy sản phẩm nào phù hợp với \"" + keyword + "\"!",
                "Kết quả tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    /**
     * Refresh products from database
     */
    public void refreshProducts() {
        loadProductsFromDatabase();
        displayProducts(allProducts);
    }
}
