package ui.manager;

import entity.Employee;

import service.EmployeeService;

import form.EmployeeForm;

import ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StaffPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    
    private EmployeeService employeeService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Employee> employees;
    
    public StaffPanel() {
        employeeService = EmployeeService.getInstance();
        employees=employeeService.getAllEmployees();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Table
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel title = new JLabel("Staff Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Manage your restaurant staff and their roles");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        // Button
        RoundedButton createBtn = new RoundedButton("Create Staff", 8);
        createBtn.setBackground(ORANGE);
        createBtn.setPreferredSize(new Dimension(180, 38));
        createBtn.setMaximumSize(new Dimension(200, 38));
        createBtn.addActionListener(e -> {
            openEmployeeForm(null);
        });
                
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(createBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filter
        panel.add(createFilterPanel(), BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Staff", "Role", "Status", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(60);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(250, 250, 250));
        table.setSelectionBackground(new Color(255, 152, 0, 50));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for DOUBLE CLICK (Standard for editing)
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the employee object from our list using the row index
                        Employee emp = employees.get(selectedRow);
                        openEmployeeForm(emp); // Edit Mode
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Search
        searchField = new JTextField("🔍 Search staff...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(200, 35));
        
        // Filters
        JComboBox<String> roleFilter = new JComboBox<>(new String[]{
            "All Roles"
        });
        roleFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleFilter.setPreferredSize(new Dimension(120, 35));
        
        panel.add(searchField);
        panel.add(roleFilter);
        
        return panel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (Employee emp : employees) {
            String roleIcon = getRoleIcon(emp.getRole());
            String statusBadge = emp.isActive() ? "Active" : "Inactive";
            
            tableModel.addRow(new Object[]{
                emp.getName(),
                emp.getRoleName(),
                emp.isActive() ? "Active" : "Inactive",
                emp.getPhone()            
            });
        }
    }

    private void openEmployeeForm(Employee emp) {
        EmployeeForm form = new EmployeeForm((JFrame) SwingUtilities.getWindowAncestor(this), emp);
        form.setVisible(true);
        
        if (form.isSaved()) {
            loadData(); 
        }
    }
    
    private String getRoleIcon(int role) {
        return switch (role) {
            case 1 -> "Chef";
            case 2 -> "Cashier";
            case 3 -> "Manager";
            default -> "Employee";
        };
    }
}
