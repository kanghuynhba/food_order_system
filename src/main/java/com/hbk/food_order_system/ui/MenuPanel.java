package com.hbk.food_order_system.ui;

import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.service.CartService;
import com.hbk.food_order_system.service.ProductService;
import com.hbk.food_order_system.ui.components.ProductCard;
import com.hbk.food_order_system.util.Constants;
import com.hbk.food_order_system.util.UIFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final CartService cartService;
    private JPanel menuItemsPanel;
    private final ProductService productService;

     public MenuPanel(CartService cartService) {
        this.cartService = cartService;
        this.productService = new ProductService();
        initComponents();
    }

     
    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(UIFactory.createPaddingBorder(24, 24, 24, 24));
        
        JPanel wrapper = new JPanel(new BorderLayout(0, 20));
        wrapper.setOpaque(false);
        wrapper.add(createSearchPanel(), BorderLayout.NORTH);
        
        menuItemsPanel = createMenuItemsPanel();
        JScrollPane scroll = new JScrollPane(menuItemsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        add(wrapper, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JTextField searchField = UIFactory.createTextField("Tìm món nhanh...");
        searchField.setPreferredSize(new Dimension(0, 50));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constants.BORDER_COLOR, 1, true),
            UIFactory.createPaddingBorder(10, 16, 10, 16)
        ));
        
        panel.add(searchField);
        return panel;
    }

    private JPanel createMenuItemsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 24));
        panel.setOpaque(false);
        
        for (Product product : productService.getAvailableProducts()) {
            panel.add(new ProductCard(product, this::addToCart));
        }
        
        return panel;
    }
    
    private void addToCart(Product product) {
        cartService.addItem(product);
    }
}


