package config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

/**
 * UIConstants - UI Constants & Design Settings
 * Path: Source Packages/config/UIConstants.java
 * 
 * Chứa tất cả constants cho giao diện người dùng
 * Dựa trên design system của Fast Food Restaurant Management
 */
public class UIConstants {
    
    // ============ COLORS - BRAND COLORS ============
    public static final Color COLOR_PRIMARY = new Color(255, 102, 0);       // Orange - Main brand color
    public static final Color COLOR_SECONDARY = new Color(255, 153, 51);    // Light Orange
    public static final Color COLOR_ACCENT = new Color(255, 69, 0);         // Dark Orange
    
    // ============ COLORS - FUNCTIONAL ============
    public static final Color COLOR_SUCCESS = new Color(76, 175, 80);       // Green
    public static final Color COLOR_WARNING = new Color(255, 193, 7);       // Yellow
    public static final Color COLOR_DANGER = new Color(244, 67, 54);        // Red
    public static final Color COLOR_INFO = new Color(33, 150, 243);         // Blue
    
    // ============ COLORS - NEUTRAL ============
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_BLACK = Color.BLACK;
    public static final Color COLOR_GRAY_LIGHT = new Color(245, 245, 245);
    public static final Color COLOR_GRAY = new Color(158, 158, 158);
    public static final Color COLOR_GRAY_DARK = new Color(66, 66, 66);
    
    // ============ COLORS - TEXT ============
    public static final Color COLOR_TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color COLOR_TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color COLOR_TEXT_DISABLED = new Color(189, 189, 189);
    public static final Color COLOR_TEXT_WHITE = Color.WHITE;
    
    // ============ COLORS - BACKGROUND ============
    public static final Color COLOR_BG_PRIMARY = Color.WHITE;
    public static final Color COLOR_BG_SECONDARY = new Color(250, 250, 250);
    public static final Color COLOR_BG_DARK = new Color(33, 33, 33);
    public static final Color COLOR_BG_HOVER = new Color(240, 240, 240);
    public static final Color COLOR_BG_SELECTED = new Color(255, 243, 224);
    
    // ============ COLORS - ORDER STATUS (Theo DB) ============
    public static final Color COLOR_ORDER_NEW = new Color(33, 150, 243);        // Blue
    public static final Color COLOR_ORDER_CONFIRMED = new Color(156, 39, 176);  // Purple
    public static final Color COLOR_ORDER_PREPARING = new Color(255, 152, 0);   // Orange
    public static final Color COLOR_ORDER_COOKING = new Color(255, 87, 34);     // Deep Orange
    public static final Color COLOR_ORDER_READY = new Color(76, 175, 80);       // Green
    public static final Color COLOR_ORDER_COMPLETED = new Color(96, 125, 139);  // Blue Grey
    public static final Color COLOR_ORDER_CANCELLED = new Color(244, 67, 54);   // Red
    
    // ============ FONTS ============
    public static final String FONT_FAMILY = "Segoe UI";
    public static final String FONT_FAMILY_MONO = "Consolas";
    
    // Font sizes
    public static final int FONT_SIZE_TINY = 10;
    public static final int FONT_SIZE_SMALL = 12;
    public static final int FONT_SIZE_NORMAL = 14;
    public static final int FONT_SIZE_MEDIUM = 16;
    public static final int FONT_SIZE_LARGE = 18;
    public static final int FONT_SIZE_XLARGE = 24;
    public static final int FONT_SIZE_XXLARGE = 32;
    
