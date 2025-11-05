package util;

import config.AppConfig;
import java.util.regex.Pattern;

/**
 * ValidationUtil - Input Validation Utility
 * Path: Source Packages/util/ValidationUtil.java
 * 
 * Provides comprehensive validation for user inputs
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ValidationUtil {
    
    // ============ PATTERNS ============
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile(AppConfig.PHONE_PATTERN);
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile(AppConfig.EMAIL_PATTERN);
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile(AppConfig.USERNAME_PATTERN);
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile(AppConfig.PASSWORD_PATTERN);
    
    // Vietnamese name pattern (allows Unicode characters)
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-ZÀ-ỹ\\s]{2,50}$");
    
    // Numeric only pattern
    private static final Pattern NUMERIC_PATTERN = 
        Pattern.compile("^\\d+$");
    
    // Alphanumeric pattern
    private static final Pattern ALPHANUMERIC_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9]+$");
    
    // ============ STRING VALIDATION ============
    
    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Check if string is blank (null, empty, or whitespace only)
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string length is within range
     */
    public static boolean isLengthValid(String str, int min, int max) {
        if (isEmpty(str)) return false;
        int length = str.trim().length();
        return length >= min && length <= max;
    }
    
    /**
     * Check if string matches pattern
     */
    public static boolean matches(String str, String pattern) {
        if (isEmpty(str) || isEmpty(pattern)) return false;
        return str.matches(pattern);
    }
    
    /**
     * Check if string matches compiled pattern
     */
    public static boolean matches(String str, Pattern pattern) {
        if (isEmpty(str) || pattern == null) return false;
        return pattern.matcher(str).matches();
    }
    
    // ============ CONTACT VALIDATION ============
    
    /**
     * Validate phone number
     * Format: Vietnamese phone (e.g., 0912345678, +84912345678)
     */
    public static boolean isValidPhone(String phone) {
        return matches(phone, PHONE_PATTERN);
    }
    
    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        return matches(email, EMAIL_PATTERN);
    }
    
    /**
     * Get phone validation error message
     */
    public static String getPhoneError(String phone) {
        if (isEmpty(phone)) {
            return "Số điện thoại không được để trống";
        }
        if (!isValidPhone(phone)) {
            return "Số điện thoại không hợp lệ (VD: 0912345678)";
        }
        return null;
    }
    
    /**
     * Get email validation error message
     */
    public static String getEmailError(String email) {
        if (isEmpty(email)) {
            return "Email không được để trống";
        }
        if (!isValidEmail(email)) {
            return "Email không hợp lệ";
        }
        return null;
    }
    
    // ============ USER CREDENTIALS VALIDATION ============
    
    /**
     * Validate username
     * Rules: 3-20 characters, alphanumeric and underscore only
     */
    public static boolean isValidUsername(String username) {
        return matches(username, USERNAME_PATTERN);
    }
    
    /**
     * Validate password
     * Rules: At least 6 characters
     */
    public static boolean isValidPassword(String password) {
        return matches(password, PASSWORD_PATTERN);
    }
    
    /**
     * Check if passwords match
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return isNotEmpty(password) && password.equals(confirmPassword);
    }
    
    /**
     * Get username validation error message
     */
    public static String getUsernameError(String username) {
        if (isEmpty(username)) {
            return "Tên đăng nhập không được để trống";
        }
        if (!isValidUsername(username)) {
            return "Tên đăng nhập phải từ 3-20 ký tự, chỉ chứa chữ cái, số và _";
        }
        return null;
    }
    
    /**
     * Get password validation error message
     */
    public static String getPasswordError(String password) {
        if (isEmpty(password)) {
            return "Mật khẩu không được để trống";
        }
        if (!isValidPassword(password)) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        return null;
    }
    
    /**
     * Get password confirmation error message
     */
    public static String getPasswordConfirmError(String password, String confirmPassword) {
        if (isEmpty(confirmPassword)) {
            return "Vui lòng xác nhận mật khẩu";
        }
        if (!passwordsMatch(password, confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }
        return null;
    }
    
    // ============ NAME VALIDATION ============
    
    /**
     * Validate person name
     * Rules: 2-50 characters, letters and spaces only
     */
    public static boolean isValidName(String name) {
        return matches(name, NAME_PATTERN);
    }
    
    /**
     * Get name validation error message
     */
    public static String getNameError(String name) {
        if (isEmpty(name)) {
            return "Tên không được để trống";
        }
        if (!isValidName(name)) {
            return "Tên phải từ 2-50 ký tự, chỉ chứa chữ cái và khoảng trắng";
        }
        return null;
    }
    
    // ============ NUMERIC VALIDATION ============
    
    /**
     * Check if string is numeric
     */
    public static boolean isNumeric(String str) {
        return matches(str, NUMERIC_PATTERN);
    }
    
    /**
     * Check if string is alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        return matches(str, ALPHANUMERIC_PATTERN);
    }
    
    /**
     * Validate integer value
     */
    public static boolean isValidInteger(String str) {
        if (isEmpty(str)) return false;
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate integer within range
     */
    public static boolean isValidInteger(String str, int min, int max) {
        if (!isValidInteger(str)) return false;
        try {
            int value = Integer.parseInt(str.trim());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate double value
     */
    public static boolean isValidDouble(String str) {
        if (isEmpty(str)) return false;
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate double within range
     */
    public static boolean isValidDouble(String str, double min, double max) {
        if (!isValidDouble(str)) return false;
        try {
            double value = Double.parseDouble(str.trim());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(String str) {
        if (!isValidInteger(str)) return false;
        int value = Integer.parseInt(str.trim());
        return value > 0;
    }
    
    /**
     * Validate positive double
     */
    public static boolean isPositiveDouble(String str) {
        if (!isValidDouble(str)) return false;
        double value = Double.parseDouble(str.trim());
        return value > 0;
    }
    
    // ============ BUSINESS LOGIC VALIDATION ============
    
    /**
     * Validate product price
     */
    public static boolean isValidProductPrice(String priceStr) {
        return isValidDouble(priceStr, 
            AppConfig.MIN_PRODUCT_PRICE, 
            AppConfig.MAX_PRODUCT_PRICE);
    }
    
    /**
     * Validate product price (double)
     */
    public static boolean isValidProductPrice(double price) {
        return price >= AppConfig.MIN_PRODUCT_PRICE && 
               price <= AppConfig.MAX_PRODUCT_PRICE;
    }
    
    /**
     * Get product price error message
     */
    public static String getProductPriceError(String priceStr) {
        if (isEmpty(priceStr)) {
            return "Giá sản phẩm không được để trống";
        }
        if (!isValidDouble(priceStr)) {
            return "Giá sản phẩm phải là số";
        }
        if (!isValidProductPrice(priceStr)) {
            return String.format("Giá sản phẩm phải từ %s đến %s",
                CurrencyUtil.format(AppConfig.MIN_PRODUCT_PRICE),
                CurrencyUtil.format(AppConfig.MAX_PRODUCT_PRICE));
        }
        return null;
    }
    
    /**
     * Validate quantity
     */
    public static boolean isValidQuantity(String quantityStr) {
        return isPositiveInteger(quantityStr);
    }
    
    /**
     * Validate quantity (int)
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
    
    /**
     * Get quantity error message
     */
    public static String getQuantityError(String quantityStr) {
        if (isEmpty(quantityStr)) {
            return "Số lượng không được để trống";
        }
        if (!isValidInteger(quantityStr)) {
            return "Số lượng phải là số nguyên";
        }
        if (!isValidQuantity(quantityStr)) {
            return "Số lượng phải lớn hơn 0";
        }
        return null;
    }
    
    /**
     * Validate salary
     */
    public static boolean isValidSalary(String salaryStr) {
        return isValidDouble(salaryStr, 
            AppConfig.MIN_SALARY, 
            AppConfig.MAX_SALARY);
    }
    
    /**
     * Validate salary (double)
     */
    public static boolean isValidSalary(double salary) {
        return salary >= AppConfig.MIN_SALARY && 
               salary <= AppConfig.MAX_SALARY;
    }
    
    /**
     * Get salary error message
     */
    public static String getSalaryError(String salaryStr) {
        if (isEmpty(salaryStr)) {
            return "Lương không được để trống";
        }
        if (!isValidDouble(salaryStr)) {
            return "Lương phải là số";
        }
        if (!isValidSalary(salaryStr)) {
            return String.format("Lương phải từ %s đến %s",
                CurrencyUtil.format(AppConfig.MIN_SALARY),
                CurrencyUtil.format(AppConfig.MAX_SALARY));
        }
        return null;
    }
    
    // ============ SPECIAL VALIDATION ============
    
    /**
     * Validate image file extension
     */
    public static boolean isValidImageExtension(String filename) {
        if (isEmpty(filename)) return false;
        
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") || 
               lowerFilename.endsWith(".jpeg") || 
               lowerFilename.endsWith(".png") || 
               lowerFilename.endsWith(".gif") ||
               lowerFilename.endsWith(".webp");
    }
    
    /**
     * Validate URL format
     */
    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) return false;
        
        String urlPattern = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
        return matches(url, urlPattern);
    }
    
    /**
     * Validate date string format (dd/MM/yyyy)
     */
    public static boolean isValidDateFormat(String dateStr) {
        return DateUtil.isValidDateFormat(dateStr);
    }
    
    /**
     * Validate currency format
     */
    public static boolean isValidCurrencyFormat(String currencyStr) {
        return CurrencyUtil.isValidFormat(currencyStr);
    }
    
    // ============ COMPOSITE VALIDATION ============
    
    /**
     * Validation result class
     */
    public static class ValidationResult {
        private boolean valid;
        private String errorMessage;
        
        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }
    }
    
    /**
     * Validate employee data
     */
    public static ValidationResult validateEmployee(String name, String email, 
                                                    String phone, String salaryStr) {
        String error = getNameError(name);
        if (error != null) return ValidationResult.error(error);
        
        error = getEmailError(email);
        if (error != null) return ValidationResult.error(error);
        
        error = getPhoneError(phone);
        if (error != null) return ValidationResult.error(error);
        
        error = getSalaryError(salaryStr);
        if (error != null) return ValidationResult.error(error);
        
        return ValidationResult.success();
    }
    
    /**
     * Validate product data
     */
    public static ValidationResult validateProduct(String name, String priceStr) {
        if (isEmpty(name)) {
            return ValidationResult.error("Tên sản phẩm không được để trống");
        }
        
        if (!isLengthValid(name, 2, 100)) {
            return ValidationResult.error("Tên sản phẩm phải từ 2-100 ký tự");
        }
        
        String error = getProductPriceError(priceStr);
        if (error != null) return ValidationResult.error(error);
        
        return ValidationResult.success();
    }
    
    /**
     * Validate login credentials
     */
    public static ValidationResult validateLogin(String username, String password) {
        String error = getUsernameError(username);
        if (error != null) return ValidationResult.error(error);
        
        error = getPasswordError(password);
        if (error != null) return ValidationResult.error(error);
        
        return ValidationResult.success();
    }
    
    // ============ SANITIZATION ============
    
    /**
     * Sanitize string input (trim and remove dangerous characters)
     */
    public static String sanitize(String input) {
        if (isEmpty(input)) return "";
        
        return input.trim()
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#x27;")
                    .replaceAll("/", "&#x2F;");
    }
    
    /**
     * Sanitize and normalize whitespace
     */
    public static String normalizeWhitespace(String input) {
        if (isEmpty(input)) return "";
        return input.trim().replaceAll("\\s+", " ");
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("✅ Testing ValidationUtil...\n");
        
        // Test 1: Phone validation
        System.out.println("=== Phone Validation ===");
        String[] phones = {"0912345678", "+84912345678", "123", "abc"};
        for (String phone : phones) {
            System.out.println(phone + " -> " + 
                (isValidPhone(phone) ? "✅ Valid" : "❌ Invalid: " + getPhoneError(phone)));
        }
        
        // Test 2: Email validation
        System.out.println("\n=== Email Validation ===");
        String[] emails = {"test@example.com", "invalid.email", "test@", "@example.com"};
        for (String email : emails) {
            System.out.println(email + " -> " + 
                (isValidEmail(email) ? "✅ Valid" : "❌ Invalid: " + getEmailError(email)));
        }
        
        // Test 3: Username validation
        System.out.println("\n=== Username Validation ===");
        String[] usernames = {"user123", "ab", "user@123", "valid_user"};
        for (String username : usernames) {
            System.out.println(username + " -> " + 
                (isValidUsername(username) ? "✅ Valid" : "❌ Invalid: " + getUsernameError(username)));
        }
        
        // Test 4: Password validation
        System.out.println("\n=== Password Validation ===");
        String[] passwords = {"123456", "pass", "strong_password_123"};
        for (String password : passwords) {
            System.out.println(password + " -> " + 
                (isValidPassword(password) ? "✅ Valid" : "❌ Invalid: " + getPasswordError(password)));
        }
        
        // Test 5: Product price validation
        System.out.println("\n=== Product Price Validation ===");
        String[] prices = {"50000", "2000", "abc", "1500000"};
        for (String price : prices) {
            System.out.println(price + " -> " + 
                (isValidProductPrice(price) ? "✅ Valid" : "❌ Invalid: " + getProductPriceError(price)));
        }
        
        // Test 6: Composite validation
        System.out.println("\n=== Employee Validation ===");
        ValidationResult result = validateEmployee(
            "Nguyễn Văn A", 
            "nguyenvana@example.com", 
            "0912345678", 
            "10000000"
        );
        System.out.println(result.isValid() ? "✅ Valid Employee Data" : "❌ " + result.getErrorMessage());
        
        System.out.println("\n✅ All tests completed!");
    }
}