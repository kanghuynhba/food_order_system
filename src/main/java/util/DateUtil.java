package util;

import config.AppConfig;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

/**
 * DateUtil - Date and Time Utility
 * Path: Source Packages/util/DateUtil.java
 * 
 * Provides date/time formatting, parsing, and manipulation
 * Uses Java 8+ Time API for better performance
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class DateUtil {
    
    // ============ FORMATTERS ============
    
    public static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern(AppConfig.DATE_FORMAT);
    
    public static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern(AppConfig.TIME_FORMAT);
    
    public static final DateTimeFormatter DATETIME_FORMATTER = 
        DateTimeFormatter.ofPattern(AppConfig.DATETIME_FORMAT);
    
    public static final DateTimeFormatter DATETIME_FORMATTER_SHORT = 
        DateTimeFormatter.ofPattern(AppConfig.DATETIME_FORMAT_SHORT);
    
    public static final DateTimeFormatter TIME_FORMATTER_SHORT = 
        DateTimeFormatter.ofPattern(AppConfig.TIME_FORMAT_SHORT);
    
    // Custom formatters
    public static final DateTimeFormatter FILENAME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = 
        DateTimeFormatter.ofPattern("MM/yyyy");
    
    public static final DateTimeFormatter YEAR_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy");
    
    // ============ CURRENT DATE/TIME ============
    
    /**
     * Get current date (java.sql.Date)
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    
    /**
     * Get current timestamp (java.sql.Timestamp)
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Get current LocalDate
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }
    
    // ============ FORMATTING METHODS ============
    
    /**
     * Format Date to string (dd/MM/yyyy)
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return formatDate(date.toLocalDate());
    }
    
    /**
     * Format LocalDate to string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format Timestamp to date string
     */
    public static String formatTimestampToDate(Timestamp timestamp) {
        if (timestamp == null) return "";
        return formatDate(timestamp.toLocalDateTime().toLocalDate());
    }
    
    /**
     * Format Timestamp to datetime string (dd/MM/yyyy HH:mm:ss)
     */
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "";
        return timestamp.toLocalDateTime().format(DATETIME_FORMATTER);
    }
    
    /**
     * Format Timestamp to datetime string short (dd/MM/yyyy HH:mm)
     */
    public static String formatTimestampShort(Timestamp timestamp) {
        if (timestamp == null) return "";
        return timestamp.toLocalDateTime().format(DATETIME_FORMATTER_SHORT);
    }
    
    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string short
     */
    public static String formatDateTimeShort(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER_SHORT);
    }
    
    /**
     * Format time only from Timestamp (HH:mm:ss)
     */
    public static String formatTime(Timestamp timestamp) {
        if (timestamp == null) return "";
        return timestamp.toLocalDateTime().format(TIME_FORMATTER);
    }
    
    /**
     * Format time only short (HH:mm)
     */
    public static String formatTimeShort(Timestamp timestamp) {
        if (timestamp == null) return "";
        return timestamp.toLocalDateTime().format(TIME_FORMATTER_SHORT);
    }
    
    /**
     * Format LocalTime to string
     */
    public static String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(TIME_FORMATTER_SHORT);
    }
    
    /**
     * Format for filename (yyyyMMdd_HHmmss)
     */
    public static String formatForFilename() {
        return LocalDateTime.now().format(FILENAME_FORMATTER);
    }
    
    /**
     * Format month and year (MM/yyyy)
     */
    public static String formatMonthYear(LocalDate date) {
        if (date == null) return "";
        return date.format(MONTH_YEAR_FORMATTER);
    }
    
    // ============ PARSING METHODS ============
    
    /**
     * Parse date string to Date (dd/MM/yyyy)
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        
        try {
            LocalDate localDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }
    
    /**
     * Parse date string to LocalDate
     */
    public static LocalDate parseToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }
    
    /**
     * Parse datetime string to Timestamp
     */
    public static Timestamp parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) return null;
        
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing timestamp: " + dateTimeStr);
            return null;
        }
    }
    
    /**
     * Parse datetime string to LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) return null;
        
        try {
            return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing datetime: " + dateTimeStr);
            return null;
        }
    }
    
    // ============ CONVERSION METHODS ============
    
    /**
     * Convert java.util.Date to java.sql.Date
     */
    public static Date toSqlDate(java.util.Date utilDate) {
        if (utilDate == null) return null;
        return new Date(utilDate.getTime());
    }
    
    /**
     * Convert java.sql.Date to java.util.Date
     */
    public static java.util.Date toUtilDate(Date sqlDate) {
        if (sqlDate == null) return null;
        return new java.util.Date(sqlDate.getTime());
    }
    
    /**
     * Convert LocalDate to Date
     */
    public static Date toSqlDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.valueOf(localDate);
    }
    
    /**
     * Convert Date to LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toLocalDate();
    }
    
    /**
     * Convert Timestamp to LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime();
    }
    
    /**
     * Convert LocalDateTime to Timestamp
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Timestamp.valueOf(localDateTime);
    }
    
    // ============ CALCULATION METHODS ============
    
    /**
     * Add days to date
     */
    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        LocalDate localDate = date.toLocalDate().plusDays(days);
        return Date.valueOf(localDate);
    }
    
    /**
     * Add months to date
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) return null;
        LocalDate localDate = date.toLocalDate().plusMonths(months);
        return Date.valueOf(localDate);
    }
    
    /**
     * Add years to date
     */
    public static Date addYears(Date date, int years) {
        if (date == null) return null;
        LocalDate localDate = date.toLocalDate().plusYears(years);
        return Date.valueOf(localDate);
    }
    
    /**
     * Calculate days between two dates
     */
    public static long daysBetween(Date start, Date end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
    }
    
    /**
     * Calculate hours between two timestamps
     */
    public static long hoursBetween(Timestamp start, Timestamp end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.HOURS.between(
            start.toLocalDateTime(), 
            end.toLocalDateTime()
        );
    }
    
    /**
     * Calculate minutes between two timestamps
     */
    public static long minutesBetween(Timestamp start, Timestamp end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.MINUTES.between(
            start.toLocalDateTime(), 
            end.toLocalDateTime()
        );
    }
    
    /**
     * Calculate age from birthdate
     */
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate.toLocalDate(), LocalDate.now()).getYears();
    }
    
    // ============ COMPARISON METHODS ============
    
    /**
     * Check if date is today
     */
    public static boolean isToday(Date date) {
        if (date == null) return false;
        return date.toLocalDate().equals(LocalDate.now());
    }
    
    /**
     * Check if timestamp is today
     */
    public static boolean isToday(Timestamp timestamp) {
        if (timestamp == null) return false;
        return timestamp.toLocalDateTime().toLocalDate().equals(LocalDate.now());
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isPast(Date date) {
        if (date == null) return false;
        return date.toLocalDate().isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isFuture(Date date) {
        if (date == null) return false;
        return date.toLocalDate().isAfter(LocalDate.now());
    }
    
    /**
     * Check if date is between two dates (inclusive)
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null) return false;
        
        LocalDate localDate = date.toLocalDate();
        LocalDate localStart = start.toLocalDate();
        LocalDate localEnd = end.toLocalDate();
        
        return !localDate.isBefore(localStart) && !localDate.isAfter(localEnd);
    }
    
    /**
     * Check if timestamp is expired (older than given minutes)
     */
    public static boolean isExpired(Timestamp timestamp, int minutes) {
        if (timestamp == null) return true;
        
        LocalDateTime then = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        
        return ChronoUnit.MINUTES.between(then, now) > minutes;
    }
    
    // ============ HELPER METHODS ============
    
    /**
     * Get start of day
     */
    public static Timestamp getStartOfDay(Date date) {
        if (date == null) return null;
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        return Timestamp.valueOf(startOfDay);
    }
    
    /**
     * Get end of day
     */
    public static Timestamp getEndOfDay(Date date) {
        if (date == null) return null;
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return Timestamp.valueOf(endOfDay);
    }
    
    /**
     * Get start of month
     */
    public static Date getStartOfMonth(Date date) {
        if (date == null) return null;
        LocalDate firstDay = date.toLocalDate().withDayOfMonth(1);
        return Date.valueOf(firstDay);
    }
    
    /**
     * Get end of month
     */
    public static Date getEndOfMonth(Date date) {
        if (date == null) return null;
        LocalDate lastDay = date.toLocalDate()
            .withDayOfMonth(date.toLocalDate().lengthOfMonth());
        return Date.valueOf(lastDay);
    }
    
    /**
     * Get relative time string (e.g., "2 hours ago", "5 minutes ago")
     */
    public static String getRelativeTime(Timestamp timestamp) {
        if (timestamp == null) return "";
        
        LocalDateTime then = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        
        long minutes = ChronoUnit.MINUTES.between(then, now);
        long hours = ChronoUnit.HOURS.between(then, now);
        long days = ChronoUnit.DAYS.between(then, now);
        
        if (minutes < 1) return "Vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        if (days == 1) return "Hôm qua";
        if (days < 7) return days + " ngày trước";
        if (days < 30) return (days / 7) + " tuần trước";
        if (days < 365) return (days / 30) + " tháng trước";
        return (days / 365) + " năm trước";
    }
    
    /**
     * Get day of week in Vietnamese
     */
    public static String getDayOfWeekVietnamese(Date date) {
        if (date == null) return "";
        
        DayOfWeek dayOfWeek = date.toLocalDate().getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }
    
    /**
     * Validate date string format
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return false;
        
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    // ============ MAIN - FOR TESTING ============
    
    public static void main(String[] args) {
        System.out.println("📅 Testing DateUtil...\n");
        
        // Test 1: Current date/time
        System.out.println("Current date: " + formatDate(getCurrentDate()));
        System.out.println("Current timestamp: " + formatTimestamp(getCurrentTimestamp()));
        System.out.println("Current time: " + formatTime(getCurrentTimestamp()));
        
        // Test 2: Formatting
        Timestamp now = getCurrentTimestamp();
        System.out.println("\nFormatting tests:");
        System.out.println("Full: " + formatTimestamp(now));
        System.out.println("Short: " + formatTimestampShort(now));
        System.out.println("Time: " + formatTime(now));
        System.out.println("Time Short: " + formatTimeShort(now));
        System.out.println("Filename: " + formatForFilename());
        
        // Test 3: Parsing
        String dateStr = "15/04/2000";
        Date parsed = parseDate(dateStr);
        System.out.println("\nParsing: " + dateStr + " -> " + formatDate(parsed));
        
        // Test 4: Calculations
        Date today = getCurrentDate();
        Date nextWeek = addDays(today, 7);
        System.out.println("\nToday: " + formatDate(today));
        System.out.println("Next week: " + formatDate(nextWeek));
        System.out.println("Days between: " + daysBetween(today, nextWeek));
        
        // Test 5: Relative time
        System.out.println("\nRelative time: " + getRelativeTime(now));
        
        // Test 6: Day of week
        System.out.println("Day of week: " + getDayOfWeekVietnamese(today));
        
        System.out.println("\n✅ All tests completed!");
    }
}