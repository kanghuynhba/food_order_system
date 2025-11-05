package dao;

import config.DBConnection;
import entity.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO - Quản lý Product/Menu trong database
 * Path: Source Packages/dao/ProductDAO.java
 */
public class ProductDAO extends BaseDAO<Product> {
    
    private static final String TABLE = "products";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Product product) {
        String sql = "INSERT INTO " + TABLE + 
                     " (name, description, price, category, image_url, available) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getCategory());
            ps.setString(5, product.getImageUrl());
            ps.setInt(6, product.getAvailable());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Product getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        return products;
    }
    
    /**
     * Lấy products theo category
     */
    public List<Product> getByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE category = ? AND available = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
        }
        return products;
    }
    
    /**
     * Lấy tất cả products available
     */
    public List<Product> getAllAvailable() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE available = 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting available products: " + e.getMessage());
        }
        return products;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Product product) {
        String sql = "UPDATE " + TABLE + 
                     " SET name = ?, description = ?, price = ?, category = ?, " +
                     "image_url = ?, available = ? WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getCategory());
            ps.setString(5, product.getImageUrl());
            ps.setInt(6, product.getAvailable());
            ps.setInt(7, product.getProductId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật giá
     */
    public boolean updatePrice(int productId, double newPrice) {
        String sql = "UPDATE " + TABLE + " SET price = ? WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, newPrice);
            ps.setInt(2, productId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating price: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật available status
     */
    public boolean updateAvailable(int productId, int available) {
        String sql = "UPDATE " + TABLE + " SET available = ? WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, available);
            ps.setInt(2, productId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating available: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    // ============ UTILITY ============
    
    @Override
    public boolean exists(int id) {
        return getById(id) != null;
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting products: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Product> search(String criteria) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE name LIKE ? OR category LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }
    
    // ============ HELPER METHODS ============
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));
        product.setAvailable(rs.getInt("available"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        return product;
    }
}