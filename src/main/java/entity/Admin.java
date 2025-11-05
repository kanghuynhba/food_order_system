package entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Admin Entity - Extends Employee
 * Path: Source Packages/entity/Admin.java
 */
public class Admin extends Employee {
    
    private String permissions;
    private String department;
    private String notes;
    
    // ============ CONSTRUCTORS ============
    
    public Admin() {}
    
    public Admin(String name, String email, String phone) {
        super(name, email, phone, 0);
    }
    
    public Admin(int employeeId, int userId, String name, String email, String phone,
                 Date dateOfBirth, String gender, String avatarUrl, int role, 
                 double salary, int status, Date hiredDate, 
                 Timestamp createdAt, Timestamp updatedAt,
                 String permissions, String department, String notes) {
        super(employeeId, userId, name, email, phone, dateOfBirth, gender, 
              avatarUrl, role, salary, status, hiredDate, createdAt, updatedAt);
        this.permissions = permissions;
        this.department = department;
        this.notes = notes;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // ============ HELPER METHODS ============
    
    public boolean hasPermission(String permission) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        return permissions.contains(permission);
    }
    
    public void addPermission(String permission) {
        if (permissions == null || permissions.isEmpty()) {
            permissions = permission;
        } else if (!permissions.contains(permission)) {
            permissions += "," + permission;
        }
    }
    
    public void removePermission(String permission) {
        if (permissions != null) {
            permissions = permissions.replace(permission + ",", "")
                                     .replace("," + permission, "")
                                     .replace(permission, "");
        }
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "employeeId=" + getEmployeeId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}