package ui.manager;

import service.EmployeeService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportEmployeePanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private EmployeeService employeeService;
    
    public ReportEmployeePanel() {
        employeeService = EmployeeService.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel title = new JLabel("👤 Employee Performance Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel info = new JLabel("Total Employees: " + employeeService.getTotalEmployeeCount());
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(info);
        
        add(panel, BorderLayout.NORTH);
    }
}