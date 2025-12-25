package form;

import entity.Ingredient;
import service.IngredientService; // Assumed service class
import form.BaseForm;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IngredientForm extends BaseForm {

    private IngredientService ingredientService;
    private Ingredient ingredient; // Null = Add Mode, Object = Edit Mode

    // --- UI Components ---
    private JTextField txtName;
    private JTextField txtQuantity;
    private JComboBox<String> cbUnit;
    private JTextField txtSupplier;
    private JTextField txtExpiryDate; // Format: yyyy-MM-dd
    private JComboBox<String> cbStatus;

    // Helper for date formatting
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // --- Constructors ---

    public IngredientForm(Frame parent) {
        super(parent, "Add New Ingredient");
        // this.ingredientService = IngredientService.getInstance(); 
        // Uncomment above line when your Service is ready
        initComponent();
    }

    public IngredientForm(Frame parent, Ingredient ingredientToEdit) {
        this(parent);
        this.setTitle("Edit Ingredient");
        this.ingredient = ingredientToEdit;
        btnDelete.setVisible(true);
        fillData();
    }

    // --- Initialization ---

    private void initComponent() {
        // 1. Basic Info
        txtName = addTextField("Ingredient Name:");
        
        // 2. Quantity & Unit
        JPanel qtyPanel = new JPanel(new BorderLayout(5, 0));
        txtQuantity = new JTextField();
        
        String[] units = {"kg", "g", "l", "ml", "pcs", "box", "can"};
        cbUnit = new JComboBox<>(units);
        cbUnit.setPreferredSize(new Dimension(80, 25));
        
        qtyPanel.add(txtQuantity, BorderLayout.CENTER);
        qtyPanel.add(cbUnit, BorderLayout.EAST);
        
        addComponent("Quantity:", qtyPanel);

        // 3. Supplier
        txtSupplier = addTextField("Supplier:");

        // 4. Expiry Date
        txtExpiryDate = addTextField("Expiry Date (yyyy-mm-dd):");
        
        // 5. Status
        String[] statuses = {"available", "low", "out_of_stock", "expired"};
        cbStatus = new JComboBox<>(statuses);
        addComponent("Status:", cbStatus);
    }

    // --- Fill Data (Edit Mode) ---

    private void fillData() {
        if (ingredient == null) return;

        txtName.setText(ingredient.getName());
        txtQuantity.setText(String.valueOf(ingredient.getQuantity()));
        cbUnit.setSelectedItem(ingredient.getUnit());
        txtSupplier.setText(ingredient.getSupplier());
        
        if (ingredient.getExpiryDate() != null) {
            txtExpiryDate.setText(dateFormat.format(ingredient.getExpiryDate()));
        }

        cbStatus.setSelectedItem(ingredient.getStatus());
    }

    // --- Save Logic ---

    @Override
    protected void onSave() {
        // 1. Validation
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingredient Name is required!");
            return;
        }

        try {
            // 2. Prepare Data
            String name = txtName.getText();
            String unit = cbUnit.getSelectedItem().toString();
            String supplier = txtSupplier.getText();
            String status = cbStatus.getSelectedItem().toString();
            
            // Parse Quantity
            double quantity = 0;
            try {
                quantity = Double.parseDouble(txtQuantity.getText());
                if (quantity < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid positive number!");
                return;
            }

            // Parse Date
            Date expiryDate = null;
            String dateStr = txtExpiryDate.getText().trim();
            if (!dateStr.isEmpty()) {
                try {
                    java.util.Date parsed = dateFormat.parse(dateStr);
                    expiryDate = new Date(parsed.getTime());
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Date format! Please use yyyy-MM-dd");
                    return;
                }
            }

            // 3. Save to Object
            if (ingredient == null) {
                // Call Service
                ingredientService.addIngredient(name, quantity, unit, expiryDate, supplier);
                JOptionPane.showMessageDialog(this, "Ingredient added successfully!");
            } else {
                // --- UPDATE ---
                ingredient.setName(name);
                ingredient.setQuantity(quantity);
                ingredient.setUnit(unit);
                ingredient.setSupplier(supplier);
                ingredient.setExpiryDate(expiryDate);
                ingredient.setStatus(status);
                
                // Call Service
                ingredientService.updateIngredient(ingredient);
                System.out.println("Updating: " + ingredient); // Debug print
                JOptionPane.showMessageDialog(this, "Ingredient updated successfully!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override 
    protected void onDelete() {
        boolean success=ingredientService.deleteIngredient(ingredient.getIngredientId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Deleted successfully!");
            // Set isSaved to true so the parent table knows to refresh
            this.isSaved = true; 
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete.");
        }
    }
}
