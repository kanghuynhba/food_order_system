package service;

import dao.UserDAO;
import entity.User;
import java.util.List;

/**
 * UserService - User Account Management Service
 * Path: Source Packages/service/UserService.java
 * 
 * Chức năng:
 * - Đăng ký user mới
 * - Quản lý tài khoản user
 * - Cập nhật profile
 * - Đổi mật khẩu
 * - Lock/Unlock account
 * - Quản lý roles
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class UserService {
    
    private static UserService instance;
    private UserDAO userDAO;
    
    // ============ SINGLETON ============
    
    private UserService() {
        this.userDAO = new UserDAO();
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    // ============ USER REGISTRATION ============
    
    /**
     * Đăng ký user mới
     */
    public boolean registerUser(String username, String password, String email, int role) {
        try {
            // Validate input
            if (!validateUsername(username)) {
                System.err.println("❌ Invalid username format");
                return false;
            }
            
            if (!validatePassword(password)) {
                System.err.println("❌ Password must be at least 6 characters");
                return false;
            }
            
            if (!validateEmail(email)) {
                System.err.println("❌ Invalid email format");
                return false;
            }
            
            // Check if username exists
            if (userDAO.getByUsername(username) != null) {
                System.err.println("❌ Username already exists");
                return false;
            }
            
            // Check if email exists
            if (userDAO.getByEmail(email) != null) {
                System.err.println("❌ Email already exists");
                return false;
            }
            
            // Create user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // TODO: Hash password
            user.setEmail(email);
            user.setRole(role);
            user.setStatus(1); // Active by default
            
            boolean success = userDAO.create(user);
            
            if (success) {
                System.out.println("✅ User registered: " + username);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Đăng ký customer (role = 4)
     */
    public boolean registerCustomer(String username, String password, String email) {
        return registerUser(username, password, email, 4);
    }
    
    /**
     * Đăng ký employee user
     */
    public boolean registerEmployee(String username, String password, String email, int role) {
        if (role < 0 || role > 3) {
            System.err.println("❌ Invalid employee role (0: Admin, 1: Manager, 2: Cashier, 3: Chef)");
            return false;
        }
        return registerUser(username, password, email, role);
    }
    
    // ============ USER MANAGEMENT ============
    
    /**
     * Lấy user theo ID
     */
    public User getUserById(int userId) {
        try {
            return userDAO.getById(userId);
        } catch (Exception e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy user theo username
     */
    public User getUserByUsername(String username) {
        try {
            return userDAO.getByUsername(username);
        } catch (Exception e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy user theo email
     */
    public User getUserByEmail(String email) {
        try {
            return userDAO.getByEmail(email);
        } catch (Exception e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả users
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all users: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy users theo role
     */
    public List<User> getUsersByRole(int role) {
        try {
            return userDAO.getByRole(role);
        } catch (Exception e) {
            System.err.println("❌ Error getting users by role: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Tìm kiếm users
     */
    public List<User> searchUsers(String keyword) {
        try {
            return userDAO.search(keyword);
        } catch (Exception e) {
            System.err.println("❌ Error searching users: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ UPDATE OPERATIONS ============
    
    /**
     * Cập nhật thông tin user
     */
    public boolean updateUser(User user) {
        try {
            if (user == null || user.getUserId() <= 0) {
                System.err.println("❌ Invalid user");
                return false;
            }
            
            if (!validateUser(user)) {
                return false;
            }
            
            boolean success = userDAO.update(user);
            
            if (success) {
                System.out.println("✅ User updated: " + user.getUsername());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật email
     */
    public boolean updateEmail(int userId, String newEmail) {
        try {
            if (!validateEmail(newEmail)) {
                System.err.println("❌ Invalid email format");
                return false;
            }
            
            // Check if email exists (except current user)
            User existingUser = userDAO.getByEmail(newEmail);
            if (existingUser != null && existingUser.getUserId() != userId) {
                System.err.println("❌ Email already exists");
                return false;
            }
            
            User user = userDAO.getById(userId);
            if (user == null) {
                System.err.println("❌ User not found");
                return false;
            }
            
            user.setEmail(newEmail);
            boolean success = userDAO.update(user);
            
            if (success) {
                System.out.println("✅ Email updated for user: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật role
     */
    public boolean updateRole(int userId, int newRole) {
        try {
            if (newRole < 0 || newRole > 4) {
                System.err.println("❌ Invalid role");
                return false;
            }
            
            User user = userDAO.getById(userId);
            if (user == null) {
                System.err.println("❌ User not found");
                return false;
            }
            
            user.setRole(newRole);
            boolean success = userDAO.update(user);
            
            if (success) {
                System.out.println("✅ Role updated for user: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating role: " + e.getMessage());
            return false;
        }
    }
    
    // ============ PASSWORD MANAGEMENT ============
    
    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.getById(userId);
            if (user == null) {
                System.err.println("❌ User not found");
                return false;
            }
            
            // Verify old password
            if (!user.getPassword().equals(oldPassword)) {
                System.err.println("❌ Old password is incorrect");
                return false;
            }
            
            // Validate new password
            if (!validatePassword(newPassword)) {
                System.err.println("❌ New password must be at least 6 characters");
                return false;
            }
            
            // Update password
            boolean success = userDAO.updatePassword(userId, newPassword);
            
            if (success) {
                System.out.println("✅ Password changed for user: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error changing password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reset mật khẩu (admin only)
     */
    public boolean resetPassword(int userId, String newPassword) {
        try {
            if (!validatePassword(newPassword)) {
                System.err.println("❌ Password must be at least 6 characters");
                return false;
            }
            
            boolean success = userDAO.updatePassword(userId, newPassword);
            
            if (success) {
                System.out.println("✅ Password reset for user: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error resetting password: " + e.getMessage());
            return false;
        }
    }
    
    // ============ ACCOUNT STATUS MANAGEMENT ============
    
    /**
     * Kích hoạt tài khoản
     */
    public boolean activateAccount(int userId) {
        try {
            boolean success = userDAO.updateStatus(userId, 1); // 1 = Active
            
            if (success) {
                System.out.println("✅ Account activated: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error activating account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Khóa tài khoản
     */
    public boolean lockAccount(int userId) {
        try {
            boolean success = userDAO.updateStatus(userId, 2); // 2 = Locked
            
            if (success) {
                System.out.println("✅ Account locked: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error locking account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Toggle account status
     */
    public boolean toggleAccountStatus(int userId) {
        try {
            User user = userDAO.getById(userId);
            if (user == null) {
                return false;
            }
            
            if (user.isActive()) {
                return lockAccount(userId);
            } else {
                return activateAccount(userId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error toggling account status: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE OPERATIONS ============
    
    /**
     * Xóa user
     */
    public boolean deleteUser(int userId) {
        try {
            User user = userDAO.getById(userId);
            if (user == null) {
                System.err.println("❌ User not found");
                return false;
            }
            
            boolean success = userDAO.delete(userId);
            
            if (success) {
                System.out.println("✅ User deleted: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate username format
     */
    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // Username: 3-20 ký tự, chỉ chữ cái, số, underscore
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * Validate password format
     */
    public boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Password: tối thiểu 6 ký tự
        return password.length() >= 6;
    }
    
    /**
     * Validate email format
     */
    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Validate user object
     */
    public boolean validateUser(User user) {
        if (user == null) return false;
        if (!validateUsername(user.getUsername())) return false;
        if (!validateEmail(user.getEmail())) return false;
        if (user.getRole() < 0 || user.getRole() > 4) return false;
        
        return true;
    }
    
    /**
     * Kiểm tra username có tồn tại không
     */
    public boolean usernameExists(String username) {
        return userDAO.getByUsername(username) != null;
    }
    
    /**
     * Kiểm tra email có tồn tại không
     */
    public boolean emailExists(String email) {
        return userDAO.getByEmail(email) != null;
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm tổng số users
     */
    public int getTotalUserCount() {
        try {
            return userDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting users: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Đếm users theo role
     */
    public int countUsersByRole(int role) {
        return getUsersByRole(role).size();
    }
    
    /**
     * Đếm active users
     */
    public int getActiveUserCount() {
        return (int) getAllUsers().stream()
            .filter(User::isActive)
            .count();
    }
    
    /**
     * Đếm locked users
     */
    public int getLockedUserCount() {
        return (int) getAllUsers().stream()
            .filter(u -> !u.isActive())
            .count();
    }
    
    /**
     * Lấy danh sách admins
     */
    public List<User> getAdmins() {
        return getUsersByRole(0);
    }
    
    /**
     * Lấy danh sách managers
     */
    public List<User> getManagers() {
        return getUsersByRole(1);
    }
    
    /**
     * Lấy danh sách cashiers
     */
    public List<User> getCashiers() {
        return getUsersByRole(2);
    }
    
    /**
     * Lấy danh sách chefs
     */
    public List<User> getChefs() {
        return getUsersByRole(3);
    }
    
    /**
     * Lấy danh sách customers
     */
    public List<User> getCustomers() {
        return getUsersByRole(4);
    }
    
    // ============ UTILITY ============
    
    /**
     * Get user info string
     */
    public String getUserInfo(int userId) {
        User user = getUserById(userId);
        if (user == null) {
            return "User not found";
        }
        
        return String.format(
            "ID: %d | Username: %s | Email: %s | Role: %s | Status: %s",
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRoleName(),
            user.getStatusName()
        );
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(int userId) {
        return userDAO.exists(userId);
    }
    
    /**
     * Get role name
     */
    public String getRoleName(int role) {
        return switch (role) {
            case 0 -> "Admin";
            case 1 -> "Manager";
            case 2 -> "Cashier";
            case 3 -> "Chef";
            case 4 -> "Customer";
            default -> "Unknown";
        };
    }
}