package ui.components;

import entity.Product;
import dao.ProductDAO;
import ui.components.ProductCard;
import ui.components.RoundedButton;
import ui.components.SearchField;
import util.ColorScheme;
import config.UIConstants;
import listener.MenuListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MenuPanel - Product browsing with categories and grid layout
 * Updated to match the categories found in database logs (Combo, Chicken, Rice, etc.)
 * * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class MenuPanel extends JPanel {
    private MenuListener listener;
    private JPanel categoriesPanel;
    private JPanel productsContainer;
    private SearchField searchField;
    private List<Product> allProducts;
    private String selectedCategory = "All";
    private ProductDAO productDAO;
    
    // Map Display Name -> Database Category Code
    private Map<String, String> categoryMap;
    
    public MenuPanel(MenuListener listener) {
        this.listener = listener;
        this.allProducts = new ArrayList<>();
        this.productDAO = new ProductDAO();
        
        // Initialize Category Map
        initCategoryMap();
        
        setLayout(new BorderLayout(0, 15));
        setBackground(ColorScheme.BG_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        
        initComponents();
        loadProductsFromDatabase(); // Load from DB
        displayProducts(allProducts);
    }
    
    private void initCategoryMap() {
        categoryMap = new LinkedHashMap<>();
        // Display Name -> Database Value (Must match your logs exactly)
        categoryMap.put("Tất cả", "All");
        categoryMap.put("Combo", "Combo");       // Covers both Group and Solo combos based on logs
        categoryMap.put("Gà Rán", "Chicken");    // Covers Fried Chicken, Tenders
        categoryMap.put("Burger", "Burger");     // Covers Burgers
        categoryMap.put("Cơm & Mì", "Rice");     // Covers Rice and Spaghetti (Migaxuxi)
        categoryMap.put("Ăn Kèm", "Sides");      // Covers Fries, Soup, Mash, etc.
        categoryMap.put("Thức Uống", "Drinks");  // Covers Pepsi, Tea, etc.
        categoryMap.put("Khác", "Other");        // Covers Sauces etc.
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Added vertical gap for wrapping
        panel.setOpaque(false);
        
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            String displayName = entry.getKey();
            String dbCode = entry.getValue();
            
            RoundedButton btn = new RoundedButton(displayName, 20);
            
            // Highlight "All" by default or create logic to highlight selected
            if (dbCode.equals("All")) {
                btn.setBackground(ColorScheme.PRIMARY);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(ColorScheme.SECONDARY);
                btn.setForeground(Color.BLACK);
            }
            
            btn.setPreferredSize(new Dimension(130, 40));
            btn.addActionListener(e -> {
                // Reset all buttons to secondary color (optional UI polish)
                for (Component c : panel.getComponents()) {
                    if (c instanceof RoundedButton) {
                        c.setBackground(ColorScheme.SECONDARY);
                        c.setForeground(Color.BLACK);
                    }
                }
                // Highlight active button
                btn.setBackground(ColorScheme.PRIMARY);
                btn.setForeground(Color.WHITE);
                
                filterByCategory(dbCode);
            });
            panel.add(btn);
        }
        
        return panel;
    }
    
    /**
     * Load products from database
     */
    private void loadProductsFromDatabase() {
        try {
            // Load all available products from DB
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
                    listener.addToCart(product, 1);
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
    
    private void filterByCategory(String categoryCode) {
        selectedCategory = categoryCode;
        System.out.println("Filtering by: " + categoryCode);
        
        if (categoryCode.equals("All")) {
            displayProducts(allProducts);
        } else {
            List<Product> filtered = new ArrayList<>();
            for (Product p : allProducts) {
                // Ensure null safety and check DB code
                if (p.getCategory() != null && p.getCategory().equalsIgnoreCase(categoryCode)) {
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
            // We search against the category code, but could also map to display name if needed
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
        // Re-apply the current filter
        filterByCategory(selectedCategory);
    }
}
