package util;

import config.AppConfig;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * ImageUtil - Image Handling Utility
 * Path: Source Packages/util/ImageUtil.java
 * 
 * Provides image loading, resizing, and processing utilities
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ImageUtil {
    
    // ============ DEFAULT/PLACEHOLDER IMAGES ============
    
    private static final String DEFAULT_PRODUCT_IMAGE = "default_product.png";
    private static final String DEFAULT_AVATAR_IMAGE = "default_avatar.png";
    private static final String PLACEHOLDER_IMAGE = "placeholder.png";
    
    // ============ IMAGE LOADING ============
    
    /**
     * Load image from file path
     * 
     * @param filePath Path to image file
     * @return BufferedImage or null if failed
     */
    public static BufferedImage loadImage(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Error loading image from file: " + filePath);
            System.err.println("  " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Load image from resources
     * 
     * @param resourcePath Path in resources folder
     * @return BufferedImage or null if failed
     */
    public static BufferedImage loadImageFromResources(String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return null;
        }
        
        try {
            URL imageUrl = ImageUtil.class.getClassLoader().getResource(resourcePath);
            if (imageUrl != null) {
                return ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            System.err.println("Error loading image from resources: " + resourcePath);
            System.err.println("  " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Load ImageIcon from file path
     * 
     * @param filePath Path to image file
     * @return ImageIcon or null if failed
     */
    public static ImageIcon loadIcon(String filePath) {
        BufferedImage image = loadImage(filePath);
        return image != null ? new ImageIcon(image) : null;
    }
    
    /**
     * Load ImageIcon from resources
     * 
     * @param resourcePath Path in resources folder
     * @return ImageIcon or null if failed
     */
    public static ImageIcon loadIconFromResources(String resourcePath) {
        BufferedImage image = loadImageFromResources(resourcePath);
        return image != null ? new ImageIcon(image) : null;
    }
    
    /**
     * Load product image with fallback to default
     * 
     * @param imageUrl Product image URL/path
     * @return BufferedImage (never null)
     */
    public static BufferedImage loadProductImage(String imageUrl) {
        // Try to load the specified image
        BufferedImage image = loadImage(AppConfig.PRODUCTS_IMAGES_PATH + imageUrl);
        
        // If failed, try to load from resources
        if (image == null && imageUrl != null && !imageUrl.isEmpty()) {
            image = loadImageFromResources("images/products/" + imageUrl);
        }
        
        // If still failed, load default product image
        if (image == null) {
            image = loadImageFromResources("images/" + DEFAULT_PRODUCT_IMAGE);
        }
        
        // If even default failed, create a placeholder
        if (image == null) {
            image = createPlaceholderImage(200, 200, "No Image");
        }
        
        return image;
    }
    
    /**
     * Load avatar image with fallback to default
     * 
     * @param avatarUrl Avatar image URL/path
     * @return BufferedImage (never null)
     */
    public static BufferedImage loadAvatar(String avatarUrl) {
        // Try to load the specified avatar
        BufferedImage image = loadImage(AppConfig.AVATARS_PATH + avatarUrl);
        
        // If failed, try to load from resources
        if (image == null && avatarUrl != null && !avatarUrl.isEmpty()) {
            image = loadImageFromResources("images/avatars/" + avatarUrl);
        }
        
        // If still failed, load default avatar
        if (image == null) {
            image = loadImageFromResources("images/" + DEFAULT_AVATAR_IMAGE);
        }
        
        // If even default failed, create a placeholder
        if (image == null) {
            image = createPlaceholderImage(100, 100, "?");
        }
        
        return image;
    }
    
    // ============ IMAGE RESIZING ============
    
    /**
     * Resize image to specific dimensions
     * 
     * @param originalImage Original image
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @return Resized image
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, 
                                           int targetWidth, int targetHeight) {
        if (originalImage == null) {
            return null;
        }
        
        BufferedImage resizedImage = new BufferedImage(
            targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = resizedImage.createGraphics();
        
        // Enable high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
                            RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        
        return resizedImage;
    }
    
    /**
     * Resize image maintaining aspect ratio
     * 
     * @param originalImage Original image
     * @param maxWidth Maximum width
     * @param maxHeight Maximum height
     * @return Resized image
     */
    public static BufferedImage resizeImageProportional(BufferedImage originalImage, 
                                                       int maxWidth, int maxHeight) {
        if (originalImage == null) {
            return null;
        }
        
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calculate scaling factor
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scalingFactor = Math.min(widthRatio, heightRatio);
        
        // Calculate new dimensions
        int newWidth = (int) (originalWidth * scalingFactor);
        int newHeight = (int) (originalHeight * scalingFactor);
        
        return resizeImage(originalImage, newWidth, newHeight);
    }
    
    /**
     * Scale ImageIcon to fit dimensions
     * 
     * @param icon Original icon
     * @param width Target width
     * @param height Target height
     * @return Scaled ImageIcon
     */
    public static ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return null;
        }
        
        Image scaledImage = icon.getImage().getScaledInstance(
            width, height, Image.SCALE_SMOOTH
        );
        
        return new ImageIcon(scaledImage);
    }
    
    /**
     * Scale ImageIcon proportionally
     * 
     * @param icon Original icon
     * @param maxWidth Maximum width
     * @param maxHeight Maximum height
     * @return Scaled ImageIcon
     */
    public static ImageIcon scaleIconProportional(ImageIcon icon, 
                                                 int maxWidth, int maxHeight) {
        if (icon == null) {
            return null;
        }
        
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scalingFactor = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * scalingFactor);
        int newHeight = (int) (originalHeight * scalingFactor);
        
        return scaleIcon(icon, newWidth, newHeight);
    }
    
    // ============ IMAGE CROPPING ============
    
    /**
     * Crop image to square (from center)
     * 
     * @param image Original image
     * @return Cropped square image
     */
    public static BufferedImage cropToSquare(BufferedImage image) {
        if (image == null) {
            return null;
        }
        
        int width = image.getWidth();
        int height = image.getHeight();
        int size = Math.min(width, height);
        
        int x = (width - size) / 2;
        int y = (height - size) / 2;
        
        return image.getSubimage(x, y, size, size);
    }
    
    /**
     * Crop image to circle (for avatars)
     * 
     * @param image Original image
     * @return Circular cropped image
     */
    public static BufferedImage cropToCircle(BufferedImage image) {
        if (image == null) {
            return null;
        }
        
        // First crop to square
        BufferedImage square = cropToSquare(image);
        int size = square.getWidth();
        
        // Create circular mask
        BufferedImage circularImage = new BufferedImage(
            size, size, BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = circularImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle
        g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2d.drawImage(square, 0, 0, null);
        g2d.dispose();
        
        return circularImage;
    }
    
    // ============ IMAGE EFFECTS ============
    
    /**
     * Convert image to grayscale
     * 
     * @param image Original image
     * @return Grayscale image
     */
    public static BufferedImage toGrayscale(BufferedImage image) {
        if (image == null) {
            return null;
        }
        
        BufferedImage grayscale = new BufferedImage(
            image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY
        );
        
        Graphics2D g2d = grayscale.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        return grayscale;
    }
    
    /**
     * Adjust image brightness
     * 
     * @param image Original image
     * @param factor Brightness factor (1.0 = no change, > 1.0 = brighter, < 1.0 = darker)
     * @return Adjusted image
     */
    public static BufferedImage adjustBrightness(BufferedImage image, float factor) {
        if (image == null) {
            return null;
        }
        
        BufferedImage adjusted = new BufferedImage(
            image.getWidth(), image.getHeight(), image.getType()
        );
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                
                int alpha = (rgb >> 24) & 0xFF;
                int red = (int) Math.min(255, ((rgb >> 16) & 0xFF) * factor);
                int green = (int) Math.min(255, ((rgb >> 8) & 0xFF) * factor);
                int blue = (int) Math.min(255, (rgb & 0xFF) * factor);
                
                int newRgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                adjusted.setRGB(x, y, newRgb);
            }
        }
        
        return adjusted;
    }
    
    /**
     * Add rounded corners to image
     * 
     * @param image Original image
     * @param cornerRadius Radius of corners
     * @return Image with rounded corners
     */
    public static BufferedImage roundCorners(BufferedImage image, int cornerRadius) {
        if (image == null) {
            return null;
        }
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage rounded = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = rounded.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(
            0, 0, width, height, cornerRadius, cornerRadius
        ));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        return rounded;
    }
    
    // ============ PLACEHOLDER & GENERATION ============
    
    /**
     * Create placeholder image with text
     * 
     * @param width Width of placeholder
     * @param height Height of placeholder
     * @param text Text to display
     * @return Placeholder image
     */
    public static BufferedImage createPlaceholderImage(int width, int height, String text) {
        BufferedImage placeholder = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_RGB
        );
        
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Background
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        
        // Border
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, width - 1, height - 1);
        
        // Text
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, Math.min(width, height) / 4));
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - textWidth) / 2;
        int y = (height + textHeight / 2) / 2;
        
        g2d.drawString(text, x, y);
        g2d.dispose();
        
        return placeholder;
    }
    
    /**
     * Create colored square image (for icons/indicators)
     * 
     * @param size Size of square
     * @param color Color of square
     * @return Colored square image
     */
    public static BufferedImage createColoredSquare(int size, Color color) {
        BufferedImage square = new BufferedImage(
            size, size, BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = square.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();
        
        return square;
    }
    
    // ============ IMAGE SAVING ============
    
    /**
     * Save image to file
     * 
     * @param image Image to save
     * @param filePath Output file path
     * @param format Image format (png, jpg, etc.)
     * @return true if saved successfully
     */
    public static boolean saveImage(BufferedImage image, String filePath, String format) {
        if (image == null || filePath == null || format == null) {
            return false;
        }
        
        try {
            File outputFile = new File(filePath);
            
            // Create parent directories if they don't exist
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            ImageIO.write(image, format, outputFile);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving image: " + filePath);
            System.err.println("  " + e.getMessage());
            return false;
        }
    }
    
    // ============ IMAGE VALIDATION ============
    
    /**
     * Check if file is a valid image
     * 
     * @param file File to check
     * @return true if valid image
     */
    public static boolean isValidImage(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Get image dimensions
     * 
     * @param filePath Path to image file
     * @return Dimension object or null if failed
     */
    public static Dimension getImageDimensions(String filePath) {
        BufferedImage image = loadImage(filePath);
        if (image != null) {
            return new Dimension(image.getWidth(), image.getHeight());
        }
        return null;
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("🖼️  Testing ImageUtil...\n");
        
        // Test 1: Create placeholder
        System.out.println("Creating placeholder image...");
        BufferedImage placeholder = createPlaceholderImage(200, 200, "Test");
        System.out.println("✅ Placeholder created: " + 
            placeholder.getWidth() + "x" + placeholder.getHeight());
        
        // Test 2: Create colored square
        System.out.println("\nCreating colored square...");
        BufferedImage colorSquare = createColoredSquare(50, Color.RED);
        System.out.println("✅ Colored square created");
        
        // Test 3: Test resizing
        System.out.println("\nTesting resize...");
        BufferedImage resized = resizeImage(placeholder, 100, 100);
        System.out.println("✅ Resized to: " + 
            resized.getWidth() + "x" + resized.getHeight());
        
        // Test 4: Test proportional resize
        System.out.println("\nTesting proportional resize...");
        BufferedImage resizedProp = resizeImageProportional(placeholder, 150, 100);
        System.out.println("✅ Resized proportionally to: " + 
            resizedProp.getWidth() + "x" + resizedProp.getHeight());
        
        // Test 5: Test effects
        System.out.println("\nTesting effects...");
        BufferedImage grayscale = toGrayscale(placeholder);
        System.out.println("✅ Grayscale conversion successful");
        
        BufferedImage brighter = adjustBrightness(placeholder, 1.5f);
        System.out.println("✅ Brightness adjustment successful");
        
        BufferedImage rounded = roundCorners(placeholder, 20);
        System.out.println("✅ Rounded corners successful");
        
        System.out.println("\n✅ All tests completed!");
    }
}