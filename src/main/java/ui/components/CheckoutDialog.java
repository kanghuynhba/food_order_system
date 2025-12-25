package ui.components;

import entity.CartItem;
import entity.OrderItem;
import entity.Order;

import util.ColorScheme;
import util.CurrencyUtil;

import config.UIConstants;

import service.OrderService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * CheckoutDialog - Payment confirmation modal
 * Based on standard checkout flow
 */
public class CheckoutDialog extends JDialog {
    
    private List<CartItem> cartItems;
    private double totalAmount;
    private boolean checkoutSuccessful = false;

    private OrderService orderService;
    
    private JComboBox<String> paymentMethodCombo;
    private JTextField customerNameField;
    private JTextField phoneField;
    private JTextArea notesArea;
    
    public CheckoutDialog(Frame parent, List<CartItem> cartItems, double totalAmount) {
        super(parent, "Thanh toán", true);
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
        this.orderService=OrderService.getInstance();
        
        setSize(UIConstants.SIZE_DIALOG_MEDIUM);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BG_SECONDARY);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Header
        JPanel headerPanel = createHeader();
        
        // Content
        JPanel contentPanel = createContentPanel();
        
        // Footer with buttons
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🛒 Xác nhận đặt hàng");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Vui lòng kiểm tra thông tin trước khi thanh toán");
        subtitleLabel.setFont(UIConstants.FONT_CAPTION);
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        
        // Customer info section
        RoundedPanel customerPanel = createCustomerInfoPanel();
        
        // Order summary section
        RoundedPanel orderPanel = createOrderSummaryPanel();
        
        // Payment method section
        RoundedPanel paymentPanel = createPaymentPanel();
        
        JPanel sectionsPanel = new JPanel();
        sectionsPanel.setLayout(new BoxLayout(sectionsPanel, BoxLayout.Y_AXIS));
        sectionsPanel.setOpaque(false);
        sectionsPanel.add(customerPanel);
        sectionsPanel.add(Box.createVerticalStrut(15));
        sectionsPanel.add(orderPanel);
        sectionsPanel.add(Box.createVerticalStrut(15));
        sectionsPanel.add(paymentPanel);
        
        JScrollPane scrollPane = new JScrollPane(sectionsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private RoundedPanel createCustomerInfoPanel() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new GridLayout(3, 1, 0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Thông tin khách hàng");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        
        // Name field
        customerNameField = new JTextField();
        customerNameField.setFont(UIConstants.FONT_BODY);
        customerNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INPUT_BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        JPanel namePanel = new JPanel(new BorderLayout(0, 5));
        namePanel.setOpaque(false);
        namePanel.add(new JLabel("Tên khách hàng:"), BorderLayout.NORTH);
        namePanel.add(customerNameField, BorderLayout.CENTER);
        
        // Phone field
        phoneField = new JTextField();
        phoneField.setFont(UIConstants.FONT_BODY);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INPUT_BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        JPanel phonePanel = new JPanel(new BorderLayout(0, 5));
        phonePanel.setOpaque(false);
        phonePanel.add(new JLabel("Số điện thoại:"), BorderLayout.NORTH);
        phonePanel.add(phoneField, BorderLayout.CENTER);
        
        panel.add(titleLabel);
        panel.add(namePanel);
        panel.add(phonePanel);
        
        return panel;
    }
    
    private RoundedPanel createOrderSummaryPanel() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Tóm tắt đơn hàng");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        
        // Items list
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);
        
        for (CartItem item : cartItems) {
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setOpaque(false);
            itemRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            
            JLabel nameLabel = new JLabel(item.getQuantity() + "x " + item.getProductName());
            nameLabel.setFont(UIConstants.FONT_BODY);
            
            JLabel priceLabel = new JLabel(CurrencyUtil.format(item.getSubtotal()));
            priceLabel.setFont(UIConstants.FONT_BODY_BOLD);
            priceLabel.setForeground(ColorScheme.TEXT_SECONDARY);
            
            itemRow.add(nameLabel, BorderLayout.WEST);
            itemRow.add(priceLabel, BorderLayout.EAST);
            
            itemsPanel.add(itemRow);
            itemsPanel.add(Box.createVerticalStrut(8));
        }
        
        // Total
        JSeparator separator = new JSeparator();
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel totalLabel = new JLabel("Tổng cộng:");
        totalLabel.setFont(UIConstants.FONT_HEADING);
        
        JLabel amountLabel = new JLabel(CurrencyUtil.format(totalAmount));
        amountLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 20));
        amountLabel.setForeground(ColorScheme.PRIMARY);
        
        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(amountLabel, BorderLayout.EAST);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(itemsPanel);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(totalPanel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private RoundedPanel createPaymentPanel() {
        RoundedPanel panel = new RoundedPanel(12, true);
        panel.setLayout(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Phương thức thanh toán");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        
        // Payment method combo
        String[] methods = {"Tiền mặt", "Thẻ tín dụng", "MoMo", "Chuyển khoản", "VNPay"};
        paymentMethodCombo = new JComboBox<>(methods);
        paymentMethodCombo.setFont(UIConstants.FONT_BODY);
        paymentMethodCombo.setPreferredSize(new Dimension(0, 40));
        
        // Notes
        JLabel notesLabel = new JLabel("Ghi chú (tùy chọn):");
        notesLabel.setFont(UIConstants.FONT_BODY);
        
        notesArea = new JTextArea(3, 20);
        notesArea.setFont(UIConstants.FONT_BODY);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INPUT_BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setBorder(null);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(paymentMethodCombo);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(notesLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(notesScroll);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        
        // Cancel button
        RoundedButton cancelBtn = new RoundedButton("Hủy", 8);
        cancelBtn.setBackground(ColorScheme.GRAY);
        cancelBtn.setPreferredSize(new Dimension(120, 45));
        cancelBtn.addActionListener(e -> dispose());
        
        // Confirm button
        RoundedButton confirmBtn = new RoundedButton("✓ Xác nhận thanh toán", 8);
        confirmBtn.setBackground(ColorScheme.SUCCESS);
        confirmBtn.setPreferredSize(new Dimension(200, 45));
        confirmBtn.addActionListener(e -> processCheckout());
        
        panel.add(cancelBtn);
        panel.add(confirmBtn);
        
        return panel;
    }
    
    // ============ CHECKOUT PROCESS ============
    
    private void processCheckout() {
        // Validate
        String name = customerNameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Vui lòng nhập tên khách hàng!",
                "Thông tin thiếu",
                JOptionPane.WARNING_MESSAGE
            );
            customerNameField.requestFocus();
            return;
        }
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Vui lòng nhập số điện thoại!",
                "Thông tin thiếu",
                JOptionPane.WARNING_MESSAGE
            );
            phoneField.requestFocus();
            return;
        }
        
        // Process order (here you would save to database)
        int paymentMethod = paymentMethodCombo.getSelectedIndex();
        String notes = notesArea.getText().trim();

        List<OrderItem> items=new ArrayList<>();

        for(CartItem item : cartItems) {
            items.add(item.toOrderItem());
        }

        Order newOrder=orderService.createOrder(name, phone, items, paymentMethod);
        
        // Simulate processing
        JOptionPane.showMessageDialog(
            this,
            String.format(
                "✅ Đặt hàng thành công!\n\n" +
                "Khách hàng: %s\n" +
                "SĐT: %s\n" +
                "Tổng tiền: %s\n" +
                "Phương thức: %s\n\n" +
                "Đơn hàng đang được xử lý...",
                name,
                phone,
                CurrencyUtil.format(totalAmount),
                paymentMethodCombo.getSelectedItem()
            ),
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        checkoutSuccessful = true;
        dispose();
    }
    
    public boolean isCheckoutSuccessful() {
        return checkoutSuccessful;
    }
}
