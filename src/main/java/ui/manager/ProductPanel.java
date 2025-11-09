package ui.manager;

import entity.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors; // <- Importa questo per filtrare
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class ProductPanel extends JPanel {
    
    // --- Costanti (Nessuna modifica) ---
    private static final Color COLOR_ACCENT = new Color(255, 152, 0);
    private static final Color COLOR_ACCENT_HOVER = new Color(230, 136, 0);
    private static final Color COLOR_BACKGROUND = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(235, 235, 235);
    private static final Color COLOR_TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color COLOR_TEXT_SECONDARY = new Color(120, 120, 120);
    
    private static final int CARD_WIDTH = 220;
    private static final int CARD_HEIGHT = 320;
    
    private static final Border BORDER_DEFAULT = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COLOR_BORDER, 1),
        new EmptyBorder(0, 0, 0, 0)
    );
    private static final Border BORDER_HOVER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COLOR_ACCENT, 2),
        new EmptyBorder(-1, -1, -1, -1)
    );
    
    private ProductService productService;
    private JPanel productsGrid;
    private JTextField searchField;
    private Timer cardLoadTimer; 
    
    public ProductPanel() {
        productService = ProductService.getInstance();
        initComponents();
        loadProducts();
    }
    
    // --- Metodi initComponents() e createHeader() (Nessuna modifica) ---
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 25, 25, 25)); 
        
        add(createHeader(), BorderLayout.NORTH);
        
        productsGrid = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        productsGrid.setBackground(COLOR_BACKGROUND);
        
        JScrollPane scrollPane = new JScrollPane(productsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JLabel title = new JLabel("Thực đơn Sản phẩm");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT_PRIMARY);
        
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(COLOR_BACKGROUND);
        
        searchField = new JTextField("🔍 Tìm món nhanh...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(280, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        
        controlsPanel.add(searchField);
        
        String[] categories = {"Tất cả Loại", "Tất cả Trạng thái"};
        for (String cat : categories) {
            JComboBox<String> combo = new JComboBox<>(new String[]{cat});
            combo.setPreferredSize(new Dimension(150, 38));
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            controlsPanel.add(combo);
        }
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    /**
     * [MODIFICATO]
     * Ora filtra i prodotti non disponibili (available == 0).
     */
    private void loadProducts() {
        productsGrid.removeAll();
        // 1. Get all products and filter them
        List<Product> allProducts = productService.getAllProducts();
        List<Product> availableProducts = allProducts.stream()
                .filter(Product::isAvailable)
                .collect(Collectors.toList());

        System.out.println("Loading " + availableProducts.size() + " available products...");

        // 2. Create an iterator for our product list
        Iterator<Product> productIterator = availableProducts.iterator();

        // 3. Stop any timer that might still be running from a previous load
        if (cardLoadTimer != null && cardLoadTimer.isRunning()) {
            cardLoadTimer.stop();
        }

        // 4. Set up the Timer
        // This timer will fire every 50 milliseconds
        int delayBetweenCards = 50; // (Adjust this value to be faster or slower)

        cardLoadTimer = new Timer(delayBetweenCards, e -> {
            if (productIterator.hasNext()) {
                // If there are more products, add the next one
                Product product = productIterator.next();
                productsGrid.add(createProductCard(product));
                
                // Refresh the layout after adding
                productsGrid.revalidate();
                productsGrid.repaint();
            } else {
                // No more products left, stop the timer
                ((Timer) e.getSource()).stop();
                System.out.println("All cards loaded.");
            }
        });

        // 5. Start the timer!
        cardLoadTimer.start();
    }
    
    // --- Metodo createProductCard() (Nessuna modifica) ---
    // Questo metodo chiama il `loadImageAsync` riscritto
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(COLOR_BACKGROUND);
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBorder(BORDER_DEFAULT);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BORDER_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BORDER_DEFAULT);
            }
        });
        
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(CARD_WIDTH, 180));
        imageContainer.setBackground(new Color(250, 250, 250));
        
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setVerticalAlignment(SwingConstants.CENTER);
        imgLabel.setText("⏳");
        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        imageContainer.add(imgLabel, BorderLayout.CENTER);
        
        // Chiama il metodo `loadImageAsync` aggiornato
        loadImageAsync(product, imgLabel, imageContainer);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(COLOR_BACKGROUND);
        infoPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String descText = product.getDescription() != null ? product.getDescription() : product.getCategory();
        if (descText.length() > 60) {
            descText = descText.substring(0, 57) + "...";
        }
        
        JLabel descLabel = new JLabel("<html><body style='width: 100%'>" + descText + "</body></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(COLOR_TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalGlue()); 
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(COLOR_BACKGROUND);
        footerPanel.setBorder(new EmptyBorder(0, 12, 10, 12)); 
        
        JLabel priceLabel = new JLabel(String.format("%,dđ", (int) product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(COLOR_ACCENT);
        
        JButton addBtn = new JButton("+ Thêm");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(COLOR_ACCENT);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(COLOR_ACCENT_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(COLOR_ACCENT);
            }
        });
        
        addBtn.addActionListener(e -> {
            System.out.println("Adding to cart: " + product.getName());
            JOptionPane.showMessageDialog(card, 
                product.getName() + " đã được thêm vào giỏ hàng!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        footerPanel.add(priceLabel, BorderLayout.WEST);
        footerPanel.add(addBtn, BorderLayout.EAST);
        
        card.add(imageContainer, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * [RISCRITTO]
     * Carica l'immagine dal CLASSPATH (cartella resources) invece che da un URL web.
     */
    private void loadImageAsync(Product product, JLabel imgLabel, JPanel imageContainer) {
        // Ora `imagePath` sarà "images/1.jpg", "images/2.png", ecc.
        String imagePath = product.getImageUrl();
        
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imgLabel.setText("🍔"); // Placeholder per nessun percorso immagine
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            return;
        }
        
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    // 1. Formatta il percorso per essere assoluto dalla root del classpath
                    String resourcePath = imagePath;
                    if (!resourcePath.startsWith("/")) {
                        resourcePath = "/" + resourcePath;
                    }

                    // 2. Ottieni l'URL della risorsa dal classpath
                    URL resourceUrl = getClass().getResource(resourcePath);
                    
                    // 3. Controlla se la risorsa è stata trovata
                    if (resourceUrl == null) {
                        System.err.println("✗ Risorsa non trovata: " + resourcePath);
                        return null; // File mancante da resources/images
                    }

                    // 4. Leggi l'immagine dalla risorsa
                    BufferedImage image = ImageIO.read(resourceUrl);
                    
                    if (image != null) {
                        // 5. Scala l'immagine
                        Image scaledImage = image.getScaledInstance(CARD_WIDTH, 180, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    } else {
                        System.err.println("✗ ImageIO.read ha restituito null per: " + resourcePath);
                        return null;
                    }
                } catch (Exception e) {
                    System.err.println("✗ Errore durante il caricamento della risorsa " + imagePath + ": " + e.getMessage());
                    e.printStackTrace(); // Per un debug dettagliato
                    return null;
                }
            }
            
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imgLabel.setIcon(icon);
                        imgLabel.setText(null);
                        
                        // Il badge "Hết hàng" non è più necessario qui
                        // perché abbiamo filtrato i prodotti in loadProducts()
                        
                    } else {
                        // Se il caricamento fallisce (null), imposta il placeholder
                        imgLabel.setText("🍔");
                        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel metodo done(): " + e.getMessage());
                    imgLabel.setText("❌"); // Placeholder per errore
                    imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                }
            }
        };
        worker.execute();
    }
    
    // --- Classe WrapLayout (Nessuna modifica) ---
    static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }
        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        if (rowWidth != 0) {
                            rowWidth += hgap;
                        }
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                addRow(dim, rowWidth, rowHeight);
                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;
                Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                if (scrollPane != null && target.isValid()) {
                    dim.width -= (hgap + 1);
                }
                return dim;
            }
        }
        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);
            if (dim.height > 0) {
                dim.height += getVgap();
            }
            dim.height += rowHeight;
        }
    }
}
