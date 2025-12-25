package form;

import javax.swing.*;
import java.awt.*;

// Abstract because we never use this class directly, only its children
public abstract class BaseForm extends JDialog {
    
    private JPanel formPanel;
    private GridBagConstraints gbc;
    private int currentRow = 0; // Tracks the vertical position
    
    // Status to track if user clicked Save
    protected boolean isSaved = false; 

    public BaseForm(Frame parent, String title) {
        super(parent, title, true); // Modal
        setLayout(new BorderLayout());
        
        // 1. Center Panel for Inputs
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        
        // Initialize Layout Constraints
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 2. Bottom Panel for Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        
        // Events
        btnSave.addActionListener(e -> {
            if (validateForm()) { // Optional validation hook
                onSave();       // Abstract method logic
                isSaved = true;
                dispose();      // Close
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Default Size
        setSize(1000, 600);
        setLocationRelativeTo(parent);
    }

    /**
     * Generic method to add ANY component (ComboBox, Checkbox, etc)
     */
    protected void addComponent(String labelText, JComponent component) {
        // Label Column
        gbc.gridx = 0; 
        gbc.gridy = currentRow;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel(labelText), gbc);

        // Component Column
        gbc.gridx = 1; 
        gbc.gridy = currentRow;
        gbc.weightx = 1.0; // Expand to fill width
        formPanel.add(component, gbc);

        currentRow++; // Move to next row for next component
    }


    /**
     * Shortcut method specifically for TextFields
     * Returns the created TextField so you can assign it to a variable
     */
    protected JTextField addTextField(String labelText) {
        JTextField tf = new JTextField(20);
        addComponent(labelText, tf);
        return tf;
    }

    protected JTextArea addTextArea(String labelText, int rows) {
        JTextArea textArea = new JTextArea(rows, 20);        
        textArea.setLineWrap(true);      
        textArea.setWrapStyleWord(true);        

        JScrollPane scrollPane = new JScrollPane(textArea);
        
        // Add to the layout
        addComponent(labelText, scrollPane);
        
        return textArea;
    }

    // Abstract methods that Children MUST implement
    protected abstract void onSave();
    
    // Optional: Children can override this to add validation logic
    protected boolean validateForm() { return true; }
    
    public boolean isSaved() { return isSaved; }
    
}

