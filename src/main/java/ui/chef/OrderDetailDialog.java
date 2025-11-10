package ui.chef;

import entity.Order;
import entity.OrderItem;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.StatusBadge;
import util.ColorScheme;
import util.CurrencyUtil;
import config.UIConstants;
import config.AppConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * OrderDetailDialog - Popup chi tiết đơn hàng
 * Hiển thị đầy đủ thông tin order khi chef click vào
 * 
 * FIXED VERSION with complete details and better UI
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class OrderDetailDialog extends JDialog {
    
    private Order order;
    private boolean actionTaken = false;

    public OrderDetailDialog(JFrame parent, Order order) {
        super(parent, "Chi tiết đơn hàng #" + order.getOrderId(), true);
        this.order = order;
        
        setSize(550, 750);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(ColorScheme.BG_SECONDARY);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header section
        JPanel headerPanel = createHeaderPanel();
        
        // Content section (scrollable)
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(ColorScheme.BG_SECONDARY);
        
        // Footer with action buttons
        JPanel footerPanel = createFooterPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        RoundedPanel headerPanel = new RoundedPanel(12, false);
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.setBackground(ColorScheme.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Left: Order icon and ID
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel orderIdLabel = new JLabel("Đơn hàng #" + order.getOrderId());
        orderIdLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 22));
        orderIdLabel.setForeground(Color.WHITE);
        
        JLabel timeLabel = new JLabel("🕐 " + order.getFormattedDateTime());
        timeLabel.setFont(UIConstants.FONT_BODY);
        timeLabel.setForeground(new Color(255, 255, 255, 200));
        
        textPanel.add(orderIdLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(timeLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(textPanel);

        // Right: Status badge
        StatusBadge statusBadge = new StatusBadge(
            AppConfig.getOrderStatusName(order.getStatus()),
            getStatusType(order.getStatus())
        );
        statusBadge.setFont(UIConstants.FONT_BODY_BOLD);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(statusBadge, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.BG_SECONDARY);
        contentPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Customer info section
        contentPanel.add(createCustomerInfoSection());
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Order items section
        contentPanel.add(createOrderItemsSection());
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Payment info section
        contentPanel.add(createPaymentInfoSection());
        
        // Order status timeline (if needed)
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createOrderStatusSection());

        return contentPanel;
    }

    private RoundedPanel createCustomerInfoSection() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel titleLabel = new JLabel("👤 Thông tin khách hàng");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoGrid = new JPanel(new GridLayout(2, 1, 0, 10));
        infoGrid.setOpaque(false);
        infoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Customer name
        JPanel nameRow = createInfoRow("Tên khách hàng:", 
            order.getCustomerName() != null ? order.getCustomerName() : "Khách lẻ");
        
        // Phone number
        JPanel phoneRow = createInfoRow("Số điện thoại:", 
            order.getPhoneNumber() != null ? order.getPhoneNumber() : "Chưa có");

        infoGrid.add(nameRow);
        infoGrid.add(phoneRow);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(infoGrid);

        return panel;
    }

    private RoundedPanel createOrderItemsSection() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));

        JLabel titleLabel = new JLabel("🍽️ Danh sách món ăn");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Items list
        JPanel itemsContainer = new JPanel();
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        itemsContainer.setOpaque(false);
        itemsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<OrderItem> items = order.getItems();
        if (items != null && !items.isEmpty()) {
            for (OrderItem item : items) {
                JPanel itemRow = createOrderItemRow(item);
                itemsContainer.add(itemRow);
                itemsContainer.add(Box.createVerticalStrut(8));
            }
        } else {
            JLabel noItemsLabel = new JLabel("Không có món nào");
            noItemsLabel.setFont(UIConstants.FONT_CAPTION);
            noItemsLabel.setForeground(ColorScheme.TEXT_DISABLED);
            itemsContainer.add(noItemsLabel);
        }

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(itemsContainer);

        return panel;
    }

    private JPanel createOrderItemRow(OrderItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(new EmptyBorder(8, 10, 8, 10));
        row.setBackground(new Color(250, 250, 250));

        // Left: Quantity badge + Name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel qtyLabel = new JLabel(item.getQuantity() + "×");
        qtyLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 13));
        qtyLabel.setForeground(Color.WHITE);
        qtyLabel.setOpaque(true);
        qtyLabel.setBackground(ColorScheme.PRIMARY);
        qtyLabel.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel nameLabel = new JLabel(item.getProductName());
        nameLabel.setFont(UIConstants.FONT_BODY);
        nameLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        leftPanel.add(qtyLabel);
        leftPanel.add(nameLabel);

        // Right: Unit price + Subtotal
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JLabel priceLabel = new JLabel(CurrencyUtil.format(item.getUnitPrice()) + " × " + item.getQuantity());
        priceLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        priceLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtotalLabel = new JLabel(CurrencyUtil.format(item.getSubtotal()));
        subtotalLabel.setFont(UIConstants.FONT_BODY_BOLD);
        subtotalLabel.setForeground(ColorScheme.ACCENT);
        subtotalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(priceLabel);
        rightPanel.add(subtotalLabel);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private RoundedPanel createPaymentInfoSection() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel titleLabel = new JLabel("💰 Thông tin thanh toán");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Payment details
        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 0, 8));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        double subtotal = order.getTotalAmount();
        double tax = subtotal * 0.1;
        double total = subtotal + tax;

        detailsPanel.add(createInfoRow("Tạm tính:", CurrencyUtil.format(subtotal)));
        detailsPanel.add(createInfoRow("Thuế VAT (10%):", CurrencyUtil.format(tax)));
        
        JSeparator separator = new JSeparator();
        detailsPanel.add(separator);

        JPanel totalRow = createInfoRow("TỔNG CỘNG:", CurrencyUtil.format(total));
        totalRow.setBackground(new Color(255, 243, 224));
        totalRow.setOpaque(true);
        ((JLabel) totalRow.getComponent(1)).setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        ((JLabel) totalRow.getComponent(1)).setForeground(ColorScheme.PRIMARY);
        detailsPanel.add(totalRow);

        // Payment method and status
        JPanel methodStatusPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        methodStatusPanel.setOpaque(false);
        methodStatusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JPanel methodPanel = createPaymentBadge("Phương thức:", 
            AppConfig.getPaymentMethodName(order.getPayMethod()), 
            ColorScheme.INFO);
        
        JPanel statusPanel = createPaymentBadge("Trạng thái:", 
            AppConfig.getPaymentStatusName(order.getPaymentStatus()),
            order.isPaid() ? ColorScheme.SUCCESS : ColorScheme.WARNING);
        
        methodStatusPanel.add(methodPanel);
        methodStatusPanel.add(statusPanel);
        
        detailsPanel.add(methodStatusPanel);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(detailsPanel);

        return panel;
    }

    private RoundedPanel createOrderStatusSection() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel titleLabel = new JLabel("📊 Trạng thái đơn hàng");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statusInfo = new JPanel(new GridLayout(2, 1, 0, 8));
        statusInfo.setOpaque(false);
        statusInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusInfo.add(createInfoRow("Trạng thái hiện tại:", 
            AppConfig.getOrderStatusName(order.getStatus())));
        statusInfo.add(createInfoRow("Thời gian tạo:", 
            order.getFormattedDateTime()));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(statusInfo);

        return panel;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(UIConstants.FONT_BODY);
        labelComp.setForeground(ColorScheme.TEXT_SECONDARY);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(UIConstants.FONT_BODY_BOLD);
        valueComp.setForeground(ColorScheme.TEXT_PRIMARY);

        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.EAST);

        return row;
    }

    private JPanel createPaymentBadge(String label, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        labelComp.setForeground(ColorScheme.TEXT_SECONDARY);
        labelComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        valueComp.setForeground(color);
        valueComp.setOpaque(true);
        valueComp.setBackground(ColorScheme.lighter(color, 0.9f));
        valueComp.setBorder(new EmptyBorder(5, 10, 5, 10));
        valueComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(labelComp);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueComp);
        
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setOpaque(false);

        // Close button
        RoundedButton closeBtn = new RoundedButton("Đóng", 8);
        closeBtn.setBackground(ColorScheme.GRAY);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.addActionListener(e -> dispose());

        footerPanel.add(closeBtn);

        return footerPanel;
    }

    private StatusBadge.StatusType getStatusType(int status) {
        return switch (status) {
            case 0, 1 -> StatusBadge.StatusType.WARNING;  // NEW, CONFIRMED
            case 2, 3 -> StatusBadge.StatusType.INFO;     // PREPARING, COOKING
            case 4 -> StatusBadge.StatusType.SUCCESS;     // READY
            case 5 -> StatusBadge.StatusType.DEFAULT;     // COMPLETED
            case 6 -> StatusBadge.StatusType.ERROR;       // CANCELLED
            default -> StatusBadge.StatusType.DEFAULT;
        };
    }

    public boolean isActionTaken() {
        return actionTaken;
    }
}