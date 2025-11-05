package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * IconButton - Circular icon-only button (edit, delete, view, etc.)
 * 
 * Features:
 * - Circular or rounded square shape
 * - Icon/emoji support
 * - Hover effects
 * - Tooltip
 * 
 * Usage:
 * IconButton editBtn = new IconButton("✏️", "Edit");
 * IconButton deleteBtn = new IconButton("🗑️", "Delete");
 */
public class IconButton extends JButton {
    
    private boolean isCircular;
    private int size;
    private Color hoverBgColor;
    private boolean isHovered;
    
    // ============ CONSTRUCTORS ============
    
    public IconButton(String icon) {
        this(icon, null, 35, true);
    }
    
    public IconButton(String icon, String tooltip) {
        this(icon, tooltip, 35, true);
    }
    
    public IconButton(String icon, String tooltip, int size, boolean isCircular) {
        super(icon);
        this.size = size;
        this.isCircular = isCircular;
        this.hoverBgColor = new Color(245, 245, 245);
        
        setPreferredSize(new Dimension(size, size));
        setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (tooltip != null) {
            setToolTipText(tooltip);
        }
        
        setupHoverEffect();
    }
    
    // ============ HOVER EFFECT ============
    
    private void setupHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    // ============ CUSTOM PAINTING ============
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isHovered) {
            g2.setColor(hoverBgColor);
            if (isCircular) {
                g2.fillOval(0, 0, size, size);
            } else {
                g2.fillRoundRect(0, 0, size, size, 8, 8);
            }
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    // ============ SETTERS ============
    
    public void setHoverBgColor(Color color) {
        this.hoverBgColor = color;
    }
    
    public void setCircular(boolean circular) {
        this.isCircular = circular;
        repaint();
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IconButton Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 50));
            panel.setBackground(Color.WHITE);
            
            // Action buttons
            IconButton editBtn = new IconButton("✏️", "Edit");
            IconButton viewBtn = new IconButton("👁️", "View");
            IconButton deleteBtn = new IconButton("🗑️", "Delete");
            IconButton addBtn = new IconButton("➕", "Add");
            IconButton settingsBtn = new IconButton("⚙️", "Settings");
            IconButton refreshBtn = new IconButton("🔄", "Refresh");
            
            panel.add(editBtn);
            panel.add(viewBtn);
            panel.add(deleteBtn);
            panel.add(addBtn);
            panel.add(settingsBtn);
            panel.add(refreshBtn);
            
            frame.add(panel);
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(frame,
                "✅ IconButton Test\n\n" +
                "Hover to see effects!\n" +
                "Tooltips on hover",
                "Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}