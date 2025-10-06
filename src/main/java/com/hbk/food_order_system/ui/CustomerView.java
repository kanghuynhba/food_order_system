package com.hbk.food_order_system.ui;

import com.hbk.food_order_system.service.CartService;
import com.hbk.food_order_system.util.Constants;

import javax.swing.*;
import java.awt.*;

public class CustomerView extends JFrame {
    private final CartService cartService;

    public CustomerView() {
        this.cartService=new CartService();
        initComponents();
    }

    public void initComponents() {
        setTitle("Order System");
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); 

        // add(new SidebarPanel(), BorderLayout.WEST);
        add(new MenuPanel(cartService), BorderLayout.CENTER);
        add(new CartPanel(cartService), BorderLayout.EAST);
        
        setLocationRelativeTo(null);
    }
} 
