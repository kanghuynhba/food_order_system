package dao;

import config.DBConnection;
import entity.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAO - Quản lý Order trong database
 * Path: Source Packages/dao/OrderDAO.java
 */
public class OrderDAO extends BaseDAO<Order> {
    
    private static final String TABLE = "orders";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Order order) {
        String sql = "INSERT INTO " + TABLE + 
                     " (customer_name, phone_number, total_amount, pay_method, " +
                     "payment_status, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getPhoneNumber());
            ps.setDouble(3, order.getTotalAmount());
            ps.setInt(4, order.getPayMethod());
            ps.setInt(5, order.getPaymentStatus());
            ps.setInt(6, order.getStatus());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Order getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        }
        return orders;
    }
    
    /**
     * Lấy orders theo status
     */
    public List<Order> getByStatus(int status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders by status: " + e.getMessage());
        }
        return orders;
    }
    
    /**
     * Lấy orders theo payment status
     */
    public List<Order> getByPaymentStatus(int paymentStatus) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE payment_status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, paymentStatus);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders by payment status: " + e.getMessage());
        }
        return orders;
    }
    
    /**
     * Lấy orders theo customer phone
     */
    public List<Order> getByPhoneNumber(String phone) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE phone_number = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders by phone: " + e.getMessage());
        }
        return orders;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Order order) {
        String sql = "UPDATE " + TABLE + 
                     " SET customer_name = ?, phone_number = ?, total_amount = ?, " +
                     "pay_method = ?, payment_status = ?, status = ?, " +
                     "assigned_chef_id = ?, updated_at = NOW() WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getPhoneNumber());
            ps.setDouble(3, order.getTotalAmount());
            ps.setInt(4, order.getPayMethod());
            ps.setInt(5, order.getPaymentStatus());
            ps.setInt(6, order.getStatus());
            ps.setInt(7, order.getAssignedChefId());
            ps.setInt(8, order.getOrderId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật status
     */
    public boolean updateStatus(int orderId, int status) {
        String sql = "UPDATE " + TABLE + " SET status = ?, updated_at = NOW() WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ps.setInt(2, orderId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật payment status
     */
    public boolean updatePaymentStatus(int orderId, int paymentStatus) {
        String sql = "UPDATE " + TABLE + " SET payment_status = ?, updated_at = NOW() WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, paymentStatus);
            ps.setInt(2, orderId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            return false;
        }
    }
     /**
     * Assign chef to order
     */
    public boolean assignChef(int orderId, int chefId) {
    String sql = "UPDATE " + TABLE + " SET assigned_chef_id = ?, updated_at = NOW() WHERE order_id = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, chefId);
        ps.setInt(2, orderId);
        
        int result = ps.executeUpdate();
        
        if (result > 0) {
            System.out.println("✅ Assigned chef " + chefId + " to order " + orderId);
            return true;
        }
        return false;
        
    } catch (SQLException e) {
        System.err.println("❌ Error assigning chef: " + e.getMessage());
        return false;
    }
}
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
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
            System.err.println("Error counting orders: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Order> search(String criteria) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE customer_name LIKE ? OR phone_number LIKE ? OR CAST(order_id AS CHAR) LIKE ? " +
                     "ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching orders: " + e.getMessage());
        }
        return orders;
    }
    
    // ============ HELPER METHODS ============
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setPhoneNumber(rs.getString("phone_number"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setPayMethod(rs.getInt("pay_method"));
        order.setPaymentStatus(rs.getInt("payment_status"));
        order.setStatus(rs.getInt("status"));
        order.setAssignedChefId(rs.getInt("assigned_chef_id"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }

}