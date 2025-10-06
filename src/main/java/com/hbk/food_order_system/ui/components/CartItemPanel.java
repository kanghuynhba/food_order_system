package com.hbk.food_order_system.ui.components;

import com.hbk.food_order_system.entity.CartItem;
import com.hbk.food_order_system.util.Constants;
import com.hbk.food_order_system.util.UIFactory;
import javax.swing.*;
import java.awt.*;

public class CartItemPanel extends JPanel {
    private final CartItem cartItem;
    private final Runnable onUpdate;
    private final Runnable onRemove;
    private JLabel quantityLabel;
    
    public CartItemPanel(CartItem cartItem, Runnable onUpdate, Runnable onRemove) {
        this.cartItem = cartItem;
        this.onUpdate = onUpdate;
        this.onRemove = onRemove;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(12, 0));
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(UIFactory.createPaddingBorder(12, 12, 12, 12));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        
        // add(createImageLabel(), BorderLayout.WEST);
        add(createInfoPanel(), BorderLayout.CENTER);
        add(createControlsPanel(), BorderLayout.EAST);
    }
    
    // private JLabel createImageLabel() {
    //     JLabel image = new JLabel("🍔");
    //     image.setPreferredSize(new Dimension(48, 48));
    //     image.setHorizontalAlignment(SwingConstants.CENTER);
    //     image.setOpaque(true);
    //     image.setBackground(new Color(200, 200, 200));
    //     return image;
    // }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel name = new JLabel(cartItem.getProduct().getName());
        name.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        name.setForeground(Color.BLACK); 
        
        JLabel price = new JLabel(Constants.CURRENCY_FORMAT.format(cartItem.getProduct().getPrice()) + " ₫");
        price.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_SMALL));
        price.setForeground(Constants.TEXT_SECONDARY);
        
        panel.add(name);
        panel.add(price);
        
        return panel;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);
        
        JButton minusBtn = UIFactory.createRoundButton("-", true);
        minusBtn.addActionListener(e -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.decrementQuantity();
                updateQuantityDisplay();
                onUpdate.run();
            }  else if (cartItem.getQuantity() == 1) {
                onRemove.run();
            }
        });
        
        quantityLabel = new JLabel(String.valueOf(cartItem.getQuantity()));
        quantityLabel.setFont(new Font(Constants.FONT_INTER, Font.BOLD, Constants.FONT_SIZE_MEDIUM));
        quantityLabel.setPreferredSize(new Dimension(30, 32));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setForeground(Color.BLACK);                                            
        JButton plusBtn = new JButton("+");
        plusBtn.setPreferredSize(new Dimension(32, 32));
        plusBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        plusBtn.setForeground(Color.WHITE);
        plusBtn.setBackground(Constants.PRIMARY_COLOR);
        plusBtn.setFocusPainted(false);
        plusBtn.setBorderPainted(false);
        plusBtn.setOpaque(true);
        plusBtn.setContentAreaFilled(true); // CRITICAL
        plusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        plusBtn.setMargin(new Insets(0, 0, 0, 0));
        plusBtn.addActionListener(e -> {
            cartItem.incrementQuantity();
            updateQuantityDisplay();
            onUpdate.run();
        });
        
        JButton removeBtn = UIFactory.createPrimaryButton("X");
        removeBtn.addActionListener(e -> onRemove.run());
        
        panel.add(minusBtn);
        panel.add(quantityLabel);
        panel.add(plusBtn);
        panel.add(removeBtn);
        
        return panel;
    }
    
    private void updateQuantityDisplay() {
        quantityLabel.setText(String.valueOf(cartItem.getQuantity()));
    }
}
