package ui.display;

import entity.Order;
import entity.OrderItem;
import service.OrderService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.border.EmptyBorder;

/**
 * TiviFrame - Public Order Display Screen
 * Màn hình hiển thị công khai đơn hàng cho khách hàng
 * Path: Source Packages/ui/display/TiviFrame.java
 * 
 * Features:
 * - Hiển thị đơn "Đang chế biến" (màu cam)
 * - Hiển thị đơn "Sẵn sàng nhận" (màu xanh)
 * - Auto refresh mỗi 5 giây
 * - Press Shift+F6 để test
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 */
public class TiviFrame extends JFrame {
    
    // ============ CONSTANTS ============
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    private static final Color DARK_TEXT = new Color(33, 33, 33);
    private static final Color LIGHT_TEXT = new Color(117, 117, 117);
    
    private static final int REFRESH_INTERVAL = 5000; // 5 seconds
    
    // ============ COMPONENTS ============
    private OrderService orderService;
    private Timer refreshTimer;
    private NumberFormat currencyFormat;
    
    // UI Components
    private JLabel lblTime;
    private JLabel lblCookingCount;
    private JLabel lblReadyCount;
    private JPanel pnlCooking;
    private JPanel pnlReady;
    
    // ============ CONSTRUCTOR ============
    
    public TiviFrame() {
        initServices();
        initComponents();
        setupKeyboardShortcut();
        startAutoRefresh();
        loadOrders();
    }
    
    // ============ INITIALIZATION ============
    
    private void initServices() {
        orderService = OrderService.getInstance();
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    }
    
    private void initComponents() {
        setTitle("FastFood Express - Order Display");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content (Cooking + Ready sections)
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(createFooter(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // ============ HEADER ============
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Logo + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(BG_COLOR);
        
        JLabel lblLogo = new JLabel("🍔");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JLabel lblTitle = new JLabel("FastFood");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitle.setForeground(ORANGE);
        
        leftPanel.add(lblLogo);
        leftPanel.add(lblTitle);
        
        // Time
        lblTime = new JLabel();
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTime.setForeground(DARK_TEXT);
        updateTime();
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(lblTime, BorderLayout.EAST);
        
        return header;
    }
    
    // ============ CONTENT ============
    
    private JPanel createContent() {
        JPanel content = new JPanel(new GridLayout(1, 2, 30, 0));
        content.setBackground(BG_COLOR);
        
        // Left: Cooking Orders
        content.add(createCookingSection());
        
        // Right: Ready Orders
        content.add(createReadySection());
        
        return content;
    }
    
    private JPanel createCookingSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ORANGE, 3),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblIcon = new JLabel("⏰");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel lblTitle = new JLabel(" ĐANG CHẾ BIẾN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(ORANGE);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblIcon);
        titlePanel.add(lblTitle);
        
        lblCookingCount = new JLabel("0");
        lblCookingCount.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCookingCount.setForeground(ORANGE);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(lblCookingCount, BorderLayout.EAST);
        
        // Orders Container
        pnlCooking = new JPanel();
        pnlCooking.setLayout(new BoxLayout(pnlCooking, BoxLayout.Y_AXIS));
        pnlCooking.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(pnlCooking);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        section.add(headerPanel, BorderLayout.NORTH);
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createReadySection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 3),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblIcon = new JLabel("✅");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel lblTitle = new JLabel(" SẴN SÀNG NHẬN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(GREEN);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblIcon);
        titlePanel.add(lblTitle);
        
        lblReadyCount = new JLabel("0");
        lblReadyCount.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblReadyCount.setForeground(GREEN);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(lblReadyCount, BorderLayout.EAST);
        
        // Orders Container
        pnlReady = new JPanel();
        pnlReady.setLayout(new BoxLayout(pnlReady, BoxLayout.Y_AXIS));
        pnlReady.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(pnlReady);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        section.add(headerPanel, BorderLayout.NORTH);
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }
    
    // ============ FOOTER ============
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ORANGE);
        footer.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblMessage = new JLabel(
            "⚡ Xin vui lòng chờ, đơn hàng sẽ sẵn sàng trong ít phút • " +
            "Cảm ơn quý khách đã lựa chọn FastFood Express!"
        );
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        
        footer.add(lblMessage, BorderLayout.CENTER);
        
