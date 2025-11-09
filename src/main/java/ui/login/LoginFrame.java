// ============================================================
// FILE: LoginFrame.java (FIXED - Complete Version)
// Path: Source Packages/ui/login/LoginFrame.java
// ============================================================
package ui.login;

import ui.components.RoundedPanel;
import ui.components.RoundedButton;
import dao.UserDAO;
import entity.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * LoginFrame - Modern centered login interface
 * 
 * Features:
 * - Centered card design
 * - Logo and branding
 * - Username/password fields
 * - Remember password checkbox
 * - Login button with loading state
 * - Forgot password link
 * - Role-based navigation
 * - Press Enter to login
 * - Shift+F6 test mode
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.1 - FIXED
 */
public class LoginFrame extends JFrame {
    
    // ============ CONSTANTS ============
    
    private static final Color ORANGE = new Color(255, 152, 0);
    private static final Color BG_GRADIENT_START = new Color(255, 152, 0);
    private static final Color BG_GRADIENT_END = new Color(255, 193, 7);
    private static final Color DARK_TEXT = new Color(33, 33, 33);
    private static final Color LIGHT_TEXT = new Color(117, 117, 117);
    
    // ============ COMPONENTS ============
    
    private UserDAO userDAO;
    private RoundedPanel loginCard;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;
    private RoundedButton loginButton;
    private JLabel errorLabel;
    
