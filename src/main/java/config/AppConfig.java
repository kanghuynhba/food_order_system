package config;

/**
 * AppConfig - Application Configuration & Constants
 * Path: Source Packages/config/AppConfig.java
 * 
 * Chứa tất cả constants và settings cho Fast Food Restaurant Management System
 * Dựa trên database schema trong fastfood_db
 */
public class AppConfig {
    
    // ============ APPLICATION INFO ============
    public static final String APP_NAME = "FastFood Management System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "Nguyễn Trương Quốc Huân & Huỳnh Bá Khang";
    public static final String APP_COPYRIGHT = "© 2025 FastFood Pro";
    
    // ============ USER ROLES ============
    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_MANAGER = 1;
    public static final int ROLE_CASHIER = 2;
    public static final int ROLE_CHEF = 3;
    public static final int ROLE_CUSTOMER = 4;
    
    // Role names
    public static final String[] ROLE_NAMES = {
        "Admin",        // 0
        "Manager",      // 1
        "Cashier",      // 2
        "Chef",         // 3
        "Customer"      // 4
    };
    
    // ============ USER STATUS ============
    public static final int STATUS_INACTIVE = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_LOCKED = 2;
    
    // Status names
    public static final String[] STATUS_NAMES = {
        "Inactive",     // 0
        "Active",       // 1
        "Locked"        // 2
    };
    
    // ============ EMPLOYEE ROLES ============
    public static final int EMPLOYEE_ROLE_CHEF = 1;
    public static final int EMPLOYEE_ROLE_CASHIER = 2;
    public static final int EMPLOYEE_ROLE_MANAGER = 3;
    
    // Employee role names
    public static final String[] EMPLOYEE_ROLE_NAMES = {
        "",            // 0 - không dùng
        "Chef",        // 1
        "Cashier",     // 2
        "Manager"      // 3
    };
    
    // ============ ORDER STATUS (Theo DB) ============
    public static final int ORDER_STATUS_NEW = 0;           // Đơn mới từ customer
    public static final int ORDER_STATUS_PREPARING = 1;     // Đang chuẩn bị
    public static final int ORDER_STATUS_COOKING = 2;       // Đang nấu
    public static final int ORDER_STATUS_READY = 3;         // Sẵn sàng
    public static final int ORDER_STATUS_COMPLETED = 4;     // Hoàn thành
    public static final int ORDER_STATUS_CANCELLED = 5;     // Đã hủy
    
    // Order status names
    public static final String[] ORDER_STATUS_NAMES = {
        "Đơn mới",          // 0
        "Đã xác nhận",      // 1
        "Đang chuẩn bị",    // 2
        "Đang nấu",         // 3
        "Sẵn sàng",         // 4
        "Hoàn thành",       // 5
        "Đã hủy"            // 6
    };
    
    // ============ PAYMENT STATUS (Theo DB) ============
    public static final int PAYMENT_STATUS_UNPAID = 0;
    public static final int PAYMENT_STATUS_PAID = 1;
    public static final int PAYMENT_STATUS_REFUNDED = 2;
    public static final int PAYMENT_STATUS_FAILED = 3;
    
    // Payment status names
    public static final String[] PAYMENT_STATUS_NAMES = {
        "Chưa thanh toán",  // 0
        "Đã thanh toán",    // 1
        "Đã hoàn tiền",     // 2
        "Thất bại"          // 3
    };
    
    // ============ PAYMENT METHODS (Theo DB) ============
    public static final int PAYMENT_METHOD_CASH = 0;
    public static final int PAYMENT_METHOD_TRANSFER = 1;
    public static final int PAYMENT_METHOD_CARD = 2;
    public static final int PAYMENT_METHOD_MOMO = 3;
    public static final int PAYMENT_METHOD_VNPAY = 4;
    
    // Payment method names
    public static final String[] PAYMENT_METHOD_NAMES = {
        "Tiền mặt",         // 0
        "Chuyển khoản",     // 1
        "Thẻ tín dụng",     // 2
        "MoMo",             // 3
        "VNPay"             // 4
    };
    
    // ============ PRODUCT CATEGORIES (Theo DB) ============
    public static final String CATEGORY_BURGER = "Burger";
    public static final String CATEGORY_PIZZA = "Pizza";
    public static final String CATEGORY_CHICKEN = "Chicken";
    public static final String CATEGORY_COMBO = "Combo";
    public static final String CATEGORY_SIDES = "Sides";
    public static final String CATEGORY_DRINKS = "Drinks";
    public static final String CATEGORY_DESSERTS = "Desserts";
    
