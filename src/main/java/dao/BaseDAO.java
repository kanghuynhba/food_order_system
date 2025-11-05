package dao;

import java.util.List;

/**
 * BaseDAO - Abstract class với CRUD operations
 * Path: Source Packages/dao/BaseDAO.java
 * Generic class cho tất cả DAO classes
 */
public abstract class BaseDAO<T> {
    
    /**
     * Thêm mới entity
     */
    public abstract boolean create(T entity);
    
    /**
     * Lấy entity theo ID
     */
    public abstract T getById(int id);
    
    /**
     * Lấy tất cả entities
     */
    public abstract List<T> getAll();
    
    /**
     * Cập nhật entity
     */
    public abstract boolean update(T entity);
    
    /**
     * Xóa entity theo ID
     */
    public abstract boolean delete(int id);
    
    /**
     * Kiểm tra entity tồn tại không
     */
    public abstract boolean exists(int id);
    
    /**
     * Lấy tổng số lượng
     */
    public abstract int count();
    
    /**
     * Tìm kiếm theo tiêu chí
     */
    public abstract List<T> search(String criteria);
}