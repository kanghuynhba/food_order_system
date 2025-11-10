package ui.chef;

import entity.Employee;
import entity.Order;
import dao.OrderDAO;
import config.AppConfig;
import util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ChefDashboardPanel - Main dashboard with 2-column layout
 * Left: Pending orders | Right: Cooking orders
 * FIXED: Character encoding and assignChef method
 */
public class ChefDashboardPanel extends JPanel {
    
    private Employee chef;
    private PendingOrdersPanel pendingOrdersPanel;
    private CookingOrdersPanel cookingOrdersPanel;
    private OrderDAO orderDAO;

    public ChefDashboardPanel(Employee chef) {
        this.chef = chef;
        this.orderDAO = new OrderDAO();
        
        setLayout(new GridLayout(1, 2, 20, 0));
        setBackground(ColorScheme.BG_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        // Left panel - Pending orders (NEW + CONFIRMED)
        pendingOrdersPanel = new PendingOrdersPanel(this, chef);
        
        // Right panel - Cooking orders (PREPARING + COOKING)
        cookingOrdersPanel = new CookingOrdersPanel(this, chef);
        
        add(pendingOrdersPanel);
        add(cookingOrdersPanel);
    }

    // ============ ORDER ACTIONS ============
    
    /**
     * Move order from pending to cooking
     */
    public void startCooking(Order order) {
        try {
            // Update status to COOKING (3)
            boolean success = orderDAO.updateStatus(order.getOrderId(), AppConfig.ORDER_STATUS_COOKING);
            
            if (success) {
                // Assign chef - FIXED: Now method exists in OrderDAO
                orderDAO.assignChef(order.getOrderId(), chef.getEmployeeId());
                
                refreshOrders();
                
                JOptionPane.showMessageDialog(
                    this,
                    "✅ Đã bắt đầu nấu đơn #" + order.getOrderId(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Không thể cập nhật trạng thái đơn hàng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Lỗi: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Complete cooking and mark order as READY
     */
    public void completeOrder(Order order) {
        try {
            // Update status to READY (4)
            boolean success = orderDAO.updateStatus(order.getOrderId(), AppConfig.ORDER_STATUS_READY);
            
            if (success) {
                refreshOrders();
                
                JOptionPane.showMessageDialog(
                    this,
                    "✅ Đơn hàng #" + order.getOrderId() + " đã sẵn sàng!",
                    "Hoàn tất",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Không thể cập nhật trạng thái đơn hàng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Lỗi: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Refresh all orders from database
     */
    public void refreshOrders() {
        loadOrders();
    }
    
    /**
     * Auto refresh (called by timer)
     */
    public void autoRefresh() {
        loadOrders();
    }
    
    private void loadOrders() {
        try {
            // Load pending orders (NEW + CONFIRMED)
            List<Order> allPending = new ArrayList<>();
            List<Order> newOrders = orderDAO.getByStatus(AppConfig.ORDER_STATUS_NEW);
            List<Order> confirmedOrders = orderDAO.getByStatus(AppConfig.ORDER_STATUS_CONFIRMED);
            
            if (newOrders != null) allPending.addAll(newOrders);
            if (confirmedOrders != null) allPending.addAll(confirmedOrders);
            
            pendingOrdersPanel.loadOrders(allPending);
            
            // Load cooking orders (PREPARING + COOKING)
            List<Order> allCooking = new ArrayList<>();
            List<Order> preparingOrders = orderDAO.getByStatus(AppConfig.ORDER_STATUS_PREPARING);
            List<Order> cookingOrders = orderDAO.getByStatus(AppConfig.ORDER_STATUS_COOKING);
            
            if (preparingOrders != null) allCooking.addAll(preparingOrders);
            if (cookingOrders != null) allCooking.addAll(cookingOrders);
            
            cookingOrdersPanel.loadOrders(allCooking);
            
        } catch (Exception e) {
            System.err.println("Error loading orders: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Lỗi tải đơn hàng từ database!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}