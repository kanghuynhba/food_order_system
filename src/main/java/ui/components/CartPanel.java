package ui.components;

import listener.MenuListener;
import entity.CartItem;
import ui.components.CartItemRow;
import ui.components.RoundedButton;
import util.ColorScheme;
import util.CurrencyUtil;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * CartPanel - Shopping cart sidebar
 * Based on Image 9 design
 */
public class CartPanel extends JPanel {
    
    private MenuListener listener;
    private JPanel cartItemsContainer;
    private JPanel subtotalRow;
    private JPanel taxRow;
    private JPanel discountRow;
    private JLabel totalLabel;
    private RoundedButton checkoutBtn;
    private JLabel emptyCartLabel;
    
    public CartPanel(MenuListener listener) {
        this.listener = listener;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, ColorScheme.DIVIDER));
        
        initComponents();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Đơn hàng");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JLabel customerLabel = new JLabel("Tên khách hàng");
        customerLabel.setFont(UIConstants.FONT_CAPTION);
        customerLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JPanel headerContent = new JPanel(new BorderLayout(0, 5));
        headerContent.setOpaque(false);
        headerContent.add(titleLabel, BorderLayout.NORTH);
        headerContent.add(customerLabel, BorderLayout.SOUTH);
        
        // Customer name input
        JTextField customerField = new JTextField("Nhập tên khách hàng");
        customerField.setFont(UIConstants.FONT_BODY);
        customerField.setForeground(ColorScheme.TEXT_SECONDARY);
        customerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.INPUT_BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JPanel customerPanel = new JPanel(new BorderLayout(0, 8));
        customerPanel.setOpaque(false);
        customerPanel.add(new JLabel("Tên khách hàng:"), BorderLayout.NORTH);
        customerPanel.add(customerField, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.NORTH);
        headerPanel.add(customerPanel, BorderLayout.SOUTH);
        
        // Cart items section
        JPanel cartSection = new JPanel(new BorderLayout());
        cartSection.setBackground(Color.WHITE);
        cartSection.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        // Category label
        JLabel categoryLabel = new JLabel("Loại đơn hàng: Ăn tại chỗ");
        categoryLabel.setFont(UIConstants.FONT_CAPTION);
        categoryLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        categoryLabel.setBorder(new EmptyBorder(0, 10, 10, 0));
        
        // Items container
        cartItemsContainer = new JPanel();
        cartItemsContainer.setLayout(new BoxLayout(cartItemsContainer, BoxLayout.Y_AXIS));
        cartItemsContainer.setBackground(Color.WHITE);
        
        // Empty cart message
        emptyCartLabel = new JLabel("<html><center>🛒<br><br>Giỏ hàng trống<br>" +
            "<small>Vui lòng chọn món để tiếp tục</small></center></html>");
        emptyCartLabel.setFont(UIConstants.FONT_BODY);
        emptyCartLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        emptyCartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(cartItemsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);
        
        cartSection.add(categoryLabel, BorderLayout.NORTH);
        cartSection.add(scrollPane, BorderLayout.CENTER);
        
        // Summary section
        JPanel summaryPanel = createSummaryPanel();
        
        // Buttons
        JPanel buttonsPanel = createButtonsPanel();
        
        // Combine bottom section
        JPanel bottomSection = new JPanel(new BorderLayout(0, 15));
        bottomSection.setBackground(Color.WHITE);
        bottomSection.setBorder(new EmptyBorder(10, 20, 20, 20));
        bottomSection.add(summaryPanel, BorderLayout.NORTH);
        bottomSection.add(buttonsPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(cartSection, BorderLayout.CENTER);
        add(bottomSection, BorderLayout.SOUTH);
        
        // Show empty cart message initially
        updateEmptyState();
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.BG_SECONDARY);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Subtotal
        subtotalRow = createSummaryRow("Subtotal:", "0đ");
        
        // Tax (10%)
        taxRow = createSummaryRow("Thuế (10%):", "0đ");
        
        // Discount
        discountRow = createSummaryRow("Giảm giá:", "-0đ");
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(ColorScheme.DIVIDER);
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel totalLabelText = new JLabel("Tổng cộng:");
        totalLabelText.setFont(UIConstants.FONT_HEADING);
        
        totalLabel = new JLabel("0đ");
        totalLabel.setFont(UIConstants.FONT_TITLE);
        totalLabel.setForeground(ColorScheme.PRIMARY);
        
        totalPanel.add(totalLabelText, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        
        panel.add(subtotalRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(taxRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(discountRow);
        panel.add(Box.createVerticalStrut(10));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        panel.add(totalPanel);
        
        return panel;
    }
    
    private JPanel createSummaryRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
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
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10));
        panel.setOpaque(false);
        
        // Checkout button
        checkoutBtn = new RoundedButton("🛒 Đặt món", 8);
        checkoutBtn.setBackground(ColorScheme.PRIMARY);
        checkoutBtn.setPreferredSize(new Dimension(0, 45));
        checkoutBtn.setFont(UIConstants.FONT_BUTTON);
        checkoutBtn.addActionListener(e -> listener.proceedToCheckout());
        
        // Clear cart button
        RoundedButton clearBtn = new RoundedButton("Xóa giỏ hàng", 8);
        clearBtn.setBackground(ColorScheme.DANGER);
        clearBtn.setPreferredSize(new Dimension(0, 40));
        clearBtn.addActionListener(e -> listener.clearCart());
        
        panel.add(checkoutBtn);
        panel.add(clearBtn);
        
        return panel;
    }
    
    // ============ REFRESH CART ============
    
    public void refreshCart() {
        cartItemsContainer.removeAll();
        
        List<CartItem> items = listener.getCartItems();
        
        if (items.isEmpty()) {
            updateEmptyState();
        } else {
            for (CartItem item : items) {
                CartItemRow row = new CartItemRow(
                    item.getProductName(),
                    item.getUnitPrice(),
                    item.getQuantity()
                );
                
                // Quantity spinner listener
                row.getQuantitySpinner().addChangeListener(e -> {
                    int newQty = (Integer) row.getQuantitySpinner().getValue();
                    listener.updateCartItemQuantity(item, newQty);
                });
                
                // Remove button listener
                row.getRemoveButton().addActionListener(e -> {
                    listener.removeFromCart(item);
                });
                
                cartItemsContainer.add(row);
                cartItemsContainer.add(Box.createVerticalStrut(5));
            }
        }
        
        updateSummary();
        cartItemsContainer.revalidate();
        cartItemsContainer.repaint();
    }
    
    private void updateEmptyState() {
        cartItemsContainer.removeAll();
        cartItemsContainer.add(Box.createVerticalGlue());
        cartItemsContainer.add(emptyCartLabel);
        cartItemsContainer.add(Box.createVerticalGlue());
    }
    
    private void updateSummary() {
        double subtotal = listener.getCartTotal();
        double tax = listener.getCartTax();
        double discount = listener.getCartDiscount();
        double total = listener.getCartGrandTotal();
        
        // Update labels (get JLabel from JPanel)
        JLabel subtotalValueLabel = (JLabel) subtotalRow.getComponent(1);
        subtotalValueLabel.setText(CurrencyUtil.format(subtotal));
        
        JLabel taxValueLabel = (JLabel) taxRow.getComponent(1);
        taxValueLabel.setText(CurrencyUtil.format(tax));
        
        JLabel discountValueLabel = (JLabel) discountRow.getComponent(1);
        discountValueLabel.setText("-" + CurrencyUtil.format(discount));
        discountValueLabel.setForeground(ColorScheme.SUCCESS);
        
        totalLabel.setText(CurrencyUtil.format(total));
        
        // Enable/disable checkout button
        checkoutBtn.setEnabled(!listener.getCartItems().isEmpty());
    }
}
