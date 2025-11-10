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
 * FIXED VERSION - Load products from database
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
        
        String[] categories = {"🍔 Burger", "🍕 Pizza", "🍗 Chicken", "🍟 Sides", "🥤 Drinks", "🎁 Combo"};
        
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
                // Nếu DB trống, load sample data
                System.out.println("⚠️ No products in database, loading sample data...");
                loadSampleProducts();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading products from database: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to sample data
            loadSampleProducts();
        }
    }
    
    /**
     * Load sample products (nếu DB trống)
     */
    private void loadSampleProducts() {
        // Sử dụng constructor ĐÚNG của Product
        // Constructor: Product(String name, String description, double price, String category, String imageUrl)
        
        // Burgers
        Product p1 = new Product("Double Cheese Burger", 
            "Burger đôi với phô mai đặc biệt", 89000, "Burger", "img1.jpg");
        p1.setProductId(1);
        p1.setAvailable(1);
        allProducts.add(p1);
        
        Product p7 = new Product("Big Burger", 
            "Burger bò với rau tươi", 65000, "Burger", "img7.jpg");
        p7.setProductId(7);
        p7.setAvailable(1);
        allProducts.add(p7);
        
        // Combo
        Product p2 = new Product("Combo Gà Rán + Pepsi", 
            "Burger gà + Khoai tây + nước", 129000, "Combo", "img2.jpg");
        p2.setProductId(2);
        p2.setAvailable(1);
        allProducts.add(p2);
        
        Product p6 = new Product("Combo Burger Set", 
            "Burger + Fries + Drink", 149000, "Combo", "img6.jpg");
        p6.setProductId(6);
        p6.setAvailable(1);
        allProducts.add(p6);
        
        // Pizza
        Product p3 = new Product("Family Pizza Combo", 
            "Pizza cỡ lớn + 2 Pepsi", 299000, "Pizza", "img3.jpg");
        p3.setProductId(3);
        p3.setAvailable(1);
        allProducts.add(p3);
        
        Product p8 = new Product("Pizza Pepperoni", 
            "Pizza xúc xích Ý", 129000, "Pizza", "img8.jpg");
        p8.setProductId(8);
        p8.setAvailable(1);
        allProducts.add(p8);
        
        // Chicken
        Product p4 = new Product("Fried Chicken Bucket (6 pcs)", 
            "Gà rán giòn tan 6 miếng", 159000, "Chicken", "img4.jpg");
        p4.setProductId(4);
        p4.setAvailable(1);
        allProducts.add(p4);
        
        // Sides
        Product p5 = new Product("Khoai Tây Chiên", 
            "Khoai tây giòn size lớn", 45000, "Sides", "img5.jpg");
        p5.setProductId(5);
        p5.setAvailable(1);
        allProducts.add(p5);
        
        // Drinks
        Product p9 = new Product("Pepsi Cola", 
            "Nước ngọt có ga size L", 25000, "Drinks", "img6.jpg");
        p9.setProductId(6);
        p9.setAvailable(1);
        allProducts.add(p9);
        
        System.out.println("✅ Loaded " + allProducts.size() + " sample products");
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