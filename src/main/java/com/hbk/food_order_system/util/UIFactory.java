package com.hbk.food_order_system.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UIFactory {
    
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Constants.PRIMARY_COLOR);
        btn.setOpaque(true);         
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public static JButton createNavButton(String text, boolean selected) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        btn.setForeground(selected ? Color.WHITE : Constants.TEXT_PLACEHOLDER);
        btn.setBackground(selected ? Constants.PRIMARY_COLOR : Color.WHITE);
        btn.setOpaque(true);         
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public static JButton createCategoryButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        return btn;
    }
    
    public static JButton createRoundButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setForeground(isPrimary ? Color.WHITE : Color.BLACK);
        btn.setBackground(isPrimary ? Constants.PRIMARY_COLOR : Constants.BORDER_COLOR);
        btn.setOpaque(true);         
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public static JLabel createLabel(String text, int fontSize, int fontStyle) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Constants.FONT_INTER, fontStyle, fontSize));
        label.setForeground(Constants.TEXT_PRIMARY);
        return label;
    }
    
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        field.setForeground(Constants.TEXT_PLACEHOLDER);
        return field;
    }
    
    public static Border createPaddingBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }
    
    private UIFactory() {} // Prevent instantiation
}
