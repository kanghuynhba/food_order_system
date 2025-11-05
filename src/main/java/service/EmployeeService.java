package service;

import dao.EmployeeDAO;
import dao.UserDAO;
import entity.Employee;
import entity.User;
import java.sql.Date;
import java.util.List;

/**
 * EmployeeService - Employee Management Service
 * Path: Source Packages/service/EmployeeService.java
 * 
 * Chức năng:
 * - CRUD nhân viên
 * - Tìm kiếm nhân viên
 * - Quản lý role & status
 * - Quản lý lương
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class EmployeeService {
    
    private static EmployeeService instance;
    private EmployeeDAO employeeDAO;
    private UserDAO userDAO;
    
    // ============ SINGLETON ============
    
    private EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
        this.userDAO = new UserDAO();
    }
    
    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }
    
    // ============ CRUD OPERATIONS ============
    
    /**
     * Tạo nhân viên mới (với user account)
     */
    public boolean createEmployee(String name, String email, String phone, 
                                  Date dateOfBirth, String gender, int role, 
                                  double salary, String username, String password) {
        try {
            // Validate
            if (name == null || name.trim().isEmpty()) {
                System.err.println("❌ Name is required");
                return false;
            }
            
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.err.println("❌ Invalid email format");
                return false;
            }
            
            if (employeeDAO.emailExists(email)) {
                System.err.println("❌ Email already exists");
                return false;
            }
            
            if (role < 1 || role > 3) {
                System.err.println("❌ Invalid role (1: Chef, 2: Cashier, 3: Manager)");
                return false;
            }
            
            // Create user account first
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // TODO: Hash password
            user.setEmail(email);
            user.setRole(role); // Map employee role to user role
            user.setStatus(1); // Active
            
            boolean userCreated = userDAO.create(user);
            if (!userCreated) {
                System.err.println("❌ Failed to create user account");
                return false;
            }
            
            // Get created user to get user_id
            User createdUser = userDAO.getByUsername(username);
            if (createdUser == null) {
                System.err.println("❌ Failed to retrieve created user");
                return false;
            }
            
            // Create employee
            Employee employee = new Employee();
            employee.setUserId(createdUser.getUserId());
            employee.setName(name);
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setDateOfBirth(dateOfBirth);
            employee.setGender(gender);
            employee.setRole(role);
            employee.setSalary(salary);
            employee.setStatus(1); // Active
            employee.setHiredDate(new Date(System.currentTimeMillis()));
            
            boolean success = employeeDAO.create(employee);
            
            if (success) {
                System.out.println("✅ Employee created: " + name);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error creating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin nhân viên
     */
    public boolean updateEmployee(Employee employee) {
        try {
            if (employee == null || employee.getEmployeeId() <= 0) {
                System.err.println("❌ Invalid employee");
                return false;
            }
            
            boolean success = employeeDAO.update(employee);
            
            if (success) {
                System.out.println("✅ Employee updated: " + employee.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa nhân viên
     */
    public boolean deleteEmployee(int employeeId) {
        try {
            Employee employee = employeeDAO.getById(employeeId);
            if (employee == null) {
                System.err.println("❌ Employee not found");
                return false;
            }
            
            boolean success = employeeDAO.delete(employeeId);
            
            if (success) {
                System.out.println("✅ Employee deleted: " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    
    // ============ QUERY OPERATIONS ============
    
    /**
     * Lấy tất cả nhân viên
     */
    public List<Employee> getAllEmployees() {
        try {
            return employeeDAO.getAll();
        } catch (Exception e) {
            System.err.println("❌ Error getting all employees: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy nhân viên theo ID
     */
    public Employee getEmployeeById(int employeeId) {
        try {
            return employeeDAO.getById(employeeId);
        } catch (Exception e) {
            System.err.println("❌ Error getting employee: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy nhân viên theo email
     */
    public Employee getEmployeeByEmail(String email) {
        try {
            return employeeDAO.getByEmail(email);
        } catch (Exception e) {
            System.err.println("❌ Error getting employee by email: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy nhân viên active
     */
    public List<Employee> getActiveEmployees() {
        try {
            return employeeDAO.getActiveEmployees();
        } catch (Exception e) {
            System.err.println("❌ Error getting active employees: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Tìm kiếm nhân viên
     */
    public List<Employee> searchEmployees(String keyword) {
        try {
            return employeeDAO.search(keyword);
        } catch (Exception e) {
            System.err.println("❌ Error searching employees: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ ROLE MANAGEMENT ============
    
    /**
     * Lấy nhân viên theo role
     */
    public List<Employee> getEmployeesByRole(int role) {
        try {
            return employeeDAO.getByRole(role);
        } catch (Exception e) {
            System.err.println("❌ Error getting employees by role: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Lấy tất cả chefs
     */
    public List<Employee> getAllChefs() {
        return getEmployeesByRole(1);
    }
    
    /**
     * Lấy tất cả cashiers
     */
    public List<Employee> getAllCashiers() {
        return getEmployeesByRole(2);
    }
    
    /**
     * Lấy tất cả managers
     */
    public List<Employee> getAllManagers() {
        return getEmployeesByRole(3);
    }
    
    /**
     * Cập nhật role
     */
    public boolean updateRole(int employeeId, int newRole) {
        try {
            if (newRole < 1 || newRole > 3) {
                System.err.println("❌ Invalid role");
                return false;
            }
            
            boolean success = employeeDAO.updateRole(employeeId, newRole);
            
            if (success) {
                System.out.println("✅ Role updated for employee: " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating role: " + e.getMessage());
            return false;
        }
    }
    
    // ============ STATUS MANAGEMENT ============
    
    /**
     * Kích hoạt nhân viên
     */
    public boolean activateEmployee(int employeeId) {
        try {
            boolean success = employeeDAO.updateStatus(employeeId, 1); // 1 = Active
            
            if (success) {
                System.out.println("✅ Employee activated: " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error activating employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Vô hiệu hóa nhân viên
     */
    public boolean deactivateEmployee(int employeeId) {
        try {
            boolean success = employeeDAO.updateStatus(employeeId, 0); // 0 = Inactive
            
            if (success) {
                System.out.println("✅ Employee deactivated: " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error deactivating employee: " + e.getMessage());
            return false;
        }
    }
    
    // ============ SALARY MANAGEMENT ============
    
    /**
     * Cập nhật lương
     */
    public boolean updateSalary(int employeeId, double newSalary) {
        try {
            if (newSalary < 0) {
                System.err.println("❌ Invalid salary amount");
                return false;
            }
            
            boolean success = employeeDAO.updateSalary(employeeId, newSalary);
            
            if (success) {
                System.out.println("✅ Salary updated for employee: " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating salary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tính tổng lương theo role
     */
    public double getTotalSalaryByRole(int role) {
        try {
            List<Employee> employees = getEmployeesByRole(role);
            return employees.stream()
                .filter(Employee::isActive)
                .mapToDouble(Employee::getSalary)
                .sum();
        } catch (Exception e) {
            System.err.println("❌ Error calculating total salary: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Tính tổng lương tất cả nhân viên
     */
    public double getTotalSalary() {
        try {
            List<Employee> employees = getActiveEmployees();
            return employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        } catch (Exception e) {
            System.err.println("❌ Error calculating total salary: " + e.getMessage());
            return 0.0;
        }
    }
    
    // ============ STATISTICS ============
    
    /**
     * Đếm tổng số nhân viên
     */
    public int getTotalEmployeeCount() {
        try {
            return employeeDAO.count();
        } catch (Exception e) {
            System.err.println("❌ Error counting employees: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Đếm nhân viên active
     */
    public int getActiveEmployeeCount() {
        return getActiveEmployees().size();
    }
    
    /**
     * Đếm nhân viên theo role
     */
    public int countByRole(int role) {
        try {
            return employeeDAO.countByRole(role);
        } catch (Exception e) {
            System.err.println("❌ Error counting by role: " + e.getMessage());
            return 0;
        }
    }
    
    // ============ VALIDATION ============
    
    /**
     * Validate employee data
     */
    public boolean validateEmployee(Employee employee) {
        if (employee == null) return false;
        if (employee.getName() == null || employee.getName().trim().isEmpty()) return false;
        if (employee.getEmail() == null || !employee.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) return false;
        if (employee.getRole() < 1 || employee.getRole() > 3) return false;
        if (employee.getSalary() < 0) return false;
        
        return true;
    }
}