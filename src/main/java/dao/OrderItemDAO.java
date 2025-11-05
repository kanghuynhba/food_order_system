package dao;

import config.DBConnection;
import entity.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderItemDAO - Quản lý OrderItem (chi tiết từng mục) trong database
 * Path: Source Packages/dao/OrderItemDAO.java
 */
public class OrderItemDAO extends BaseDAO<OrderItem> {
    
    private static final String TABLE = "order_items";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(OrderItem orderItem) {
        String sql = "INSERT INTO " + TABLE + 
                     " (order_id, product_id, product_name, quantity, unit_price, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());
            ps.setString(3, orderItem.getProductName());  // ← ĐÃ THÊM FIELD NÀY
            ps.setInt(4, orderItem.getQuantity());
            ps.setDouble(5, orderItem.getUnitPrice());
            ps.setDouble(6, orderItem.getSubtotal());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderItem.setOrderItemId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating order item: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public OrderItem getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrderItem(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order item by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orderItems.add(mapResultSetToOrderItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all order items: " + e.getMessage());
        }
        return orderItems;
    }
    
    /**
     * Lấy order items theo order_id
     */
    public List<OrderItem> getByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orderItems.add(mapResultSetToOrderItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order items by order ID: " + e.getMessage());
        }
        return orderItems;
    }
    
    /**
     * Lấy order items theo product_id
     */
    public List<OrderItem> getByProductId(int productId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orderItems.add(mapResultSetToOrderItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order items by product ID: " + e.getMessage());
        }
        return orderItems;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(OrderItem orderItem) {
        String sql = "UPDATE " + TABLE + 
                     " SET order_id = ?, product_id = ?, product_name = ?, quantity = ?, " +
                     "unit_price = ?, subtotal = ? WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());
            ps.setString(3, orderItem.getProductName());
            ps.setInt(4, orderItem.getQuantity());
            ps.setDouble(5, orderItem.getUnitPrice());
            ps.setDouble(6, orderItem.getSubtotal());
            ps.setInt(7, orderItem.getOrderItemId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật quantity
     */
    public boolean updateQuantity(int orderItemId, int quantity) {
        String sql = "UPDATE " + TABLE + " SET quantity = ?, " +
                     "subtotal = unit_price * ? WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, quantity);
            ps.setInt(3, orderItemId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating quantity: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa tất cả items của 1 order
     */
    public boolean deleteByOrderId(int orderId) {
        String sql = "DELETE FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order items by order ID: " + e.getMessage());
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
            System.err.println("Error counting order items: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Đếm items trong 1 order
     */
    public int countByOrderId(int orderId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting items by order ID: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<OrderItem> search(String criteria) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE product_name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orderItems.add(mapResultSetToOrderItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching order items: " + e.getMessage());
        }
        return orderItems;
    }
    
    // ============ HELPER METHODS ============
    
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setUnitPrice(rs.getDouble("unit_price"));
        orderItem.setSubtotal(rs.getDouble("subtotal"));
        return orderItem;
    }
}