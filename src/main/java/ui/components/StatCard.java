package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * StatCard - KPI/Metric display card for dashboard
 * 
 * Features:
 * - Icon/emoji
 * - Label and value
 * - Change indicator (%, ↑↓)
 * - Accent color
 */
public class StatCard extends RoundedPanel {
    
    private JLabel iconLabel;
    private JLabel titleLabel;
    private JLabel valueLabel;
    private JLabel changeLabel;
    private Color accentColor;
    
    // ============ CONSTRUCTOR ============
    
    public StatCard(String icon, String title, String value, String change, Color accentColor) {
        super(12, true);
        this.accentColor = accentColor;
        
        initComponents();
        setData(icon, title, value, change);
    }
    
    // ============ INIT COMPONENTS ============
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(280, 120));
        
        // Icon
        iconLabel = new JLabel();
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(accentColor.getRed(), 
            accentColor.getGreen(), accentColor.getBlue(), 30));
        iconLabel.setPreferredSize(new Dimension(60, 60));
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        
        valueLabel = new JLabel();
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(33, 33, 33));
        
        changeLabel = new JLabel();
        changeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changeLabel.setForeground(accentColor);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(changeLabel);
        
        add(iconLabel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    // ============ SETTERS ============
    
    public void setData(String icon, String title, String value, String change) {
        iconLabel.setText(icon);
        titleLabel.setText(title);
        valueLabel.setText(value);
        changeLabel.setText(change);
    }
    
    public void setValue(String value) {
        valueLabel.setText(value);
    }
    
    public void setChange(String change) {
        changeLabel.setText(change);
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("StatCard Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 400);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
            panel.setBackground(new Color(245, 245, 245));
            
            panel.add(new StatCard("💰", "Total Revenue", "24,580đ", "+12.5%", 
                new Color(76, 175, 80)));
            panel.add(new StatCard("🛒", "Orders", "1,245", "+8.2%", 
                new Color(33, 150, 243)));
            panel.add(new StatCard("⭐", "Avg Rating", "4.8", "+0.3", 
                new Color(255, 152, 0)));
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
