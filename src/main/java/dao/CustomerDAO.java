package dao;

import config.DBConnection;
import entity.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO - Quản lý Customer trong database
 * Path: Source Packages/dao/CustomerDAO.java
 */
public class CustomerDAO extends BaseDAO<Customer> {
    
    private static final String TABLE = "customers";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Customer customer) {
        String sql = "INSERT INTO " + TABLE + " (name, phone_number, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Customer getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy customer theo phone number
     */
    public Customer getByPhoneNumber(String phone) {
        String sql = "SELECT * FROM " + TABLE + " WHERE phone_number = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by phone: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy customer theo email
     */
    public Customer getByEmail(String email) {
        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by email: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        return customers;
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE " + TABLE + 
                     " SET name = ?, phone_number = ?, email = ?, updated_at = NOW() " +
                     "WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getCustomerId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
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
            System.err.println("Error counting customers: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Customer> search(String criteria) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE name LIKE ? OR phone_number LIKE ? OR email LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        return customers;
    }
    
    // ============ HELPER METHODS ============
    
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        customer.setUpdatedAt(rs.getTimestamp("updated_at"));
        return customer;
    }
}