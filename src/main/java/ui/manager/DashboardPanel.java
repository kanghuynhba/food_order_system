package ui.manager;

import service.OrderService;
import service.ProductService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    private static final Color BLUE = new Color(33, 150, 243);
    private static final Color RED = new Color(244, 67, 54);
    
    private OrderService orderService;
    private ProductService productService;
    private NumberFormat currencyFormat;
    
    public DashboardPanel() {
        orderService = OrderService.getInstance();
        productService = ProductService.getInstance();
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Stats cards
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel title = new JLabel("Sales Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Fast Food Analytics & Performance");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        JButton exportBtn = new JButton("📥 Export");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(ORANGE);
        exportBtn.setFocusPainted(false);
        exportBtn.setBorderPainted(false);
        exportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportBtn.setPreferredSize(new Dimension(100, 40));
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(exportBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Calculate stats
        double totalRevenue = orderService.getTotalRevenue();
        int totalOrders = orderService.getTotalOrderCount();
        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        int totalProducts = productService.getTotalProductCount();
        
        // Stats cards
        panel.add(createStatCard("💰", "Total Revenue", 
            currencyFormat.format(totalRevenue) + "đ", "+12.5%", GREEN));
        panel.add(createStatCard("🛒", "Orders", 
            String.valueOf(totalOrders), "+8.2%", BLUE));
        panel.add(createStatCard("📊", "Avg Order Value", 
            currencyFormat.format(avgOrderValue) + "đ", "+5.1%", ORANGE));
        panel.add(createStatCard("⭐", "Top Item", 
            "Big Mac Combo", "#1", RED));
        
        return panel;
    }
    
    private JPanel createStatCard(String icon, String label, String value, 
                                   String change, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(accentColor.getRed(), 
            accentColor.getGreen(), accentColor.getBlue(), 30));
        iconLabel.setPreferredSize(new Dimension(60, 60));
        
        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(33, 33, 33));
        
        JLabel changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changeLabel.setForeground(accentColor);
        
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(valueLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(changeLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(Box.createHorizontalStrut(15), BorderLayout.CENTER);
        card.add(content, BorderLayout.EAST);
        
        return card;
    }
}