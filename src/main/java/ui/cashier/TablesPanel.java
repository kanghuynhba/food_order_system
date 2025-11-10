package ui.cashier;

import ui.components.RoundedPanel;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TablesPanel - Table management for Cashier
 * Path: Source Packages/ui/cashier/TablesPanel.java
 * 
 * Simple table status view
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class TablesPanel extends JPanel {
    
    private static final Color GREEN = new Color(76, 175, 80);
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color RED = new Color(244, 67, 54);
    
    private CashierMainFrame mainFrame;
    
    // ============ CONSTRUCTOR ============
    
    public TablesPanel(CashierMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        initComponents();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("🪑 Quản lý bàn ăn");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        
        // Tables grid
        JPanel tablesContainer = new JPanel(new GridLayout(0, 5, 15, 15));
        tablesContainer.setBackground(new Color(245, 245, 245));
        
        // Create 20 tables
        for (int i = 1; i <= 20; i++) {
            tablesContainer.add(createTableCard(i, getRandomStatus()));
        }
        
        JScrollPane scrollPane = new JScrollPane(tablesContainer);
        scrollPane.setBorder(null);
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private RoundedPanel createTableCard(int tableNum, String status) {
        RoundedPanel card = new RoundedPanel(12, true);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(150, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel iconLabel = new JLabel("🪑");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Table number
        JLabel numLabel = new JLabel("Table #" + tableNum);
        numLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        numLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Status badge
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 11));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        switch (status) {
            case "Available" -> statusLabel.setBackground(GREEN);
            case "Occupied" -> statusLabel.setBackground(ORANGE);
            case "Reserved" -> statusLabel.setBackground(RED);
        }
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(numLabel);
        
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);
        
        // Click to view details
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewTableDetails(tableNum);
            }
        });
        
        return card;
    }
    
    private String getRandomStatus() {
        String[] statuses = {"Available", "Occupied", "Reserved"};
        return statuses[(int) (Math.random() * 3)];
    }
    
    private void viewTableDetails(int tableNum) {
        JOptionPane.showMessageDialog(this,
            "Table #" + tableNum + " Details\n\n" +
            "(Chức năng đang phát triển)",
            "Table Details",
            JOptionPane.INFORMATION_MESSAGE);
    }
}