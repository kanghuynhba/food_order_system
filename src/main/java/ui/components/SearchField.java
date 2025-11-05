package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * SearchField - Modern search input with icon
 */
public class SearchField extends JPanel {
    
    private JTextField textField;
    
    public SearchField(String placeholder) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        setPreferredSize(new Dimension(250, 40));
        
        // Icon
        JLabel icon = new JLabel("🔍");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        // TextField
        textField = new JTextField(placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setForeground(Color.GRAY);
        textField.setBorder(null);
        textField.setOpaque(false);
        
        add(icon, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
    }
    
    public JTextField getTextField() {
        return textField;
    }
    
    public String getText() {
        return textField.getText();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        g2.dispose();
    }
}
