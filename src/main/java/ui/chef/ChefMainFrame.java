package ui.chef;

import entity.Employee;
import ui.components.RoundedButton;
import util.ColorScheme;
import config.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ChefMainFrame - Main window for Chef
 */
public class ChefMainFrame extends JFrame {
    
    private Employee chef;
    private ChefDashboardPanel dashboardPanel;
    private JLabel timeLabel;
    private JLabel chefNameLabel;
    private Timer clockTimer;
    private Timer refreshTimer;

    public ChefMainFrame(Employee chef) {
        this.chef = chef;
        
        setTitle("Chef Dashboard - " + chef.getName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        startTimers();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(ColorScheme.BG_SECONDARY);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Dashboard panel (main content)
        dashboardPanel = new ChefDashboardPanel(chef);
        mainPanel.add(dashboardPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Left side - Title with icon
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("👨‍🍳");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Chef Dashboard");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Quản lý đơn hàng bếp");
        subtitleLabel.setFont(UIConstants.FONT_CAPTION);
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(subtitleLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(textPanel);

        // Right side - Chef info, time and actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        // Chef info panel
        JPanel chefInfoPanel = new JPanel();
        chefInfoPanel.setLayout(new BoxLayout(chefInfoPanel, BoxLayout.Y_AXIS));
        chefInfoPanel.setOpaque(false);
        
        chefNameLabel = new JLabel("👨‍🍳 " + chef.getName());
        chefNameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 15));
        chefNameLabel.setForeground(Color.WHITE);
        chefNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        timeLabel.setForeground(new Color(255, 255, 255, 200));
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        chefInfoPanel.add(chefNameLabel);
        chefInfoPanel.add(Box.createVerticalStrut(3));
        chefInfoPanel.add(timeLabel);
        
        // Refresh button
        RoundedButton refreshBtn = new RoundedButton("🔄 Refresh", 8);
        refreshBtn.setBackground(new Color(255, 255, 255, 30));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setPreferredSize(new Dimension(110, 42));
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        // Logout button
        RoundedButton logoutBtn = new RoundedButton("🚪 Logout", 8);
        logoutBtn.setBackground(ColorScheme.DANGER);
        logoutBtn.setPreferredSize(new Dimension(110, 42));
        logoutBtn.addActionListener(e -> logout());
        
        rightPanel.add(chefInfoPanel);
        rightPanel.add(refreshBtn);
        rightPanel.add(logoutBtn);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private void startTimers() {
        // Clock timer - update every second
        clockTimer = new Timer(1000, e -> updateTime());
        clockTimer.start();
        updateTime();
        
        // Auto refresh timer - refresh orders every 10 seconds
        refreshTimer = new Timer(10000, e -> dashboardPanel.autoRefresh());
        refreshTimer.start();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeLabel.setText("🕐 " + now.format(formatter));
    }

    private void refreshDashboard() {
        dashboardPanel.refreshOrders();
        showNotification("✅ Đã làm mới dữ liệu!");
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
            
            // Navigate to login screen (implement later)
            JOptionPane.showMessageDialog(null, "Đã đăng xuất thành công!");
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

    // ============ MAIN FOR TESTING ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create sample chef
            Employee sampleChef = new Employee();
            sampleChef.setEmployeeId(1);
            sampleChef.setName("Nguyễn Văn A");
            sampleChef.setRole(1); // Chef role
            
            ChefMainFrame frame = new ChefMainFrame(sampleChef);
            frame.setVisible(true);
        });
    }
}