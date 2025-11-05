package dao;

import config.DBConnection;
import entity.Cart;
import entity.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CartDAO - Quản lý Cart trong database
 * Path: Source Packages/dao/CartDAO.java
 * 
 * Chức năng:
 * - CRUD operations cho Cart
 * - Quản lý trạng thái giỏ hàng (active, checked_out, abandoned)
 * - Tự động load CartItems khi lấy Cart
 * - Hỗ trợ tìm kiếm và thống kê
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CartDAO extends BaseDAO<Cart> {
    
    private static final String TABLE = "carts";
    
    // ============ CREATE ============
    
    /**
     * Tạo cart mới trong database
     * @param cart Cart object cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    @Override
    public boolean create(Cart cart) {
        String sql = "INSERT INTO " + TABLE + " (customer_id, total_amount, status) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, cart.getCustomerId());
            ps.setDouble(2, cart.getTotalAmount());
            ps.setInt(3, cart.getStatus());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cart.setCartId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error creating cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ READ ============
    
    /**
     * Lấy cart theo ID
     * @param id Cart ID
     * @return Cart object hoặc null nếu không tìm thấy
     */
    @Override
    public Cart getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                // Load cart items
                cart.setItems(loadCartItems(cart.getCartId()));
                return cart;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting cart by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả carts
     * @return List các Cart
     */
    @Override
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                cart.setItems(loadCartItems(cart.getCartId()));
                carts.add(cart);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting all carts: " + e.getMessage());
            e.printStackTrace();
        }
        return carts;
    }
    
    /**
     * Lấy cart theo customer_id (cart gần nhất)
     * @param customerId Customer ID
     * @return Cart object hoặc null
     */
    public Cart getByCustomerId(int customerId) {
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE customer_id = ? AND status = 0 " +
                     "ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                cart.setItems(loadCartItems(cart.getCartId()));
                return cart;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting cart by customer ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy active cart của customer (status = 0)
     * Nếu không có, tự động tạo cart mới
     * @param customerId Customer ID
     * @return Active Cart object
     */
    public Cart getOrCreateActiveCart(int customerId) {
        Cart cart = getActiveCart(customerId);
        
        if (cart == null) {
            // Tạo cart mới
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setTotalAmount(0.0);
            cart.setStatus(0); // Active
            
            if (create(cart)) {
                System.out.println("✅ Created new cart for customer: " + customerId);
            } else {
                System.err.println("❌ Failed to create cart for customer: " + customerId);
            }
        }
        
        return cart;
    }
    
    /**
     * Lấy active cart của customer
     * @param customerId Customer ID
     * @return Active Cart hoặc null
     */
    public Cart getActiveCart(int customerId) {
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE customer_id = ? AND status = 0 LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                cart.setItems(loadCartItems(cart.getCartId()));
                return cart;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting active cart: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy carts theo status
     * @param status Cart status (0: active, 1: checked_out, 2: abandoned)
     * @return List các Cart
     */
    public List<Cart> getByStatus(int status) {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE status = ? ORDER BY updated_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                cart.setItems(loadCartItems(cart.getCartId()));
                carts.add(cart);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting carts by status: " + e.getMessage());
            e.printStackTrace();
        }
        return carts;
    }
    
    // ============ UPDATE ============
    
    /**
     * Cập nhật thông tin cart
     * @param cart Cart object cần update
     * @return true nếu thành công
     */
    @Override
    public boolean update(Cart cart) {
        String sql = "UPDATE " + TABLE + 
                     " SET customer_id = ?, total_amount = ?, status = ?, updated_at = NOW() " +
                     "WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cart.getCustomerId());
            ps.setDouble(2, cart.getTotalAmount());
            ps.setInt(3, cart.getStatus());
            ps.setInt(4, cart.getCartId());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Updated cart: " + cart.getCartId());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật total amount của cart
     * @param cartId Cart ID
     * @param totalAmount Tổng tiền mới
     * @return true nếu thành công
     */
    public boolean updateTotalAmount(int cartId, double totalAmount) {
        String sql = "UPDATE " + TABLE + 
                     " SET total_amount = ?, updated_at = NOW() WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, totalAmount);
            ps.setInt(2, cartId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating total amount: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật status của cart
     * @param cartId Cart ID
     * @param status Status mới (0: active, 1: checked_out, 2: abandoned)
     * @return true nếu thành công
     */
    public boolean updateStatus(int cartId, int status) {
        String sql = "UPDATE " + TABLE + 
                     " SET status = ?, updated_at = NOW() WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ps.setInt(2, cartId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Updated cart status: " + cartId + " -> " + status);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating cart status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tính lại và cập nhật total amount dựa trên cart items
     * @param cartId Cart ID
     * @return true nếu thành công
     */
    public boolean recalculateTotalAmount(int cartId) {
        String sql = "UPDATE " + TABLE + " SET total_amount = " +
                     "(SELECT COALESCE(SUM(subtotal), 0) FROM cart_items WHERE cart_id = ?), " +
                     "updated_at = NOW() WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ps.setInt(2, cartId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error recalculating total amount: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ DELETE ============
    
    /**
     * Xóa cart và tất cả cart items
     * @param id Cart ID
     * @return true nếu thành công
     */
    @Override
    public boolean delete(int id) {
        Connection conn = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Xóa cart items trước
            CartItemDAO cartItemDAO = new CartItemDAO();
            cartItemDAO.deleteByCartId(id);
            
            // Xóa cart
            String sql = "DELETE FROM " + TABLE + " WHERE cart_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            
            conn.commit(); // Commit transaction
            ps.close();
            
            if (result > 0) {
                System.out.println("✅ Deleted cart: " + id);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting cart: " + e.getMessage());
            e.printStackTrace();
            
            // Rollback nếu có lỗi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Xóa các carts abandoned (status = 2) cũ hơn X ngày
     * @param daysOld Số ngày
     * @return Số lượng carts đã xóa
     */
    public int deleteAbandonedCarts(int daysOld) {
        String sql = "DELETE FROM " + TABLE + 
                     " WHERE status = 2 AND updated_at < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, daysOld);
            int deleted = ps.executeUpdate();
            
            if (deleted > 0) {
                System.out.println("✅ Deleted " + deleted + " abandoned carts");
            }
            return deleted;
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting abandoned carts: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Xóa tất cả items trong cart (clear cart)
     * @param cartId Cart ID
     * @return true nếu thành công
     */
    public boolean clearCart(int cartId) {
        CartItemDAO cartItemDAO = new CartItemDAO();
        boolean success = cartItemDAO.deleteByCartId(cartId);
        
        if (success) {
            // Update total amount = 0
            updateTotalAmount(cartId, 0.0);
            System.out.println("✅ Cleared cart: " + cartId);
        }
        return success;
    }
    
    // ============ UTILITY ============
    
    /**
     * Kiểm tra cart có tồn tại không
     * @param id Cart ID
     * @return true nếu tồn tại
     */
    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking cart exists: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Đếm tổng số carts
     * @return Số lượng carts
     */
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
            System.err.println("❌ Error counting carts: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Đếm số cart theo status
     * @param status Cart status
     * @return Số lượng carts
     */
    public int countByStatus(int status) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting carts by status: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Tìm kiếm carts
     * @param criteria Từ khóa tìm kiếm
     * @return List các Cart tìm thấy
     */
    @Override
    public List<Cart> search(String criteria) {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE CAST(customer_id AS CHAR) LIKE ? OR CAST(cart_id AS CHAR) LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Cart cart = mapResultSetToCart(rs);
                cart.setItems(loadCartItems(cart.getCartId()));
                carts.add(cart);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error searching carts: " + e.getMessage());
        }
        return carts;
    }
    
    // ============ STATISTICS ============
    
    /**
     * Lấy tổng giá trị của tất cả active carts
     * @return Tổng tiền
     */
    public double getTotalActiveCartsValue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM " + TABLE + " WHERE status = 0";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting total active carts value: " + e.getMessage());
        }
        return 0.0;
    }
    
    // ============ HELPER METHODS ============
    
    /**
     * Load cart items cho một cart
     * @param cartId Cart ID
     * @return List các CartItem
     */
    private List<CartItem> loadCartItems(int cartId) {
        CartItemDAO cartItemDAO = new CartItemDAO();
        return cartItemDAO.getByCartId(cartId);
    }
    
    /**
     * Map ResultSet sang Cart object
     * @param rs ResultSet
     * @return Cart object
     * @throws SQLException
     */
    private Cart mapResultSetToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        cart.setCustomerId(rs.getInt("customer_id"));
        cart.setTotalAmount(rs.getDouble("total_amount"));
        cart.setStatus(rs.getInt("status"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));
        cart.setUpdatedAt(rs.getTimestamp("updated_at"));
        return cart;
    }
}