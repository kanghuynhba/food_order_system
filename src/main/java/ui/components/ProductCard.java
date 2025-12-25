package ui.components;

import entity.Product;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

import java.io.File;


/**
 * ProductCard - Modern product display card
 * 
 * Features:
 * - Product image area
 * - Name, price, description
 * - Status badge
 * - Add to cart button
 * - Hover effects
 */
public class ProductCard extends RoundedPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color RED = new Color(244, 67, 54);

    private Product product;
    private JLabel imgLabel;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel descLabel;
    private RoundedButton addBtn;
    private RoundedButton editBtn;
    private StatusBadge statusBadge;
    private NumberFormat currencyFormat;
    private boolean disableBtn;
    
    // ============ CONSTRUCTOR ============
    
    public ProductCard(Product product) {
        super(12, true);
        this.product = product;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        initComponents();
        loadProductData();
        enableHoverEffect(true);
    }

    public ProductCard(Product product, boolean disableBtn) {
        this(product);
        this.disableBtn=disableBtn;
        initComponents();
        loadProductData();
        enableHoverEffect(true);

    }
    
    // ============ INIT COMPONENTS ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 10, 0));
        setPreferredSize(new Dimension(220, 320));
        
        // Image area
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBackground(new Color(245, 245, 245));
        imgPanel.setPreferredSize(new Dimension(220, 160));
        
        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setVerticalAlignment(SwingConstants.CENTER);
        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        imgLabel.setText("⏳");
        imgPanel.add(imgLabel, BorderLayout.CENTER);
        
        // Status badge (top-right corner)
        statusBadge = new StatusBadge("", StatusBadge.StatusType.SUCCESS);
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        badgePanel.setOpaque(false);
        badgePanel.add(statusBadge);
        imgPanel.add(badgePanel, BorderLayout.NORTH);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        
        // Name
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(new Color(33, 33, 33));
        
        // Description
        descLabel = new JLabel();
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        
        // Price
        priceLabel = new JLabel();
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(ORANGE);
        
        // Add button
        addBtn = new RoundedButton("Thêm", 8);
        addBtn.setBackground(ORANGE);
        addBtn.setPreferredSize(new Dimension(180, 38));
        addBtn.setMaximumSize(new Dimension(200, 38));

        // Edit button
        editBtn = new RoundedButton("Sửa", 8);
        editBtn.setBackground(ORANGE);
        editBtn.setPreferredSize(new Dimension(180, 38));
        editBtn.setMaximumSize(new Dimension(200, 38));

        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        if(!disableBtn) {
            infoPanel.add(addBtn);
        } else {
            infoPanel.add(editBtn);
        }
        
        add(imgPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }    
    // ============ LOAD DATA ============
    
    private void loadProductData() {
        if (product == null) return;

        loadProductImage();
        
        // Set name (truncate if too long)
        String name = product.getName();
        if (name.length() > 25) {
            name = name.substring(0, 22) + "...";
        }
        nameLabel.setText(name);
        
        // Set description
        String desc = product.getDescription();
        if (desc != null && desc.length() > 35) {
            desc = desc.substring(0, 32) + "...";
        }
        descLabel.setText(desc != null ? desc : product.getCategory());
        
        // Set price
        priceLabel.setText(currencyFormat.format(product.getPrice()) + "đ");
        
        // Set status badge
        if (product.isAvailable()) {
            statusBadge.setVisible(false);
        } else {
            statusBadge.setText("Hết hàng");
            statusBadge.setType(StatusBadge.StatusType.ERROR);
            statusBadge.setVisible(true);
            addBtn.setEnabled(false);
        }
    }

    private void loadProductImage() {
        String imagePath = product.getImageUrl();

        // 1. Check if path is empty
        if (imagePath == null || imagePath.trim().isEmpty()) {
            showEmoji();
            return;
        }

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    // --- FIX STARTS HERE ---
                    
                    // Case A: Handling Local Files (from JFileChooser)
                    File file = new File(imagePath);
                    if (file.exists()) {
                         BufferedImage image = ImageIO.read(file);
                         if (image != null) {
                             return scaleImage(image);
                         }
                    }

                    // Case B: Handling Web URLs (if you paste an https:// link)
                    if (imagePath.startsWith("http")) {
                         URL url = new URL(imagePath);
                         BufferedImage image = ImageIO.read(url);
                         if (image != null) {
                             return scaleImage(image);
                         }
                    }

                    // --- FIX ENDS HERE ---

                    System.err.println("✗ File not found or invalid: " + imagePath);
                    return null;

                } catch (Exception e) {
                    System.err.println("✗ Error loading image: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imgLabel.setIcon(icon);
                        imgLabel.setText(null); // Remove text/emoji
                    } else {
                        showEmoji();
                    }
                } catch (Exception e) {
                    showEmoji();
                }
            }
        };
        worker.execute();
    }

// Helper method to keep code clean
private ImageIcon scaleImage(BufferedImage originalImage) {
    Image scaledImage = originalImage.getScaledInstance(220, 160, Image.SCALE_SMOOTH);
    return new ImageIcon(scaledImage);
}

// Helper method to show Emoji
private void showEmoji() {
    String emoji = getEmojiForCategory(product.getCategory());
    imgLabel.setIcon(null);
    imgLabel.setText(emoji);
    imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
}
    
    // ============ HELPER ============
    
    private String getEmojiForCategory(String category) {
        if (category == null) return "🍔";
        return switch (category.toLowerCase()) {
            case "burger" -> "🍔";
            case "pizza" -> "🍕";
            case "chicken" -> "🍗";
            case "sides" -> "🍟";
            case "drinks" -> "🥤";
            case "combo" -> "🎁";
            default -> "🍽️";
        };
    }
    
    // ============ GETTERS ============
    
    public Product getProduct() {
        return product;
    }
    
    public RoundedButton getAddButton() {
        return addBtn;
    }

    public RoundedButton getEditButton() {
        return editBtn;
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ProductCard Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            
            JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
            container.setBackground(new Color(245, 245, 245));
            
            // Test products
            Product p1 = new Product("Big Mac Combo", "Burger gà với phô mai cheddar", 
                89000, "Burger", "img.jpg");
            Product p2 = new Product("Pizza Pepperoni", "Pizza truyền thống với xúc xích", 
                129000, "Pizza", "img.jpg");
            Product p3 = new Product("Gà Rán Giòn (6 pcs)", "Gà rán giòn tan", 
                65000, "Chicken", "img.jpg");
            p3.setAvailable(0); // Out of stock
            
            container.add(new ProductCard(p1));
            container.add(new ProductCard(p2));
            container.add(new ProductCard(p3));
            
            frame.add(container);
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(frame,
                "✅ ProductCard Test\n\n" +
                "3 product cards:\n" +
                "- Available products\n" +
                "- Out of stock product\n" +
                "Hover to see effects!",
                "Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
