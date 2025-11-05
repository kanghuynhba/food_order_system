package entity;

import java.sql.Timestamp;

/**
 * User Entity - Người dùng hệ thống
 * Path: Source Packages/entity/User.java
 */
public class User {
    
    private int userId;
    private String username;
    private String password;
    private String email;
    private int role;  // 0: Admin, 1: Manager, 2: Cashier, 3: Chef, 4: Customer
    private int status;  // 1: Active, 0: Locked
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    public User() {}
    
    public User(String username, String password, String email, int role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = 1;
    }
    
    public User(int userId, String username, String password, String email, 
                int role, int status, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ HELPER METHODS ============
    
    public String getRoleName() {
        return switch (role) {
            case 0 -> "Admin";
            case 1 -> "Manager";
            case 2 -> "Cashier";
            case 3 -> "Chef";
            case 4 -> "Customer";
            default -> "Unknown";
        };
    }
    
    public String getStatusName() {
        return status == 1 ? "Active" : "Locked";
    }
    
    public boolean isActive() {
        return status == 1;
    }
    
    public boolean isAdmin() {
        return role == 0;
    }
    
    public boolean isManager() {
        return role == 1;
    }
    
    public boolean isCashier() {
        return role == 2;
    }
    
    public boolean isChef() {
        return role == 3;
    }
    
    public boolean isCustomer() {
        return role == 4;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + getRoleName() +
                ", status=" + getStatusName() +
                '}';
    }
}