package util;

import config.AppConfig;

/**
 * Constants - Application Constants
 * Path: Source Packages/util/Constants.java
 * 
 * Tập trung tất cả constants của ứng dụng
 * Wrapper class cho AppConfig để dễ access
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class Constants {
    
    // ============ APPLICATION INFO ============
    
    public static final String APP_NAME = AppConfig.APP_NAME;
    public static final String APP_VERSION = AppConfig.APP_VERSION;
    public static final String APP_AUTHOR = AppConfig.APP_AUTHOR;
    public static final String APP_COPYRIGHT = AppConfig.APP_COPYRIGHT;
    
    // ============ USER ROLES ============
    
    public static final int ROLE_ADMIN = AppConfig.ROLE_ADMIN;
    public static final int ROLE_MANAGER = AppConfig.ROLE_MANAGER;
    public static final int ROLE_CASHIER = AppConfig.ROLE_CASHIER;
    public static final int ROLE_CHEF = AppConfig.ROLE_CHEF;
    public static final int ROLE_CUSTOMER = AppConfig.ROLE_CUSTOMER;
    
    public static final String[] ROLE_NAMES = AppConfig.ROLE_NAMES;
    
    // ============ USER STATUS ============
    
    public static final int STATUS_INACTIVE = AppConfig.STATUS_INACTIVE;
    public static final int STATUS_ACTIVE = AppConfig.STATUS_ACTIVE;
    public static final int STATUS_LOCKED = AppConfig.STATUS_LOCKED;
    
    public static final String[] STATUS_NAMES = AppConfig.STATUS_NAMES;
    
    // ============ EMPLOYEE ROLES ============
    
    public static final int EMPLOYEE_ROLE_CHEF = AppConfig.EMPLOYEE_ROLE_CHEF;
    public static final int EMPLOYEE_ROLE_CASHIER = AppConfig.EMPLOYEE_ROLE_CASHIER;
    public static final int EMPLOYEE_ROLE_MANAGER = AppConfig.EMPLOYEE_ROLE_MANAGER;
    
    public static final String[] EMPLOYEE_ROLE_NAMES = AppConfig.EMPLOYEE_ROLE_NAMES;
    
    // ============ ORDER STATUS ============
    
    public static final int ORDER_STATUS_NEW = AppConfig.ORDER_STATUS_NEW;
    public static final int ORDER_STATUS_PREPARING = AppConfig.ORDER_STATUS_PREPARING;
    public static final int ORDER_STATUS_COOKING = AppConfig.ORDER_STATUS_COOKING;
    public static final int ORDER_STATUS_READY = AppConfig.ORDER_STATUS_READY;
    public static final int ORDER_STATUS_COMPLETED = AppConfig.ORDER_STATUS_COMPLETED;
    public static final int ORDER_STATUS_CANCELLED = AppConfig.ORDER_STATUS_CANCELLED;
    
    public static final String[] ORDER_STATUS_NAMES = AppConfig.ORDER_STATUS_NAMES;
    
    // ============ PAYMENT STATUS ============
    
    public static final int PAYMENT_STATUS_UNPAID = AppConfig.PAYMENT_STATUS_UNPAID;
    public static final int PAYMENT_STATUS_PAID = AppConfig.PAYMENT_STATUS_PAID;
    public static final int PAYMENT_STATUS_REFUNDED = AppConfig.PAYMENT_STATUS_REFUNDED;
    public static final int PAYMENT_STATUS_FAILED = AppConfig.PAYMENT_STATUS_FAILED;
    
    public static final String[] PAYMENT_STATUS_NAMES = AppConfig.PAYMENT_STATUS_NAMES;
    
    // ============ PAYMENT METHODS ============
    
    public static final int PAYMENT_METHOD_CASH = AppConfig.PAYMENT_METHOD_CASH;
    public static final int PAYMENT_METHOD_TRANSFER = AppConfig.PAYMENT_METHOD_TRANSFER;
    public static final int PAYMENT_METHOD_CARD = AppConfig.PAYMENT_METHOD_CARD;
    public static final int PAYMENT_METHOD_MOMO = AppConfig.PAYMENT_METHOD_MOMO;
    public static final int PAYMENT_METHOD_VNPAY = AppConfig.PAYMENT_METHOD_VNPAY;
    
    public static final String[] PAYMENT_METHOD_NAMES = AppConfig.PAYMENT_METHOD_NAMES;
    
    // ============ PRODUCT CATEGORIES ============
    
    public static final String CATEGORY_BURGER = AppConfig.CATEGORY_BURGER;
    public static final String CATEGORY_PIZZA = AppConfig.CATEGORY_PIZZA;
    public static final String CATEGORY_CHICKEN = AppConfig.CATEGORY_CHICKEN;
    public static final String CATEGORY_COMBO = AppConfig.CATEGORY_COMBO;
    public static final String CATEGORY_SIDES = AppConfig.CATEGORY_SIDES;
    public static final String CATEGORY_DRINKS = AppConfig.CATEGORY_DRINKS;
    public static final String CATEGORY_DESSERTS = AppConfig.CATEGORY_DESSERTS;
    
    public static final String[] PRODUCT_CATEGORIES = AppConfig.PRODUCT_CATEGORIES;
    
    // ============ PRODUCT STATUS ============
    
    public static final int PRODUCT_UNAVAILABLE = AppConfig.PRODUCT_UNAVAILABLE;
    public static final int PRODUCT_AVAILABLE = AppConfig.PRODUCT_AVAILABLE;
    
    // ============ INGREDIENT STATUS ============
    
    public static final String INGREDIENT_STATUS_AVAILABLE = AppConfig.INGREDIENT_STATUS_AVAILABLE;
    public static final String INGREDIENT_STATUS_LOW = AppConfig.INGREDIENT_STATUS_LOW;
    public static final String INGREDIENT_STATUS_OUT_OF_STOCK = AppConfig.INGREDIENT_STATUS_OUT_OF_STOCK;
    public static final String INGREDIENT_STATUS_EXPIRED = AppConfig.INGREDIENT_STATUS_EXPIRED;
    
    // ============ CART STATUS ============
    
    public static final int CART_STATUS_ACTIVE = AppConfig.CART_STATUS_ACTIVE;
    public static final int CART_STATUS_CHECKED_OUT = AppConfig.CART_STATUS_CHECKED_OUT;
    public static final int CART_STATUS_ABANDONED = AppConfig.CART_STATUS_ABANDONED;
    
    // ============ BUSINESS RULES ============
    
    public static final double LOW_STOCK_THRESHOLD = AppConfig.LOW_STOCK_THRESHOLD;
    public static final double OUT_OF_STOCK_THRESHOLD = AppConfig.OUT_OF_STOCK_THRESHOLD;
    
    public static final int MAX_ITEMS_PER_ORDER = AppConfig.MAX_ITEMS_PER_ORDER;
    public static final double MIN_ORDER_AMOUNT = AppConfig.MIN_ORDER_AMOUNT;
    public static final double MAX_ORDER_AMOUNT = AppConfig.MAX_ORDER_AMOUNT;
    
    public static final double MIN_PRODUCT_PRICE = AppConfig.MIN_PRODUCT_PRICE;
    public static final double MAX_PRODUCT_PRICE = AppConfig.MAX_PRODUCT_PRICE;
    
    public static final double MIN_SALARY = AppConfig.MIN_SALARY;
    public static final double MAX_SALARY = AppConfig.MAX_SALARY;
    
    // ============ TIME SETTINGS ============
    
    public static final int ORDER_TIMEOUT_MINUTES = AppConfig.ORDER_TIMEOUT_MINUTES;
    public static final int SESSION_TIMEOUT_MINUTES = AppConfig.SESSION_TIMEOUT_MINUTES;
    public static final int AUTO_REFRESH_SECONDS = AppConfig.AUTO_REFRESH_SECONDS;
    public static final int NOTIFICATION_DURATION_SECONDS = AppConfig.NOTIFICATION_DURATION_SECONDS;
    
    // ============ DISPLAY SETTINGS ============
    
    public static final int ORDERS_PER_PAGE = AppConfig.ORDERS_PER_PAGE;
    public static final int PRODUCTS_PER_PAGE = AppConfig.PRODUCTS_PER_PAGE;
    public static final int EMPLOYEES_PER_PAGE = AppConfig.EMPLOYEES_PER_PAGE;
    public static final int CUSTOMERS_PER_PAGE = AppConfig.CUSTOMERS_PER_PAGE;
    
    // ============ DATE/TIME FORMATS ============
    
    public static final String DATE_FORMAT = AppConfig.DATE_FORMAT;
    public static final String TIME_FORMAT = AppConfig.TIME_FORMAT;
    public static final String DATETIME_FORMAT = AppConfig.DATETIME_FORMAT;
    public static final String DATETIME_FORMAT_SHORT = AppConfig.DATETIME_FORMAT_SHORT;
    public static final String TIME_FORMAT_SHORT = AppConfig.TIME_FORMAT_SHORT;
    
    // ============ CURRENCY SETTINGS ============
    
    public static final String CURRENCY_SYMBOL = AppConfig.CURRENCY_SYMBOL;
    public static final String CURRENCY_CODE = AppConfig.CURRENCY_CODE;
    public static final String CURRENCY_FORMAT = AppConfig.CURRENCY_FORMAT;
    public static final String CURRENCY_FORMAT_FULL = AppConfig.CURRENCY_FORMAT_FULL;
    
    // ============ FILE PATHS ============
    
    public static final String IMAGES_PATH = AppConfig.IMAGES_PATH;
    public static final String PRODUCTS_IMAGES_PATH = AppConfig.PRODUCTS_IMAGES_PATH;
    public static final String AVATARS_PATH = AppConfig.AVATARS_PATH;
    public static final String ICONS_PATH = AppConfig.ICONS_PATH;
    public static final String REPORTS_PATH = AppConfig.REPORTS_PATH;
    public static final String LOGS_PATH = AppConfig.LOGS_PATH;
    public static final String TEMP_PATH = AppConfig.TEMP_PATH;
    
    // ============ VALIDATION PATTERNS ============
    
    public static final String PHONE_PATTERN = AppConfig.PHONE_PATTERN;
    public static final String EMAIL_PATTERN = AppConfig.EMAIL_PATTERN;
    public static final String USERNAME_PATTERN = AppConfig.USERNAME_PATTERN;
    public static final String PASSWORD_PATTERN = AppConfig.PASSWORD_PATTERN;
    
    // ============ ERROR MESSAGES ============
    
    public static final String ERROR_DB_CONNECTION = AppConfig.ERROR_DB_CONNECTION;
    public static final String ERROR_INVALID_INPUT = AppConfig.ERROR_INVALID_INPUT;
    public static final String ERROR_PERMISSION_DENIED = AppConfig.ERROR_PERMISSION_DENIED;
    public static final String ERROR_NOT_FOUND = AppConfig.ERROR_NOT_FOUND;
    public static final String ERROR_DUPLICATE = AppConfig.ERROR_DUPLICATE;
    public static final String ERROR_LOGIN_FAILED = AppConfig.ERROR_LOGIN_FAILED;
    public static final String ERROR_ACCOUNT_LOCKED = AppConfig.ERROR_ACCOUNT_LOCKED;
    public static final String ERROR_ORDER_FAILED = AppConfig.ERROR_ORDER_FAILED;
    public static final String ERROR_PAYMENT_FAILED = AppConfig.ERROR_PAYMENT_FAILED;
    
    // ============ SUCCESS MESSAGES ============
    
    public static final String SUCCESS_CREATE = AppConfig.SUCCESS_CREATE;
    public static final String SUCCESS_UPDATE = AppConfig.SUCCESS_UPDATE;
    public static final String SUCCESS_DELETE = AppConfig.SUCCESS_DELETE;
    public static final String SUCCESS_LOGIN = AppConfig.SUCCESS_LOGIN;
    public static final String SUCCESS_LOGOUT = AppConfig.SUCCESS_LOGOUT;
    public static final String SUCCESS_ORDER = AppConfig.SUCCESS_ORDER;
    public static final String SUCCESS_PAYMENT = AppConfig.SUCCESS_PAYMENT;
    
    // ============ CONFIRMATION MESSAGES ============
    
    public static final String CONFIRM_DELETE = AppConfig.CONFIRM_DELETE;
    public static final String CONFIRM_CANCEL = AppConfig.CONFIRM_CANCEL;
    public static final String CONFIRM_LOGOUT = AppConfig.CONFIRM_LOGOUT;
    public static final String CONFIRM_UPDATE = AppConfig.CONFIRM_UPDATE;
    
    // ============ ADDITIONAL CONSTANTS ============
    
    // File extensions
    public static final String[] IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "webp"};
    public static final String[] DOCUMENT_EXTENSIONS = {"pdf", "doc", "docx", "xls", "xlsx"};
    
    // Timeouts (milliseconds)
    public static final int NETWORK_TIMEOUT = 30000;  // 30 seconds
    public static final int DATABASE_TIMEOUT = 10000; // 10 seconds
    public static final int UI_DELAY = 300;           // 300ms
    
    // Limits
    public static final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024; // 5MB
    public static final int MAX_TEXT_LENGTH = 1000;
    public static final int MAX_SEARCH_RESULTS = 100;
    
    // Keys for session attributes
    public static final String SESSION_USER = "current_user";
    public static final String SESSION_EMPLOYEE = "current_employee";
    public static final String SESSION_CART = "shopping_cart";
    public static final String SESSION_ORDER = "current_order";
    public static final String SESSION_LANGUAGE = "language";
    
    // Keys for preferences
    public static final String PREF_THEME = "theme";
    public static final String PREF_FONT_SIZE = "font_size";
    public static final String PREF_AUTO_REFRESH = "auto_refresh";
    public static final String PREF_NOTIFICATIONS = "notifications";
    
    // ============ HELPER METHODS ============
    
    /**
     * Get role name by role code
     */
    public static String getRoleName(int role) {
        return AppConfig.getRoleName(role);
    }
    
    /**
     * Get employee role name
     */
    public static String getEmployeeRoleName(int role) {
        return AppConfig.getEmployeeRoleName(role);
    }
    
    /**
     * Get status name by status code
     */
    public static String getStatusName(int status) {
        return AppConfig.getStatusName(status);
    }
    
    /**
     * Get order status name
     */
    public static String getOrderStatusName(int status) {
        return AppConfig.getOrderStatusName(status);
    }
    
    /**
     * Get payment status name
     */
    public static String getPaymentStatusName(int status) {
        return AppConfig.getPaymentStatusName(status);
    }
    
    /**
     * Get payment method name
     */
    public static String getPaymentMethodName(int method) {
        return AppConfig.getPaymentMethodName(method);
    }
    
    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        return AppConfig.isValidPhone(phone);
    }
    
    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        return AppConfig.isValidEmail(email);
    }
    
    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        return AppConfig.isValidUsername(username);
    }
    
    /**
     * Validate password
     */
    public static boolean isValidPassword(String password) {
        return AppConfig.isValidPassword(password);
    }
    
    /**
     * Check if user is admin
     */
    public static boolean isAdmin(int role) {
        return AppConfig.isAdmin(role);
    }
    
    /**
     * Check if user is manager
     */
    public static boolean isManager(int role) {
        return AppConfig.isManager(role);
    }
    
    /**
     * Check if user is cashier
     */
    public static boolean isCashier(int role) {
        return AppConfig.isCashier(role);
    }
    
    /**
     * Check if user is chef
     */
    public static boolean isChef(int role) {
        return AppConfig.isChef(role);
    }
    
    /**
     * Check if user is customer
     */
    public static boolean isCustomer(int role) {
        return AppConfig.isCustomer(role);
    }
}
