package ui.manager;

import service.IngredientService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportIngredientPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private IngredientService ingredientService;
    
    public ReportIngredientPanel() {
        ingredientService = IngredientService.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel title = new JLabel("📦 Inventory Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel lowStock = new JLabel("⚠️ Low Stock Items: " + 
            ingredientService.getLowStockCount());
        lowStock.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lowStock.setForeground(Color.RED);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lowStock);
        
        add(panel, BorderLayout.NORTH);
    }
}