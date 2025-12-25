package ui.manager;

import entity.Product;

import service.ProductService;

import ui.components.ProductCard;
import ui.components.RoundedButton;

import form.ProductForm;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class ProductPanel extends JPanel {
    
    // --- Hằng số (Không thay đổi) ---
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(0, 204, 0);

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
    
    private ManagerMainFrame parentFrame;
    private ProductService productService;
    private JPanel productsGrid;
    private JTextField searchField;
    private Timer cardLoadTimer; 
    private RoundedButton addProductBtn;
    private RoundedButton refreshBtn;

    
    public ProductPanel(ManagerMainFrame parentFrame) {
        this.parentFrame=parentFrame;
        productService = ProductService.getInstance();
        initComponents();
        loadProducts();
    }
    
    // --- Phương thức initComponents() và createHeader() (Không thay đổi) ---
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
        
        // searchField = new JTextField("🔍 Tìm món nhanh...");
        // searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // searchField.setForeground(Color.GRAY);
        // searchField.setPreferredSize(new Dimension(280, 38));
        // searchField.setBorder(BorderFactory.createCompoundBorder(
        //     BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
        //     new EmptyBorder(5, 12, 5, 12)
        // ));
        
        // controlsPanel.add(searchField);
        addProductBtn = new RoundedButton("Thêm Product", 8);
        addProductBtn.setBackground(ORANGE);
        addProductBtn.setPreferredSize(new Dimension(180, 38));
        addProductBtn.setMaximumSize(new Dimension(200, 38));
        addProductBtn.addActionListener(e -> {
            new ProductForm(parentFrame).setVisible(true);
            
        });
        controlsPanel.add(addProductBtn);

        refreshBtn = new RoundedButton("Refresh", 8);
        refreshBtn.setBackground(GREEN);
        refreshBtn.setPreferredSize(new Dimension(180, 38));
        refreshBtn.setMaximumSize(new Dimension(200, 38));
        refreshBtn.addActionListener(e -> {
            loadProducts();
        });
        controlsPanel.add(refreshBtn);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    /**
     * [ĐÃ SỬA ĐỔI]
     * Lọc các sản phẩm không có sẵn (available == 0).
     */
    private void loadProducts() {
        productsGrid.removeAll();
        // 1. Lấy tất cả sản phẩm và lọc chúng
        List<Product> allProducts = productService.getAllProducts();
        List<Product> availableProducts = allProducts.stream()
                .filter(Product::isAvailable)
                .collect(Collectors.toList());

        System.out.println("Đang tải " + availableProducts.size() + " sản phẩm có sẵn...");

        // 2. Tạo iterator cho danh sách sản phẩm
        Iterator<Product> productIterator = availableProducts.iterator();

        // 3. Dừng bất kỳ timer nào đang chạy từ lần tải trước
        if (cardLoadTimer != null && cardLoadTimer.isRunning()) {
            cardLoadTimer.stop();
        }

        // 4. Thiết lập Timer
        // Timer này sẽ kích hoạt mỗi 50 mili giây
        int delayBetweenCards = 50; // (Điều chỉnh giá trị này để nhanh hơn hoặc chậm hơn)

        cardLoadTimer = new Timer(delayBetweenCards, e -> {
            if (productIterator.hasNext()) {
                // Nếu còn sản phẩm, thêm sản phẩm tiếp theo
                Product product = productIterator.next();
                productsGrid.add(createProductCard(product));
                
                // Làm mới bố cục sau khi thêm
                productsGrid.revalidate();
                productsGrid.repaint();
            } else {
                // Không còn sản phẩm nào, dừng timer
                ((Timer) e.getSource()).stop();
                System.out.println("Đã tải xong tất cả thẻ sản phẩm.");
            }
        });

        // 5. Bắt đầu timer!
        cardLoadTimer.start();
    }
    
    private JPanel createProductCard(Product product) {
        ProductCard card=new ProductCard(product, true);

        card.getEditButton().addActionListener(e -> {
            new ProductForm(parentFrame, product).setVisible(true);
        });

        return card;
    }
    
    /**
     * Tải hình ảnh từ CLASSPATH (thư mục resources) thay vì từ URL web.
     */
    private void loadImageAsync(Product product, JLabel imgLabel, JPanel imageContainer) {
        // Bây giờ `imagePath` sẽ là "images/1.jpg", "images/2.png", v.v.
        String imagePath = product.getImageUrl();
        
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imgLabel.setText("🍔"); // Placeholder khi không có đường dẫn ảnh
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            return;
        }
        
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    // 1. Định dạng đường dẫn để trở thành tuyệt đối từ root của classpath
                    String resourcePath = imagePath;
                    if (!resourcePath.startsWith("/")) {
                        resourcePath = "/" + resourcePath;
                    }

                    // 2. Lấy URL của tài nguyên từ classpath
                    URL resourceUrl = getClass().getResource(resourcePath);
                    
                    // 3. Kiểm tra xem tài nguyên có được tìm thấy không
                    if (resourceUrl == null) {
                        System.err.println("✗ Không tìm thấy tài nguyên: " + resourcePath);
                        return null; // File bị thiếu từ resources/images
                    }

                    // 4. Đọc hình ảnh từ tài nguyên
                    BufferedImage image = ImageIO.read(resourceUrl);
                    
                    if (image != null) {
                        // 5. Thay đổi kích thước hình ảnh
                        Image scaledImage = image.getScaledInstance(CARD_WIDTH, 180, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    } else {
                        System.err.println("✗ ImageIO.read trả về null cho: " + resourcePath);
                        return null;
                    }
                } catch (Exception e) {
                    System.err.println("✗ Lỗi khi tải tài nguyên " + imagePath + ": " + e.getMessage());
                    e.printStackTrace(); // Để debug chi tiết
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
                        
                        // Badge "Hết hàng" không còn cần thiết ở đây
                        // vì chúng ta đã lọc sản phẩm trong loadProducts()
                        
                    } else {
                        // Nếu tải thất bại (null), đặt placeholder
                        imgLabel.setText("🍔");
                        imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi trong phương thức done(): " + e.getMessage());
                    imgLabel.setText("❌"); // Placeholder cho lỗi
                    imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                }
            }
        };
        worker.execute();
    }
    
    // --- Lớp WrapLayout (Không thay đổi) ---
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
            return layoutSize(target, false);
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
