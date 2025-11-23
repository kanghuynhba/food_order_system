package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBConnection - Database Connection Manager (Improved)
 * Path: Source Packages/config/DBConnection.java
 * 
 * Quản lý kết nối đến MySQL Database
 * Version 1.5 - Cải tiến với proper URL params
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 */
public class DBConnection {
    
    // ============ DATABASE CONFIGURATION ============
    
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "fastfood_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "131104";
    
    // Complete URL with all necessary parameters
    private static final String DB_URL = String.format(
        "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true&characterEncoding=utf8&useUnicode=true",
        DB_HOST, DB_PORT, DB_NAME
    );
    
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // ThreadLocal để mỗi thread có connection riêng (thread-safe hơn)
    private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    
    // ============ CONSTRUCTOR ============
    
    /**
     * Private constructor để prevent instantiation
     */
    private DBConnection() {
        // Utility class
    }
    
    // ============ GET CONNECTION ============
    
    /**
     * Lấy connection cho thread hiện tại
     * Thread-safe với ThreadLocal
     * 
     * @return Connection object
     * @throws SQLException nếu kết nối thất bại
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = threadLocalConnection.get();
        
        try {
            // Load JDBC Driver (chỉ cần 1 lần)
            Class.forName(DB_DRIVER);
            
            // Kiểm tra connection còn valid không
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                conn.setAutoCommit(true);
                threadLocalConnection.set(conn);
                
                System.out.println("✅ Database connected successfully! (Thread: " + 
                    Thread.currentThread().getName() + ")");
            }
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("   Please add mysql-connector-j to dependencies");
            throw new SQLException("JDBC Driver not found: " + e.getMessage());
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("   URL: " + DB_URL);
            System.err.println("   User: " + DB_USER);
            System.err.println("   Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Tạo connection mới (không dùng ThreadLocal)
     * Dùng cho multi-threading hoặc transaction riêng biệt
     * 
     * @return Connection object mới
     * @throws SQLException nếu kết nối thất bại
     */
    public static Connection createNewConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            Connection newConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            newConnection.setAutoCommit(true);
            return newConnection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found: " + e.getMessage());
        }
    }
    
    // ============ TRANSACTION SUPPORT ============
    
    /**
     * Bắt đầu transaction
     * @param conn Connection object
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.setAutoCommit(false);
        }
    }
    
    /**
     * Commit transaction
     * @param conn Connection object
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Rollback transaction
     * @param conn Connection object
     */
    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error rolling back transaction: " + e.getMessage());
        }
    }
    
    /**
     * Execute operation trong transaction - Helper method
     */
    public static boolean executeInTransaction(TransactionOperation operation) {
        Connection conn = null;
        try {
            conn = createNewConnection(); // Dùng connection riêng cho transaction
            beginTransaction(conn);
            
            boolean success = operation.execute(conn);
            
            if (success) {
                commitTransaction(conn);
            } else {
                rollbackTransaction(conn);
            }
            
            return success;
            
        } catch (Exception e) {
            rollbackTransaction(conn);
            System.err.println("❌ Transaction failed: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Functional interface cho transaction operations
     */
    @FunctionalInterface
    public interface TransactionOperation {
        boolean execute(Connection conn) throws SQLException;
    }
    
    // ============ CLOSE CONNECTION ============
    
    /**
     * Đóng connection của thread hiện tại
     */
    public static void closeConnection() {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            } finally {
                threadLocalConnection.remove();
            }
        }
    }
    
    /**
     * Đóng connection cụ thể
     * @param conn Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
        }
    }
    
    /**
     * Đóng Statement
     */
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing statement: " + e.getMessage());
        }
    }
    
    // ============ TEST & INFO ============
    
    /**
     * Test database connection
     * @return true nếu connection thành công
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = createNewConnection();
            boolean isValid = conn.isValid(5);
            
            if (isValid) {
                System.out.println("✅ Database connection test: SUCCESS");
                System.out.println("   Database: " + conn.getCatalog());
                System.out.println("   URL: " + DB_URL);
            } else {
                System.out.println("❌ Database connection test: FAILED");
            }
            
            return isValid;
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection test: FAILED");
            System.err.println("   Error: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Lấy thông tin database
     */
    public static void printDatabaseInfo() {
        Connection conn = null;
        try {
            conn = createNewConnection();
            System.out.println("\n========== DATABASE INFO ==========");
            System.out.println("URL:         " + DB_URL);
            System.out.println("User:        " + DB_USER);
            System.out.println("Driver:      " + DB_DRIVER);
            System.out.println("Database:    " + conn.getCatalog());
            System.out.println("Auto Commit: " + conn.getAutoCommit());
            System.out.println("Valid:       " + conn.isValid(5));
            System.out.println("Timezone:    Asia/Ho_Chi_Minh");
            System.out.println("Encoding:    UTF-8");
            System.out.println("===================================\n");
        } catch (SQLException e) {
            System.err.println("❌ Error getting database info: " + e.getMessage());
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Kiểm tra xem database có đang connected không
     */
    public static boolean isConnected() {
        Connection conn = threadLocalConnection.get();
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    // ============ GETTERS ============
    
    public static String getDbUrl() {
        return DB_URL;
    }
    
    public static String getDbUser() {
        return DB_USER;
    }
    
    public static String getDbName() {
        return DB_NAME;
    }
    
    public static String getDbDriver() {
        return DB_DRIVER;
    }
    
    // ============ MAIN - FOR TESTING ============
    
    /**
     * Test connection và các chức năng
     */
    public static void main(String[] args) {
        System.out.println("🔌 Testing Database Connection...\n");
        
        // Test 1: Basic connection
        if (!testConnection()) {
            System.err.println("\n❌ Connection test failed!");
            System.err.println("   Please check:");
            System.err.println("   1. MySQL Server is running");
            System.err.println("   2. Database 'fastfood_db' exists");
            System.err.println("   3. Username: " + DB_USER);
            System.err.println("   4. Password: " + DB_PASSWORD);
            System.err.println("   5. Run the SQL script to create database");
            return;
        }
        
        printDatabaseInfo();
        
        // Test 2: Query data
        System.out.println("🔌 Testing queries...");
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            
            // Test users table
            var rs = stmt.executeQuery("SELECT COUNT(*) as total FROM users");
            if (rs.next()) {
                System.out.println("✅ Total users: " + rs.getInt("total"));
            }
            rs.close();
            
            // Test products table
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM products");
            if (rs.next()) {
                System.out.println("✅ Total products: " + rs.getInt("total"));
            }
            rs.close();
            
            // Test orders table
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM orders");
            if (rs.next()) {
                System.out.println("✅ Total orders: " + rs.getInt("total"));
            }
            rs.close();
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error testing queries: " + e.getMessage());
        } finally {
            closeConnection(conn);
        }
        
        // Test 3: Transaction
        System.out.println("\n🔌 Testing transaction...");
        boolean transactionSuccess = executeInTransaction(c -> {
            try (Statement stmt = c.createStatement()) {
                stmt.execute("SELECT 1");
                return true;
            }
        });
        System.out.println(transactionSuccess ? 
            "✅ Transaction test: SUCCESS" : 
            "❌ Transaction test: FAILED");
        
        // Test 4: UTF-8 support
        System.out.println("\n🔌 Testing UTF-8 (Vietnamese characters)...");
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT name FROM customers LIMIT 1");
            if (rs.next()) {
                String name = rs.getString("name");
                System.out.println("✅ UTF-8 test: " + name);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("❌ UTF-8 test failed: " + e.getMessage());
        } finally {
            closeConnection(conn);
        }
        
        System.out.println("\n🎉 All tests completed!");
    }
}
