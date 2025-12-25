package form;

import entity.Product;

import service.ProductService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Timestamp;

public class ProductForm extends BaseForm {
    private ProductService productService;
    private Product product;

    private JTextField txtName;
    private JTextArea txtDesc;
    private JTextField txtPrice;
    private String txtImageUrl=null;
    private JComboBox<String> cbCategory;
    private JCheckBox chkAvailable;

    private JLabel lblImagePreview; 
    private JButton btnChooseImage;

   // Constructor cho Thêm Mới
    public ProductForm(Frame parent) {
        super(parent, "Add New Product");
        this.productService = ProductService.getInstance();
        initComponent();
    }

    // Constructor cho Edit (Update)
    public ProductForm(Frame parent, Product productToEdit) {
        this(parent);        
        this.setTitle("Edit Product"); 
        this.product = productToEdit;
        fillData();     
    }

    private void initComponent() {
        txtName = addTextField("Product Name:");
        txtDesc = addTextArea("Product Description:", 2);
        txtPrice = addTextField("Price:");

        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
        btnChooseImage = new JButton("Chọn Ảnh");

        imagePanel.add(btnChooseImage, BorderLayout.EAST);
        
        addComponent("Image Path:", imagePanel);

        // Preview img
        lblImagePreview = new JLabel();
        lblImagePreview.setPreferredSize(new Dimension(150, 150)); // Kích thước khung ảnh
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
        lblImagePreview.setText("No Image");
        
        // Thêm vào form (Dùng addComponent của BaseForm)
        addComponent("Preview:", lblImagePreview);

        btnChooseImage.addActionListener(this::chooseImage);

        String[] categories={"Combo", "Chicken", "Burger", 
            "Rice", "Sides", "Drinks", "Other"};
        cbCategory = new JComboBox<>(categories);
        addComponent("Category:", cbCategory); 

        chkAvailable=new JCheckBox("Is Available");
        addComponent("Status:", chkAvailable);
    }

    private void chooseImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        // Chỉ cho phép chọn file ảnh
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            txtImageUrl=path;
            setImageIcon(path);
        }
    }

    // Hàm helper để resize ảnh vừa vặn với khung
    private void setImageIcon(String path) {
        if (path == null || path.isEmpty()) return;
        
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage();
        // Resize ảnh về 150x150 (hoặc kích thước bạn muốn)
        Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        lblImagePreview.setIcon(new ImageIcon(scaledImage));
        lblImagePreview.setText(""); // Xóa chữ "No Image"
    }

    private void fillData() {
        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        txtPrice.setText(String.valueOf(product.getPrice()));
        cbCategory.setSelectedItem(product.getCategory());
        chkAvailable.setSelected(product.getAvailable() == 1);
        
        // Fill ảnh nếu có
        if (product.getImageUrl() != null) {
            txtImageUrl=product.getImageUrl();
            setImageIcon(product.getImageUrl());
        }
    }

    @Override
    protected void onSave() {
        // 1. Validation (Kiểm tra dữ liệu)
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(txtPrice.getText());
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số dương hợp lệ!");
            return;
        }

        // 2. Lấy dữ liệu từ form
        String name = txtName.getText();
        String desc = txtDesc.getText();
        String category = cbCategory.getSelectedItem().toString();
        int available = chkAvailable.isSelected() ? 1 : 0;
        String finalImageUrl = (txtImageUrl != null) ? txtImageUrl : ""; 

        // 3. Phân biệt Add hay Update
        if (this.product == null) {
            boolean success = productService.addProduct(name, desc, price, 
                    category, finalImageUrl, available);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                this.dispose(); // Đóng form
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } else {
            // Cập nhật các trường vào object product hiện tại
            this.product.setName(name);
            this.product.setDescription(desc);
            this.product.setPrice(price);
            this.product.setCategory(category);
            this.product.setImageUrl(finalImageUrl);
            this.product.setAvailable(available);

            // Gọi hàm update bên Service (Bạn cần chắc chắn Service có hàm này)
            boolean success = productService.updateProduct(this.product);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }
}
