package ui.chef;

import entity.Employee;
import entity.Order;
import entity.OrderItem;
import dao.OrderItemDAO;
import ui.components.RoundedPanel;
import ui.components.RoundedButton;
import ui.components.StatusBadge;
import util.ColorScheme;
import util.CurrencyUtil;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * CookingOrdersPanel - Display orders being cooked
 * Status: PREPARING (2) + COOKING (3)
 * 
 * FIXED VERSION with better UI and error handling
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class CookingOrdersPanel extends JPanel {
    
    private JPanel ordersContainer;
    private ChefDashboardPanel parentDashboard;
    private Employee chef;
    private JLabel countLabel;
    private OrderItemDAO orderItemDAO;

    public CookingOrdersPanel(ChefDashboardPanel parentDashboard, Employee chef) {
        this.parentDashboard = parentDashboard;
        this.chef = chef;
        this.orderItemDAO = new OrderItemDAO();
        
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BG_SECONDARY);
        
        initComponents();
    }

    private void initComponents() {
        // Header
        RoundedPanel headerPanel = new RoundedPanel(12, false);
        headerPanel.setLayout(new BorderLayout(0, 8));
        headerPanel.setBackground(new Color(224, 247, 250)); // Light blue
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("🔵 ĐANG NẤU");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 22));
        titleLabel.setForeground(ColorScheme.INFO);
        
        countLabel = new JLabel("0 đơn hàng đang nấu");
        countLabel.setFont(UIConstants.FONT_BODY);
        countLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(countLabel, BorderLayout.SOUTH);

        // Orders container
        ordersContainer = new JPanel();
        ordersContainer.setLayout(new BoxLayout(ordersContainer, BoxLayout.Y_AXIS));
        ordersContainer.setBackground(ColorScheme.BG_SECONDARY);
        ordersContainer.setBorder(new EmptyBorder(20, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(ordersContainer);
        scrollPane.setBackground(ColorScheme.BG_SECONDARY);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(ColorScheme.BG_SECONDARY);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadOrders(List<Order> orders) {
        ordersContainer.removeAll();
        
        if (orders == null || orders.isEmpty()) {
            showEmptyState();
        } else {
            for (Order order : orders) {
                try {
                    // Load items from database
                    List<OrderItem> items = orderItemDAO.getByOrderId(order.getOrderId());
                    order.setItems(items);
                    
                    RoundedPanel orderCard = createOrderCard(order);
                    ordersContainer.add(orderCard);
                    ordersContainer.add(Box.createVerticalStrut(15));
                } catch (Exception e) {
                    System.err.println("Error loading order #" + order.getOrderId() + ": " + e.getMessage());
                }
            }
        }
        
        updateCount(orders != null ? orders.size() : 0);
        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    private RoundedPanel createOrderCard(Order order) {
        RoundedPanel card = new RoundedPanel(15, true);
        card.setLayout(new BorderLayout(12, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INFO, 3),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        // Header with status badge
        JPanel headerSection = new JPanel(new BorderLayout(0, 6));
        headerSection.setBackground(Color.WHITE);
        
        // Top row: Order ID + Status badge + View Details button
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        
        JLabel orderIdLabel = new JLabel("#" + order.getOrderId());
        orderIdLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        orderIdLabel.setForeground(ColorScheme.INFO);
        
        // Right side: Status badge + View Details button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        StatusBadge statusBadge = new StatusBadge("Đang nấu", StatusBadge.StatusType.INFO);
        
        // View Details button
        RoundedButton viewDetailsBtn = new RoundedButton("👁 Chi tiết", 8);
        viewDetailsBtn.setBackground(ColorScheme.GRAY_LIGHT);
        viewDetailsBtn.setForeground(ColorScheme.TEXT_PRIMARY);
        viewDetailsBtn.setPreferredSize(new Dimension(100, 30));
        viewDetailsBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        viewDetailsBtn.addActionListener(e -> showOrderDetails(order));
        
        rightPanel.add(statusBadge);
        rightPanel.add(viewDetailsBtn);
        
        topRow.add(orderIdLabel, BorderLayout.WEST);
        topRow.add(rightPanel, BorderLayout.EAST);
        
        // Customer info
        JLabel timeLabel = new JLabel("⏰ " + order.getFormattedTime() + " • " + 
                                     (order.getCustomerName() != null ? order.getCustomerName() : "Khách hàng"));
        timeLabel.setFont(UIConstants.FONT_BODY);
        timeLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JLabel totalLabel = new JLabel("Tổng: " + CurrencyUtil.format(order.getTotalAmount()));
        totalLabel.setFont(UIConstants.FONT_BODY_BOLD);
        totalLabel.setForeground(ColorScheme.ACCENT);
        
        headerSection.add(topRow, BorderLayout.NORTH);
        headerSection.add(timeLabel, BorderLayout.CENTER);
        headerSection.add(totalLabel, BorderLayout.SOUTH);

        // Items section
        JPanel itemsSection = new JPanel();
        itemsSection.setLayout(new BoxLayout(itemsSection, BoxLayout.Y_AXIS));
        itemsSection.setBackground(new Color(240, 248, 255)); // Alice blue
        itemsSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INFO, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            int itemCount = 0;
            for (OrderItem item : order.getItems()) {
                if (itemCount >= 5) { // Show max 5 items
                    JLabel moreLabel = new JLabel("  ... và " + (order.getItems().size() - 5) + " món khác");
                    moreLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.ITALIC, 12));
                    moreLabel.setForeground(ColorScheme.TEXT_SECONDARY);
                    itemsSection.add(moreLabel);
                    break;
                }
                
                JLabel itemLabel = new JLabel("  ✓ " + item.getQuantity() + "× " + item.getProductName());
                itemLabel.setFont(UIConstants.FONT_BODY);
                itemLabel.setForeground(ColorScheme.TEXT_PRIMARY);
                itemsSection.add(itemLabel);
                itemsSection.add(Box.createVerticalStrut(6));
                itemCount++;
            }
        } else {
            JLabel noItemsLabel = new JLabel("Không có món");
            noItemsLabel.setFont(UIConstants.FONT_CAPTION);
            noItemsLabel.setForeground(ColorScheme.TEXT_DISABLED);
            itemsSection.add(noItemsLabel);
        }

        // Progress indicator
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(0, 8));
        progressBar.setBackground(ColorScheme.BG_SECONDARY);
        progressBar.setForeground(ColorScheme.INFO);

        // Complete button
        RoundedButton completeBtn = new RoundedButton("✓ HOÀN TẤT", 10);
        completeBtn.setBackground(ColorScheme.SUCCESS);
        completeBtn.setPreferredSize(new Dimension(0, 45));
        completeBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        
        completeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Đơn hàng #" + order.getOrderId() + " đã nấu xong?",
                "Hoàn tất đơn hàng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                parentDashboard.completeOrder(order);
            }
        });

        // Bottom panel with progress and button
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(completeBtn, BorderLayout.SOUTH);

        card.add(headerSection, BorderLayout.NORTH);
        card.add(itemsSection, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showEmptyState() {
        JLabel emptyLabel = new JLabel("<html><center>🍳<br><br>Không có đơn đang nấu<br>" +
            "<small>Bắt đầu nấu từ đơn chờ</small></center></html>");
        emptyLabel.setFont(UIConstants.FONT_BODY);
        emptyLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        ordersContainer.add(Box.createVerticalGlue());
        ordersContainer.add(emptyLabel);
        ordersContainer.add(Box.createVerticalGlue());
    }
    
    private void updateCount(int count) {
        countLabel.setText(count + " đơn hàng đang nấu");
    }
    
    /**
     * Show order details dialog
     */
    private void showOrderDetails(Order order) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        OrderDetailDialog dialog = new OrderDetailDialog(parentFrame, order);
        dialog.setVisible(true);
    }
}