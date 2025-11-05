package dao;

import config.DBConnection;
import entity.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CartItemDAO - Quản lý CartItem trong database
 * Path: Source Packages/dao/CartItemDAO.java
 * 
 * Chức năng:
 * - CRUD operations cho CartItem
 * - Quản lý items trong giỏ hàng
 * - Cập nhật số lượng và subtotal
 * - Hỗ trợ tìm kiếm theo cart và product
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CartItemDAO extends BaseDAO<CartItem> {
    
    private static final String TABLE = "cart_items";
    
    // ============ CREATE ============
    
    /**
     * Thêm item vào cart
     * @param cartItem CartItem object
     * @return true nếu thành công
     */
    @Override
    public boolean create(CartItem cartItem) {
        String sql = "INSERT INTO " + TABLE + 
                     " (cart_id, product_id, product_name, unit_price, quantity, subtotal, image_url, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());
            ps.setString(3, cartItem.getProductName());
            ps.setDouble(4, cartItem.getUnitPrice());
            ps.setInt(5, cartItem.getQuantity());
            ps.setDouble(6, cartItem.getSubtotal());
            ps.setString(7, cartItem.getImageUrl());
            ps.setString(8, cartItem.getNotes());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cartItem.setCartItemId(generatedKeys.getInt(1));
                }
                System.out.println("✅ Added item to cart: " + cartItem.getProductName());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error creating cart item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thêm hoặc cập nhật item trong cart
     * Nếu product đã có trong cart thì cộng thêm quantity
     * @param cartItem CartItem object
     * @return true nếu thành công
     */
    public boolean addOrUpdate(CartItem cartItem) {
        // Kiểm tra xem product đã có trong cart chưa
        CartItem existingItem = getByCartAndProduct(
            cartItem.getCartId(), 
            cartItem.getProductId()
        );
        
        if (existingItem != null) {
            // Nếu đã có, cộng thêm quantity
            int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setSubtotal(existingItem.getUnitPrice() * newQuantity);
            return update(existingItem);
        } else {
            // Nếu chưa có, thêm mới
            return create(cartItem);
        }
    }
    
    // ============ READ ============
    
    /**
     * Lấy cart item theo ID
     * @param id CartItem ID
     * @return CartItem object hoặc null
     */
    @Override
    public CartItem getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCartItem(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting cart item by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả cart items
     * @return List các CartItem
     */
    @Override
    public List<CartItem> getAll() {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cartItems.add(mapResultSetToCartItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting all cart items: " + e.getMessage());
            e.printStackTrace();
        }
        return cartItems;
    }
    
    /**
     * Lấy tất cả items trong một cart
     * @param cartId Cart ID
     * @return List các CartItem
     */
    public List<CartItem> getByCartId(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE cart_id = ? ORDER BY cart_item_id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                cartItems.add(mapResultSetToCartItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting cart items by cart ID: " + e.getMessage());
            e.printStackTrace();
        }
        return cartItems;
    }
    
    /**
     * Lấy cart item theo cart_id và product_id
     * @param cartId Cart ID
     * @param productId Product ID
     * @return CartItem object hoặc null
     */
    public CartItem getByCartAndProduct(int cartId, int productId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE cart_id = ? AND product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCartItem(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting cart item by cart and product: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Kiểm tra xem product đã có trong cart chưa
     * @param cartId Cart ID
     * @param productId Product ID
     * @return true nếu đã có
     */
    public boolean existsInCart(int cartId, int productId) {
        return getByCartAndProduct(cartId, productId) != null;
    }
    
    // ============ UPDATE ============
    
    /**
     * Cập nhật cart item
     * @param cartItem CartItem object
     * @return true nếu thành công
     */
    @Override
    public boolean update(CartItem cartItem) {
        String sql = "UPDATE " + TABLE + 
                     " SET cart_id = ?, product_id = ?, product_name = ?, unit_price = ?, " +
                     "quantity = ?, subtotal = ?, image_url = ?, notes = ? " +
                     "WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());
            ps.setString(3, cartItem.getProductName());
            ps.setDouble(4, cartItem.getUnitPrice());
            ps.setInt(5, cartItem.getQuantity());
            ps.setDouble(6, cartItem.getSubtotal());
            ps.setString(7, cartItem.getImageUrl());
            ps.setString(8, cartItem.getNotes());
            ps.setInt(9, cartItem.getCartItemId());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Updated cart item: " + cartItem.getCartItemId());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating cart item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật số lượng của cart item
     * Tự động tính lại subtotal
     * @param cartItemId CartItem ID
     * @param quantity Số lượng mới
     * @return true nếu thành công
     */
    public boolean updateQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            // Nếu quantity <= 0 thì xóa item
            return delete(cartItemId);
        }
        
        String sql = "UPDATE " + TABLE + 
                     " SET quantity = ?, subtotal = unit_price * ? WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, quantity);
            ps.setInt(3, cartItemId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Updated quantity for cart item: " + cartItemId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật số lượng theo cart_id và product_id
     * @param cartId Cart ID
     * @param productId Product ID
     * @param quantity Số lượng mới
     * @return true nếu thành công
     */
    public boolean updateQuantityByCartAndProduct(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            // Nếu quantity <= 0 thì xóa item
            return deleteByCartAndProduct(cartId, productId);
        }
        
        String sql = "UPDATE " + TABLE + 
                     " SET quantity = ?, subtotal = unit_price * ? " +
                     "WHERE cart_id = ? AND product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, quantity);
            ps.setInt(3, cartId);
            ps.setInt(4, productId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Updated quantity for product " + productId + " in cart " + cartId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating quantity by cart and product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tăng số lượng của item
     * @param cartItemId CartItem ID
     * @param amount Số lượng tăng thêm
     * @return true nếu thành công
     */
    public boolean increaseQuantity(int cartItemId, int amount) {
        CartItem item = getById(cartItemId);
        if (item != null) {
            return updateQuantity(cartItemId, item.getQuantity() + amount);
        }
        return false;
    }
    
    /**
     * Giảm số lượng của item
     * @param cartItemId CartItem ID
     * @param amount Số lượng giảm đi
     * @return true nếu thành công
     */
    public boolean decreaseQuantity(int cartItemId, int amount) {
        CartItem item = getById(cartItemId);
        if (item != null) {
            int newQuantity = item.getQuantity() - amount;
            return updateQuantity(cartItemId, newQuantity);
        }
        return false;
    }
    
    /**
     * Cập nhật notes cho cart item
     * @param cartItemId CartItem ID
     * @param notes Ghi chú mới
     * @return true nếu thành công
     */
    public boolean updateNotes(int cartItemId, String notes) {
        String sql = "UPDATE " + TABLE + " SET notes = ? WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, notes);
            ps.setInt(2, cartItemId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating notes: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    /**
     * Xóa cart item
     * @param id CartItem ID
     * @return true nếu thành công
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Deleted cart item: " + id);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting cart item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả items của một cart
     * @param cartId Cart ID
     * @return true nếu thành công
     */
    public boolean deleteByCartId(int cartId) {
        String sql = "DELETE FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            int deleted = ps.executeUpdate();
            
            if (deleted > 0) {
                System.out.println("✅ Deleted " + deleted + " items from cart: " + cartId);
            }
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting cart items by cart ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa cart item theo cart_id và product_id
     * @param cartId Cart ID
     * @param productId Product ID
     * @return true nếu thành công
     */
    public boolean deleteByCartAndProduct(int cartId, int productId) {
        String sql = "DELETE FROM " + TABLE + " WHERE cart_id = ? AND product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Deleted product " + productId + " from cart " + cartId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting cart item by cart and product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ UTILITY ============
    
    /**
     * Kiểm tra cart item có tồn tại không
     * @param id CartItem ID
     * @return true nếu tồn tại
     */
    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE cart_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking cart item exists: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Đếm tổng số cart items
     * @return Số lượng cart items
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
            System.err.println("❌ Error counting cart items: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Đếm số lượng items trong một cart
     * @param cartId Cart ID
     * @return Số lượng items
     */
    public int countByCartId(int cartId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting cart items by cart ID: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Tìm kiếm cart items
     * @param criteria Từ khóa tìm kiếm
     * @return List các CartItem
     */
    @Override
    public List<CartItem> search(String criteria) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE product_name LIKE ? OR notes LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                cartItems.add(mapResultSetToCartItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error searching cart items: " + e.getMessage());
        }
        return cartItems;
    }
    
    // ============ STATISTICS ============
    
    /**
     * Tính tổng giá trị của một cart
     * @param cartId Cart ID
     * @return Tổng tiền
     */
    public double calculateCartTotal(int cartId) {
        String sql = "SELECT COALESCE(SUM(subtotal), 0) FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating cart total: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Tính tổng số lượng items trong cart
     * @param cartId Cart ID
     * @return Tổng số lượng
     */
    public int getTotalQuantity(int cartId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM " + TABLE + " WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting total quantity: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Lấy top N sản phẩm được thêm vào giỏ nhiều nhất
     * @param limit Số lượng top items
     * @return List các CartItem
     */
    public List<CartItem> getTopAddedProducts(int limit) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT product_id, product_name, SUM(quantity) as total_qty " +
                     "FROM " + TABLE + " GROUP BY product_id, product_name " +
                     "ORDER BY total_qty DESC LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setQuantity(rs.getInt("total_qty"));
                items.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting top added products: " + e.getMessage());
        }
        return items;
    }
    
    // ============ HELPER METHODS ============
    
    /**
     * Map ResultSet sang CartItem object
     * @param rs ResultSet
     * @return CartItem object
     * @throws SQLException
     */
    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(rs.getInt("cart_item_id"));
        cartItem.setCartId(rs.getInt("cart_id"));
        cartItem.setProductId(rs.getInt("product_id"));
        cartItem.setProductName(rs.getString("product_name"));
        cartItem.setUnitPrice(rs.getDouble("unit_price"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setSubtotal(rs.getDouble("subtotal"));
        cartItem.setImageUrl(rs.getString("image_url"));
        cartItem.setNotes(rs.getString("notes"));
        return cartItem;
    }
}