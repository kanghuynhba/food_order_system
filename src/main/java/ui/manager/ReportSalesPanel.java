package ui.manager;

import service.OrderService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ReportSalesPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private OrderService orderService;
    private JTable table;
    private DefaultTableModel tableModel;
    private NumberFormat currencyFormat;
    
    public ReportSalesPanel() {
        orderService = OrderService.getInstance();
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
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
        
        JLabel title = new JLabel("💰 Sales Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Date filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        
        JButton todayBtn = new JButton("Today");
        JButton weekBtn = new JButton("7d");
        JButton monthBtn = new JButton("30d");
        JButton customBtn = new JButton("Custom");
        
        for (JButton btn : new JButton[]{todayBtn, weekBtn, monthBtn, customBtn}) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setPreferredSize(new Dimension(80, 30));
            filterPanel.add(btn);
        }
        
        JButton exportBtn = new JButton("📥 Export CSV");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(ORANGE);
        exportBtn.setFocusPainted(false);
        exportBtn.setBorderPainted(false);
        
        panel.add(title, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.CENTER);
        panel.add(exportBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String[] columns = {"Item", "Qty", "Revenue", "Margin %"};
        tableModel = new DefaultTableModel(columns, 0);
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadData() {
        tableModel.addRow(new Object[]{"Big Mac Combo", "245", "5,000,000đ", "68%"});
        tableModel.addRow(new Object[]{"Quarter Pounder", "198", "4,000,000đ", "62%"});
        tableModel.addRow(new Object[]{"Chicken McNuggets", "156", "2,000,000đ", "58%"});
    }
}
