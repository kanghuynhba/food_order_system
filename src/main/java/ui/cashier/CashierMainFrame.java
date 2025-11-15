package ui.cashier;

import entity.User;
import ui.components.RoundedButton;
import util.ColorScheme;
import config.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CashierMainFrame - Main window for Cashier
 * Path: Source Packages/ui/cashier/CashierMainFrame.java
 * 
 * Features:
 * - Header with stats (live orders, today revenue)
 * - Sidebar navigation
 * - Main content area with CardLayout
 * - Orders panel (card grid view)
 * - POS Menu panel (create new orders)
 * - Tables panel (table management)
 * - Real-time clock and auto-refresh
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CashierMainFrame extends JFrame {
    
    // ============ FIELDS ============
    
    private User currentUser;
    private CashierSidebarPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Header components
    private JLabel timeLabel;
    private JLabel cashierNameLabel;
    private JLabel liveOrdersLabel;
    private JLabel todayRevenueLabel;
    
    // Timers
    private Timer clockTimer;
    private Timer refreshTimer;
    
    // Stats
    private int liveOrdersCount = 0;
    private double todayRevenue = 0.0;
    
    // ============ CONSTRUCTOR ============
    
    public CashierMainFrame() {
        this(null);
    }
    
    public CashierMainFrame(User user) {
        this.currentUser = user;
        
        String cashierName = (user != null) ? user.getUsername() : "Cashier";
        setTitle("FastFood Cashier - " + cashierName);
        setSize(UIConstants.SIZE_CASHIER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadInitialData();
        startTimers();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(ColorScheme.BG_SECONDARY);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Sidebar
        sidebar = new CashierSidebarPanel(this);
        sidebar.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 0));
        mainPanel.add(sidebar, BorderLayout.WEST);
        
        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ColorScheme.BG_SECONDARY);
        
        // Add panels
        contentPanel.add(new OrdersPanel(this), "orders");
        contentPanel.add(new MenuPOSPanel(this), "pos");
        contentPanel.add(new TablesPanel(this), "tables");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Show orders by default
        showPanel("orders");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left: Logo + Title + Stats
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        leftPanel.setOpaque(false);
        
        // Logo
        JLabel logoLabel = new JLabel("🍔");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        // Title + subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Cashier Order");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Quản lý đơn hàng & thanh toán");
        subtitleLabel.setFont(UIConstants.FONT_CAPTION);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(subtitleLabel);
        
        // Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        statsPanel.setOpaque(false);
        
        // Live orders stat
        JPanel liveOrdersPanel = createStatItem("📋", "Live Orders: ", "0");
        liveOrdersLabel = (JLabel) liveOrdersPanel.getComponent(1);
        
        // Today revenue stat
        JPanel revenuePanel = createStatItem("💰", "Today: ", "0đ");
        todayRevenueLabel = (JLabel) revenuePanel.getComponent(1);
        
        statsPanel.add(liveOrdersPanel);
        statsPanel.add(revenuePanel);
        
        leftPanel.add(logoLabel);
        leftPanel.add(titlePanel);
        leftPanel.add(Box.createHorizontalStrut(30));
        leftPanel.add(statsPanel);
        
        // Right: Cashier info + buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);
        
        // Cashier info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        String cashierName = (currentUser != null) ? currentUser.getUsername() : "Cashier";
        cashierNameLabel = new JLabel("👤 " + cashierName);
        cashierNameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        cashierNameLabel.setForeground(Color.WHITE);
        cashierNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        timeLabel.setForeground(new Color(255, 255, 255, 200));
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        infoPanel.add(cashierNameLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(timeLabel);
        
        // Refresh button
        RoundedButton refreshBtn = new RoundedButton("🔄", 8);
        refreshBtn.setBackground(new Color(255, 255, 255, 30));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setPreferredSize(new Dimension(45, 45));
        refreshBtn.setToolTipText("Làm mới");
        refreshBtn.addActionListener(e -> refreshData());
        
        // Logout button
        RoundedButton logoutBtn = new RoundedButton("🚪", 8);
        logoutBtn.setBackground(ColorScheme.DANGER);
        logoutBtn.setPreferredSize(new Dimension(45, 45));
        logoutBtn.setToolTipText("Đăng xuất");
        logoutBtn.addActionListener(e -> logout());
        
        rightPanel.add(infoPanel);
        rightPanel.add(refreshBtn);
        rightPanel.add(logoutBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatItem(String icon, String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        valueLabel.setForeground(Color.WHITE);
        
        panel.add(iconLabel);
        panel.add(valueLabel);
        
        return panel;
    }
    
    // ============ NAVIGATION ============
    
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        sidebar.setActiveButton(panelName);
    }
    
    // ============ DATA LOADING ============
    
    private void loadInitialData() {
        // Load initial stats
        updateStats();
    }
    
    private void updateStats() {
        // TODO: Load from database
        // For now, use dummy data
        liveOrdersCount = 12;
        todayRevenue = 2847000;
        
        liveOrdersLabel.setText(String.valueOf(liveOrdersCount));
        todayRevenueLabel.setText(formatCurrency(todayRevenue));
    }
    
    // ============ TIMERS ============
    
    private void startTimers() {
        // Clock timer - update every second
        clockTimer = new Timer(1000, e -> updateTime());
        clockTimer.start();
        updateTime();
        
        // Auto refresh timer - refresh stats every 10 seconds
        refreshTimer = new Timer(10000, e -> updateStats());
        refreshTimer.start();
    }
    
    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeLabel.setText("🕐 " + now.format(formatter));
    }
    
    // ============ ACTIONS ============
    
    private void refreshData() {
        updateStats();
        
        // Notify panels to refresh
        Component currentPanel = getCurrentPanel();
        if (currentPanel instanceof OrdersPanel) {
            ((OrdersPanel) currentPanel).refreshOrders();
        } else if (currentPanel instanceof MenuPOSPanel) {
            ((MenuPOSPanel) currentPanel).refreshProducts();
        }
        
        showNotification("✅ Đã làm mới dữ liệu!");
    }
    
    private Component getCurrentPanel() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            clockTimer.stop();
            refreshTimer.stop();
            dispose();
            
            // Navigate to login screen
            JOptionPane.showMessageDialog(null, "Đã đăng xuất thành công!");
            // TODO: Open LoginFrame
            // new ui.login.LoginFrame().setVisible(true);
            System.exit(0);
        }
    }
    
    private void showNotification(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // ============ GETTERS ============
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    // ============ HELPERS ============
    
    private String formatCurrency(double amount) {
        return String.format("%,.0fđ", amount);
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // Create sample user
            entity.User sampleUser = new entity.User();
            sampleUser.setUserId(1);
            sampleUser.setUsername("cashier01");
            sampleUser.setRole(2); // Cashier role
            
            CashierMainFrame frame = new CashierMainFrame(sampleUser);
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(frame,
                "🎯 Cashier UI Test Mode\n\n" +
                "Features:\n" +
                "✅ Header with live stats\n" +
                "✅ Sidebar navigation\n" +
                "✅ Orders panel (Image 5 style)\n" +
                "✅ POS Menu panel (Image 9 style)\n" +
                "✅ Tables panel\n" +
                "✅ Real-time clock\n" +
                "✅ Auto-refresh\n\n" +
                "Navigate using sidebar buttons!",
                "Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}