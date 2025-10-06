package com.hbk.food_order_system.ui.components;

import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.util.Constants;
import com.hbk.food_order_system.util.UIFactory;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.function.Consumer;

public class ProductCard extends JPanel {
    private final Product product;
    private final Consumer<Product> onAddToCart;
    
    public ProductCard(Product product, Consumer<Product> onAddToCart) {
        this.product = product;
        this.onAddToCart = onAddToCart;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
            new LineBorder(Constants.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(0, 0, 16, 0)
        ));
        
        add(createImagePanel(), BorderLayout.NORTH);
        add(createInfoPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createImagePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0, 192));
        panel.setBackground(new Color(200, 200, 200));
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(UIFactory.createPaddingBorder(16, 16, 0, 16));

        JLabel name = UIFactory.createLabel(product.getName(), Constants.FONT_SIZE_LARGE, Font.BOLD);
        JLabel desc = new JLabel(product.getDescription());
        desc.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_SMALL));
        desc.setForeground(Constants.TEXT_SECONDARY);
        
        panel.add(name);
        panel.add(Box.createVerticalStrut(4));
        panel.add(desc);
        panel.add(Box.createVerticalStrut(8));
        panel.add(createBottomPanel());
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(UIFactory.createPaddingBorder(12, 0, 0, 0));
        
        JLabel price = new JLabel(Constants.CURRENCY_FORMAT.format(product.getPrice()) + " ₫");
        price.setFont(new Font(Constants.FONT_INTER, Font.BOLD, 24));
        price.setForeground(Constants.PRIMARY_COLOR);
        
        JButton addBtn = UIFactory.createPrimaryButton("Thêm");
        addBtn.addActionListener(e -> onAddToCart.accept(product));
        
        panel.add(price, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);
        
        return panel;
    }
}
