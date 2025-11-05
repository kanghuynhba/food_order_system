package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * StatusBadge - Colored status indicator badge
 * 
 * Types:
 * - SUCCESS (Green): Active, OK, Ready
 * - WARNING (Yellow): Pending, Low Stock
 * - ERROR (Red): Inactive, Out of Stock
 * - INFO (Blue): Processing
 * - DEFAULT (Gray): Unknown
 */
public class StatusBadge extends JLabel {
    
    public enum StatusType {
        SUCCESS, WARNING, ERROR, INFO, DEFAULT
    }
    
    private StatusType type;
    
    // ============ CONSTRUCTOR ============
    
    public StatusBadge(String text, StatusType type) {
        super(text);
        this.type = type;
        
        setFont(new Font("Segoe UI", Font.BOLD, 11));
        setOpaque(true);
        setBorder(new EmptyBorder(4, 10, 4, 10));
        setHorizontalAlignment(SwingConstants.CENTER);
        
        updateColors();
    }
    
    // ============ UPDATE COLORS ============
    
    private void updateColors() {
        switch (type) {
            case SUCCESS:
                setBackground(new Color(76, 175, 80, 30));
                setForeground(new Color(46, 125, 50));
                break;
            case WARNING:
                setBackground(new Color(251, 192, 45, 30));
                setForeground(new Color(230, 162, 0));
                break;
            case ERROR:
                setBackground(new Color(244, 67, 54, 30));
                setForeground(new Color(211, 47, 47));
                break;
            case INFO:
                setBackground(new Color(33, 150, 243, 30));
                setForeground(new Color(25, 118, 210));
                break;
            case DEFAULT:
                setBackground(new Color(158, 158, 158, 30));
                setForeground(new Color(97, 97, 97));
                break;
        }
    }
    
    // ============ SETTERS ============
    
    public void setType(StatusType type) {
        this.type = type;
        updateColors();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("StatusBadge Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 50));
            panel.setBackground(Color.WHITE);
            
            panel.add(new StatusBadge("Active", StatusType.SUCCESS));
            panel.add(new StatusBadge("Pending", StatusType.WARNING));
            panel.add(new StatusBadge("Out", StatusType.ERROR));
            panel.add(new StatusBadge("Processing", StatusType.INFO));
            panel.add(new StatusBadge("Unknown", StatusType.DEFAULT));
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
