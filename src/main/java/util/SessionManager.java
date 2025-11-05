package util;

import entity.User;
import entity.Employee;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * SessionManager - User Session Management
 * Path: Source Packages/util/SessionManager.java
 * 
 * Manages current user session, authentication state, and session data
 * Singleton pattern for global access
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class SessionManager {
    
    // ============ SINGLETON INSTANCE ============
    
    private static SessionManager instance;
    
    /**
     * Get singleton instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }
    
    // ============ SESSION DATA ============
    
    private User currentUser;
    private Employee currentEmployee;
    private Timestamp loginTime;
    private Timestamp lastActivityTime;
    private boolean authenticated;
    
    // Additional session data storage
    private Map<String, Object> sessionData;
    
    // ============ CONSTRUCTOR ============
    
    /**
     * Private constructor (Singleton pattern)
     */
    private SessionManager() {
        this.authenticated = false;
        this.sessionData = new HashMap<>();
    }
    
    // ============ SESSION MANAGEMENT ============
    
    /**
     * Start new session with user login
     * 
     * @param user Logged in user
     * @return true if session started successfully
     */
    public boolean startSession(User user) {
        if (user == null) {
            return false;
        }
        
        this.currentUser = user;
        this.loginTime = DateUtil.getCurrentTimestamp();
        this.lastActivityTime = DateUtil.getCurrentTimestamp();
        this.authenticated = true;
        
        System.out.println("✅ Session started for user: " + user.getUsername());
        System.out.println("   Role: " + user.getRoleName());
        System.out.println("   Login time: " + DateUtil.formatTimestamp(loginTime));
        
        return true;
    }
    
    /**
     * Start session with user and employee data
     * 
     * @param user Logged in user
     * @param employee Employee data
     * @return true if session started successfully
     */
    public boolean startSession(User user, Employee employee) {
        if (!startSession(user)) {
            return false;
        }
        
        this.currentEmployee = employee;
        System.out.println("   Employee ID: " + employee.getEmployeeId());
        System.out.println("   Employee Name: " + employee.getName());
        
        return true;
    }
    
    /**
     * End current session (logout)
     */
    public void endSession() {
        if (isAuthenticated()) {
            System.out.println("👋 Ending session for user: " + currentUser.getUsername());
            System.out.println("   Session duration: " + getSessionDuration() + " minutes");
        }
        
        this.currentUser = null;
        this.currentEmployee = null;
        this.loginTime = null;
        this.lastActivityTime = null;
        this.authenticated = false;
        this.sessionData.clear();
        
        System.out.println("✅ Session ended successfully");
    }
    
    /**
     * Update last activity time (keep session alive)
     */
    public void updateActivity() {
        this.lastActivityTime = DateUtil.getCurrentTimestamp();
    }
    
    /**
     * Check if session is still valid (not timed out)
     * 
     * @param timeoutMinutes Timeout in minutes
     * @return true if session is valid
     */
    public boolean isSessionValid(int timeoutMinutes) {
        if (!isAuthenticated() || lastActivityTime == null) {
            return false;
        }
        
        return !DateUtil.isExpired(lastActivityTime, timeoutMinutes);
    }
    
    /**
     * Check if session has timed out
     * 
     * @param timeoutMinutes Timeout in minutes
     * @return true if session timed out
     */
    public boolean isSessionExpired(int timeoutMinutes) {
        return !isSessionValid(timeoutMinutes);
    }
    
    // ============ USER ACCESS ============
    
    /**
     * Get current user
     * 
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current employee
     * 
     * @return Current employee or null if not available
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    /**
     * Get current user ID
     * 
     * @return User ID or -1 if not logged in
     */
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    
    /**
     * Get current employee ID
     * 
     * @return Employee ID or -1 if not available
     */
    public int getCurrentEmployeeId() {
        return currentEmployee != null ? currentEmployee.getEmployeeId() : -1;
    }
    
    /**
     * Get current username
     * 
     * @return Username or null if not logged in
     */
    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    /**
     * Get current user's role
     * 
     * @return User role or -1 if not logged in
     */
    public int getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : -1;
    }
    
    /**
     * Get current user's role name
     * 
     * @return Role name or "Guest" if not logged in
     */
    public String getCurrentUserRoleName() {
        return currentUser != null ? currentUser.getRoleName() : "Guest";
    }
    
    // ============ AUTHENTICATION STATE ============
    
    /**
     * Check if user is authenticated
     * 
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return authenticated && currentUser != null;
    }
    
    /**
     * Check if current user is admin
     * 
     * @return true if admin
     */
    public boolean isAdmin() {
        return isAuthenticated() && currentUser.isAdmin();
    }
    
    /**
     * Check if current user is manager
     * 
     * @return true if manager
     */
    public boolean isManager() {
        return isAuthenticated() && currentUser.isManager();
    }
    
    /**
     * Check if current user is cashier
     * 
     * @return true if cashier
     */
    public boolean isCashier() {
        return isAuthenticated() && currentUser.isCashier();
    }
    
    /**
     * Check if current user is chef
     * 
     * @return true if chef
     */
    public boolean isChef() {
        return isAuthenticated() && currentUser.isChef();
    }
    
    /**
     * Check if current user is customer
     * 
     * @return true if customer
     */
    public boolean isCustomer() {
        return isAuthenticated() && currentUser.isCustomer();
    }
    
    /**
     * Check if user has specific role
     * 
     * @param role Role code to check
     * @return true if user has this role
     */
    public boolean hasRole(int role) {
        return isAuthenticated() && currentUser.getRole() == role;
    }
    
    /**
     * Check if user has any of the specified roles
     * 
     * @param roles Array of role codes
     * @return true if user has any of these roles
     */
    public boolean hasAnyRole(int... roles) {
        if (!isAuthenticated()) {
            return false;
        }
        
        int userRole = currentUser.getRole();
        for (int role : roles) {
            if (userRole == role) {
                return true;
            }
        }
        
        return false;
    }
    
    // ============ SESSION DATA STORAGE ============
    
    /**
     * Store data in session
     * 
     * @param key Data key
     * @param value Data value
     */
    public void setAttribute(String key, Object value) {
        sessionData.put(key, value);
    }
    
    /**
     * Get data from session
     * 
     * @param key Data key
     * @return Data value or null if not found
     */
    public Object getAttribute(String key) {
        return sessionData.get(key);
    }
    
    /**
     * Get data from session with type casting
     * 
     * @param key Data key
     * @param type Expected type class
     * @return Typed data or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, Class<T> type) {
        Object value = sessionData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Remove data from session
     * 
     * @param key Data key
     */
    public void removeAttribute(String key) {
        sessionData.remove(key);
    }
    
    /**
     * Check if session has data with key
     * 
     * @param key Data key
     * @return true if exists
     */
    public boolean hasAttribute(String key) {
        return sessionData.containsKey(key);
    }
    
    /**
     * Clear all session data
     */
    public void clearAttributes() {
        sessionData.clear();
    }
    
    // ============ SESSION INFO ============
    
    /**
     * Get login time
     * 
     * @return Login timestamp or null if not logged in
     */
    public Timestamp getLoginTime() {
        return loginTime;
    }
    
    /**
     * Get last activity time
     * 
     * @return Last activity timestamp or null if not logged in
     */
    public Timestamp getLastActivityTime() {
        return lastActivityTime;
    }
    
    /**
     * Get session duration in minutes
     * 
     * @return Duration in minutes or 0 if not logged in
     */
    public long getSessionDuration() {
        if (loginTime == null) {
            return 0;
        }
        
        return DateUtil.minutesBetween(loginTime, DateUtil.getCurrentTimestamp());
    }
    
    /**
     * Get idle time in minutes (time since last activity)
     * 
     * @return Idle time in minutes or 0 if not logged in
     */
    public long getIdleTime() {
        if (lastActivityTime == null) {
            return 0;
        }
        
        return DateUtil.minutesBetween(lastActivityTime, DateUtil.getCurrentTimestamp());
    }
    
    /**
     * Get formatted login time
     * 
     * @return Formatted login time or empty string
     */
    public String getFormattedLoginTime() {
        if (loginTime == null) {
            return "";
        }
        
        return DateUtil.formatTimestamp(loginTime);
    }
    
    /**
     * Get formatted session info
     * 
     * @return Session info string
     */
    public String getSessionInfo() {
        if (!isAuthenticated()) {
            return "No active session";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(getCurrentUsername()).append("\n");
        sb.append("Role: ").append(getCurrentUserRoleName()).append("\n");
        sb.append("Login: ").append(getFormattedLoginTime()).append("\n");
        sb.append("Duration: ").append(getSessionDuration()).append(" minutes\n");
        sb.append("Idle: ").append(getIdleTime()).append(" minutes");
        
        return sb.toString();
    }
    
    // ============ PERMISSION HELPERS ============
    
    /**
     * Check if user can manage employees
     * Only Admin and Manager can manage employees
     * 
     * @return true if allowed
     */
    public boolean canManageEmployees() {
        return hasAnyRole(0, 1); // Admin or Manager
    }
    
    /**
     * Check if user can manage products
     * Only Admin and Manager can manage products
     * 
     * @return true if allowed
     */
    public boolean canManageProducts() {
        return hasAnyRole(0, 1); // Admin or Manager
    }
    
    /**
     * Check if user can manage ingredients
     * Only Admin, Manager, and Cashier can manage ingredients
     * 
     * @return true if allowed
     */
    public boolean canManageIngredients() {
        return hasAnyRole(0, 1, 2); // Admin, Manager, or Cashier
    }
    
    /**
     * Check if user can view reports
     * Only Admin and Manager can view reports
     * 
     * @return true if allowed
     */
    public boolean canViewReports() {
        return hasAnyRole(0, 1); // Admin or Manager
    }
    
    /**
     * Check if user can process orders
     * Cashier, Chef, and Manager can process orders
     * 
     * @return true if allowed
     */
    public boolean canProcessOrders() {
        return hasAnyRole(1, 2, 3); // Manager, Cashier, or Chef
    }
    
    /**
     * Check if user can create orders
     * Customer, Cashier, and Manager can create orders
     * 
     * @return true if allowed
     */
    public boolean canCreateOrders() {
        return hasAnyRole(1, 2, 4); // Manager, Cashier, or Customer
    }
    
    // ============ UTILITY METHODS ============
    
    /**
     * Reset session (for testing)
     */
    public void reset() {
        endSession();
        instance = null;
    }
    
    /**
     * Print session info to console
     */
    public void printSessionInfo() {
        System.out.println("\n========== SESSION INFO ==========");
        if (isAuthenticated()) {
            System.out.println(getSessionInfo());
        } else {
            System.out.println("No active session");
        }
        System.out.println("==================================\n");
    }
    
    @Override
    public String toString() {
        return "SessionManager{" +
                "authenticated=" + authenticated +
                ", user=" + (currentUser != null ? currentUser.getUsername() : "none") +
                ", role=" + getCurrentUserRoleName() +
                ", duration=" + getSessionDuration() + "min" +
                '}';
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("🔐 Testing SessionManager...\n");
        
        SessionManager session = SessionManager.getInstance();
        
        // Test 1: No session initially
        System.out.println("=== Initial State ===");
        System.out.println("Authenticated: " + session.isAuthenticated());
        session.printSessionInfo();
        
        // Test 2: Start session
        System.out.println("=== Starting Session ===");
        User testUser = new User("testuser", "password", "test@example.com", 2); // Cashier
        testUser.setUserId(1);
        session.startSession(testUser);
        session.printSessionInfo();
        
        // Test 3: Check permissions
        System.out.println("=== Permission Tests ===");
        System.out.println("Is Cashier: " + session.isCashier());
        System.out.println("Can manage employees: " + session.canManageEmployees());
        System.out.println("Can create orders: " + session.canCreateOrders());
        System.out.println("Can manage ingredients: " + session.canManageIngredients());
        
        // Test 4: Session data
        System.out.println("\n=== Session Data ===");
        session.setAttribute("current_order", 12345);
        session.setAttribute("cart_items", 3);
        System.out.println("Stored order: " + session.getAttribute("current_order"));
        System.out.println("Cart items: " + session.getAttribute("cart_items"));
        
        // Test 5: Session timeout
        System.out.println("\n=== Session Timeout ===");
        System.out.println("Session valid (60 min): " + session.isSessionValid(60));
        System.out.println("Session expired (0 min): " + session.isSessionExpired(0));
        
        // Test 6: End session
        System.out.println("\n=== Ending Session ===");
        session.endSession();
        session.printSessionInfo();
        
        System.out.println("✅ All tests completed!");
    }
}