package util;

import config.AppConfig;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * CurrencyUtil - Currency Formatting Utility
 * Path: Source Packages/util/CurrencyUtil.java
 * 
 * Provides currency formatting and parsing for VND (Vietnamese Dong)
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class CurrencyUtil {
    
    // ============ FORMATTERS ============
    
    private static final DecimalFormat CURRENCY_FORMATTER = 
        new DecimalFormat(AppConfig.CURRENCY_FORMAT);
    
    private static final DecimalFormat CURRENCY_FORMATTER_FULL = 
        new DecimalFormat(AppConfig.CURRENCY_FORMAT_FULL);
    
    private static final NumberFormat VN_CURRENCY_FORMAT = 
        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    // ============ FORMATTING METHODS ============
    
    /**
     * Format amount to currency string with symbol (e.g., "100,000đ")
     * 
     * @param amount Amount to format
     * @return Formatted currency string
     */
    public static String format(double amount) {
        return CURRENCY_FORMATTER.format(amount) + AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Format amount with decimal places (e.g., "100,000.50đ")
     * 
     * @param amount Amount to format
     * @return Formatted currency string with decimals
     */
    public static String formatWithDecimals(double amount) {
        return CURRENCY_FORMATTER_FULL.format(amount) + AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Format amount without symbol (e.g., "100,000")
     * 
     * @param amount Amount to format
     * @return Formatted number string without symbol
     */
    public static String formatNumber(double amount) {
        return CURRENCY_FORMATTER.format(amount);
    }
    
    /**
     * Format amount with VND symbol (e.g., "100,000 VNĐ")
     * 
     * @param amount Amount to format
     * @return Formatted string with VND
     */
    public static String formatVND(double amount) {
        return CURRENCY_FORMATTER.format(amount) + " " + AppConfig.CURRENCY_CODE;
    }
    
    /**
     * Format amount in compact form (e.g., "1.5K", "2.3M")
     * 
     * @param amount Amount to format
     * @return Compact formatted string
     */
    public static String formatCompact(double amount) {
        if (amount < 1000) {
            return format(amount);
        } else if (amount < 1_000_000) {
            return String.format("%.1fK", amount / 1000) + AppConfig.CURRENCY_SYMBOL;
        } else if (amount < 1_000_000_000) {
            return String.format("%.1fM", amount / 1_000_000) + AppConfig.CURRENCY_SYMBOL;
        } else {
            return String.format("%.1fB", amount / 1_000_000_000) + AppConfig.CURRENCY_SYMBOL;
        }
    }
    
    /**
     * Format amount in words (Vietnamese)
     * E.g., 123,456 -> "Một trăm hai mươi ba nghìn bốn trăm năm mươi sáu đồng"
     * 
     * @param amount Amount to format
     * @return Amount in Vietnamese words
     */
    public static String formatInWords(double amount) {
        if (amount == 0) {
            return "Không đồng";
        }
        
        if (amount < 0) {
            return "Âm " + formatInWords(-amount);
        }
        
        // Convert to long (remove decimals)
        long intAmount = (long) amount;
        
        String[] units = {"", "nghìn", "triệu", "tỷ"};
        String result = "";
        int unitIndex = 0;
        
        while (intAmount > 0) {
            long group = intAmount % 1000;
            if (group > 0) {
                String groupWords = convertGroupToWords(group);
                if (unitIndex > 0) {
                    result = groupWords + " " + units[unitIndex] + " " + result;
                } else {
                    result = groupWords + " " + result;
                }
            }
            intAmount /= 1000;
            unitIndex++;
        }
        
        return result.trim() + " đồng";
    }
    
    /**
     * Helper method to convert a group of 3 digits to words
     */
    private static String convertGroupToWords(long number) {
        if (number == 0) return "";
        
        String[] ones = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] teens = {"mười", "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm", 
                         "mười sáu", "mười bảy", "mười tám", "mười chín"};
        
        int hundreds = (int) (number / 100);
        int remainder = (int) (number % 100);
        int tens = remainder / 10;
        int unit = remainder % 10;
        
        String result = "";
        
        // Hundreds
        if (hundreds > 0) {
            result = ones[hundreds] + " trăm";
        }
        
        // Tens and ones
        if (remainder > 0) {
            if (remainder < 10) {
                if (hundreds > 0) {
                    result += " lẻ";
                }
                result += " " + ones[remainder];
            } else if (remainder < 20) {
                result += " " + teens[remainder - 10];
            } else {
                if (tens == 1) {
                    result += " mười";
                } else {
                    result += " " + ones[tens] + " mươi";
                }
                
                if (unit > 0) {
                    if (unit == 5 && tens >= 1) {
                        result += " lăm";
                    } else if (unit == 1 && tens >= 2) {
                        result += " mốt";
                    } else {
                        result += " " + ones[unit];
                    }
                }
            }
        }
        
        return result.trim();
    }
    
    // ============ PARSING METHODS ============
    
    /**
     * Parse currency string to double
     * Accepts formats: "100,000đ", "100000", "100,000"
     * 
     * @param currencyStr Currency string to parse
     * @return Parsed amount
     */
    public static double parse(String currencyStr) {
        if (currencyStr == null || currencyStr.isEmpty()) {
            return 0.0;
        }
        
        try {
            // Remove currency symbols and whitespace
            String cleaned = currencyStr
                .replace(AppConfig.CURRENCY_SYMBOL, "")
                .replace(AppConfig.CURRENCY_CODE, "")
                .replace("VNĐ", "")
                .replace("₫", "")
                .replace(" ", "")
                .trim();
            
            // Parse using DecimalFormat
            Number number = CURRENCY_FORMATTER.parse(cleaned);
            return number.doubleValue();
            
        } catch (ParseException e) {
            System.err.println("Error parsing currency: " + currencyStr);
            return 0.0;
        }
    }
    
    /**
     * Try to parse currency string, return default value if failed
     * 
     * @param currencyStr Currency string to parse
     * @param defaultValue Default value if parsing fails
     * @return Parsed amount or default value
     */
    public static double parseOrDefault(String currencyStr, double defaultValue) {
        try {
            return parse(currencyStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    // ============ VALIDATION METHODS ============
    
    /**
     * Check if amount is valid (positive)
     * 
     * @param amount Amount to check
     * @return true if valid
     */
    public static boolean isValidAmount(double amount) {
        return amount >= 0;
    }
    
    /**
     * Check if amount is within range
     * 
     * @param amount Amount to check
     * @param min Minimum value
     * @param max Maximum value
     * @return true if within range
     */
    public static boolean isInRange(double amount, double min, double max) {
        return amount >= min && amount <= max;
    }
    
    /**
     * Check if amount is valid product price
     * 
     * @param price Price to check
     * @return true if valid
     */
    public static boolean isValidProductPrice(double price) {
        return isInRange(price, AppConfig.MIN_PRODUCT_PRICE, AppConfig.MAX_PRODUCT_PRICE);
    }
    
    /**
     * Check if amount is valid order amount
     * 
     * @param amount Amount to check
     * @return true if valid
     */
    public static boolean isValidOrderAmount(double amount) {
        return isInRange(amount, AppConfig.MIN_ORDER_AMOUNT, AppConfig.MAX_ORDER_AMOUNT);
    }
    
    /**
     * Check if string is valid currency format
     * 
     * @param currencyStr String to check
     * @return true if valid format
     */
    public static boolean isValidFormat(String currencyStr) {
        if (currencyStr == null || currencyStr.isEmpty()) {
            return false;
        }
        
        try {
            double amount = parse(currencyStr);
            return isValidAmount(amount);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ============ CALCULATION METHODS ============
    
    /**
     * Calculate percentage of amount
     * 
     * @param amount Base amount
     * @param percentage Percentage (e.g., 10 for 10%)
     * @return Calculated amount
     */
    public static double calculatePercentage(double amount, double percentage) {
        return amount * percentage / 100.0;
    }
    
    /**
     * Apply discount to amount
     * 
     * @param amount Original amount
     * @param discountPercent Discount percentage
     * @return Amount after discount
     */
    public static double applyDiscount(double amount, double discountPercent) {
        double discount = calculatePercentage(amount, discountPercent);
        return amount - discount;
    }
    
    /**
     * Calculate tax (VAT 10% for Vietnam)
     * 
     * @param amount Amount before tax
     * @return Tax amount
     */
    public static double calculateTax(double amount) {
        return calculatePercentage(amount, 10.0); // 10% VAT
    }
    
    /**
     * Calculate total with tax
     * 
     * @param amount Amount before tax
     * @return Total amount with tax
     */
    public static double calculateTotalWithTax(double amount) {
        return amount + calculateTax(amount);
    }
    
    /**
     * Round amount to nearest 1000 (common in Vietnam)
     * 
     * @param amount Amount to round
     * @return Rounded amount
     */
    public static double roundTo1000(double amount) {
        return Math.round(amount / 1000.0) * 1000.0;
    }
    
    /**
     * Round amount to nearest 5000
     * 
     * @param amount Amount to round
     * @return Rounded amount
     */
    public static double roundTo5000(double amount) {
        return Math.round(amount / 5000.0) * 5000.0;
    }
    
    /**
     * Calculate change (for cash payment)
     * 
     * @param paid Amount paid
     * @param total Total amount
     * @return Change amount
     */
    public static double calculateChange(double paid, double total) {
        double change = paid - total;
        return change >= 0 ? change : 0;
    }
    
    // ============ COMPARISON METHODS ============
    
    /**
     * Compare two amounts (handle floating point precision)
     * 
     * @param amount1 First amount
     * @param amount2 Second amount
     * @return 0 if equal, -1 if amount1 < amount2, 1 if amount1 > amount2
     */
    public static int compare(double amount1, double amount2) {
        double epsilon = 0.01; // Precision to 1 cent
        
        if (Math.abs(amount1 - amount2) < epsilon) {
            return 0;
        } else if (amount1 < amount2) {
            return -1;
        } else {
            return 1;
        }
    }
    
    /**
     * Check if two amounts are equal (with precision)
     * 
     * @param amount1 First amount
     * @param amount2 Second amount
     * @return true if equal
     */
    public static boolean equals(double amount1, double amount2) {
        return compare(amount1, amount2) == 0;
    }
    
    // ============ DISPLAY HELPERS ============
    
    /**
     * Format amount for display in table/list
     * Right-aligned with fixed width
     * 
     * @param amount Amount to format
     * @param width Total width
     * @return Formatted string
     */
    public static String formatForDisplay(double amount, int width) {
        String formatted = format(amount);
        return String.format("%" + width + "s", formatted);
    }
    
    /**
     * Format amount with color indicator (for profit/loss)
     * 
     * @param amount Amount to format
     * @return Formatted string with + or - prefix
     */
    public static String formatWithSign(double amount) {
        String formatted = format(Math.abs(amount));
        if (amount > 0) {
            return "+" + formatted;
        } else if (amount < 0) {
            return "-" + formatted;
        } else {
            return formatted;
        }
    }
    
    /**
     * Get currency symbol
     */
    public static String getSymbol() {
        return AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Get currency code
     */
    public static String getCode() {
        return AppConfig.CURRENCY_CODE;
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("💰 Testing CurrencyUtil...\n");
        
        double[] testAmounts = {0, 1234, 12345, 123456, 1234567, 12345678};
        
        // Test 1: Formatting
        System.out.println("=== Formatting Tests ===");
        for (double amount : testAmounts) {
            System.out.println(amount + " -> " + format(amount));
        }
        
        // Test 2: Compact format
        System.out.println("\n=== Compact Format ===");
        for (double amount : testAmounts) {
            System.out.println(amount + " -> " + formatCompact(amount));
        }
        
        // Test 3: Format in words
        System.out.println("\n=== Format in Words ===");
        double[] wordTests = {0, 15, 123, 1234, 123456};
        for (double amount : wordTests) {
            System.out.println(amount + " -> " + formatInWords(amount));
        }
        
        // Test 4: Parsing
        System.out.println("\n=== Parsing Tests ===");
        String[] parseTests = {"100,000đ", "1,234,567", "50000", "2.5M"};
        for (String str : parseTests) {
            System.out.println(str + " -> " + parse(str));
        }
        
        // Test 5: Calculations
        System.out.println("\n=== Calculation Tests ===");
        double baseAmount = 100000;
        System.out.println("Base: " + format(baseAmount));
        System.out.println("10% discount: " + format(applyDiscount(baseAmount, 10)));
        System.out.println("Tax: " + format(calculateTax(baseAmount)));
        System.out.println("Total with tax: " + format(calculateTotalWithTax(baseAmount)));
        System.out.println("Round to 1000: " + format(roundTo1000(123456)));
        
        // Test 6: Validation
        System.out.println("\n=== Validation Tests ===");
        System.out.println("Is 50000 valid product price? " + isValidProductPrice(50000));
        System.out.println("Is 2000 valid product price? " + isValidProductPrice(2000));
        System.out.println("Is 50000 valid order? " + isValidOrderAmount(50000));
        
        System.out.println("\n✅ All tests completed!");
    }
}