    // All categories
    public static final String[] PRODUCT_CATEGORIES = {
        CATEGORY_BURGER,
        CATEGORY_PIZZA,
        CATEGORY_CHICKEN,
        CATEGORY_COMBO,
        CATEGORY_SIDES,
        CATEGORY_DRINKS,
        CATEGORY_DESSERTS
    };
    
    // ============ PRODUCT STATUS ============
    public static final int PRODUCT_UNAVAILABLE = 0;
    public static final int PRODUCT_AVAILABLE = 1;
    
    // ============ INGREDIENT/MATERIAL STATUS (Theo DB) ============
    public static final String INGREDIENT_STATUS_AVAILABLE = "available";
    public static final String INGREDIENT_STATUS_LOW = "low";
    public static final String INGREDIENT_STATUS_OUT_OF_STOCK = "out_of_stock";
    public static final String INGREDIENT_STATUS_EXPIRED = "expired";
    
    // ============ CART STATUS ============
    public static final int CART_STATUS_ACTIVE = 0;
    public static final int CART_STATUS_CHECKED_OUT = 1;
    public static final int CART_STATUS_ABANDONED = 2;
    
    // ============ BUSINESS RULES ============
    
    // Stock thresholds
    public static final double LOW_STOCK_THRESHOLD = 10.0;
    public static final double OUT_OF_STOCK_THRESHOLD = 0.0;
    
    // Order limits
    public static final int MAX_ITEMS_PER_ORDER = 50;
    public static final double MIN_ORDER_AMOUNT = 10000;       // 10,000 VNĐ
    public static final double MAX_ORDER_AMOUNT = 10000000;    // 10,000,000 VNĐ
    
    // Product limits
    public static final double MIN_PRODUCT_PRICE = 5000;       // 5,000 VNĐ
    public static final double MAX_PRODUCT_PRICE = 1000000;    // 1,000,000 VNĐ
    
    // Employee salary limits
    public static final double MIN_SALARY = 5000000;           // 5,000,000 VNĐ
    public static final double MAX_SALARY = 50000000;          // 50,000,000 VNĐ
    
    // ============ TIME SETTINGS ============
    public static final int ORDER_TIMEOUT_MINUTES = 30;
    public static final int SESSION_TIMEOUT_MINUTES = 60;
    public static final int AUTO_REFRESH_SECONDS = 10;
    public static final int NOTIFICATION_DURATION_SECONDS = 5;
    
    // ============ DISPLAY SETTINGS ============
    public static final int ORDERS_PER_PAGE = 10;
    public static final int PRODUCTS_PER_PAGE = 12;
    public static final int EMPLOYEES_PER_PAGE = 20;
    public static final int CUSTOMERS_PER_PAGE = 20;
    
    // ============ DATE/TIME FORMATS ============
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATETIME_FORMAT_SHORT = "dd/MM/yyyy HH:mm";
    public static final String TIME_FORMAT_SHORT = "HH:mm";
    
    // ============ CURRENCY SETTINGS ============
    public static final String CURRENCY_SYMBOL = "đ";
    public static final String CURRENCY_CODE = "VND";
    public static final String CURRENCY_FORMAT = "#,###";
    public static final String CURRENCY_FORMAT_FULL = "#,##0.00";
    
    // ============ FILE PATHS ============
    public static final String IMAGES_PATH = "src/main/resources/images/";
    public static final String PRODUCTS_IMAGES_PATH = IMAGES_PATH + "products/";
    public static final String AVATARS_PATH = IMAGES_PATH + "avatars/";
    public static final String ICONS_PATH = IMAGES_PATH + "icons/";
    public static final String REPORTS_PATH = "reports/";
    public static final String LOGS_PATH = "logs/";
    public static final String TEMP_PATH = "temp/";
    
    // ============ VALIDATION PATTERNS ============
    public static final String PHONE_PATTERN = "^(\\+84|0)[0-9]{9}$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
    public static final String PASSWORD_PATTERN = "^.{6,}$";
    
