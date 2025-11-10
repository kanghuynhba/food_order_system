package ui.cashier;

import ui.components.RoundedButton;
import util.ColorScheme;
import config.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CashierSidebarPanel - Navigation sidebar for Cashier
 * Path: Source Packages/ui/cashier/CashierSidebarPanel.java
 * 
 * Features:
 * - Orders button
 * - POS Menu button
 * - Tables button
 * - Active state highlighting
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class CashierSidebarPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color ACTIVE_BG = new Color(255, 243, 224);
    
    private CashierMainFrame mainFrame;
    private Map<String, JButton> buttonMap;
    private String activePanel;
    
    // ============ CONSTRUCTOR ============
    
    public CashierSidebarPanel(CashierMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.buttonMap = new HashMap<>();
        
        initComponents();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        
        // Main menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(BG_COLOR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // Menu items
        menuPanel.add(createMenuButton("📋", "Đơn hàng", "orders"));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createMenuButton("🍔", "POS Menu", "pos"));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createMenuButton("🪑", "Bàn ăn", "tables"));
        
        add(menuPanel, BorderLayout.NORTH);
    }
    
    // ============ CREATE MENU BUTTON ============
    
    private JButton createMenuButton(String icon, String text, String panelName) {
        JButton button = new JButton();
        button.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setPreferredSize(new Dimension(220, 50));
        button.setBackground(BG_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        // Text label
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        textLabel.setForeground(new Color(66, 66, 66));
        
        button.add(iconLabel);
        button.add(textLabel);
        
        // Action
        button.addActionListener(e -> {
            mainFrame.showPanel(panelName);
        });
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!panelName.equals(activePanel)) {
                    button.setBackground(new Color(245, 245, 245));
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!panelName.equals(activePanel)) {
                    button.setBackground(BG_COLOR);
                }
            }
        });
        
        buttonMap.put(panelName, button);
        return button;
    }
    
    // ============ SET ACTIVE BUTTON ============
    
    public void setActiveButton(String panelName) {
        // Reset all buttons
        for (Map.Entry<String, JButton> entry : buttonMap.entrySet()) {
            JButton btn = entry.getValue();
            btn.setBackground(BG_COLOR);
            
            // Update text color
            for (Component comp : btn.getComponents()) {
                if (comp instanceof JLabel && !(((JLabel) comp).getText().length() <= 2)) {
                    ((JLabel) comp).setForeground(new Color(66, 66, 66));
                }
            }
        }
        
        // Set active button
        JButton activeButton = buttonMap.get(panelName);
        if (activeButton != null) {
            activeButton.setBackground(ACTIVE_BG);
            
            // Update text color to orange
            for (Component comp : activeButton.getComponents()) {
                if (comp instanceof JLabel && !(((JLabel) comp).getText().length() <= 2)) {
                    ((JLabel) comp).setForeground(ORANGE);
                }
            }
        }
        
        this.activePanel = panelName;
    }
}