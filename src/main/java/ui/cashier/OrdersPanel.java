package ui.cashier;

import dao.OrderDAO;
import dao.OrderItemDAO;
import entity.Order;
import entity.OrderItem;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import config.AppConfig;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * OrdersPanel - Display orders in card grid view (Image 5 style)
 * Path: Source Packages/ui/cashier/OrdersPanel.java
 * 
 * Features:
 * - Grid layout of order cards
 * - Filter by status
 * - Search orders
 * - Send to chef / Mark as paid buttons
 * - Real-time refresh
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class OrdersPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    private static final Color BLUE = new Color(33, 150, 243);
    
    private CashierMainFrame mainFrame;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    
    // UI Components
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JPanel ordersContainer;
    private JScrollPane scrollPane;
    
    // Data
    private List<Order> orders;
    
    // ============ CONSTRUCTOR ============
    
    public OrdersPanel(CashierMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        
        initComponents();
        loadOrders();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel: Search + Filter
        add(createTopPanel(), BorderLayout.NORTH);
        
        // Orders container with grid layout
        ordersContainer = new JPanel();
        ordersContainer.setLayout(new GridLayout(0, 2, 15, 15)); // 2 columns
        ordersContainer.setBackground(new Color(245, 245, 245));
        
        scrollPane = new JScrollPane(ordersContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(new Color(245, 245, 245));
        
        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        searchPanel.setPreferredSize(new Dimension(300, 40));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        searchField = new JTextField("Search orders...");
        searchField.setFont(UIConstants.FONT_BODY);
        searchField.setBorder(null);
        searchField.setForeground(Color.GRAY);
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search orders...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search orders...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Filter combo
        filterCombo = new JComboBox<>(new String[]{
            "All Orders", "New Orders", "Confirmed", "Preparing", 
            "Cooking", "Ready", "Completed", "Cancelled"
        });
        filterCombo.setFont(UIConstants.FONT_BODY);
        filterCombo.setPreferredSize(new Dimension(150, 40));
        filterCombo.addActionListener(e -> filterOrders());
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(245, 245, 245));
        
        RoundedButton refreshBtn = new RoundedButton("🔄 Refresh", 8);
        refreshBtn.setBackground(BLUE);
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.addActionListener(e -> refreshOrders());
        
        RoundedButton printBtn = new RoundedButton("🖨️ Print All", 8);
        printBtn.setBackground(new Color(117, 117, 117));
        printBtn.setPreferredSize(new Dimension(120, 40));
        printBtn.addActionListener(e -> printAllOrders());
        
        buttonsPanel.add(refreshBtn);
        buttonsPanel.add(printBtn);
        
        // Left: search + filter
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.add(searchPanel);
        leftPanel.add(filterCombo);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    // ============ LOAD ORDERS ============
    
    private void loadOrders() {
        orders = orderDAO.getAll();
        displayOrders(orders);
    }
    
    private void displayOrders(List<Order> ordersToDisplay) {
        ordersContainer.removeAll();
        
        if (ordersToDisplay.isEmpty()) {
            JLabel emptyLabel = new JLabel("Không có đơn hàng nào");
            emptyLabel.setFont(UIConstants.FONT_HEADING);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            ordersContainer.add(emptyLabel);
        } else {
            for (Order order : ordersToDisplay) {
                ordersContainer.add(createOrderCard(order));
            }
        }
        
        ordersContainer.revalidate();
        ordersContainer.repaint();
    }
    
    // ============ CREATE ORDER CARD ============
    
    private RoundedPanel createOrderCard(Order order) {
        RoundedPanel card = new RoundedPanel(12, true);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(400, 220));
        
        // Top: Table + Status
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel tableLabel = new JLabel("🍽️ Table #" + (order.getOrderId() % 20));
        tableLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        
        JLabel statusLabel = new JLabel(getStatusBadge(order.getStatus()));
        statusLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 11));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(getStatusColor(order.getStatus()));
        statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        JLabel timeLabel = new JLabel(order.getFormattedTime());
        timeLabel.setFont(UIConstants.FONT_CAPTION);
        timeLabel.setForeground(Color.GRAY);
        
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.setBackground(Color.WHITE);
        topLeftPanel.add(tableLabel);
        topLeftPanel.add(Box.createVerticalStrut(3));
        topLeftPanel.add(timeLabel);
        
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.EAST);
        
        // Middle: Items list
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        
        List<OrderItem> items = orderItemDAO.getByOrderId(order.getOrderId());
        int displayCount = Math.min(items.size(), 3);
        
        for (int i = 0; i < displayCount; i++) {
            OrderItem item = items.get(i);
            JLabel itemLabel = new JLabel(item.getQuantity() + "x " + item.getProductName());
            itemLabel.setFont(UIConstants.FONT_BODY);
            itemsPanel.add(itemLabel);
            itemsPanel.add(Box.createVerticalStrut(3));
        }
        
        if (items.size() > 3) {
            JLabel moreLabel = new JLabel("+" + (items.size() - 3) + " more items...");
            moreLabel.setFont(UIConstants.FONT_CAPTION);
            moreLabel.setForeground(Color.GRAY);
            itemsPanel.add(moreLabel);
        }
        
        // Bottom: Total + Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("Total: " + AppConfig.formatCurrency(order.getTotalAmount()));
        totalLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        totalLabel.setForeground(ORANGE);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setBackground(Color.WHITE);
        
        // Buttons based on status
        if (order.getStatus() == 0 || order.getStatus() == 1) { // New or Confirmed
            RoundedButton sendBtn = new RoundedButton("✓ Send to chef", 6);
            sendBtn.setBackground(GREEN);
            sendBtn.setPreferredSize(new Dimension(120, 32));
            sendBtn.addActionListener(e -> sendToChef(order));
            buttonsPanel.add(sendBtn);
        }
        
        if (!order.isPaid()) {
            RoundedButton payBtn = new RoundedButton("💳 Mark as Paid", 6);
            payBtn.setBackground(BLUE);
            payBtn.setPreferredSize(new Dimension(130, 32));
            payBtn.addActionListener(e -> markAsPaid(order));
            buttonsPanel.add(payBtn);
        }
        
        RoundedButton printBtn = new RoundedButton("🖨️", 6);
        printBtn.setBackground(new Color(117, 117, 117));
        printBtn.setPreferredSize(new Dimension(40, 32));
        printBtn.addActionListener(e -> printOrder(order));
        buttonsPanel.add(printBtn);
        
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Assemble card
        card.add(topPanel, BorderLayout.NORTH);
        card.add(itemsPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    // ============ STATUS HELPERS ============
    
    private String getStatusBadge(int status) {
        return switch (status) {
            case 0 -> "New Order";
            case 1 -> "Confirmed";
            case 2 -> "Preparing";
            case 3 -> "Cooking";
            case 4 -> "Ready";
            case 5 -> "Completed";
            case 6 -> "Cancelled";
            default -> "Unknown";
        };
    }
    
    private Color getStatusColor(int status) {
        return switch (status) {
            case 0 -> new Color(33, 150, 243);      // Blue
            case 1 -> new Color(156, 39, 176);      // Purple
            case 2 -> new Color(255, 152, 0);       // Orange
            case 3 -> new Color(255, 87, 34);       // Deep Orange
            case 4 -> new Color(76, 175, 80);       // Green
            case 5 -> new Color(96, 125, 139);      // Blue Grey
            case 6 -> new Color(244, 67, 54);       // Red
            default -> Color.GRAY;
        };
    }
    
    // ============ ACTIONS ============
    
    private void sendToChef(Order order) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Gửi đơn #" + order.getOrderId() + " đến bếp?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Update status to Preparing (2)
            if (orderDAO.updateStatus(order.getOrderId(), 2)) {
                JOptionPane.showMessageDialog(this, "✅ Đã gửi đơn đến bếp!");
                refreshOrders();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Lỗi khi gửi đơn!");
            }
        }
    }
    
    private void markAsPaid(Order order) {
        // Open payment dialog
        PaymentDialog dialog = new PaymentDialog((Frame) SwingUtilities.getWindowAncestor(this), order);
        dialog.setVisible(true);
        
        // Refresh if paid
        if (dialog.isPaid()) {
            refreshOrders();
        }
    }
    
    private void printOrder(Order order) {
        JOptionPane.showMessageDialog(this, 
            "🖨️ In hóa đơn #" + order.getOrderId() + "\n" +
            "(Chức năng đang phát triển)",
            "In hóa đơn",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void printAllOrders() {
        JOptionPane.showMessageDialog(this, 
            "🖨️ In tất cả đơn hàng\n" +
            "(Chức năng đang phát triển)",
            "In tất cả",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void filterOrders() {
        int selectedIndex = filterCombo.getSelectedIndex();
        
        if (selectedIndex == 0) {
            displayOrders(orders);
        } else {
            int status = selectedIndex - 1;
            List<Order> filtered = orders.stream()
                .filter(o -> o.getStatus() == status)
                .toList();
            displayOrders(filtered);
        }
    }
    
    public void refreshOrders() {
        loadOrders();
    }
}