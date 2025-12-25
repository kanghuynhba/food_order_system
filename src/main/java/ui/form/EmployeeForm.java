package form;

import entity.Employee;
import service.EmployeeService;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeeForm extends BaseForm {

    private EmployeeService employeeService;
    private Employee employee; // Null if Adding, Object if Editing

    // --- UI Components ---
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDob;      // Input for Date of Birth
    private JComboBox<String> cbGender;
    private JComboBox<RoleOption> cbRole;
    private JTextField txtSalary;
    
    // New fields for Account Creation
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    private JCheckBox chkActive;

    // Helper for date format
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeeForm(Frame parent) {
        super(parent, "Add New Employee");
        this.employeeService = EmployeeService.getInstance();
        initComponent();
    }

    public EmployeeForm(Frame parent, Employee employeeToEdit) {
        this(parent);
        this.setTitle("Edit Employee");
        this.employee = employeeToEdit;
        btnDelete.setVisible(true);
        fillData();
    }

    private void initComponent() {
        // 1. Personal Information
        txtName = addTextField("Full Name:");
        txtEmail = addTextField("Email:");
        txtPhone = addTextField("Phone Number:");
        txtDob = addTextField("Date of Birth (yyyy-mm-dd):");
        
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        addComponent("Gender:", cbGender);

        // 2. Job Information
        txtSalary = addTextField("Salary:");
        
        cbRole = new JComboBox<>();
        cbRole.addItem(new RoleOption(1, "Chef"));
        cbRole.addItem(new RoleOption(2, "Cashier"));
        cbRole.addItem(new RoleOption(3, "Manager"));
        addComponent("Role:", cbRole);

        // 3. Account Information (Important for createEmployee)
        // Only show password field when adding new user, or leave blank to keep old password
        addSeparator("Account Info"); 
        txtUsername = addTextField("Username:");
        txtPassword = new JPasswordField(20);
        addComponent("Password:", txtPassword);

        chkActive = new JCheckBox("Is Active?");
        chkActive.setSelected(true);
        addComponent("Status:", chkActive);
    }
    
    // Helper to draw a line separator
    private void addSeparator(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("--- " + title + " ---"), BorderLayout.CENTER);
        addComponent("", p);
    }

    private void fillData() {
        if (employee == null) return;

        txtName.setText(employee.getName());
        txtEmail.setText(employee.getEmail());
        txtPhone.setText(employee.getPhone());
        
        if (employee.getDateOfBirth() != null) {
            txtDob.setText(dateFormat.format(employee.getDateOfBirth()));
        }

        cbGender.setSelectedItem(employee.getGender());
        txtSalary.setText(String.valueOf(employee.getSalary()));
        
        // Fill Role
        for (int i = 0; i < cbRole.getItemCount(); i++) {
            if (cbRole.getItemAt(i).id == employee.getRole()) {
                cbRole.setSelectedIndex(i);
                break;
            }
        }
        
        // Note: Username might be in a separate User table depending on your DB design.
        // If Employee entity has userId, you might need to fetch username separately.
        // For now, assuming you handle it or leave it blank on Edit.
        // txtUsername.setText(...); 

        chkActive.setSelected(employee.isActive());
        
        // Disable editing username if not allowed
        // txtUsername.setEditable(false); 
    }

    @Override
    protected void onSave() {
        // 1. Validation
        if (txtName.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Username are required!");
            return;
        }

        try {
            // 2. Get Data
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            String gender = cbGender.getSelectedItem().toString();
            double salary = Double.parseDouble(txtSalary.getText());
            
            // Get Role ID
            int role = ((RoleOption) cbRole.getSelectedItem()).id;
            
            // Get Account Data
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Parse Date
            Date dob = null;
            if (!txtDob.getText().trim().isEmpty()) {
                java.util.Date parsed = dateFormat.parse(txtDob.getText());
                dob = new Date(parsed.getTime());
            }

            // 3. Call Service
            if (employee == null) {
                // --- ADD NEW (Matches your method signature) ---
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password is required for new staff!");
                    return;
                }

                boolean success = employeeService.createEmployee(
                    name, email, phone, dob, gender, role, salary, username, password
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Staff created successfully!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create staff.");
                }

            } else {
                // Since update usually doesn't need username/password again unless changed
                // You will need a separate update method in your Service
                employee.setName(name);
                employee.setEmail(email);
                employee.setPhone(phone);
                employee.setDateOfBirth(dob);
                employee.setGender(gender);
                employee.setRole(role);
                employee.setSalary(salary);
                employee.setStatus(chkActive.isSelected() ? 1 : 0);

                // Assuming you have an update method:
                // employeeService.updateEmployee(employee);
                
                JOptionPane.showMessageDialog(this, "Staff updated (Note: Password not changed in this demo)");
                this.dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a number!");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid Date format! Use yyyy-mm-dd");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override 
    protected void onDelete() {
        boolean success=employeeService.deleteEmployee(employee.getEmployeeId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Deleted successfully!");
            // Set isSaved to true so the parent table knows to refresh
            this.isSaved = true; 
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete.");
        }
    }

    // Role Helper
    class RoleOption {
        int id;
        String name;
        public RoleOption(int id, String name) { this.id = id; this.name = name; }
        public String toString() { return name; }
    }
}
