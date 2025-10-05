package com.hbk.food_order_system;

import com.hbk.food_order_system.ui.CustomerView;

import javax.swing.*;

public class OrderApplicationStartup {
    public static void main( String []args) {
        OrderApplication.getInstance();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CustomerView().setVisible(true);
        });
    }
}
