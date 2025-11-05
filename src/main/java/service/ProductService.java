package service;

import dao.ProductDAO;
import entity.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService - Product/Menu Management Service
 * Path: Source Packages/service/ProductService.java
 * 
 * Chức năng:
 * - CRUD sản phẩm/món ăn
 * - Quản lý menu theo danh mục
 * - Quản lý trạng thái available
 * - Tìm kiếm và filter sản phẩm
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ProductService {
    
    private static ProductService instance;
    private ProductDAO productDAO;
    
    // ============ SINGLETON ============
    
    private ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }
    
    // ============ CRUD OPERATIONS ============
    
    /**
     * Thêm sản phẩm mới
     */
    public boolean addProduct(String name, String description, double price, 
                             String category, String imageUrl) {
        try {
            // Validate
            if (name == null || name.trim().isEmpty()) {
                System.err.println("❌ Product name is required");
                return false;
            }
            
            if (price <= 0) {
                System.err.println("❌ Price must be greater than 0");
                return false;
            }
            
            if (category == null || category.trim().isEmpty()) {
                System.err.println("❌ Category is required");
                return false;
            }
            
            // Create product
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setImageUrl(imageUrl);
            product.setAvailable(1); // Available by default
            
            boolean success = productDAO.create(product);
            
            if (success) {
                System.out.println("✅ Product added: " + name);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật sản phẩm
     */
    public boolean updateProduct(Product product) {
        try {
            if (product == null || product.getProductId() <= 0) {
                System.err.println("❌ Invalid product");
                return false;
            }
            
            if (!validateProduct(product)) {
                return false;
            }
            
            boolean success = productDAO.update(product);
            
            if (success) {
                System.out.println("✅ Product updated: " + product.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa sản phẩm
     */
    public boolean deleteProduct(int productId) {
        try {
            Product product = productDAO.getById(productId);
            if (product == null) {
                System.err.println("❌ Product not found");
                return false;
            }
            
            boolean success = productDAO.delete(productId);
            
            if (success) {
                System.out.println("✅ Product deleted: " + productId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    // ============ QUERY OPERATIONS ============
    
    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all products: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy sản phẩm theo ID
     */
    public Product getProductById(int productId) {
        try {
            return productDAO.getById(productId);
        } catch (Exception e) {
            System.err.println("❌ Error getting product: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy sản phẩm available
     */
    public List<Product> getAvailableProducts() {
        try {
            return productDAO.getAllAvailable();
        } catch (Exception e) {
            System.err.println("❌ Error getting available products: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy sản phẩm theo category
     */
    public List<Product> getProductsByCategory(String category) {
        try {
            return productDAO.getByCategory(category);
        } catch (Exception e) {
            System.err.println("❌ Error getting products by category: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Tìm kiếm sản phẩm
     */
    public List<Product> searchProducts(String keyword) {
        try {
            return productDAO.search(keyword);
        } catch (Exception e) {
            System.err.println("❌ Error searching products: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ CATEGORY MANAGEMENT ============
    
    /**
     * Lấy danh sách tất cả categories
     */
    public List<String> getAllCategories() {
        try {
            List<Product> products = getAllProducts();
            return products.stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting categories: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Đếm số sản phẩm theo category
     */
    public int countProductsByCategory(String category) {
        return getProductsByCategory(category).size();
    }
    
    /**
     * Lấy category có nhiều sản phẩm nhất
     */
    public String getMostPopularCategory() {
        List<String> categories = getAllCategories();
        String mostPopular = null;
        int maxCount = 0;
        
        for (String category : categories) {
            int count = countProductsByCategory(category);
            if (count > maxCount) {
                maxCount = count;
                mostPopular = category;
            }
        }
        
        return mostPopular;
    }
    
    // ============ AVAILABILITY MANAGEMENT ============
    
    /**
     * Đánh dấu sản phẩm available
     */
    public boolean markAsAvailable(int productId) {
        try {
            boolean success = productDAO.updateAvailable(productId, 1);
            
            if (success) {
                System.out.println("✅ Product marked as available: " + productId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error marking product as available: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đánh dấu sản phẩm unavailable
     */
    public boolean markAsUnavailable(int productId) {
        try {
            boolean success = productDAO.updateAvailable(productId, 0);
            
            if (success) {
                System.out.println("✅ Product marked as unavailable: " + productId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error marking product as unavailable: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Toggle availability status
     */
    public boolean toggleAvailability(int productId) {
        Product product = getProductById(productId);
        if (product == null) return false;
        
        if (product.isAvailable()) {
            return markAsUnavailable(productId);
        } else {
            return markAsAvailable(productId);
        }
    }
    
    // ============ PRICE MANAGEMENT ============
    
    /**
     * Cập nhật giá sản phẩm
     */
    public boolean updatePrice(int productId, double newPrice) {
        try {
            if (newPrice <= 0) {
                System.err.println("❌ Price must be greater than 0");
                return false;
            }
            
            boolean success = productDAO.updatePrice(productId, newPrice);
            
            if (success) {
                System.out.println("✅ Price updated for product: " + productId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating price: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Áp dụng discount cho sản phẩm
     */
    public boolean applyDiscount(int productId, double discountPercent) {
        try {
            Product product = getProductById(productId);
            if (product == null) {
                System.err.println("❌ Product not found");
                return false;
            }
            
            if (discountPercent < 0 || discountPercent > 100) {
                System.err.println("❌ Discount must be between 0 and 100");
                return false;
            }
            
            double originalPrice = product.getPrice();
            double newPrice = originalPrice * (1 - discountPercent / 100);
            
            return updatePrice(productId, newPrice);
            
        } catch (Exception e) {
            System.err.println("❌ Error applying discount: " + e.getMessage());
            return false;
        }
    }
    
    // ============ FILTER & SORT ============
    
    /**
     * Lấy sản phẩm trong khoảng giá
     */
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return getAllProducts().stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }
    
    /**
     * Sort sản phẩm theo giá tăng dần
     */
    public List<Product> sortByPriceAsc(List<Product> products) {
        return products.stream()
            .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
            .collect(Collectors.toList());
    }
    
    /**
     * Sort sản phẩm theo giá giảm dần
     */
    public List<Product> sortByPriceDesc(List<Product> products) {
        return products.stream()
            .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
            .collect(Collectors.toList());
    }
    
    /**
     * Sort sản phẩm theo tên
     */
    public List<Product> sortByName(List<Product> products) {
        return products.stream()
            .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
            .collect(Collectors.toList());
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm tổng số sản phẩm
     */
    public int getTotalProductCount() {
        try {
            return productDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting products: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Đếm số sản phẩm available
     */
    public int getAvailableProductCount() {
        return getAvailableProducts().size();
    }
    
    /**
     * Đếm số sản phẩm unavailable
     */
    public int getUnavailableProductCount() {
        return getTotalProductCount() - getAvailableProductCount();
    }
    
    /**
     * Tính giá trung bình
     */
    public double getAveragePrice() {
        List<Product> products = getAllProducts();
        if (products.isEmpty()) return 0.0;
        
        return products.stream()
            .mapToDouble(Product::getPrice)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Lấy sản phẩm giá cao nhất
     */
    public Product getMostExpensiveProduct() {
        return getAllProducts().stream()
            .max((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
            .orElse(null);
    }
    
    /**
     * Lấy sản phẩm giá thấp nhất
     */
    public Product getCheapestProduct() {
        return getAllProducts().stream()
            .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
            .orElse(null);
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate sản phẩm
     */
    public boolean validateProduct(Product product) {
        if (product == null) return false;
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.err.println("❌ Product name is required");
            return false;
        }
        
        if (product.getPrice() <= 0) {
            System.err.println("❌ Price must be greater than 0");
            return false;
        }
        
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            System.err.println("❌ Category is required");
            return false;
        }
        
        return true;
    }
    
    /**
     * Kiểm tra sản phẩm có tồn tại không
     */
    public boolean exists(int productId) {
        return productDAO.exists(productId);
    }
    
    // ============ UTILITY ============
    
    /**
     * Format giá
     */
    public String formatPrice(double price) {
        return String.format("%,.0fđ", price);
    }
    
    /**
     * Lấy product summary
     */
    public String getProductSummary(int productId) {
        Product product = getProductById(productId);
        if (product == null) {
            return "Product not found";
        }
        
        return String.format(
            "%s - %s - %s",
            product.getName(),
            formatPrice(product.getPrice()),
            product.getAvailableStatus()
        );
    }
    
    /**
     * Clone product
     */
    public Product cloneProduct(int productId, String newName) {
        Product original = getProductById(productId);
        if (original == null) return null;
        
        Product clone = new Product();
        clone.setName(newName);
        clone.setDescription(original.getDescription());
        clone.setPrice(original.getPrice());
        clone.setCategory(original.getCategory());
        clone.setImageUrl(original.getImageUrl());
        clone.setAvailable(original.getAvailable());
        
        if (productDAO.create(clone)) {
            return clone;
        }
        
        return null;
    }
}