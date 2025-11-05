package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * RoundedPanel - Modern card-like panel with rounded corners
 * 
 * Features:
 * - Rounded corners (customizable radius)
 * - Drop shadow effect
 * - Hover animation (optional)
 * - Border color customization
 * 
 * Usage:
 * RoundedPanel card = new RoundedPanel(15);
 * card.setBackground(Color.WHITE);
 */
public class RoundedPanel extends JPanel {
    
    private int cornerRadius;
    private Color shadowColor;
    private boolean hasShadow;
    private boolean hoverEffect;
    private boolean isHovered;
    
    // ============ CONSTRUCTORS ============
    
    public RoundedPanel() {
        this(15);
    }
    
    public RoundedPanel(int cornerRadius) {
        this(cornerRadius, true);
    }
    
    public RoundedPanel(int cornerRadius, boolean hasShadow) {
        this.cornerRadius = cornerRadius;
        this.hasShadow = hasShadow;
        this.shadowColor = new Color(0, 0, 0, 20);
        this.hoverEffect = false;
        this.isHovered = false;
        
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    // ============ CUSTOM PAINTING ============
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow
        if (hasShadow) {
            g2.setColor(shadowColor);
            g2.fillRoundRect(2, 2, width - 4, height - 4, cornerRadius, cornerRadius);
        }
        
        // Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - 2, height - 2, cornerRadius, cornerRadius);
        
        // Hover effect
        if (hoverEffect && isHovered) {
            g2.setColor(new Color(255, 152, 0, 20));
            g2.fillRoundRect(0, 0, width - 2, height - 2, cornerRadius, cornerRadius);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    // ============ SETTERS ============
    
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }
    
    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }
    
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    public void enableHoverEffect(boolean enable) {
        this.hoverEffect = enable;
    }
    
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
        repaint();
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RoundedPanel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            
            JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            container.setBackground(new Color(245, 245, 245));
            
            // Test 1: Basic card
            RoundedPanel card1 = new RoundedPanel(15);
            card1.setPreferredSize(new Dimension(200, 150));
            card1.setBackground(Color.WHITE);
            card1.add(new JLabel("Basic Card"));
            
            // Test 2: No shadow
            RoundedPanel card2 = new RoundedPanel(15, false);
            card2.setPreferredSize(new Dimension(200, 150));
            card2.setBackground(new Color(255, 152, 0));
            card2.add(new JLabel("No Shadow"));
            
            // Test 3: Large radius
            RoundedPanel card3 = new RoundedPanel(30);
            card3.setPreferredSize(new Dimension(200, 150));
            card3.setBackground(new Color(76, 175, 80));
            card3.add(new JLabel("Large Radius"));
            
            container.add(card1);
            container.add(card2);
            container.add(card3);
            
            frame.add(container);
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(frame,
                "✅ RoundedPanel Test\n\n" +
                "Press Shift+F6 to test!\n" +
                "3 cards with different styles",
                "Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}