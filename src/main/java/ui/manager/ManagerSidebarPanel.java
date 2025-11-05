package ui.manager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ManagerSidebarPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color SIDEBAR_BG = new Color(33, 33, 33);
    private static final Color HOVER_COLOR = new Color(255, 152, 0, 30);
    
    private ManagerMainFrame mainFrame;
    private ButtonGroup buttonGroup;
    
    public ManagerSidebarPanel(ManagerMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 900));
        setBackground(SIDEBAR_BG);
        
        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Menu
        JPanel menu = createMenu();
        add(menu, BorderLayout.CENTER);
        
        // Footer
        JPanel footer = createFooter();
        add(footer, BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel logo = new JLabel("🍔");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel title = new JLabel("FastFood");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ORANGE);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(SIDEBAR_BG);
        leftPanel.add(logo);
        leftPanel.add(title);
        
        JLabel subtitle = new JLabel("Manager Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.LIGHT_GRAY);
        
        panel.add(leftPanel, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        buttonGroup = new ButtonGroup();
        
        addMenuItem(panel, "📊 Dashboard", "dashboard", true);
        addMenuItem(panel, "🥗 Inventory", "ingredients", false);
        addMenuItem(panel, "👥 Staff Management", "staff", false);
        addMenuItem(panel, "🍔 Menu Products", "products", false);
        
        panel.add(Box.createVerticalStrut(20));
        JLabel reportLabel = new JLabel("  REPORTS");
        reportLabel.setForeground(Color.GRAY);
        reportLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        reportLabel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(reportLabel);
        panel.add(Box.createVerticalStrut(10));
        
        addMenuItem(panel, "💰 Sales Report", "sales", false);
        addMenuItem(panel, "👤 Employee Report", "employee", false);
        addMenuItem(panel, "📦 Inventory Report", "inventory", false);
        
        return panel;
    }
    
    private void addMenuItem(JPanel parent, String text, String panelName, boolean selected) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> mainFrame.showPanel(panelName));
        
        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(HOVER_COLOR);
                btn.setForeground(ORANGE);
            } else if (btn.getModel().isRollover()) {
                btn.setBackground(HOVER_COLOR);
            } else {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(Color.WHITE);
            }
        });
        
        buttonGroup.add(btn);
        btn.setSelected(selected);
        parent.add(btn);
        parent.add(Box.createVerticalStrut(5));
    }
    
    private JPanel createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel user = new JLabel("👤 Manager");
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(220, 53, 69));
        logout.setFocusPainted(false);
        logout.setBorderPainted(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(user, BorderLayout.NORTH);
        panel.add(logout, BorderLayout.SOUTH);
        
        return panel;
    }
}
