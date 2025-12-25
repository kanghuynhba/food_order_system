package ui.manager;

import entity.Ingredient;

import service.IngredientService;

import form.IngredientForm;

import ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class IngredientPanel extends JPanel {
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color GREEN = new Color(46, 125, 50);
    private static final Color YELLOW = new Color(251, 192, 45);
    private static final Color RED = new Color(211, 47, 47);
    
    private IngredientService ingredientService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private List<Ingredient> ingredients;
    
    public IngredientPanel() {
        ingredientService = IngredientService.getInstance();
        ingredients=ingredientService.getAllIngredients();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Table
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel title = new JLabel("Ingredients FastFood");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Manage your ingredient stock levels and reorder points");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        // Buttons
        RoundedButton addBtn = new RoundedButton("Add Ingredient");
        addBtn.setBackground(ORANGE);
        addBtn.setPreferredSize(new Dimension(180, 38));
        addBtn.setMaximumSize(new Dimension(200, 38));
        addBtn.addActionListener(e -> {
            openIngredientForm(null);
        });
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filters
        panel.add(createFilterPanel(), BorderLayout.NORTH);
        
        // Table
        String[] columns = {"SKU", "Tên", "Danh mục", "Tồn kho", "ĐVT", 
                           "Reorder Point", "Trạng thái", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(250, 250, 250));
        table.setSelectionBackground(new Color(255, 152, 0, 50));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for DOUBLE CLICK (Standard for editing)
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the employee object from our list using the row index
                        Ingredient ingredient = ingredients.get(selectedRow);
                        openIngredientForm(ingredient); // Edit Mode
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Search
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JLabel searchIcon = new JLabel(" Search ingredients...");
        searchIcon.setForeground(Color.GRAY);
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
      
        panel.add(searchField);
       
        return panel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (Ingredient ing : ingredients) {
            String status = getStatusLabel(ing.getStatus());
            tableModel.addRow(new Object[]{
                "ING-" + String.format("%03d", ing.getIngredientId()),
                ing.getName(),
                ing.getSupplier() != null ? ing.getSupplier() : "N/A",
                (int) ing.getQuantity(),
                ing.getUnit(),
                "20", // Reorder point placeholder
                status,
                "2 hours ago"
            });
        }
    }

    private void openIngredientForm(Ingredient ingredient) {
        IngredientForm form = new IngredientForm((JFrame) SwingUtilities.getWindowAncestor(this), ingredient);
        form.setVisible(true);
        
        if (form.isSaved()) {
            loadData(); 
        }
    }
    
    private String getStatusLabel(String status) {
        return switch (status) {
            case "available" -> "OK";
            case "low" -> "Low";
            case "out_of_stock" -> "Out";
            case "expired" -> "Expired";
            default -> status;
        };
    }
}
