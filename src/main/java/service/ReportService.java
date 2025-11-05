package service;

import dao.ReportDAO;
import dao.OrderDAO;
import dao.EmployeeDAO;
import dao.IngredientDAO;
import dao.ProductDAO;
import dao.PaymentDAO;
import entity.Order;
import entity.Employee;
import entity.Ingredient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ReportService - Reports & Analytics Service
 * Path: Source Packages/service/ReportService.java
 * 
 * Chức năng:
 * - Báo cáo bán hàng (sales reports)
 * - Báo cáo nhân viên (employee reports)
 * - Báo cáo nguyên liệu (ingredient reports)
 * - Dashboard statistics
 * - Revenue analytics
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public class ReportService {
    
    private static ReportService instance;
    private ReportDAO reportDAO;
    private OrderDAO orderDAO;
    private EmployeeDAO employeeDAO;
    private IngredientDAO ingredientDAO;
    private ProductDAO productDAO;
    private PaymentDAO paymentDAO;
    
    // ============ SINGLETON ============
    
    private ReportService() {
        this.reportDAO = new ReportDAO();
        this.orderDAO = new OrderDAO();
        this.employeeDAO = new EmployeeDAO();
        this.ingredientDAO = new IngredientDAO();
        this.productDAO = new ProductDAO();
        this.paymentDAO = new PaymentDAO();
    }
    
    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }
    
    // ============ SALES REPORTS ============
    
    /**
     * Lấy báo cáo bán hàng theo ngày
     */
    public Map<String, Object> getSalesReportByDate(String date) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            double revenue = reportDAO.getTotalRevenueByDate(date);
            int orderCount = reportDAO.getTotalOrdersByDate(date);
            double avgOrderValue = orderCount > 0 ? revenue / orderCount : 0;
            
            report.put("date", date);
            report.put("totalRevenue", revenue);
            report.put("totalOrders", orderCount);
            report.put("averageOrderValue", avgOrderValue);
            report.put("topProducts", reportDAO.getTopSellingProducts(5));
            
            System.out.println("✅ Sales report generated for: " + date);
            
        } catch (Exception e) {
            System.err.println("❌ Error generating sales report: " + e.getMessage());
        }
        
        return report;
    }
    
    /**
     * Lấy báo cáo bán hàng theo khoảng thời gian
     */
    public Map<String, Object> getSalesReportByDateRange(String startDate, String endDate) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            double revenue = reportDAO.getTotalRevenueByDateRange(startDate, endDate);
            
            // Get orders in date range
            List<Order> orders = orderDAO.getAll().stream()
                .filter(o -> {
                    String orderDate = o.getCreatedAt().toString().substring(0, 10);
                    return orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0;
                })
                .filter(o -> o.getStatus() != 6) // Exclude cancelled
                .toList();
            
            int orderCount = orders.size();
            double avgOrderValue = orderCount > 0 ? revenue / orderCount : 0;
            
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("totalRevenue", revenue);
            report.put("totalOrders", orderCount);
            report.put("averageOrderValue", avgOrderValue);
            report.put("topProducts", reportDAO.getTopSellingProducts(10));
            
            System.out.println("✅ Sales report generated for: " + startDate + " to " + endDate);
            
        } catch (Exception e) {
            System.err.println("❌ Error generating sales report: " + e.getMessage());
        }
        
        return report;
    }
    
    /**
     * Lấy báo cáo bán hàng hôm nay
     */
    public Map<String, Object> getTodaySalesReport() {
        String today = LocalDate.now().toString();
        return getSalesReportByDate(today);
    }
    
    /**
     * Lấy báo cáo bán hàng tuần này
     */
    public Map<String, Object> getWeeklySalesReport() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        
        return getSalesReportByDateRange(weekStart.toString(), weekEnd.toString());
    }
    
    /**
     * Lấy báo cáo bán hàng tháng này
     */
    public Map<String, Object> getMonthlySalesReport() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
        
        return getSalesReportByDateRange(monthStart.toString(), monthEnd.toString());
    }
    
    /**
     * Lấy top sản phẩm bán chạy
     */
    public Map<String, Integer> getTopSellingProducts(int limit) {
        try {
            return reportDAO.getTopSellingProducts(limit);
        } catch (Exception e) {
            System.err.println("❌ Error getting top products: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // ============ EMPLOYEE REPORTS ============
    
    /**
     * Lấy báo cáo nhân viên
     */
    public Map<String, Object> getEmployeeReport() {
        Map<String, Object> report = new HashMap<>();
        
        try {
            int totalEmployees = reportDAO.getTotalActiveEmployees();
            int totalChefs = reportDAO.getEmployeeCountByRole(1);
            int totalCashiers = reportDAO.getEmployeeCountByRole(2);
            int totalManagers = reportDAO.getEmployeeCountByRole(3);
            
            double totalSalary = reportDAO.getTotalSalaryByRole(1) + 
                                reportDAO.getTotalSalaryByRole(2) + 
                                reportDAO.getTotalSalaryByRole(3);
            
            report.put("totalEmployees", totalEmployees);
            report.put("totalChefs", totalChefs);
            report.put("totalCashiers", totalCashiers);
            report.put("totalManagers", totalManagers);
            report.put("totalSalary", totalSalary);
            report.put("averageSalary", totalEmployees > 0 ? totalSalary / totalEmployees : 0);
            
            // Breakdown by role
            Map<String, Object> salaryByRole = new HashMap<>();
            salaryByRole.put("chefs", reportDAO.getTotalSalaryByRole(1));
            salaryByRole.put("cashiers", reportDAO.getTotalSalaryByRole(2));
            salaryByRole.put("managers", reportDAO.getTotalSalaryByRole(3));
            report.put("salaryByRole", salaryByRole);
            
            System.out.println("✅ Employee report generated");
            
        } catch (Exception e) {
            System.err.println("❌ Error generating employee report: " + e.getMessage());
        }
        
        return report;
    }
    
    /**
     * Lấy danh sách nhân viên với thống kê
     */
    public List<Map<String, Object>> getEmployeeList() {
        try {
            List<Employee> employees = employeeDAO.getActiveEmployees();
            return employees.stream()
                .map(emp -> {
                    Map<String, Object> empData = new HashMap<>();
                    empData.put("id", emp.getEmployeeId());
                    empData.put("name", emp.getName());
                    empData.put("email", emp.getEmail());
                    empData.put("role", emp.getRoleName());
                    empData.put("salary", emp.getSalary());
                    empData.put("status", emp.getStatusName());
                    return empData;
                })
                .toList();
        } catch (Exception e) {
            System.err.println("❌ Error getting employee list: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ INGREDIENT REPORTS ============
    
    /**
     * Lấy báo cáo nguyên liệu
     */
    public Map<String, Object> getIngredientReport() {
        Map<String, Object> report = new HashMap<>();
        
        try {
            int totalIngredients = ingredientDAO.count();
            int lowStockCount = reportDAO.getLowStockIngredientCount();
            int outOfStockCount = reportDAO.getOutOfStockIngredientCount();
            int expiredCount = reportDAO.getExpiredIngredientCount();
            
            List<Ingredient> lowStockItems = ingredientDAO.getLowStockIngredients();
            List<Ingredient> expiredItems = ingredientDAO.getExpiredIngredients();
            
            report.put("totalIngredients", totalIngredients);
            report.put("lowStockCount", lowStockCount);
            report.put("outOfStockCount", outOfStockCount);
            report.put("expiredCount", expiredCount);
            report.put("lowStockItems", lowStockItems);
            report.put("expiredItems", expiredItems);
            
            // Alert level
            String alertLevel = "NORMAL";
            if (expiredCount > 0 || outOfStockCount > 0) {
                alertLevel = "CRITICAL";
            } else if (lowStockCount > 0) {
                alertLevel = "WARNING";
            }
            report.put("alertLevel", alertLevel);
            
            System.out.println("✅ Ingredient report generated");
            
        } catch (Exception e) {
            System.err.println("❌ Error generating ingredient report: " + e.getMessage());
        }
        
        return report;
    }
    
    /**
     * Lấy danh sách nguyên liệu với trạng thái
     */
    public List<Map<String, Object>> getIngredientList() {
        try {
            List<Ingredient> ingredients = ingredientDAO.getAll();
            return ingredients.stream()
                .map(ing -> {
                    Map<String, Object> ingData = new HashMap<>();
                    ingData.put("id", ing.getIngredientId());
                    ingData.put("name", ing.getName());
                    ingData.put("quantity", ing.getQuantity());
                    ingData.put("unit", ing.getUnit());
                    ingData.put("status", ing.getStatus());
                    ingData.put("expiryDate", ing.getExpiryDate());
                    ingData.put("supplier", ing.getSupplier());
                    return ingData;
                })
                .toList();
        } catch (Exception e) {
            System.err.println("❌ Error getting ingredient list: " + e.getMessage());
            return List.of();
        }
    }
    
    // ============ DASHBOARD STATISTICS ============
    
    /**
     * Lấy dashboard statistics tổng hợp
     */
    public Map<String, Object> getDashboardStatistics() {
        try {
            return reportDAO.getDashboardStatistics();
        } catch (Exception e) {
            System.err.println("❌ Error getting dashboard statistics: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * Lấy overview statistics
     */
    public Map<String, Object> getOverviewStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Today's stats
            String today = LocalDate.now().toString();
            double todayRevenue = reportDAO.getTotalRevenueByDate(today);
            int todayOrders = reportDAO.getTotalOrdersByDate(today);
            
            // Orders by status
            int pendingOrders = reportDAO.getOrderCountByStatus(0);
            int cookingOrders = reportDAO.getOrderCountByStatus(3);
            int readyOrders = reportDAO.getOrderCountByStatus(4);
            
            // Employees & Ingredients
            int activeEmployees = reportDAO.getTotalActiveEmployees();
            int lowStockItems = reportDAO.getLowStockIngredientCount();
            
            stats.put("todayRevenue", todayRevenue);
            stats.put("todayOrders", todayOrders);
            stats.put("pendingOrders", pendingOrders);
            stats.put("cookingOrders", cookingOrders);
            stats.put("readyOrders", readyOrders);
            stats.put("activeEmployees", activeEmployees);
            stats.put("lowStockItems", lowStockItems);
            
            System.out.println("✅ Overview statistics generated");
            
        } catch (Exception e) {
            System.err.println("❌ Error generating overview statistics: " + e.getMessage());
        }
        
        return stats;
    }
    
    // ============ REVENUE ANALYTICS ============
    
    /**
     * Lấy revenue theo payment method
     */
    public Map<String, Double> getRevenueByPaymentMethod() {
        Map<String, Double> revenue = new HashMap<>();
        
        try {
            revenue.put("cash", paymentDAO.getTotalRevenueByMethod(0));
            revenue.put("transfer", paymentDAO.getTotalRevenueByMethod(1));
            revenue.put("total", paymentDAO.getTotalRevenue());
            
        } catch (Exception e) {
            System.err.println("❌ Error getting revenue by payment method: " + e.getMessage());
        }
        
        return revenue;
    }
    
    /**
     * So sánh revenue giữa hai khoảng thời gian
     */
    public Map<String, Object> compareRevenue(String period1Start, String period1End,
                                               String period2Start, String period2End) {
        Map<String, Object> comparison = new HashMap<>();
        
        try {
            double revenue1 = reportDAO.getTotalRevenueByDateRange(period1Start, period1End);
            double revenue2 = reportDAO.getTotalRevenueByDateRange(period2Start, period2End);
            
            double difference = revenue2 - revenue1;
            double percentChange = revenue1 > 0 ? (difference / revenue1) * 100 : 0;
            
            comparison.put("period1Revenue", revenue1);
            comparison.put("period2Revenue", revenue2);
            comparison.put("difference", difference);
            comparison.put("percentChange", percentChange);
            comparison.put("trend", difference > 0 ? "UP" : difference < 0 ? "DOWN" : "STABLE");
            
        } catch (Exception e) {
            System.err.println("❌ Error comparing revenue: " + e.getMessage());
        }
        
        return comparison;
    }
    
    // ============ EXPORT & FORMATTING ============
    
    /**
     * Export báo cáo sang text format
     */
    public String exportSalesReportToText(String startDate, String endDate) {
        Map<String, Object> report = getSalesReportByDateRange(startDate, endDate);
        
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("      BÁO CÁO BÁN HÀNG\n");
        sb.append("========================================\n\n");
        sb.append("Từ ngày: ").append(startDate).append("\n");
        sb.append("Đến ngày: ").append(endDate).append("\n\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("Tổng doanh thu: %,.0fđ\n", report.get("totalRevenue")));
        sb.append(String.format("Tổng đơn hàng: %d\n", report.get("totalOrders")));
        sb.append(String.format("Giá trị TB/đơn: %,.0fđ\n", report.get("averageOrderValue")));
        sb.append("----------------------------------------\n");
        sb.append("========================================\n");
        
        return sb.toString();
    }
    
    /**
     * Format currency
     */
    public String formatCurrency(double amount) {
        return String.format("%,.0fđ", amount);
    }
    
    /**
     * Format percentage
     */
    public String formatPercentage(double percent) {
        return String.format("%.2f%%", percent);
    }
    
    // ============ UTILITY ============
    
    /**
     * Validate date format
     */
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get current date
     */
    public String getCurrentDate() {
        return LocalDate.now().toString();
    }
    
    /**
     * Get date range for period
     */
    public Map<String, String> getDateRangeForPeriod(String period) {
        LocalDate today = LocalDate.now();
        LocalDate start, end;
        
        switch (period.toLowerCase()) {
            case "today":
                start = end = today;
                break;
            case "week":
                start = today.minusDays(today.getDayOfWeek().getValue() - 1);
                end = start.plusDays(6);
                break;
            case "month":
                start = today.withDayOfMonth(1);
                end = today.withDayOfMonth(today.lengthOfMonth());
                break;
            case "year":
                start = today.withDayOfYear(1);
                end = today.withDayOfYear(today.lengthOfYear());
                break;
            default:
                start = end = today;
        }
        
        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("startDate", start.toString());
        dateRange.put("endDate", end.toString());
        return dateRange;
    }
}