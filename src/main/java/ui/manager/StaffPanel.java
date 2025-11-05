package ui.manager;

import entity.Employee;
import service.EmployeeService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    
    private EmployeeService employeeService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public StaffPanel() {
        employeeService = EmployeeService.getInstance();
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
        JButton createBtn = new JButton("➕ Create Staff");
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(ORANGE);
        createBtn.setFocusPainted(false);
        createBtn.setBorderPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.setPreferredSize(new Dimension(130, 40));
        
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
        String[] columns = {"Staff", "Role", "CA", "Status", "Contact", "Actions"};
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
        List<Employee> employees = employeeService.getAllEmployees();
        
        for (Employee emp : employees) {
            String roleIcon = getRoleIcon(emp.getRole());
            String statusBadge = emp.isActive() ? "Active" : "Inactive";
            
            tableModel.addRow(new Object[]{
                emp.getName() + "\nID: EMP" + String.format("%03d", emp.getEmployeeId()),
                roleIcon + " " + emp.getRoleName(),
                "A",
                statusBadge,
                emp.getPhone() != null ? emp.getPhone() : "N/A",
                "✏️ 👁️ 🗑️"
            });
        }
    }
    
    private String getRoleIcon(int role) {
        return switch (role) {
            case 1 -> "👨‍🍳";
            case 2 -> "💵";
            case 3 -> "👔";
            default -> "👤";
        };
    }
}