    // Font styles
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_XLARGE);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_LARGE);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_MEDIUM);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_NORMAL);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_NORMAL);
    public static final Font FONT_CAPTION = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_SMALL);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_NORMAL);
    
    // ============ DIMENSIONS - WINDOW SIZES ============
    public static final Dimension SIZE_LOGIN = new Dimension(400, 500);
    public static final Dimension SIZE_CUSTOMER = new Dimension(1400, 800);
    public static final Dimension SIZE_CASHIER = new Dimension(1200, 700);
    public static final Dimension SIZE_CHEF = new Dimension(1000, 700);
    public static final Dimension SIZE_MANAGER = new Dimension(1400, 900);
    public static final Dimension SIZE_DISPLAY = new Dimension(1920, 1080);
    
    // Minimum sizes
    public static final Dimension SIZE_MIN_WINDOW = new Dimension(800, 600);
    
    // ============ DIMENSIONS - COMPONENT SIZES ============
    public static final Dimension SIZE_BUTTON_SMALL = new Dimension(80, 30);
    public static final Dimension SIZE_BUTTON_MEDIUM = new Dimension(120, 36);
    public static final Dimension SIZE_BUTTON_LARGE = new Dimension(160, 42);
    
    public static final Dimension SIZE_TEXTFIELD_SHORT = new Dimension(150, 32);
    public static final Dimension SIZE_TEXTFIELD_MEDIUM = new Dimension(250, 32);
    public static final Dimension SIZE_TEXTFIELD_LONG = new Dimension(350, 32);
    
    public static final Dimension SIZE_PRODUCT_CARD = new Dimension(200, 280);
    public static final Dimension SIZE_ORDER_CARD = new Dimension(350, 200);
    
    // ============ SPACING & PADDING ============
    public static final int SPACING_TINY = 4;
    public static final int SPACING_SMALL = 8;
    public static final int SPACING_NORMAL = 12;
    public static final int SPACING_MEDIUM = 16;
    public static final int SPACING_LARGE = 24;
    public static final int SPACING_XLARGE = 32;
    
    public static final int PADDING_TINY = 4;
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_NORMAL = 12;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 20;
    public static final int PADDING_XLARGE = 24;
    
    // ============ BORDER RADIUS ============
    public static final int RADIUS_SMALL = 4;
    public static final int RADIUS_NORMAL = 8;
    public static final int RADIUS_MEDIUM = 12;
    public static final int RADIUS_LARGE = 16;
    public static final int RADIUS_CIRCLE = 50;
    
    // ============ BORDER THICKNESS ============
    public static final int BORDER_THIN = 1;
    public static final int BORDER_NORMAL = 2;
    public static final int BORDER_THICK = 3;
    
    // ============ ICON SIZES ============
    public static final int ICON_SIZE_SMALL = 16;
    public static final int ICON_SIZE_NORMAL = 24;
    public static final int ICON_SIZE_MEDIUM = 32;
    public static final int ICON_SIZE_LARGE = 48;
    public static final int ICON_SIZE_XLARGE = 64;
    
    // ============ IMAGE SIZES ============
    public static final Dimension SIZE_PRODUCT_IMAGE = new Dimension(180, 180);
    public static final Dimension SIZE_PRODUCT_IMAGE_SMALL = new Dimension(80, 80);
    public static final Dimension SIZE_AVATAR = new Dimension(40, 40);
    public static final Dimension SIZE_AVATAR_LARGE = new Dimension(100, 100);
    
    // ============ ANIMATION DURATIONS (milliseconds) ============
    public static final int ANIMATION_FAST = 150;
    public static final int ANIMATION_NORMAL = 300;
    public static final int ANIMATION_SLOW = 500;
    
    // ============ Z-INDEX (LAYERING) ============
    public static final int ZINDEX_BACKGROUND = 0;
    public static final int ZINDEX_CONTENT = 10;
    public static final int ZINDEX_OVERLAY = 100;
    public static final int ZINDEX_MODAL = 200;
    public static final int ZINDEX_NOTIFICATION = 300;
    public static final int ZINDEX_TOOLTIP = 400;
    
    // ============ TABLE SETTINGS ============
    public static final int TABLE_ROW_HEIGHT = 40;
    public static final int TABLE_HEADER_HEIGHT = 45;
    
    // ============ DIALOG SIZES ============
    public static final Dimension SIZE_DIALOG_SMALL = new Dimension(400, 300);
    public static final Dimension SIZE_DIALOG_MEDIUM = new Dimension(600, 450);
    public static final Dimension SIZE_DIALOG_LARGE = new Dimension(800, 600);
    
    // ============ SIDEBAR ============
    public static final int SIDEBAR_WIDTH = 250;
    public static final int SIDEBAR_WIDTH_COLLAPSED = 60;
    
    // ============ HEADER/FOOTER ============
    public static final int HEADER_HEIGHT = 60;
    public static final int FOOTER_HEIGHT = 40;
    
    // ============ OPACITY ============
    public static final float OPACITY_DISABLED = 0.5f;
    public static final float OPACITY_HOVER = 0.8f;
    public static final float OPACITY_OVERLAY = 0.7f;
    
    // ============ HELPER METHODS ============
    
    /**
     * Get color by order status (theo DB schema)
     */
    public static Color getOrderStatusColor(int status) {
        return switch (status) {
            case 0 -> COLOR_ORDER_NEW;          // New
            case 1 -> COLOR_ORDER_CONFIRMED;    // Confirmed
            case 2 -> COLOR_ORDER_PREPARING;    // Preparing
            case 3 -> COLOR_ORDER_COOKING;      // Cooking
            case 4 -> COLOR_ORDER_READY;        // Ready
            case 5 -> COLOR_ORDER_COMPLETED;    // Completed
            case 6 -> COLOR_ORDER_CANCELLED;    // Cancelled
            default -> COLOR_GRAY;
        };
    }
    
    /**
     * Create a lighter version of a color
     */
    public static Color lighter(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    /**
     * Create a darker version of a color
     */
    public static Color darker(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Create color with alpha (transparency)
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}