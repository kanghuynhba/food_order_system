package dao;

import config.DBConnection;
import entity.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAO - Quản lý Payment trong database
 * Path: Source Packages/dao/PaymentDAO.java
 */
public class PaymentDAO extends BaseDAO<Payment> {
    
    private static final String TABLE = "payments";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Payment payment) {
        String sql = "INSERT INTO " + TABLE + 
                     " (order_id, amount, method, status, notes) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, payment.getOrderId());
            ps.setDouble(2, payment.getAmount());
            ps.setInt(3, payment.getMethod());
            ps.setInt(4, payment.getStatus());
            ps.setString(5, payment.getNotes());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Payment getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payment by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY paid_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all payments: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Lấy payment theo order_id
     */
    public Payment getByOrderId(int orderId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payment by order ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy payments theo status
     */
    public List<Payment> getByStatus(int status) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = ? ORDER BY paid_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payments by status: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Lấy payments theo method
     */
    public List<Payment> getByMethod(int method) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE method = ? ORDER BY paid_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, method);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payments by method: " + e.getMessage());
        }
        return payments;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Payment payment) {
        String sql = "UPDATE " + TABLE + 
                     " SET order_id = ?, amount = ?, method = ?, status = ?, notes = ? " +
                     "WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, payment.getOrderId());
            ps.setDouble(2, payment.getAmount());
            ps.setInt(3, payment.getMethod());
            ps.setInt(4, payment.getStatus());
            ps.setString(5, payment.getNotes());
            ps.setInt(6, payment.getPaymentId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật status
     */
    public boolean updateStatus(int paymentId, int status) {
        String sql = "UPDATE " + TABLE + " SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ps.setInt(2, paymentId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
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
            System.err.println("Error counting payments: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Payment> search(String criteria) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE CAST(order_id AS CHAR) LIKE ? OR notes LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching payments: " + e.getMessage());
        }
        return payments;
    }
    
    // ============ STATISTICS ============
    
    /**
     * Lấy tổng revenue theo method
     */
    public double getTotalRevenueByMethod(int method) {
        String sql = "SELECT SUM(amount) FROM " + TABLE + " WHERE method = ? AND status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, method);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy tổng revenue
     */
    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) FROM " + TABLE + " WHERE status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        return 0;
    }
    
    // ============ HELPER METHODS ============
    
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setMethod(rs.getInt("method"));
        payment.setStatus(rs.getInt("status"));
        payment.setNotes(rs.getString("notes"));
        payment.setPaidAt(rs.getTimestamp("paid_at"));
        return payment;
    }
}