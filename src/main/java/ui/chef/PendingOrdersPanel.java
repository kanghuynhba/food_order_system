package ui.chef;

import entity.Employee;
import entity.Order;
import entity.OrderItem;
import dao.OrderItemDAO;
import ui.components.RoundedPanel;
import ui.components.RoundedButton;
import util.ColorScheme;
import util.CurrencyUtil;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * PendingOrdersPanel - Display orders waiting to be cooked
 * Status: NEW (0) + CONFIRMED (1)
 * FIXED: Character encoding
 */
public class PendingOrdersPanel extends JPanel {
    
    private JPanel ordersContainer;
    private ChefDashboardPanel parentDashboard;
    private Employee chef;
    private JLabel countLabel;
    private OrderItemDAO orderItemDAO;

    public PendingOrdersPanel(ChefDashboardPanel parentDashboard, Employee chef) {
        this.parentDashboard = parentDashboard;
        this.chef = chef;
        this.orderItemDAO = new OrderItemDAO();
        
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BG_SECONDARY);
        
        initComponents();
    }

    private void initComponents() {
        // Header with count
        RoundedPanel headerPanel = new RoundedPanel(12, false);
        headerPanel.setLayout(new BorderLayout(0, 8));
        headerPanel.setBackground(new Color(255, 243, 224)); // Light orange
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("🔥 CHƯA NẤU");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 22));
        titleLabel.setForeground(ColorScheme.PRIMARY);
        
        countLabel = new JLabel("0 đơn hàng đang chờ");
        countLabel.setFont(UIConstants.FONT_BODY);
        countLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(countLabel, BorderLayout.SOUTH);

        // Orders container with scroll
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
                // Load order items from database
                List<OrderItem> items = orderItemDAO.getByOrderId(order.getOrderId());
                order.setItems(items);
                
                RoundedPanel orderCard = createOrderCard(order);
                ordersContainer.add(orderCard);
                ordersContainer.add(Box.createVerticalStrut(15));
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
            BorderFactory.createLineBorder(ColorScheme.PRIMARY, 3),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Header: Order ID + Time
        JPanel headerSection = new JPanel(new BorderLayout(0, 6));
        headerSection.setBackground(Color.WHITE);
        
        JLabel orderIdLabel = new JLabel("#" + order.getOrderId());
        orderIdLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        orderIdLabel.setForeground(ColorScheme.PRIMARY);
        
        JLabel timeLabel = new JLabel("⏰ " + order.getFormattedTime() + " • " + order.getCustomerName());
        timeLabel.setFont(UIConstants.FONT_BODY);
        timeLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JLabel totalLabel = new JLabel("Tổng: " + CurrencyUtil.format(order.getTotalAmount()));
        totalLabel.setFont(UIConstants.FONT_BODY_BOLD);
        totalLabel.setForeground(ColorScheme.ACCENT);
        
        headerSection.add(orderIdLabel, BorderLayout.NORTH);
        headerSection.add(timeLabel, BorderLayout.CENTER);
        headerSection.add(totalLabel, BorderLayout.SOUTH);

        // Items section
        JPanel itemsSection = new JPanel();
        itemsSection.setLayout(new BoxLayout(itemsSection, BoxLayout.Y_AXIS));
        itemsSection.setBackground(new Color(250, 250, 250));
        itemsSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.DIVIDER, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (OrderItem item : order.getItems()) {
                JLabel itemLabel = new JLabel("  • " + item.getQuantity() + "× " + item.getProductName());
                itemLabel.setFont(UIConstants.FONT_BODY);
                itemLabel.setForeground(ColorScheme.TEXT_PRIMARY);
                itemsSection.add(itemLabel);
                itemsSection.add(Box.createVerticalStrut(6));
            }
        } else {
            JLabel noItemsLabel = new JLabel("Không có món");
            noItemsLabel.setFont(UIConstants.FONT_CAPTION);
            noItemsLabel.setForeground(ColorScheme.TEXT_DISABLED);
            itemsSection.add(noItemsLabel);
        }

        // Button
        RoundedButton startBtn = new RoundedButton("▶ BẮT ĐẦU NẤU", 10);
        startBtn.setBackground(ColorScheme.PRIMARY);
        startBtn.setPreferredSize(new Dimension(0, 45));
        startBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        
        startBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bắt đầu nấu đơn hàng #" + order.getOrderId() + "?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                parentDashboard.startCooking(order);
            }
        });

        card.add(headerSection, BorderLayout.NORTH);
        card.add(itemsSection, BorderLayout.CENTER);
        card.add(startBtn, BorderLayout.SOUTH);

        return card;
    }

    private void showEmptyState() {
        JLabel emptyLabel = new JLabel("<html><center>📋<br><br>Không có đơn hàng chờ<br>" +
            "<small>Các đơn mới sẽ hiển thị ở đây</small></center></html>");
        emptyLabel.setFont(UIConstants.FONT_BODY);
        emptyLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        ordersContainer.add(Box.createVerticalGlue());
        ordersContainer.add(emptyLabel);
        ordersContainer.add(Box.createVerticalGlue());
    }
    
    private void updateCount(int count) {
        countLabel.setText(count + " đơn hàng đang chờ");
    }
}