        return footer;
    }
    
    // ============ ORDER CARDS ============
    
    private JPanel createOrderCard(Order order, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Left: Order Number
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblOrderNum = new JLabel("#" + order.getOrderId());
        lblOrderNum.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblOrderNum.setForeground(accentColor);
        leftPanel.add(lblOrderNum, BorderLayout.CENTER);
        
        // Center: Order Details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        // Time
        JLabel lblTime = new JLabel(order.getFormattedTime());
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTime.setForeground(LIGHT_TEXT);
        
        // Items
        List<OrderItem> items = orderService.getOrderItems(order.getOrderId());
        StringBuilder itemsText = new StringBuilder("<html>");
        for (int i = 0; i < Math.min(items.size(), 3); i++) {
            OrderItem item = items.get(i);
            itemsText.append(String.format("• %dx %s<br>", 
                item.getQuantity(), item.getProductName()));
        }
        if (items.size() > 3) {
            itemsText.append("+ ").append(items.size() - 3).append(" món khác");
        }
        itemsText.append("</html>");
        
        JLabel lblItems = new JLabel(itemsText.toString());
        lblItems.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblItems.setForeground(DARK_TEXT);
        
        centerPanel.add(lblTime);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(lblItems);
        
        // Right: Status
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        
        String statusText = order.getStatus() == 3 ? 
            "<html><center>Đang chế biến<br>IN PROGRESS</center></html>" :
            "<html><center>Sẵn sàng<br>✓ READY</center></html>";
        
        JLabel lblStatus = new JLabel(statusText);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblStatus.setForeground(accentColor);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setPreferredSize(new Dimension(140, 80));
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        rightPanel.add(lblStatus, BorderLayout.CENTER);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    // ============ DATA LOADING ============
    
    private void loadOrders() {
        // Load Cooking Orders (status = 3)
        List<Order> cookingOrders = orderService.getOrdersByStatus(3);
        pnlCooking.removeAll();
        
        for (Order order : cookingOrders) {
            pnlCooking.add(createOrderCard(order, ORANGE));
            pnlCooking.add(Box.createVerticalStrut(10));
        }
        
        lblCookingCount.setText(String.valueOf(cookingOrders.size()));
        
        // Load Ready Orders (status = 4)
        List<Order> readyOrders = orderService.getOrdersByStatus(4);
        pnlReady.removeAll();
        
        for (Order order : readyOrders) {
            pnlReady.add(createOrderCard(order, GREEN));
            pnlReady.add(Box.createVerticalStrut(10));
        }
        
        lblReadyCount.setText(String.valueOf(readyOrders.size()));
        
        // Refresh UI
        pnlCooking.revalidate();
        pnlCooking.repaint();
        pnlReady.revalidate();
        pnlReady.repaint();
    }
    
    // ============ AUTO REFRESH ============
    
    private void startAutoRefresh() {
        refreshTimer = new Timer(REFRESH_INTERVAL, e -> {
            loadOrders();
            updateTime();
        });
        refreshTimer.start();
    }
    
    private void updateTime() {
        String currentTime = LocalTime.now()
            .format(DateTimeFormatter.ofPattern("HH:mm"));
        lblTime.setText(currentTime);
    }
    
    // ============ KEYBOARD SHORTCUT ============
    
    private void setupKeyboardShortcut() {
        // Shift + F6 to test/refresh
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    if (e.getID() == KeyEvent.KEY_PRESSED && 
                        e.getKeyCode() == KeyEvent.VK_F6 && 
                        e.isShiftDown()) {
                        
                        loadOrders();
                        JOptionPane.showMessageDialog(
                            TiviFrame.this,
                            "✅ Đã refresh dữ liệu!\n" +
                            "Cooking: " + lblCookingCount.getText() + " đơn\n" +
                            "Ready: " + lblReadyCount.getText() + " đơn",
                            "Refresh Complete",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        return true;
                    }
                    return false;
                }
            });
    }
    
    // ============ CLEANUP ============
    
    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TiviFrame frame = new TiviFrame();
            frame.setVisible(true);
            
            // Show instruction
            JOptionPane.showMessageDialog(
                frame,
                "🎯 FastFood Display Screen\n\n" +
                "• Auto refresh mỗi 5 giây\n" +
                "• Press Shift+F6 để refresh thủ công\n" +
                "• Màu cam: Đang chế biến\n" +
                "• Màu xanh: Sẵn sàng nhận\n\n" +
                "Màn hình này hiển thị công khai cho khách hàng!",
                "Hướng dẫn",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}