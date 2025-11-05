package service;

import dao.UserDAO;
import dao.EmployeeDAO;
import entity.User;
import entity.Employee;

/**
 * AuthService - Authentication & Authorization Service
 * Path: Source Packages/service/AuthService.java
 * 
 * Chức năng:
 * - Xác thực đăng nhập (login)
 * - Quản lý session hiện tại
 * - Kiểm tra quyền truy cập
 * - Đăng xuất (logout)
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class AuthService {
    
    private static AuthService instance;
    private UserDAO userDAO;
    private EmployeeDAO employeeDAO;
    
    // Current logged in user
    private User currentUser;
    private Employee currentEmployee;
    
    // ============ SINGLETON ============
    
    private AuthService() {
        this.userDAO = new UserDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    // ============ AUTHENTICATION ============
    
    /**
     * Đăng nhập hệ thống
     * @param username Tên đăng nhập
     * @param password Mật khẩu (plain text)
     * @return User nếu thành công, null nếu thất bại
     */
    public User login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.err.println("❌ Username cannot be empty");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("❌ Password cannot be empty");
            return null;
        }
        
        try {
            // Get user from database
            User user = userDAO.getByUsername(username.trim());
            
            if (user == null) {
                System.err.println("❌ User not found: " + username);
                return null;
            }
            
            // Check account status
            if (!user.isActive()) {
                System.err.println("❌ Account is locked: " + username);
                return null;
            }
            
            // Verify password (TODO: Should use hashed password)
            if (!password.equals(user.getPassword())) {
                System.err.println("❌ Invalid password for user: " + username);
                return null;
            }
            
            // Set current user
            this.currentUser = user;
            
            // Load employee info if user is employee
            if (user.getRole() >= 1 && user.getRole() <= 3) {
                this.currentEmployee = employeeDAO.getByUserId(user.getUserId());
            }
            
            System.out.println("✅ Login successful: " + username + " (" + user.getRoleName() + ")");
            return user;
            
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Đăng xuất
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("👋 Logout: " + currentUser.getUsername());
        }
        this.currentUser = null;
        this.currentEmployee = null;
    }
    
    /**
     * Kiểm tra đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    // ============ SESSION MANAGEMENT ============
    
    /**
     * Lấy user hiện tại
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Lấy employee hiện tại
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    /**
     * Lấy user ID hiện tại
     */
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    
    /**
     * Lấy username hiện tại
     */
    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    /**
     * Lấy role hiện tại
     */
    public int getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : -1;
    }
    
    /**
     * Lấy employee ID hiện tại
     */
    public int getCurrentEmployeeId() {
        return currentEmployee != null ? currentEmployee.getEmployeeId() : -1;
    }
    
    // ============ AUTHORIZATION ============
    
    /**
     * Kiểm tra user có quyền admin không
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Kiểm tra user có quyền manager không
     */
    public boolean isManager() {
        return currentUser != null && currentUser.isManager();
    }
    
    /**
     * Kiểm tra user có quyền cashier không
     */
    public boolean isCashier() {
        return currentUser != null && currentUser.isCashier();
    }
    
    /**
     * Kiểm tra user có quyền chef không
     */
    public boolean isChef() {
        return currentUser != null && currentUser.isChef();
    }
    
    /**
     * Kiểm tra user có quyền customer không
     */
    public boolean isCustomer() {
        return currentUser != null && currentUser.isCustomer();
    }
    
    /**
     * Kiểm tra user có role cụ thể không
     */
    public boolean hasRole(int role) {
        return currentUser != null && currentUser.getRole() == role;
    }
    
    /**
     * Kiểm tra user có một trong các roles không
     */
    public boolean hasAnyRole(int... roles) {
        if (currentUser == null) return false;
        
        for (int role : roles) {
            if (currentUser.getRole() == role) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Kiểm tra user có quyền truy cập chức năng không
     * @param requiredRole Role tối thiểu cần thiết
     */
    public boolean canAccess(int requiredRole) {
        if (currentUser == null) return false;
        
        // Admin có thể truy cập mọi thứ
        if (currentUser.isAdmin()) return true;
        
        // Check role
        return currentUser.getRole() == requiredRole;
    }
    
    // ============ PASSWORD MANAGEMENT ============
    
    /**
     * Đổi mật khẩu
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            System.err.println("❌ No user logged in");
            return false;
        }
        
        // Validate
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            System.err.println("❌ Old password cannot be empty");
            return false;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            System.err.println("❌ New password must be at least 6 characters");
            return false;
        }
        
        // Verify old password
        if (!oldPassword.equals(currentUser.getPassword())) {
            System.err.println("❌ Old password is incorrect");
            return false;
        }
        
        // Update password
        boolean success = userDAO.updatePassword(currentUser.getUserId(), newPassword);
        
        if (success) {
            currentUser.setPassword(newPassword);
            System.out.println("✅ Password changed successfully");
        }
        
        return success;
    }
    
    /**
     * Reset mật khẩu (chỉ admin)
     */
    public boolean resetPassword(int userId, String newPassword) {
        if (!isAdmin()) {
            System.err.println("❌ Only admin can reset password");
            return false;
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("❌ Password must be at least 6 characters");
            return false;
        }
        
        return userDAO.updatePassword(userId, newPassword);
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate username format
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // Username: 3-20 ký tự, chỉ chữ cái, số, underscore
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * Validate password format
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Password: tối thiểu 6 ký tự
        return password.length() >= 6;
    }
    
    /**
     * Validate email format
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // ============ UTILITY ============
    
    /**
     * Get display name
     */
    public String getCurrentDisplayName() {
        if (currentEmployee != null) {
            return currentEmployee.getName();
        }
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return "Guest";
    }
    
    /**
     * Force logout (for admin)
     */
    public void forceLogout() {
        this.currentUser = null;
        this.currentEmployee = null;
    }
    
    /**
     * Refresh current user data
     */
    public void refreshCurrentUser() {
        if (currentUser != null) {
            User updated = userDAO.getById(currentUser.getUserId());
            if (updated != null) {
                this.currentUser = updated;
                
                if (updated.getRole() >= 1 && updated.getRole() <= 3) {
                    this.currentEmployee = employeeDAO.getByUserId(updated.getUserId());
                }
            }
        }
    }
}