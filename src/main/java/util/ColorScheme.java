package util;

import config.UIConstants;
import java.awt.Color;

/**
 * ColorScheme - Color Scheme Management
 * Path: Source Packages/util/ColorScheme.java
 * 
 * Quản lý màu sắc cho toàn bộ ứng dụng
 * Cung cấp các color scheme và theme colors
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ColorScheme {
    
    // ============ BRAND COLORS ============
    
    public static final Color PRIMARY = UIConstants.COLOR_PRIMARY;           // Orange #FF6600
    public static final Color SECONDARY = UIConstants.COLOR_SECONDARY;       // Light Orange #FF9933
    public static final Color ACCENT = UIConstants.COLOR_ACCENT;             // Dark Orange #FF4500
    
    // ============ FUNCTIONAL COLORS ============
    
    public static final Color SUCCESS = UIConstants.COLOR_SUCCESS;           // Green #4CAF50
    public static final Color WARNING = UIConstants.COLOR_WARNING;           // Yellow #FFC107
    public static final Color DANGER = UIConstants.COLOR_DANGER;             // Red #F44336
    public static final Color INFO = UIConstants.COLOR_INFO;                 // Blue #2196F3
    
    // ============ NEUTRAL COLORS ============
    
    public static final Color WHITE = UIConstants.COLOR_WHITE;
    public static final Color BLACK = UIConstants.COLOR_BLACK;
    public static final Color GRAY_LIGHT = UIConstants.COLOR_GRAY_LIGHT;
    public static final Color GRAY = UIConstants.COLOR_GRAY;
    public static final Color GRAY_DARK = UIConstants.COLOR_GRAY_DARK;
    
    // ============ TEXT COLORS ============
    
    public static final Color TEXT_PRIMARY = UIConstants.COLOR_TEXT_PRIMARY;
    public static final Color TEXT_SECONDARY = UIConstants.COLOR_TEXT_SECONDARY;
    public static final Color TEXT_DISABLED = UIConstants.COLOR_TEXT_DISABLED;
    public static final Color TEXT_WHITE = UIConstants.COLOR_TEXT_WHITE;
    
    // ============ BACKGROUND COLORS ============
    
    public static final Color BG_PRIMARY = UIConstants.COLOR_BG_PRIMARY;
    public static final Color BG_SECONDARY = UIConstants.COLOR_BG_SECONDARY;
    public static final Color BG_DARK = UIConstants.COLOR_BG_DARK;
    public static final Color BG_HOVER = UIConstants.COLOR_BG_HOVER;
    public static final Color BG_SELECTED = UIConstants.COLOR_BG_SELECTED;
    
    // ============ ORDER STATUS COLORS ============
    
    public static final Color ORDER_NEW = UIConstants.COLOR_ORDER_NEW;
    public static final Color ORDER_CONFIRMED = UIConstants.COLOR_ORDER_CONFIRMED;
    public static final Color ORDER_PREPARING = UIConstants.COLOR_ORDER_PREPARING;
    public static final Color ORDER_COOKING = UIConstants.COLOR_ORDER_COOKING;
    public static final Color ORDER_READY = UIConstants.COLOR_ORDER_READY;
    public static final Color ORDER_COMPLETED = UIConstants.COLOR_ORDER_COMPLETED;
    public static final Color ORDER_CANCELLED = UIConstants.COLOR_ORDER_CANCELLED;
    
    // ============ CUSTOM COLORS ============
    
    // Additional colors for specific use cases
    public static final Color SIDEBAR_BG = new Color(33, 33, 33);
    public static final Color HEADER_BG = WHITE;
    public static final Color CARD_BG = WHITE;
    public static final Color CARD_BORDER = new Color(224, 224, 224);
    public static final Color DIVIDER = new Color(224, 224, 224);
    public static final Color SHADOW = new Color(0, 0, 0, 30);
    public static final Color OVERLAY = new Color(0, 0, 0, 128);
    
    // Button colors
    public static final Color BUTTON_PRIMARY = PRIMARY;
    public static final Color BUTTON_PRIMARY_HOVER = darker(PRIMARY, 0.1f);
    public static final Color BUTTON_SECONDARY = GRAY_LIGHT;
    public static final Color BUTTON_SECONDARY_HOVER = GRAY;
    public static final Color BUTTON_DANGER = DANGER;
    public static final Color BUTTON_DANGER_HOVER = darker(DANGER, 0.1f);
    
    // Input colors
    public static final Color INPUT_BG = WHITE;
    public static final Color INPUT_BORDER = new Color(204, 204, 204);
    public static final Color INPUT_BORDER_FOCUS = PRIMARY;
    public static final Color INPUT_ERROR = DANGER;
    
    // Table colors
    public static final Color TABLE_HEADER_BG = GRAY_LIGHT;
    public static final Color TABLE_ROW_EVEN = WHITE;
    public static final Color TABLE_ROW_ODD = new Color(250, 250, 250);
    public static final Color TABLE_ROW_HOVER = BG_SELECTED;
    public static final Color TABLE_ROW_SELECTED = lighter(PRIMARY, 0.9f);
    
    // Chart colors
    public static final Color[] CHART_COLORS = {
        new Color(255, 99, 132),   // Red
        new Color(54, 162, 235),   // Blue
        new Color(255, 206, 86),   // Yellow
        new Color(75, 192, 192),   // Teal
        new Color(153, 102, 255),  // Purple
        new Color(255, 159, 64),   // Orange
        new Color(199, 199, 199),  // Gray
        new Color(83, 102, 255)    // Indigo
    };
    
    // ============ HELPER METHODS ============
    
    /**
     * Tạo màu sáng hơn
     */
    public static Color lighter(Color color, float factor) {
        return UIConstants.lighter(color, factor);
    }
    
    /**
     * Tạo màu tối hơn
     */
    public static Color darker(Color color, float factor) {
        return UIConstants.darker(color, factor);
    }
    
    /**
     * Tạo màu với alpha (độ trong suốt)
     */
    public static Color withAlpha(Color color, int alpha) {
        return UIConstants.withAlpha(color, alpha);
    }
    
    /**
     * Lấy màu theo order status
     */
    public static Color getOrderStatusColor(int status) {
        return UIConstants.getOrderStatusColor(status);
    }
    
    /**
     * Lấy màu từ chart colors theo index
     */
    public static Color getChartColor(int index) {
        return CHART_COLORS[index % CHART_COLORS.length];
    }
    
    /**
     * Tạo gradient color
     */
    public static Color[] createGradient(Color startColor, Color endColor, int steps) {
        Color[] gradient = new Color[steps];
        
        for (int i = 0; i < steps; i++) {
            float ratio = (float) i / (steps - 1);
            
            int red = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
            int green = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
            int blue = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));
            
            gradient[i] = new Color(red, green, blue);
        }
        
        return gradient;
    }
    
    /**
     * Convert color to hex string
     */
    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X", 
            color.getRed(), 
            color.getGreen(), 
            color.getBlue());
    }
    
    /**
     * Parse hex string to color
     */
    public static Color fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        
        try {
            return new Color(
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16)
            );
        } catch (Exception e) {
            return Color.BLACK;
        }
    }
}