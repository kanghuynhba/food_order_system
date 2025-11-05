package ui.components;

import entity.Order;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * OrderCard - Order display card for cashier/chef view
 * 
 * Features:
 * - Order number (large)
 * - Order time
 * - Items list
 * - Status badge
 * - Action buttons
 */
public class OrderCard extends RoundedPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    
    private Order order;
    private JLabel orderNumLabel;
    private JLabel timeLabel;
    private JTextArea itemsArea;
    private StatusBadge statusBadge;
    private NumberFormat currencyFormat;
    
    // ============ CONSTRUCTOR ============
    
    public OrderCard(Order order) {
        super(12, true);
        this.order = order;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        initComponents();
        loadOrderData();
    }
    
    // ============ INIT COMPONENTS ============
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(280, 200));
        
        // Left: Order number
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        orderNumLabel = new JLabel();
        orderNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        orderNumLabel.setForeground(ORANGE);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(Color.GRAY);
        
        leftPanel.add(orderNumLabel, BorderLayout.CENTER);
        leftPanel.add(timeLabel, BorderLayout.SOUTH);
        
        // Center: Items
        itemsArea = new JTextArea();
        itemsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemsArea.setEditable(false);
        itemsArea.setOpaque(false);
        itemsArea.setLineWrap(true);
        itemsArea.setWrapStyleWord(true);
        
        // Right: Status + Actions
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        statusBadge = new StatusBadge("", StatusBadge.StatusType.WARNING);
        rightPanel.add(statusBadge);
        
        add(leftPanel, BorderLayout.WEST);
        add(itemsArea, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    // ============ LOAD DATA ============
    
    private void loadOrderData() {
        if (order == null) return;
        
        // Order number
        orderNumLabel.setText("#" + order.getOrderId());
        
        // Time
        timeLabel.setText(order.getFormattedTime());
        
        // Items (placeholder - needs OrderItems)
        itemsArea.setText("2x Big Mac\n1x Coca Cola\n1x French Fries");
        
        // Status
        statusBadge.setText(order.getStatusName());
        statusBadge.setType(getStatusType(order.getStatus()));
    }
    
    private StatusBadge.StatusType getStatusType(int status) {
        return switch (status) {
            case 1, 2 -> StatusBadge.StatusType.WARNING;
            case 3 -> StatusBadge.StatusType.INFO;
            case 4 -> StatusBadge.StatusType.SUCCESS;
            case 6 -> StatusBadge.StatusType.ERROR;
            default -> StatusBadge.StatusType.DEFAULT;
        };
    }
    
    // ============ GETTERS ============
    
    public Order getOrder() {
        return order;
    }
    
    // ============ TEST MAIN ============
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OrderCard Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 400);
            frame.setLocationRelativeTo(null);
            
            JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
            container.setBackground(new Color(245, 245, 245));
            
            // Test orders
            Order o1 = new Order("Nguyen Van A", "0905999999", 150000, 0);
            o1.setOrderId(1023);
            o1.setStatus(3); // Cooking
            
            Order o2 = new Order("Tran Thi B", "0905888888", 200000, 1);
            o2.setOrderId(1024);
            o2.setStatus(4); // Ready
            
            container.add(new OrderCard(o1));
            container.add(new OrderCard(o2));
            
            frame.add(container);
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(frame,
                "✅ OrderCard Test\n\n" +
                "2 order cards with different statuses",
                "Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
