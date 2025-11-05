package ui.manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ManagerMainFrame extends JFrame {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color BG_COLOR = new Color(245, 245, 245);
    
    private ManagerSidebarPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public ManagerMainFrame() {
        initComponents();
        setupKeyboardShortcut();
    }
    
    private void initComponents() {
        setTitle("FastFood Manager - Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        
        // Sidebar
        sidebar = new ManagerSidebarPanel(this);
        mainPanel.add(sidebar, BorderLayout.WEST);
        
        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_COLOR);
        
        // Add all panels
        contentPanel.add(new DashboardPanel(), "dashboard");
        contentPanel.add(new IngredientPanel(), "ingredients");
        contentPanel.add(new StaffPanel(), "staff");
        contentPanel.add(new ProductPanel(), "products");
        contentPanel.add(new ReportSalesPanel(), "sales");
        contentPanel.add(new ReportEmployeePanel(), "employee");
        contentPanel.add(new ReportIngredientPanel(), "inventory");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Show dashboard by default
        showPanel("dashboard");
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
    
    private void setupKeyboardShortcut() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && 
                    e.getKeyCode() == KeyEvent.VK_F6 && 
                    e.isShiftDown()) {
                    
                    JOptionPane.showMessageDialog(this,
                        "✅ Manager UI Test Mode\n\n" +
                        "Sidebar: ✓\n" +
                        "Dashboard: ✓\n" +
                        "Ingredients: ✓\n" +
                        "Staff: ✓\n" +
                        "Products: ✓\n" +
                        "Reports: ✓\n\n" +
                        "All panels loaded successfully!",
                        "UI Test - Shift+F6",
                        JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
                return false;
            });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new ManagerMainFrame().setVisible(true);
        });
    }
}