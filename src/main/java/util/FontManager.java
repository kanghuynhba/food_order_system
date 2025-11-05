package util;

import config.UIConstants;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * FontManager - Font Management
 * Path: Source Packages/util/FontManager.java
 * 
 * Quản lý fonts cho toàn bộ ứng dụng
 * Load custom fonts, cache fonts
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class FontManager {
    
    // ============ SINGLETON INSTANCE ============
    
    private static FontManager instance;
    
    /**
     * Get singleton instance
     */
    public static FontManager getInstance() {
        if (instance == null) {
            synchronized (FontManager.class) {
                if (instance == null) {
                    instance = new FontManager();
                }
            }
        }
        return instance;
    }
    
    // ============ FONT CACHE ============
    
    private Map<String, Font> fontCache;
    private GraphicsEnvironment ge;
    
    // ============ DEFAULT FONTS ============
    
    public static final String DEFAULT_FONT = UIConstants.FONT_FAMILY;
    public static final String MONOSPACE_FONT = UIConstants.FONT_FAMILY_MONO;
    
    // Font sizes from UIConstants
    public static final int SIZE_TINY = UIConstants.FONT_SIZE_TINY;
    public static final int SIZE_SMALL = UIConstants.FONT_SIZE_SMALL;
    public static final int SIZE_NORMAL = UIConstants.FONT_SIZE_NORMAL;
    public static final int SIZE_MEDIUM = UIConstants.FONT_SIZE_MEDIUM;
    public static final int SIZE_LARGE = UIConstants.FONT_SIZE_LARGE;
    public static final int SIZE_XLARGE = UIConstants.FONT_SIZE_XLARGE;
    public static final int SIZE_XXLARGE = UIConstants.FONT_SIZE_XXLARGE;
    
    // ============ CONSTRUCTOR ============
    
    /**
     * Private constructor (Singleton)
     */
    private FontManager() {
        fontCache = new HashMap<>();
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        initializeDefaultFonts();
    }
    
    /**
     * Initialize default fonts from UIConstants
     */
    private void initializeDefaultFonts() {
        // Cache commonly used fonts
        fontCache.put("title", UIConstants.FONT_TITLE);
        fontCache.put("subtitle", UIConstants.FONT_SUBTITLE);
        fontCache.put("heading", UIConstants.FONT_HEADING);
        fontCache.put("body", UIConstants.FONT_BODY);
        fontCache.put("body_bold", UIConstants.FONT_BODY_BOLD);
        fontCache.put("caption", UIConstants.FONT_CAPTION);
        fontCache.put("button", UIConstants.FONT_BUTTON);
    }
    
    // ============ FONT GETTERS (FROM UICONSTANTS) ============
    
    /**
     * Get title font (32pt bold)
     */
    public static Font getTitleFont() {
        return UIConstants.FONT_TITLE;
    }
    
    /**
     * Get subtitle font (18pt bold)
     */
    public static Font getSubtitleFont() {
        return UIConstants.FONT_SUBTITLE;
    }
    
    /**
     * Get heading font (16pt bold)
     */
    public static Font getHeadingFont() {
        return UIConstants.FONT_HEADING;
    }
    
    /**
     * Get body font (14pt plain)
     */
    public static Font getBodyFont() {
        return UIConstants.FONT_BODY;
    }
    
    /**
     * Get body bold font (14pt bold)
     */
    public static Font getBodyBoldFont() {
        return UIConstants.FONT_BODY_BOLD;
    }
    
    /**
     * Get caption font (12pt plain)
     */
    public static Font getCaptionFont() {
        return UIConstants.FONT_CAPTION;
    }
    
    /**
     * Get button font (14pt bold)
     */
    public static Font getButtonFont() {
        return UIConstants.FONT_BUTTON;
    }
    
    // ============ CUSTOM FONT CREATION ============
    
    /**
     * Create font with specific size
     */
    public Font createFont(String fontFamily, int style, int size) {
        String key = fontFamily + "_" + style + "_" + size;
        
        if (fontCache.containsKey(key)) {
            return fontCache.get(key);
        }
        
        Font font = new Font(fontFamily, style, size);
        fontCache.put(key, font);
        return font;
    }
    
    /**
     * Create plain font
     */
    public Font createPlainFont(int size) {
        return createFont(DEFAULT_FONT, Font.PLAIN, size);
    }
    
    /**
     * Create bold font
     */
    public Font createBoldFont(int size) {
        return createFont(DEFAULT_FONT, Font.BOLD, size);
    }
    
    /**
     * Create italic font
     */
    public Font createItalicFont(int size) {
        return createFont(DEFAULT_FONT, Font.ITALIC, size);
    }
    
    /**
     * Create bold italic font
     */
    public Font createBoldItalicFont(int size) {
        return createFont(DEFAULT_FONT, Font.BOLD | Font.ITALIC, size);
    }
    
    /**
     * Create monospace font
     */
    public Font createMonospaceFont(int size) {
        return createFont(MONOSPACE_FONT, Font.PLAIN, size);
    }
    
    // ============ FONT MODIFICATION ============
    
    /**
     * Derive font with new size
     */
    public Font deriveSize(Font baseFont, float newSize) {
        return baseFont.deriveFont(newSize);
    }
    
    /**
     * Derive font with new style
     */
    public Font deriveStyle(Font baseFont, int newStyle) {
        return baseFont.deriveFont(newStyle);
    }
    
    /**
     * Derive font with new size and style
     */
    public Font derive(Font baseFont, int newStyle, float newSize) {
        return baseFont.deriveFont(newStyle, newSize);
    }
    
    /**
     * Make font larger
     */
    public Font larger(Font baseFont, int increment) {
        return baseFont.deriveFont((float) (baseFont.getSize() + increment));
    }
    
    /**
     * Make font smaller
     */
    public Font smaller(Font baseFont, int decrement) {
        return baseFont.deriveFont((float) Math.max(8, baseFont.getSize() - decrement));
    }
    
    /**
     * Make font bold
     */
    public Font toBold(Font baseFont) {
        return baseFont.deriveFont(Font.BOLD);
    }
    
    /**
     * Make font plain
     */
    public Font toPlain(Font baseFont) {
        return baseFont.deriveFont(Font.PLAIN);
    }
    
    /**
     * Make font italic
     */
    public Font toItalic(Font baseFont) {
        return baseFont.deriveFont(Font.ITALIC);
    }
    
    // ============ CUSTOM FONT LOADING ============
    
    /**
     * Load custom font from file
     */
    public Font loadFont(File fontFile) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font from file: " + fontFile.getName());
            System.err.println("  " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load custom font from resources
     */
    public Font loadFontFromResources(String resourcePath) {
        try {
            InputStream fontStream = getClass().getClassLoader()
                .getResourceAsStream(resourcePath);
            
            if (fontStream == null) {
                System.err.println("Font resource not found: " + resourcePath);
                return null;
            }
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            ge.registerFont(font);
            fontStream.close();
            return font;
            
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font from resources: " + resourcePath);
            System.err.println("  " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Register custom font and cache it
     */
    public boolean registerFont(String name, Font font) {
        if (font == null) {
            return false;
        }
        
        fontCache.put(name, font);
        return ge.registerFont(font);
    }
    
    /**
     * Get registered custom font
     */
    public Font getCustomFont(String name) {
        return fontCache.get(name);
    }
    
    /**
     * Get custom font with size
     */
    public Font getCustomFont(String name, float size) {
        Font baseFont = fontCache.get(name);
        if (baseFont == null) {
            return null;
        }
        return baseFont.deriveFont(size);
    }
    
    /**
     * Get custom font with style and size
     */
    public Font getCustomFont(String name, int style, float size) {
        Font baseFont = fontCache.get(name);
        if (baseFont == null) {
            return null;
        }
        return baseFont.deriveFont(style, size);
    }
    
    // ============ FONT QUERIES ============
    
    /**
     * Get all available font families
     */
    public String[] getAvailableFonts() {
        return ge.getAvailableFontFamilyNames();
    }
    
    /**
     * Check if font family is available
     */
    public boolean isFontAvailable(String fontFamily) {
        String[] availableFonts = ge.getAvailableFontFamilyNames();
        for (String font : availableFonts) {
            if (font.equalsIgnoreCase(fontFamily)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get font metrics
     */
    public java.awt.FontMetrics getFontMetrics(Font font) {
        java.awt.Canvas canvas = new java.awt.Canvas();
        return canvas.getFontMetrics(font);
    }
    
    /**
     * Calculate text width
     */
    public int getTextWidth(String text, Font font) {
        java.awt.FontMetrics fm = getFontMetrics(font);
        return fm.stringWidth(text);
    }
    
    /**
     * Calculate text height
     */
    public int getTextHeight(Font font) {
        java.awt.FontMetrics fm = getFontMetrics(font);
        return fm.getHeight();
    }
    
    // ============ CACHE MANAGEMENT ============
    
    /**
     * Clear font cache
     */
    public void clearCache() {
        fontCache.clear();
        initializeDefaultFonts();
    }
    
    /**
     * Remove font from cache
     */
    public void removeFromCache(String key) {
        fontCache.remove(key);
    }
    
    /**
     * Get cache size
     */
    public int getCacheSize() {
        return fontCache.size();
    }
    
    // ============ UTILITY METHODS ============
    
    /**
     * Get font info string
     */
    public static String getFontInfo(Font font) {
        if (font == null) {
            return "null";
        }
        
        StringBuilder info = new StringBuilder();
        info.append(font.getFamily());
        info.append(" ");
        
        if (font.isBold() && font.isItalic()) {
            info.append("Bold Italic");
        } else if (font.isBold()) {
            info.append("Bold");
        } else if (font.isItalic()) {
            info.append("Italic");
        } else {
            info.append("Plain");
        }
        
        info.append(" ");
        info.append(font.getSize());
        info.append("pt");
        
        return info.toString();
    }
    
    /**
     * Print all cached fonts
     */
    public void printCachedFonts() {
        System.out.println("\n========== CACHED FONTS ==========");
        fontCache.forEach((key, font) -> {
            System.out.println(key + ": " + getFontInfo(font));
        });
        System.out.println("Total: " + fontCache.size() + " fonts");
        System.out.println("==================================\n");
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("🔤 Testing FontManager...\n");
        
        FontManager fm = FontManager.getInstance();
        
        // Test 1: Default fonts
        System.out.println("=== Default Fonts ===");
        System.out.println("Title: " + getFontInfo(getTitleFont()));
        System.out.println("Subtitle: " + getFontInfo(getSubtitleFont()));
        System.out.println("Heading: " + getFontInfo(getHeadingFont()));
        System.out.println("Body: " + getFontInfo(getBodyFont()));
        System.out.println("Caption: " + getFontInfo(getCaptionFont()));
        
        // Test 2: Custom fonts
        System.out.println("\n=== Custom Font Creation ===");
        Font custom14 = fm.createPlainFont(14);
        Font customBold16 = fm.createBoldFont(16);
        Font customItalic12 = fm.createItalicFont(12);
        
        System.out.println("Plain 14: " + getFontInfo(custom14));
        System.out.println("Bold 16: " + getFontInfo(customBold16));
        System.out.println("Italic 12: " + getFontInfo(customItalic12));
        
        // Test 3: Font modification
        System.out.println("\n=== Font Modification ===");
        Font base = getBodyFont();
        Font larger = fm.larger(base, 4);
        Font smaller = fm.smaller(base, 2);
        Font bold = fm.toBold(base);
        
        System.out.println("Base: " + getFontInfo(base));
        System.out.println("Larger: " + getFontInfo(larger));
        System.out.println("Smaller: " + getFontInfo(smaller));
        System.out.println("Bold: " + getFontInfo(bold));
        
        // Test 4: Font availability
        System.out.println("\n=== Font Availability ===");
        System.out.println("Segoe UI available: " + fm.isFontAvailable("Segoe UI"));
        System.out.println("Arial available: " + fm.isFontAvailable("Arial"));
        System.out.println("Comic Sans available: " + fm.isFontAvailable("Comic Sans MS"));
        
        // Test 5: Text measurements
        System.out.println("\n=== Text Measurements ===");
        String testText = "Hello World!";
        Font testFont = getBodyFont();
        int width = fm.getTextWidth(testText, testFont);
        int height = fm.getTextHeight(testFont);
        
        System.out.println("Text: '" + testText + "'");
        System.out.println("Font: " + getFontInfo(testFont));
        System.out.println("Width: " + width + "px");
        System.out.println("Height: " + height + "px");
        
        // Test 6: Cache info
        System.out.println("\n=== Cache Info ===");
        System.out.println("Cache size: " + fm.getCacheSize());
        fm.printCachedFonts();
        
        System.out.println("✅ All tests completed!");
    }
}