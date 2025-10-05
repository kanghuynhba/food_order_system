package com.hbk.food_order_system.util;

import java.awt.Color;
import java.text.NumberFormat;

public class Constants {
    public static final Color PRIMARY_COLOR = new Color(255, 107, 0);
    public static final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    public static final Color BORDER_COLOR = new Color(229, 231, 235);
    public static final Color TEXT_PRIMARY = new Color(31, 41, 55);
    public static final Color TEXT_SECONDARY = new Color(75, 85, 99);
    public static final Color TEXT_PLACEHOLDER = new Color(173, 174, 188);
    
    public static final String FONT_INTER = "Inter";
    public static final int FONT_SIZE_SMALL = 14;
    public static final int FONT_SIZE_MEDIUM = 16;
    public static final int FONT_SIZE_LARGE = 18;
    public static final int FONT_SIZE_XLARGE = 20;
    
    public static final int SIDEBAR_WIDTH = 191;
    public static final int CART_WIDTH = 384;
    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 900;
    
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getInstance();
    
    private Constants() {} // Prevent instantiation
}
