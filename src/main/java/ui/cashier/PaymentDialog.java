package ui.cashier;

import dao.OrderDAO;
import dao.PaymentDAO;
import entity.Order;
import entity.Payment;
import ui.components.RoundedButton;
import config.AppConfig;
import config.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * PaymentDialog - Payment confirmation dialog
 * Path: Source Packages/ui/cashier/PaymentDialog.java
 * 
 * Features:
 * - Select payment method
 * - Enter amount received
 * - Calculate change
 * - Confirm payment
 * 
 * @author Nguyễn Trường Quốc Huân & Huỳnh Bá Khang
 */
public class PaymentDialog extends JDialog {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(76, 175, 80);
    
    private Order order;
    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;
    
    private JComboBox<String> methodCombo;
    private JTextField amountField;
    private JLabel changeLabel;
    private boolean paid = false;
    
    // ============ CONSTRUCTOR ============
    
    public PaymentDialog(Frame parent, Order order) {
        super(parent, "Thanh toán - Đơn #" + order.getOrderId(), true);
        this.order = order;
        this.orderDAO = new OrderDAO();
        this.paymentDAO = new PaymentDAO();
        
        initComponents();
        setSize(450, 400);
        setLocationRelativeTo(parent);
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        getContentPane().setBackground(Color.WHITE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("💳 Thanh toán");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        
        JLabel orderLabel = new JLabel("Đơn hàng #" + order.getOrderId());
        orderLabel.setFont(UIConstants.FONT_BODY);
        orderLabel.setForeground(Color.GRAY);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(orderLabel, BorderLayout.SOUTH);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Total amount
        JPanel totalPanel = createInfoRow("Tổng tiền:", 
            AppConfig.formatCurrency(order.getTotalAmount()), ORANGE);    
        
        // Payment method
        JPanel methodPanel = new JPanel(new BorderLayout(0, 8));
        methodPanel.setBackground(Color.WHITE);
        methodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel methodLabel = new JLabel("Phương thức thanh toán");
        methodLabel.setFont(UIConstants.FONT_BODY_BOLD);
        
        methodCombo = new JComboBox<>(AppConfig.PAYMENT_METHOD_NAMES);
        methodCombo.setFont(UIConstants.FONT_BODY);
        methodCombo.setPreferredSize(new Dimension(0, 40));
        
        methodPanel.add(methodLabel, BorderLayout.NORTH);
        methodPanel.add(methodCombo, BorderLayout.CENTER);
        
        // Amount received (for cash only)
        JPanel amountPanel = new JPanel(new BorderLayout(0, 8));
        amountPanel.setBackground(Color.WHITE);
        amountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel amountLabel = new JLabel("Tiền khách đưa");
        amountLabel.setFont(UIConstants.FONT_BODY_BOLD);
        
        amountField = new JTextField();
        amountField.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 16));
        amountField.setPreferredSize(new Dimension(0, 40));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Calculate change on input
        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateChange();
            }
        });
        
        amountPanel.add(amountLabel, BorderLayout.NORTH);
        amountPanel.add(amountField, BorderLayout.CENTER);
        
        // Change
        JPanel changePanel = createInfoRow("Tiền thối:", "0đ", GREEN);
        changeLabel = (JLabel) changePanel.getComponent(1);
        
        // Add to form
        formPanel.add(totalPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(methodPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(amountPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(changePanel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        RoundedButton cancelBtn = new RoundedButton("Hủy", 8);
        cancelBtn.setBackground(new Color(158, 158, 158));
        cancelBtn.setPreferredSize(new Dimension(100, 45));
        cancelBtn.addActionListener(e -> dispose());
        
        RoundedButton confirmBtn = new RoundedButton("✓ Xác nhận", 8);
        confirmBtn.setBackground(GREEN);
        confirmBtn.setPreferredSize(new Dimension(130, 45));
        confirmBtn.addActionListener(e -> confirmPayment());
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);
        
        // Assembly
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoRow(String label, String value, Color valueColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(UIConstants.FONT_BODY_BOLD);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        valueComp.setForeground(valueColor);
        
        panel.add(labelComp, BorderLayout.WEST);
        panel.add(valueComp, BorderLayout.EAST);
        
        return panel;
    }
    
    // ============ CALCULATE CHANGE ============
    
    private void calculateChange() {
        try {
            String text = amountField.getText().replaceAll("[^0-9]", "");
            if (text.isEmpty()) {
                changeLabel.setText("0đ");
                return;
            }
            
            double received = Double.parseDouble(text);
            double change = received - order.getTotalAmount();
            
            if (change < 0) {
                changeLabel.setText("Chưa đủ");
                changeLabel.setForeground(new Color(244, 67, 54));
            } else {
                changeLabel.setText(AppConfig.formatCurrency(change));
                changeLabel.setForeground(GREEN);
            }
        } catch (Exception e) {
            changeLabel.setText("0đ");
        }
    }
    
    // ============ CONFIRM PAYMENT ============
    
    private void confirmPayment() {
        int method = methodCombo.getSelectedIndex();
        
        // Validate cash payment
        if (method == 0) { // Cash
            try {
                String text = amountField.getText().replaceAll("[^0-9]", "");
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!");
                    return;
                }
                
                double received = Double.parseDouble(text);
                if (received < order.getTotalAmount()) {
                    JOptionPane.showMessageDialog(this, "Số tiền chưa đủ!");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!");
                return;
            }
        }
        
        // Create payment record
        Payment payment = new Payment(order.getOrderId(), order.getTotalAmount(), method);
        payment.setStatus(1); // Success
        
        if (paymentDAO.create(payment)) {
            // Update order payment status
            if (orderDAO.updatePaymentStatus(order.getOrderId(), 1)) {
                paid = true;
                
                JOptionPane.showMessageDialog(this,
                    "✅ Thanh toán thành công!\n\n" +
                    "Đơn: #" + order.getOrderId() + "\n" +
                    "Tổng: " + AppConfig.formatCurrency(order.getTotalAmount()) + "\n" +
                    "Phương thức: " + AppConfig.getPaymentMethodName(method),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Lỗi cập nhật trạng thái đơn hàng!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Lỗi tạo thanh toán!");
        }
    }
    
    // ============ GETTER ============
    
    public boolean isPaid() {
        return paid;
    }
}
