package dao;

import config.DBConnection;
import entity.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * IngredientDAO - Quản lý Ingredient/Kho trong database
 * Path: Source Packages/dao/IngredientDAO.java
 */
public class IngredientDAO extends BaseDAO<Ingredient> {
    
    private static final String TABLE = "materials";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Ingredient ingredient) {
        String sql = "INSERT INTO " + TABLE + 
                     " (name, quantity, unit, expiry_date, supplier, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingredient.getName());
            ps.setDouble(2, ingredient.getQuantity());
            ps.setString(3, ingredient.getUnit());
            ps.setDate(4, ingredient.getExpiryDate());
            ps.setString(5, ingredient.getSupplier());
            ps.setString(6, ingredient.getStatus());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating ingredient: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Ingredient getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToIngredient(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ingredient by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Ingredient> getAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ingredients.add(mapResultSetToIngredient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all ingredients: " + e.getMessage());
        }
        return ingredients;
    }
    
    /**
     * Lấy ingredients theo status
     */
    public List<Ingredient> getByStatus(String status) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ingredients.add(mapResultSetToIngredient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ingredients by status: " + e.getMessage());
        }
        return ingredients;
    }
    
    /**
     * Lấy low stock ingredients
     */
    public List<Ingredient> getLowStockIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status IN ('low', 'out_of_stock')";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ingredients.add(mapResultSetToIngredient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting low stock ingredients: " + e.getMessage());
        }
        return ingredients;
    }
    
    /**
     * Lấy expired ingredients
     */
    public List<Ingredient> getExpiredIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = 'expired'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ingredients.add(mapResultSetToIngredient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expired ingredients: " + e.getMessage());
        }
        return ingredients;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Ingredient ingredient) {
        String sql = "UPDATE " + TABLE + 
                     " SET name = ?, quantity = ?, unit = ?, expiry_date = ?, " +
                     "supplier = ?, status = ?, updated_at = NOW() WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingredient.getName());
            ps.setDouble(2, ingredient.getQuantity());
            ps.setString(3, ingredient.getUnit());
            ps.setDate(4, ingredient.getExpiryDate());
            ps.setString(5, ingredient.getSupplier());
            ps.setString(6, ingredient.getStatus());
            ps.setInt(7, ingredient.getIngredientId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating ingredient: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật quantity
     */
    public boolean updateQuantity(int ingredientId, double quantity) {
        String sql = "UPDATE " + TABLE + " SET quantity = ?, updated_at = NOW() " +
                     "WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, quantity);
            ps.setInt(2, ingredientId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating quantity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thêm quantity
     */
    public boolean addQuantity(int ingredientId, double amount) {
        String sql = "UPDATE " + TABLE + " SET quantity = quantity + ?, updated_at = NOW() " +
                     "WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, amount);
            ps.setInt(2, ingredientId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding quantity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Trừ quantity
     */
    public boolean reduceQuantity(int ingredientId, double amount) {
        String sql = "UPDATE " + TABLE + " SET quantity = GREATEST(0, quantity - ?), " +
                     "updated_at = NOW() WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, amount);
            ps.setInt(2, ingredientId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error reducing quantity: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE material_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting ingredient: " + e.getMessage());
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
            System.err.println("Error counting ingredients: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Ingredient> search(String criteria) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE name LIKE ? OR supplier LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ingredients.add(mapResultSetToIngredient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching ingredients: " + e.getMessage());
        }
        return ingredients;
    }
    
    // ============ HELPER METHODS ============
    
    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(rs.getInt("material_id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setQuantity(rs.getDouble("quantity"));
        ingredient.setUnit(rs.getString("unit"));
        ingredient.setExpiryDate(rs.getDate("expiry_date"));
        ingredient.setSupplier(rs.getString("supplier"));
        ingredient.setStatus(rs.getString("status"));
        ingredient.setCreatedAt(rs.getTimestamp("created_at"));
        ingredient.setUpdatedAt(rs.getTimestamp("updated_at"));
        return ingredient;
    }
}