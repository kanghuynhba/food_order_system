package ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * CustomTable - Pre-styled JTable with modern look
 */
public class CustomTable extends JTable {
    
    public CustomTable(DefaultTableModel model) {
        super(model);
        setupStyle();
    }
    
    private void setupStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setRowHeight(45);
        setSelectionBackground(new Color(255, 152, 0, 50));
        setSelectionForeground(Color.BLACK);
        setShowGrid(true);
        setGridColor(new Color(240, 240, 240));
        setIntercellSpacing(new Dimension(1, 1));
        
        // Header style
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(250, 250, 250));
        header.setForeground(new Color(33, 33, 33));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Center align renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Apply to all columns (optional)
        // for (int i = 0; i < getColumnCount(); i++) {
        //     getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        // }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CustomTable Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            
            String[] columns = {"ID", "Name", "Role", "Status"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            model.addRow(new Object[]{"1", "John Doe", "Manager", "Active"});
            model.addRow(new Object[]{"2", "Jane Smith", "Chef", "Active"});
            model.addRow(new Object[]{"3", "Bob Wilson", "Cashier", "Inactive"});
            
            CustomTable table = new CustomTable(model);
            JScrollPane scroll = new JScrollPane(table);
            
            frame.add(scroll);
            frame.setVisible(true);
        });
    }
}