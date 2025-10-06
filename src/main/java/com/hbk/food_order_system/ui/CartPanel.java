package com.hbk.food_order_system.ui;

import com.hbk.food_order_system.entity.CartItem;
import com.hbk.food_order_system.service.CartService;
// import com.hbk.food_order_system.service.OrderService;
import com.hbk.food_order_system.ui.components.CartItemPanel;
import com.hbk.food_order_system.util.Constants;
import com.hbk.food_order_system.util.UIFactory;
import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel implements CartService.CartUpdateListener {
    private final CartService cartService;
    // private final OrderService orderService;
    
    private JPanel cartItemsPanel;
    private JTextField customerNameField;
    private JComboBox<String> orderTypeCombo;
    private JLabel subtotalValue, taxValue, discountValue, totalValue;
    
    public CartPanel(CartService cartService) {
        this.cartService = cartService;
        // this.orderService = new OrderService();
        this.cartService.addListener(this);
        initComponents();
    }
    
    private void initComponents() {
        setPreferredSize(new Dimension(Constants.CART_WIDTH, 0));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Constants.BORDER_COLOR));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createFormPanel(), BorderLayout.NORTH);
        centerPanel.add(createCartScrollPane(), BorderLayout.CENTER);
        
        add(createHeader(), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        header.setBorder(UIFactory.createPaddingBorder(24, 24, 0, 24));
        
        JLabel title = UIFactory.createLabel("Đơn hàng", Constants.FONT_SIZE_XLARGE, Font.BOLD);
        header.add(title);
        
        return header;
    }
    
    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(UIFactory.createPaddingBorder(20, 24, 20, 24));
        
        customerNameField = UIFactory.createTextField("Nhập tên khách hàng...");
        customerNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        
        orderTypeCombo = new JComboBox<>(new String[]{"Ăn tại chỗ", "Mang đi"});
        orderTypeCombo.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_MEDIUM));
        orderTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        form.add(createFormLabel("Tên khách hàng"));
        form.add(customerNameField);
        form.add(Box.createVerticalStrut(16));
        form.add(createFormLabel("Loại đơn hàng"));
        form.add(orderTypeCombo);
        
        return form;
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Constants.FONT_INTER, Font.PLAIN, Constants.FONT_SIZE_SMALL));
        label.setForeground(new Color(55, 65, 81));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JScrollPane createCartScrollPane() {
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(cartItemsPanel);
        scroll.setBorder(UIFactory.createPaddingBorder(20, 24, 20, 24));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        return scroll;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(UIFactory.createPaddingBorder(0, 24, 24, 24));
        
        panel.add(createSummaryPanel(), BorderLayout.CENTER);
        panel.add(createCheckoutButton(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.BORDER_COLOR),
            UIFactory.createPaddingBorder(16, 0, 0, 0)
        ));
        
        JPanel subtotalPanel = createSummaryRow("Subtotal:", "0 ₫", false);
        // JPanel taxPanel = createSummaryRow("Thuế (10%):", "0 ₫", false);
        // JPanel discountPanel = createSummaryRow("Giảm giá:", "-20.000 ₫", false);
        JPanel totalPanel = createSummaryRow("Tổng cộng:", "0 ₫", true);
        
        subtotalValue = (JLabel) subtotalPanel.getComponent(1);
        // taxValue = (JLabel) taxPanel.getComponent(1);
        // discountValue = (JLabel) discountPanel.getComponent(1);
        totalValue = (JLabel) totalPanel.getComponent(1);
        
        // discountValue.setForeground(new Color(22, 163, 74));
        totalValue.setForeground(Constants.PRIMARY_COLOR);
        
        panel.add(subtotalPanel);
        // panel.add(Box.createVerticalStrut(8));
        // panel.add(taxPanel);
        // panel.add(Box.createVerticalStrut(8));
        // panel.add(discountPanel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(totalPanel);
        
        return panel;
    }
    
    private JPanel createSummaryRow(String label, String value, boolean bold) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        
        JLabel left = new JLabel(label);
        left.setFont(new Font(Constants.FONT_INTER, bold ? Font.BOLD : Font.PLAIN, 
                     bold ? Constants.FONT_SIZE_LARGE : Constants.FONT_SIZE_MEDIUM));
        left.setForeground(bold ? Color.BLACK : Constants.TEXT_SECONDARY);
        
        JLabel right = new JLabel(value);
        right.setFont(new Font(Constants.FONT_INTER, bold ? Font.BOLD : Font.PLAIN, 
                      bold ? Constants.FONT_SIZE_LARGE : Constants.FONT_SIZE_MEDIUM));
        right.setForeground(Color.BLACK);
        
        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createCheckoutButton() {
        JButton btn = new JButton("Đặt món");
        btn.setFont(new Font(Constants.FONT_INTER, Font.BOLD, Constants.FONT_SIZE_LARGE));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Constants.PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleCheckout());
        
        return btn;
    }
    
    private void handleCheckout() {
        // String customerName = customerNameField.getText();
        // String orderType = (String) orderTypeCombo.getSelectedItem();
        
        // var order = orderService.createOrder(customerName, orderType, cartService);
        
        // if (orderService.validateOrder(order)) {
        //     orderService.processOrder(order);
        //     JOptionPane.showMessageDialog(this, 
        //         "Đơn hàng đã được đặt thành công!", 
        //         "Thành công", 
        //         JOptionPane.INFORMATION_MESSAGE);
        //     cartService.clear();
        //     customerNameField.setText("Nhập tên khách hàng...");
        // } else {
        //     JOptionPane.showMessageDialog(this, 
        //         "Vui lòng nhập tên khách hàng và chọn món!", 
        //         "Lỗi", 
        //         JOptionPane.ERROR_MESSAGE);
        // }
    }
    
    @Override
    public void onCartUpdated() {
        updateCartDisplay();
        updateSummary();
    }
    
    private void updateCartDisplay() {
        cartItemsPanel.removeAll();
        
        JLabel title = new JLabel("Món đã chọn");
        title.setFont(new Font(Constants.FONT_INTER, Font.BOLD, Constants.FONT_SIZE_MEDIUM));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartItemsPanel.add(title);
        cartItemsPanel.add(Box.createVerticalStrut(16));
        
        for (CartItem item : cartService.getItems()) {
            CartItemPanel itemPanel = new CartItemPanel(
                item,
                this::onCartUpdated,
                () -> {
                    cartService.removeItem(item.getProduct().getName());
                }
            );
            cartItemsPanel.add(itemPanel);
            cartItemsPanel.add(Box.createVerticalStrut(8));
        }
        
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    private void updateSummary() {
        int subtotal = cartService.getSubtotal();
        int total = cartService.getTotal();
        
        subtotalValue.setText(Constants.CURRENCY_FORMAT.format(subtotal) + " ₫");
        totalValue.setText(Constants.CURRENCY_FORMAT.format(total) + " ₫");
    }
}