    // ============ CONSTRUCTOR ============
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
        setupKeyboardShortcut();
    }
    
    // ============ INITIALIZATION ============
    
    private void initComponents() {
        setTitle("FastFood Management - Đăng nhập vào hệ thống");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        // Login card
        loginCard = createLoginCard();
        
        mainPanel.add(loginCard);
        add(mainPanel);
    }
    
    // ============ LOGIN CARD ============
    
    private RoundedPanel createLoginCard() {
        RoundedPanel card = new RoundedPanel(20, true);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(450, 550));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Logo and title
        contentPanel.add(createHeader());
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Form fields
        contentPanel.add(createUsernameField());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createPasswordField());
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Remember & Forgot password
        contentPanel.add(createRememberPanel());
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Login button
        contentPanel.add(createLoginButton());
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Error message
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(244, 67, 54));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(errorLabel);
        
        // Footer
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createFooter());
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // ============ HEADER (Logo + Title) ============
    
    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Logo
        JLabel logoLabel = new JLabel("🍔");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // App name
        JLabel appNameLabel = new JLabel("FastFood Management");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appNameLabel.setForeground(ORANGE);
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Đăng nhập vào hệ thống");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(appNameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    // ============ USERNAME FIELD ============
    
    private JPanel createUsernameField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Label
        JLabel label = new JLabel("Tên đăng nhập");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Input field
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(350, 45));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Icon prefix
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setBorder(new EmptyBorder(0, 12, 0, 0));
        
        usernameField.setBorder(new EmptyBorder(10, 10, 10, 15));
        
        inputPanel.add(iconLabel, BorderLayout.WEST);
        inputPanel.add(usernameField, BorderLayout.CENTER);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(inputPanel);
        
        return panel;
    }
    
    // ============ PASSWORD FIELD ============
    
    private JPanel createPasswordField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Label
        JLabel label = new JLabel("Mật khẩu");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Input field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(350, 45));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Icon prefix + show/hide button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setBorder(new EmptyBorder(0, 12, 0, 0));
        
        JButton showBtn = new JButton("👁️");
        showBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showBtn.setContentAreaFilled(false);
        showBtn.setBorderPainted(false);
        showBtn.setFocusPainted(false);
        showBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showBtn.setToolTipText("Hiện/Ẩn mật khẩu");
        
        showBtn.addActionListener(e -> {
            if (passwordField.getEchoChar() == (char) 0) {
                passwordField.setEchoChar('•');
                showBtn.setText("👁️");
            } else {
                passwordField.setEchoChar((char) 0);
                showBtn.setText("🙈");
            }
        });
        
        passwordField.setBorder(new EmptyBorder(10, 10, 10, 10));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        inputPanel.add(iconLabel, BorderLayout.WEST);
        inputPanel.add(passwordField, BorderLayout.CENTER);
        inputPanel.add(showBtn, BorderLayout.EAST);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(inputPanel);
        
        return panel;
    }
    
    // ============ REMEMBER & FORGOT PASSWORD ============
    
    private JPanel createRememberPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        // Remember checkbox
        rememberCheckbox = new JCheckBox("Ghi nhớ mật khẩu");
        rememberCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberCheckbox.setForeground(DARK_TEXT);
        rememberCheckbox.setBackground(Color.WHITE);
        rememberCheckbox.setFocusPainted(false);
        
        // Forgot password link
        JLabel forgotLabel = new JLabel("Quên mật khẩu?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(ORANGE);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Vui lòng liên hệ admin để reset mật khẩu:\n" +
                    "Email: admin@fastfood.com\n" +
                    "Phone: 0123-456-789",
                    "Quên mật khẩu",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(rememberCheckbox, BorderLayout.WEST);
        panel.add(forgotLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    // ============ LOGIN BUTTON ============
    
    private JPanel createLoginButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        loginButton = new RoundedButton("🔑 Đăng nhập", 10);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(ORANGE);
        loginButton.setPreferredSize(new Dimension(350, 50));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loginButton.addActionListener(e -> handleLogin());
        
        panel.add(loginButton);
        
        return panel;
    }
    
    // ============ FOOTER ============
    
    private JPanel createFooter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel copyrightLabel = new JLabel("© 2024 FastFood Management System");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyrightLabel.setForeground(LIGHT_TEXT);
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(copyrightLabel);
        
        return panel;
    }
    
    // ============ LOGIN HANDLER (FIXED) ============
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
            passwordField.requestFocus();
            return;
        }
        
        // Show loading
        loginButton.setEnabled(false);
        loginButton.setText("⏳ Đang đăng nhập...");
        errorLabel.setText(" ");
        
        // Authenticate (in background thread)
        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                Thread.sleep(500); // Simulate network delay
                
                // FIXED: Authenticate using UserDAO
                User user = userDAO.getByUsername(username);
                
                if (user != null && 
                    user.getPassword().equals(password) && 
                    user.getStatus() == 1) {
                    return user;
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    
                    if (user != null) {
                        // Login success
                        showSuccess("Đăng nhập thành công!");
                        
                        // Navigate based on role
                        SwingUtilities.invokeLater(() -> {
                            navigateToRoleScreen(user);
                            dispose();
                        });
                    } else {
                        // Login failed
                        showError("Tên đăng nhập hoặc mật khẩu không đúng");
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception e) {
                    showError("Lỗi hệ thống: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("🔑 Đăng nhập");
                }
            }
        };
        
        worker.execute();
    }
    
    // ============ NAVIGATION ============
    
    private void navigateToRoleScreen(User user) {
        String roleName = user.getRoleName();
        
        JOptionPane.showMessageDialog(this,
            "✅ Đăng nhập thành công!\n\n" +
            "Xin chào: " + user.getUsername() + "\n" +
            "Vai trò: " + roleName + "\n\n" +
            "Chuyển hướng đến màn hình " + roleName + "...",
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE);
        
        // TODO: Open role-specific frame
        switch (user.getRole()) {
            case 0: // Admin
                System.out.println("→ Navigate to AdminFrame");
                // new AdminFrame(user).setVisible(true);
                break;
            case 1: // Manager
                System.out.println("→ Navigate to ManagerMainFrame");
                 new ui.manager.ManagerMainFrame().setVisible(true);
                break;
            case 2: // Cashier
                System.out.println("→ Navigate to CashierFrame");
                // new CashierFrame(user).setVisible(true);
                break;
            case 3: // Chef
                System.out.println("→ Navigate to ChefFrame");
                // new ChefFrame(user).setVisible(true);
                break;
            case 4: // Customer
                System.out.println("→ Navigate to CustomerFrame");
                // new CustomerFrame(user).setVisible(true);
                break;
        }
    }
    
    // ============ ERROR/SUCCESS MESSAGES ============
    
    private void showError(String message) {
        errorLabel.setText("❌ " + message);
        errorLabel.setForeground(new Color(244, 67, 54));
    }
    
    private void showSuccess(String message) {
        errorLabel.setText("✅ " + message);
        errorLabel.setForeground(new Color(76, 175, 80));
    }
    
    // ============ KEYBOARD SHORTCUT ============
    
    private void setupKeyboardShortcut() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && 
                    e.getKeyCode() == KeyEvent.VK_F6 && 
                    e.isShiftDown()) {
                    
                    // Test credentials
                    JOptionPane.showMessageDialog(this,
                        "🧪 TEST MODE - Login Credentials:\n\n" +
                        "Manager:\n" +
                        "  Username: manager\n" +
                        "  Password: 123456\n\n" +
                        "Cashier:\n" +
                        "  Username: cashier\n" +
                        "  Password: 123456\n\n" +
                        "Chef:\n" +
                        "  Username: chef\n" +
                        "  Password: 123456\n\n" +
                        "Press Enter to login!",
                        "Test Mode - Shift+F6",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Auto-fill for quick test
                    usernameField.setText("manager01");
                    passwordField.setText("123456");
                    usernameField.requestFocus();
                    
                    return true;
                }
                return false;
            });
    }
    
    // ============ GRADIENT BACKGROUND PANEL ============
    
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int w = getWidth();
            int h = getHeight();
            
            GradientPaint gp = new GradientPaint(
                0, 0, BG_GRADIENT_START,
                w, h, BG_GRADIENT_END
            );
            
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            
            // Show instructions
            JOptionPane.showMessageDialog(frame,
                "🎯 FastFood Login Screen (FIXED)\n\n" +
                "Features:\n" +
                "• Modern centered card design\n" +
                "• Gradient background\n" +
                "• Show/hide password\n" +
                "• Press Enter to login\n" +
                "• Press Shift+F6 for test credentials\n\n" +
                "Test login:\n" +
                "Username: manager01\n" +
                "Password: 123456\n\n" +
                "✅ FIXED: Using UserDAO.getByUsername()",
                "Login Test Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}