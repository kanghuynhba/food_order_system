package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * CartItemRow - Single cart item display
 */
public class CartItemRow extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JSpinner quantitySpinner;
    private IconButton removeBtn;
    
    public CartItemRow(String name, double price, int quantity) {
        setLayout(new BorderLayout(10, 0));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Name
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Price
        priceLabel = new JLabel(String.format("%,.0fđ", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(ORANGE);
        
        // Quantity
        SpinnerNumberModel model = new SpinnerNumberModel(quantity, 1, 99, 1);
        quantitySpinner = new JSpinner(model);
        quantitySpinner.setPreferredSize(new Dimension(60, 30));
        
        // Remove button
        removeBtn = new IconButton("✗", "Remove", 30, false);
        removeBtn.setHoverBgColor(new Color(244, 67, 54, 30));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(priceLabel);
        rightPanel.add(quantitySpinner);
        rightPanel.add(removeBtn);
        
        add(nameLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public JSpinner getQuantitySpinner() {
        return quantitySpinner;
    }
    
    public IconButton getRemoveButton() {
        return removeBtn;
    }
}
