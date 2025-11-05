package dao;

import config.DBConnection;
import entity.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO - Quản lý Employee trong database
 * Path: Source Packages/dao/EmployeeDAO.java
 */
public class EmployeeDAO extends BaseDAO<Employee> {
    
    private static final String TABLE = "employees";
    
    // ============ CREATE ============
    
    @Override
    public boolean create(Employee employee) {
        String sql = "INSERT INTO " + TABLE + 
                     " (user_id, name, email, phone, date_of_birth, gender, avatar_url, " +
                     "role, salary, status, hired_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, employee.getUserId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getPhone());
            ps.setDate(5, employee.getDateOfBirth());
            ps.setString(6, employee.getGender());
            ps.setString(7, employee.getAvatarUrl());
            ps.setInt(8, employee.getRole());
            ps.setDouble(9, employee.getSalary());
            ps.setInt(10, employee.getStatus());
            ps.setDate(11, employee.getHiredDate());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    employee.setEmployeeId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }
    
    // ============ READ ============
    
    @Override
    public Employee getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy employee theo user_id
     */
    public Employee getByUserId(int userId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee by user ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lấy employee theo email
     */
    public Employee getByEmail(String email) {
        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee by email: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
        }
        return employees;
    }
    
    /**
     * Lấy employees theo role (1: Chef, 2: Cashier, 3: Manager)
     */
    public List<Employee> getByRole(int role) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE role = ? ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employees by role: " + e.getMessage());
        }
        return employees;
    }
    
    /**
     * Lấy employees đang active
     */
    public List<Employee> getActiveEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = 1 ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active employees: " + e.getMessage());
        }
        return employees;
    }
    
    /**
     * Lấy all chefs
     */
    public List<Employee> getAllChefs() {
        return getByRole(1); // role = 1 là Chef
    }
    
    /**
     * Lấy all cashiers
     */
    public List<Employee> getAllCashiers() {
        return getByRole(2); // role = 2 là Cashier
    }
    
    /**
     * Lấy all managers
     */
    public List<Employee> getAllManagers() {
        return getByRole(3); // role = 3 là Manager
    }
    
    // ============ UPDATE ============
    
    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE " + TABLE + 
                     " SET user_id = ?, name = ?, email = ?, phone = ?, " +
                     "date_of_birth = ?, gender = ?, avatar_url = ?, role = ?, " +
                     "salary = ?, status = ?, hired_date = ?, updated_at = NOW() " +
                     "WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employee.getUserId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getPhone());
            ps.setDate(5, employee.getDateOfBirth());
            ps.setString(6, employee.getGender());
            ps.setString(7, employee.getAvatarUrl());
            ps.setInt(8, employee.getRole());
            ps.setDouble(9, employee.getSalary());
            ps.setInt(10, employee.getStatus());
            ps.setDate(11, employee.getHiredDate());
            ps.setInt(12, employee.getEmployeeId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật salary
     */
    public boolean updateSalary(int employeeId, double newSalary) {
        String sql = "UPDATE " + TABLE + " SET salary = ?, updated_at = NOW() WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, newSalary);
            ps.setInt(2, employeeId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating salary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật status (activate/deactivate)
     */
    public boolean updateStatus(int employeeId, int status) {
        String sql = "UPDATE " + TABLE + " SET status = ?, updated_at = NOW() WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ps.setInt(2, employeeId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật role
     */
    public boolean updateRole(int employeeId, int newRole) {
        String sql = "UPDATE " + TABLE + " SET role = ?, updated_at = NOW() WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newRole);
            ps.setInt(2, employeeId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee role: " + e.getMessage());
            return false;
        }
    }
    
    // ============ DELETE ============
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE employee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    
    // ============ UTILITY ============
    
    @Override
    public boolean exists(int id) {
        return getById(id) != null;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean emailExists(String email) {
        return getByEmail(email) != null;
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
            System.err.println("Error counting employees: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Đếm employees theo role
     */
    public int countByRole(int role) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE role = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting employees by role: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Employee> search(String criteria) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + 
                     " WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + criteria + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
        }
        return employees;
    }
    
    // ============ HELPER METHODS ============
    
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setUserId(rs.getInt("user_id"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("phone"));
        employee.setDateOfBirth(rs.getDate("date_of_birth"));
        employee.setGender(rs.getString("gender"));
        employee.setAvatarUrl(rs.getString("avatar_url"));
        employee.setRole(rs.getInt("role"));
        employee.setSalary(rs.getDouble("salary"));
        employee.setStatus(rs.getInt("status"));
        employee.setHiredDate(rs.getDate("hired_date"));
        employee.setCreatedAt(rs.getTimestamp("created_at"));
        employee.setUpdatedAt(rs.getTimestamp("updated_at"));
        return employee;
    }
}