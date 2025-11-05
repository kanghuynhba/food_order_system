package entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Employee Entity - Nhân viên hệ thống
 * Path: Source Packages/entity/Employee.java
 */
public class Employee {
    
    private int employeeId;
    private int userId;
    private String name;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private String gender;
    private String avatarUrl;
    private int role;  // 1: Chef, 2: Cashier, 3: Manager
    private double salary;
    private int status;  // 1: Active, 0: Inactive
    private Date hiredDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    public Employee() {}
    
    public Employee(String name, String email, String phone, int role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = 1;
    }
    
    public Employee(int employeeId, int userId, String name, String email, String phone,
                    Date dateOfBirth, String gender, String avatarUrl, int role, 
                    double salary, int status, Date hiredDate, 
                    Timestamp createdAt, Timestamp updatedAt) {
        this.employeeId = employeeId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.salary = salary;
        this.status = status;
        this.hiredDate = hiredDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public Date getHiredDate() {
        return hiredDate;
    }
    
    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
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
            case 1 -> "Chef";
            case 2 -> "Cashier";
            case 3 -> "Manager";
            default -> "Unknown";
        };
    }
    
    public String getStatusName() {
        return status == 1 ? "Active" : "Inactive";
    }
    
    public boolean isActive() {
        return status == 1;
    }
    
    public boolean isChef() {
        return role == 1;
    }
    
    public boolean isCashier() {
        return role == 2;
    }
    
    public boolean isManager() {
        return role == 3;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + getRoleName() +
                ", status=" + getStatusName() +
                '}';
    }
}