    // ============ ERROR MESSAGES ============
    public static final String ERROR_DB_CONNECTION = "Không thể kết nối đến database!";
    public static final String ERROR_INVALID_INPUT = "Dữ liệu nhập vào không hợp lệ!";
    public static final String ERROR_PERMISSION_DENIED = "Bạn không có quyền thực hiện thao tác này!";
    public static final String ERROR_NOT_FOUND = "Không tìm thấy dữ liệu!";
    public static final String ERROR_DUPLICATE = "Dữ liệu đã tồn tại!";
    public static final String ERROR_LOGIN_FAILED = "Đăng nhập thất bại! Kiểm tra lại username/password.";
    public static final String ERROR_ACCOUNT_LOCKED = "Tài khoản đã bị khóa!";
    public static final String ERROR_ORDER_FAILED = "Không thể tạo đơn hàng!";
    public static final String ERROR_PAYMENT_FAILED = "Thanh toán thất bại!";
    
    // ============ SUCCESS MESSAGES ============
    public static final String SUCCESS_CREATE = "Thêm mới thành công!";
    public static final String SUCCESS_UPDATE = "Cập nhật thành công!";
    public static final String SUCCESS_DELETE = "Xóa thành công!";
    public static final String SUCCESS_LOGIN = "Đăng nhập thành công!";
    public static final String SUCCESS_LOGOUT = "Đăng xuất thành công!";
    public static final String SUCCESS_ORDER = "Đặt hàng thành công!";
    public static final String SUCCESS_PAYMENT = "Thanh toán thành công!";
    
    // ============ CONFIRMATION MESSAGES ============
    public static final String CONFIRM_DELETE = "Bạn có chắc chắn muốn xóa?";
    public static final String CONFIRM_CANCEL = "Bạn có chắc chắn muốn hủy?";
    public static final String CONFIRM_LOGOUT = "Bạn có chắc chắn muốn đăng xuất?";
    public static final String CONFIRM_UPDATE = "Bạn có chắc chắn muốn cập nhật?";
    
    // ============ HELPER METHODS ============
    
    /**
     * Get role name by role code
     */
    public static String getRoleName(int role) {
        if (role >= 0 && role < ROLE_NAMES.length) {
            return ROLE_NAMES[role];
        }
        return "Unknown";
    }
    
    /**
     * Get employee role name
     */
    public static String getEmployeeRoleName(int role) {
        if (role > 0 && role < EMPLOYEE_ROLE_NAMES.length) {
            return EMPLOYEE_ROLE_NAMES[role];
        }
        return "Unknown";
    }
    
    /**
     * Get status name by status code
     */
    public static String getStatusName(int status) {
        if (status >= 0 && status < STATUS_NAMES.length) {
            return STATUS_NAMES[status];
        }
        return "Unknown";
    }
    
    /**
     * Get order status name by status code
     */
    public static String getOrderStatusName(int status) {
        if (status >= 0 && status < ORDER_STATUS_NAMES.length) {
            return ORDER_STATUS_NAMES[status];
        }
        return "Unknown";
    }
    
    /**
     * Get payment status name by status code
     */
    public static String getPaymentStatusName(int status) {
        if (status >= 0 && status < PAYMENT_STATUS_NAMES.length) {
            return PAYMENT_STATUS_NAMES[status];
        }
        return "Unknown";
    }
    
    /**
     * Get payment method name by method code
     */
    public static String getPaymentMethodName(int method) {
        if (method >= 0 && method < PAYMENT_METHOD_NAMES.length) {
            return PAYMENT_METHOD_NAMES[method];
        }
        return "Unknown";
    }
    
    /**
     * Format currency
     */
    public static String formatCurrency(double amount) {
        return String.format("%,.0f%s", amount, CURRENCY_SYMBOL);
    }
    
    /**
     * Format currency with decimal
     */
    public static String formatCurrencyFull(double amount) {
        return String.format("%,.2f%s", amount, CURRENCY_SYMBOL);
    }
    
    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches(PHONE_PATTERN);
    }
    
    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_PATTERN);
    }
    
    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches(USERNAME_PATTERN);
    }
    
    /**
     * Validate password
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
    
    /**
     * Check if user is admin
     */
    public static boolean isAdmin(int role) {
        return role == ROLE_ADMIN;
    }
    
    /**
     * Check if user is manager
     */
    public static boolean isManager(int role) {
        return role == ROLE_MANAGER;
    }
    
    /**
     * Check if user is cashier
     */
    public static boolean isCashier(int role) {
        return role == ROLE_CASHIER;
    }
    
    /**
     * Check if user is chef
     */
    public static boolean isChef(int role) {
        return role == ROLE_CHEF;
    }
    
    /**
     * Check if user is customer
     */
    public static boolean isCustomer(int role) {
        return role == ROLE_CUSTOMER;
    }
}
