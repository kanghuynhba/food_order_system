package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PasswordUtil - Password Hashing & Verification Utility
 * Path: Source Packages/util/PasswordUtil.java
 * 
 * Provides secure password hashing using SHA-256 with salt
 * For production: Consider using BCrypt or Argon2
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String SEPARATOR = ":";
    
    // ============ HASHING METHODS ============
    
    /**
     * Hash password with random salt
     * Format: salt:hashedPassword
     * 
     * @param password Plain text password
     * @return Hashed password with salt
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            // Generate random salt
            byte[] salt = generateSalt();
            
            // Hash password with salt
            byte[] hashedPassword = hashWithSalt(password, salt);
            
            // Encode to Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);
            
            // Return format: salt:hash
            return saltBase64 + SEPARATOR + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Hash password with specific salt (for verification)
     * 
     * @param password Plain text password
     * @param salt Salt bytes
     * @return Hashed password bytes
     */
    private static byte[] hashWithSalt(String password, byte[] salt) 
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        
        // Add salt to hash
        md.update(salt);
        
        // Hash password
        return md.digest(password.getBytes());
    }
    
    /**
     * Generate random salt
     * 
     * @return Random salt bytes
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    // ============ VERIFICATION METHODS ============
    
    /**
     * Verify password against stored hash
     * 
     * @param password Plain text password to verify
     * @param storedHash Stored hash (format: salt:hash)
     * @return true if password matches
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        
        try {
            // Split stored hash into salt and hash
            String[] parts = storedHash.split(SEPARATOR);
            if (parts.length != 2) {
                return false;
            }
            
            // Decode salt and hash
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);
            
            // Hash input password with same salt
            byte[] inputHashBytes = hashWithSalt(password, salt);
            
            // Compare hashes
            return MessageDigest.isEqual(storedHashBytes, inputHashBytes);
            
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    // ============ PASSWORD STRENGTH VALIDATION ============
    
    /**
     * Check if password meets minimum requirements
     * - At least 6 characters
     * 
     * @param password Password to check
     * @return true if password is strong enough
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        return password.length() >= 6;
    }
    
    /**
     * Check password strength (detailed)
     * 
     * @param password Password to check
     * @return PasswordStrength enum
     */
    public static PasswordStrength checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        
        int length = password.length();
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        int score = 0;
        if (length >= 6) score++;
        if (length >= 8) score++;
        if (length >= 12) score++;
        if (hasUpperCase) score++;
        if (hasLowerCase) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        
        if (score < 2) return PasswordStrength.WEAK;
        if (score < 4) return PasswordStrength.MEDIUM;
        if (score < 6) return PasswordStrength.STRONG;
        return PasswordStrength.VERY_STRONG;
    }
    
    /**
     * Get password strength message
     * 
     * @param password Password to check
     * @return Feedback message
     */
    public static String getPasswordStrengthMessage(String password) {
        PasswordStrength strength = checkPasswordStrength(password);
        return switch (strength) {
            case INVALID -> "Mật khẩu không hợp lệ";
            case WEAK -> "Mật khẩu yếu - Cần ít nhất 6 ký tự";
            case MEDIUM -> "Mật khẩu trung bình - Nên thêm chữ hoa, số hoặc ký tự đặc biệt";
            case STRONG -> "Mật khẩu mạnh";
            case VERY_STRONG -> "Mật khẩu rất mạnh";
        };
    }
    
    // ============ HELPER METHODS ============
    
    /**
     * Generate random password
     * 
     * @param length Password length
     * @return Random password
     */
    public static String generateRandomPassword(int length) {
        if (length < 6) {
            length = 6;
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    /**
     * Simple hash (for legacy support or testing)
     * NOT RECOMMENDED for production
     */
    public static String simpleHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage(), e);
        }
    }
    
    // ============ ENUMS ============
    
    public enum PasswordStrength {
        INVALID,
        WEAK,
        MEDIUM,
        STRONG,
        VERY_STRONG
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("🔐 Testing PasswordUtil...\n");
        
        // Test 1: Hash password
        String password = "admin123";
        String hashed = hashPassword(password);
        System.out.println("Original: " + password);
        System.out.println("Hashed: " + hashed);
        
        // Test 2: Verify correct password
        boolean verified = verifyPassword(password, hashed);
        System.out.println("Verification (correct): " + (verified ? "✅" : "❌"));
        
        // Test 3: Verify wrong password
        boolean wrongVerified = verifyPassword("wrongpassword", hashed);
        System.out.println("Verification (wrong): " + (wrongVerified ? "❌" : "✅"));
        
        // Test 4: Password strength
        String[] testPasswords = {
            "123",
            "password",
            "Password123",
            "P@ssw0rd!123"
        };
        
        System.out.println("\n🔍 Password Strength Tests:");
        for (String pwd : testPasswords) {
            PasswordStrength strength = checkPasswordStrength(pwd);
            System.out.println("Password: " + pwd + " -> " + strength + 
                             " (" + getPasswordStrengthMessage(pwd) + ")");
        }
        
        // Test 5: Generate random password
        System.out.println("\n🎲 Random password: " + generateRandomPassword(12));
        
        System.out.println("\n✅ All tests completed!");
    }
}