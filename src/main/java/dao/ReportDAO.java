package dao;

import config.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ReportDAO - Quản lý Reports và Analytics
 * Path: Source Packages/dao/ReportDAO.java
 */
public class ReportDAO {
    
    // ============ SALES REPORTS ============
    
    /**
     * Lấy total revenue theo ngày
     */
    public double getTotalRevenueByDate(String date) {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE DATE(created_at) = ? AND status != 6";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting revenue by date: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy total revenue trong range ngày
     */
    public double getTotalRevenueByDateRange(String startDate, String endDate) {
        String sql = "SELECT SUM(total_amount) FROM orders " +
                     "WHERE DATE(created_at) BETWEEN ? AND ? AND status != 6";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting revenue by date range: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy số lượng order theo ngày
     */
    public int getTotalOrdersByDate(String date) {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order count by date: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy top best-selling products
     */
    public Map<String, Integer> getTopSellingProducts(int limit) {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT p.name, SUM(oi.quantity) as total_sold " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "GROUP BY oi.product_id ORDER BY total_sold DESC LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("total_sold"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting top selling products: " + e.getMessage());
        }
        return result;
    }
    
    // ============ EMPLOYEE REPORTS ============
    
    /**
     * Lấy tổng số employee theo role
     */
    public int getEmployeeCountByRole(int role) {
        String sql = "SELECT COUNT(*) FROM employees WHERE role = ? AND status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee count by role: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy tổng salary theo role
     */
    public double getTotalSalaryByRole(int role) {
        String sql = "SELECT SUM(salary) FROM employees WHERE role = ? AND status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total salary by role: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy tổng số active employees
     */
    public int getTotalActiveEmployees() {
        String sql = "SELECT COUNT(*) FROM employees WHERE status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total active employees: " + e.getMessage());
        }
        return 0;
    }
    
    // ============ INGREDIENT REPORTS ============
    
    /**
     * Lấy low stock ingredients
     */
    public int getLowStockIngredientCount() {
        String sql = "SELECT COUNT(*) FROM materials WHERE quantity < 10";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting low stock ingredient count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy out of stock ingredients
     */
    public int getOutOfStockIngredientCount() {
        String sql = "SELECT COUNT(*) FROM materials WHERE quantity <= 0";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting out of stock ingredient count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy expired ingredients
     */
    public int getExpiredIngredientCount() {
        String sql = "SELECT COUNT(*) FROM materials WHERE expiry_date < CURDATE()";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expired ingredient count: " + e.getMessage());
        }
        return 0;
    }
    
    // ============ ORDER STATUS REPORTS ============
    
    /**
     * Lấy số lượng order theo status
     */
    public int getOrderCountByStatus(int status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order count by status: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy số lượng paid orders
     */
    public int getPaidOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE payment_status = 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting paid order count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy số lượng unpaid orders
     */
    public int getUnpaidOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE payment_status = 0";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting unpaid order count: " + e.getMessage());
        }
        return 0;
    }
    
    // ============ DASHBOARD STATISTICS ============
    
    /**
     * Lấy tất cả statistics cho dashboard
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("totalOrders", getTotalOrdersByDate(java.time.LocalDate.now().toString()));
            stats.put("totalRevenue", getTotalRevenueByDate(java.time.LocalDate.now().toString()));
            stats.put("totalEmployees", getTotalActiveEmployees());
            stats.put("lowStockItems", getLowStockIngredientCount());
            stats.put("paidOrders", getPaidOrderCount());
            stats.put("unpaidOrders", getUnpaidOrderCount());
            stats.put("topProducts", getTopSellingProducts(5));
            
        } catch (Exception e) {
            System.err.println("Error getting dashboard statistics: " + e.getMessage());
        }
        
        return stats;
    }
}